package louchtch.graphmatch.matching;

import louchtch.graphmatch.model.*;

import java.util.*;
import java.util.function.Consumer;

public class MaxFlowMatching<T> implements Matching
{
	private final MaxFlowGraph<T> graph;
	private final SearchType searchType;

	public MaxFlowMatching(MaxFlowGraph<T> graph, SearchType searchType)
	{
		this.graph = graph;
		this.searchType = searchType;
	}

	@Override
	public Object stuff()
	{
		return null;
	}

	private void bestMatching()
	{
		BellmanFordAugmentingPath<T> path = findAugmentingPath();
		while (path.isAugmenting()) {
			augmentFlowGraph(path);
		}

		// matching in graph source sink
	}

	private BellmanFordAugmentingPath<T> findAugmentingPath()
	{
		// searh type distinction -- todo: move to own implementations of MaxFlowMatching instead

		// todo: implement shortest path or remove from SearchType enum
		//if (searchType == SearchType.ShortestPath)

		if (searchType == SearchType.MinCost)
		{
			BellmanFordAugmentingPath<T> path = new BellmanFordAugmentingPath<>(graph);
			return path;
		}

		throw new RuntimeException("Unsupported search type: " + searchType);
	}

	private void augmentFlowGraph(BellmanFordAugmentingPath<T> path)
	{
		if (!path.isAugmenting()) {
			throw new RuntimeException("Path is not an augmenting one, cannot augment flow graph");
		}

		graph.augmentAlong(path);
	}

	static class BellmanFordAugmentingPath<T>
	{
		private final MaxFlowGraph<T> graph;
		private List<Edge<T>> pathAsList = null;

		private BellmanFordAugmentingPath(MaxFlowGraph<T> graph)
		{
			this.graph = graph;
		}

		public void forEach(Consumer<Edge<T>> fn)
		{
			if (pathAsList == null)
			{
				pathAsList = determine();
			}
			pathAsList.forEach(fn);
		}

		boolean isAugmenting()
		{
			return bellmanFordParents().containsKey(graph.sink);
		}

		private Map<Vertex<T>, Vertex<T>> _parents = null;
		private Map<Vertex<T>, Vertex<T>> bellmanFordParents()
		{
			if (_parents == null) {
				/* Distances & initialization of, todo: move into own class */
				Map<Vertex<T>, Float> distance = new IdentityHashMap<>(graph.vertices.count());
				Map<Vertex<T>, Vertex<T>> parents = new IdentityHashMap<>(graph.vertices.count());

				/* init distances (from source) to infinity*/
				graph.vertices.forEach(v -> {
					distance.put(v, Float.POSITIVE_INFINITY);
				});
				// distance from source to self is 0
				distance.put(graph.source, 0f);

				/* step 2 */
				graph.vertices.forEach(i -> {
					graph.edges.forEach(edge -> {

						if (distance.get(edge.from) + edge.weight < distance.get(edge.to))
						{
							float newDistance = distance.get(edge.from) + edge.weight;
							distance.put(edge.to, newDistance);

							parents.put(edge.to, edge.from);
						}
					});
				});

				/* step 3: negative-weight cycle detection */
				graph.edges.forEach(edge -> {
					if (distance.get(edge.from) + edge.weight < distance.get(edge.to))
					{
						// todo: emit more info
						throw new RuntimeException("graph contains a negative-weight cycle");
					}
				});

				_parents = parents;
			}

			return _parents;
		}

		// see: https://en.wikipedia.org/wiki/Bellman%E2%80%93Ford_algorithm
		private List<Edge<T>> determine()
		{
			Map<Vertex<T>, Vertex<T>> parents = bellmanFordParents();

			// reconstruct the path
			List<Edge<T>> path = new ArrayList<>();
			Vertex<T> current = graph.sink;
			while (current != graph.source) {
				Vertex<T> parent = parents.get(current);

				Edge<T> edge = Edge.between(parent, current);
				path.add(edge);

				current = parent;

				if (path.size() > parents.size()) {
					throw new RuntimeException("Cannot reconstruct path (graphmatch exception: 'Cannot terminate. Use integral edge weights.')");
				}
			}

			Collections.reverse(path);
			return path;
		}
	}
}
