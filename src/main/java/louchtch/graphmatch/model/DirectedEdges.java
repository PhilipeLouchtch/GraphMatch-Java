package louchtch.graphmatch.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DirectedEdges<T>
{
	List<DirectedEdge<T>> edges;

	public DirectedEdges(List<DirectedEdge<T>> edges)
	{
		this.edges = edges;
	}

	public static <T> DirectedEdges<T> make(Vertex<T> from, Vertices<T> to)
	{
		List<DirectedEdge<T>> edges = to.stream()
			.map(toVertex -> DirectedEdge.make(from, toVertex))
			.collect(Collectors.toList());

		return new DirectedEdges<T>(edges);
	}

	public static <T> DirectedEdges<T> make(Vertices<T> from, Vertex<T> to)
	{
		List<DirectedEdge<T>> edges = from.stream()
			.map(fromVertex -> DirectedEdge.make(fromVertex, to))
			.collect(Collectors.toList());

		return new DirectedEdges<>(edges);
	}

	public DirectedEdges<T> with(DirectedEdges<T> others)
	{
		ArrayList<DirectedEdge<T>> combined = new ArrayList<>(this.edges.size() + others.edges.size());
		combined.addAll(others.edges);

		return new DirectedEdges<>(combined);
	}

	public static class DirectedEdge<T>
	{
		public final Vertex<T> from;
		public final Vertex<T> to;

		private DirectedEdge(Vertex<T> from, Vertex<T> to)
		{
			this.from = from;
			this.to = to;
		}

		public static <T> DirectedEdge<T> make(Vertex<T> from, Vertex<T> to)
		{
			return new DirectedEdge<>(from, to);
		}
	}
}
