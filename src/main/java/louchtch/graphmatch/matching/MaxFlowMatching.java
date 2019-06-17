package louchtch.graphmatch.matching;

import louchtch.graphmatch.model.*;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

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
	public List<Edge<T>> asListOfEdges()
	{
		return bestMatching();
	}

	private List<Edge<T>> matchingAsListOfEdges = null;
	private List<Edge<T>> bestMatching()
	{
		if (matchingAsListOfEdges != null) {
			return matchingAsListOfEdges;
		}

		BellmanFordAugmentingPath<T> path = findAugmentingPath();
		while (path.isAugmenting()) {
			augmentFlowGraph(path);
			path = findAugmentingPath();
		}

		List<Vertex<T>> matchedProjects = graph.edges.findAllFrom(graph.sink)
			.stream().map(edge -> edge.to)
			.collect(Collectors.toList());

		List<Edge<T>> matching = matchedProjects.stream()
			.map(matchedProject -> graph.edges.findAllFrom(matchedProject).stream().findFirst())
			.filter(Optional::isPresent).map(Optional::get)
			.map(matchEdge -> Edge.between(matchEdge.to, matchEdge.from))
			.collect(Collectors.toList());

		matchingAsListOfEdges = matching;
		return matchingAsListOfEdges;
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

	public static class BellmanFordAugmentingPath<T>
	{
		private final MaxFlowGraph<T> graph;
		private List<Edge<T>> pathAsList = null;

		private BellmanFordAugmentingPath(MaxFlowGraph<T> graph)
		{
			this.graph = graph;
		}

		private List<Edge<T>> pathAsList()
		{
			if (pathAsList == null)
			{
				pathAsList = determine();
			}

			return pathAsList;
		}

		public void forEach(Consumer<Edge<T>> fn)
		{
			pathAsList().forEach(fn);
		}

		boolean isAugmenting()
		{
			return bellmanFordParents().containsKey(graph.sink);
		}

		@Override
		public String toString()
		{
			return "path (isAugmenting: " + isAugmenting() + ", \n" +
				"[" + Arrays.toString(pathAsList().toArray()) + "]";
		}

		private Map<Vertex<T>, Vertex<T>> _parents = null;
		private Map<Vertex<T>, Vertex<T>> bellmanFordParents()
		{
			if (_parents == null) {
				/* Distances & initialization of */
				Distance<T> distance = new Distance<>(graph.vertices, Float.POSITIVE_INFINITY);
				Parents<T> parents = new Parents<>(graph.vertices);

				// distance from source to self is 0
				distance.set(graph.source, 0f);

				/* step 2 */
				graph.vertices.forEach(i ->
					graph.edges.forEach(edge -> {
						float distanceVertFrom = distance.get(edge.from);
						float distanceVertTo = distance.get(edge.to);

						float newDistance = distanceVertFrom + edge.weight;
						if (newDistance < distanceVertTo)
						{
							distance.set(edge.to, newDistance);
							parents.set(edge.to, edge.from);
						}
				}));

				/* step 3: negative-weight cycle detection */
				NegativeWeightCycleDetection<T> negativeWeightCycleDetection = new NegativeWeightCycleDetection<T>(distance);
				graph.edges.forEach(negativeWeightCycleDetection::detectFor);

				_parents = parents.asMap();
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

		private static class Distance<T>
		{
			private final int min;
			private final int max;

			private float[] distances;

			public Distance(Vertices<T> verts, float initialValue)
			{
				// determine min id && max
				ArrayList<Vertex<T>> asList = new ArrayList<>(verts.asReadonlyList());
				asList.sort(Comparator.comparing(v -> v.id));

				this.min = asList.get(0).id;
				this.max = asList.get(asList.size() - 1).id;

				distances = new float[max - min + 1];

				// initialize
				Arrays.fill(distances, initialValue);
			}

			public float get(Vertex<T> vert)
			{
				return distances[vert.id - min];
			}

			public void set(Vertex<T> vert, float newValue)
			{
				distances[vert.id - min] = newValue;
			}
		}

		private static class Parents<T>
		{
			private final int min;
			private final int max;
			private final Vertices<T> verts;

			private Vertex[] edges;

			public Parents(Vertices<T> verts)
			{
				this.verts = verts;
				// determine min id && max
				ArrayList<Vertex<T>> asList = new ArrayList<>(verts.asReadonlyList());
				asList.sort(Comparator.comparing(v -> v.id));

				this.min = asList.get(0).id;
				this.max = asList.get(asList.size() - 1).id;

				edges = new Vertex[max - min + 1];

				// initialize
				Arrays.fill(edges, null);
			}

			public void set(Vertex<T> from, Vertex<T> to)
			{
				edges[from.id - min] = to;
			}

			private Vertex<T> get(Vertex<T> v)
			{
				return edges[v.id - min];
			}

			public Map<Vertex<T>, Vertex<T>> asMap()
			{
				Map<Vertex<T>, Vertex<T>> map = new IdentityHashMap<>(verts.count());

				verts.forEach(v -> {

					Vertex<T> to = get(v);

					if (to != null)
						map.put(v, to);
				});

				return map;
			}
		}

		private static class NegativeWeightCycleDetection<T>
		{
			private final Distance<T> distance;

			public NegativeWeightCycleDetection(Distance<T> distance)
			{
				this.distance = distance;
			}

			public void detectFor(DirectedWeightedEdges.DirectedWeightedEdge<T> edge)
			{
				if (distance.get(edge.from) + edge.weight < distance.get(edge.to))
				{
					// todo: emit more info
					throw new RuntimeException("graph contains a negative-weight cycle");
				}
			}
		}
	}
}
