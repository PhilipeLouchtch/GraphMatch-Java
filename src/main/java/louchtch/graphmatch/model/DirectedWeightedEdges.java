package louchtch.graphmatch.model;

import java.util.*;
import java.util.function.Consumer;

// TODO: make immutable
public class DirectedWeightedEdges<T>
{
	Map<Vertex<T>, Map<Vertex<T>, DirectedWeightedEdge<T>>> edges;

	public DirectedWeightedEdges()
	{
		this.edges = new IdentityHashMap<>();
	}

	public void forEach(Consumer<DirectedWeightedEdge<T>> fn)
	{
		edges.forEach((fromVertex, mapTo) ->
			mapTo.forEach((toVertex, edge) ->
				fn.accept(edge)
			)
		);
	}

	public void turnResidiual(Edge<T> edge)
	{
		// edgeWeight =  -edges[u][v]
		int edgeWeight = edges.get(edge.from).get(edge.to).weight;

		// edges[v] ||= {}
		if (!edges.containsKey(edge.to)) {
			edges.put(edge.to, new IdentityHashMap<>());
		}

		// edges[v][u] = -edges[u][v]
		edges.get(edge.to).put(edge.from, DirectedWeightedEdge.between(edge.to, edge.from, -edgeWeight));

		// edges[u].delete v
		edges.get(edge.from).remove(edge.to);
	}

	public void add(DirectedWeightedEdge<T> other)
	{
		if (!edges.containsKey(other.from)) {
			edges.put(other.from, new IdentityHashMap<>());
		}

		edges.get(other.from).put(other.to, other);
	}

	public void add(DirectedWeightedEdges<T> others)
	{
		others.forEach(other -> add(other));
	}

	public static class DirectedWeightedEdge<T> extends Edge<T>
	{
		public final int weight;

		private DirectedWeightedEdge(Vertex<T> from, Vertex<T> to, int weight)
		{
			super(from, to);
			this.weight = weight;
		}

		public static <T> DirectedWeightedEdge<T> between(Vertex<T> from, Vertex<T> to, int weight)
		{
			return new DirectedWeightedEdge<>(from, to, weight);
		}

		@Override
		public boolean equals(Object o)
		{
			if (this == o)
			{
				return true;
			}
			if (o == null || getClass() != o.getClass())
			{
				return false;
			}
			DirectedWeightedEdge<?> that = (DirectedWeightedEdge<?>) o;
			return weight == that.weight && from == that.from && to == that.to;
		}

		@Override
		public int hashCode()
		{
			return Objects.hash(weight, from, to);
		}
	}
}
