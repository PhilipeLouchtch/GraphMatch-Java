package louchtch.graphmatch.model;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class Vertex<TContent>
{
	private TContent content;

	public Vertex(TContent content)
	{
		this.content = content;
	}

	public TContent content()
	{
		return content;
	}

	public DirectedEdges<TContent> makeEdgesTo(Vertices<TContent> toVertices)
	{
		List<DirectedEdges.DirectedEdge<TContent>> edges = toVertices.listOfVertices.stream()
			.map(toVertex -> DirectedEdges.DirectedEdge.make(this, toVertex))
			.collect(Collectors.toList());

		return new DirectedEdges<>(edges);
	}

	/* Singleton types */
	private static final Vertex<?> sourceVertex = new Vertex<>(null);
	private static final Vertex<?> sinkVertex = new Vertex<>(null);

	/**
	 * The Source vertex
	 * @return
	 */
	public static <T> Vertex<T> source()
	{
		return (Vertex<T>) sourceVertex;
	}

	/**
	 * The Sink vertex
	 * @return
	 */
	public static <T> Vertex<T> sink()
	{
		return (Vertex<T>) sinkVertex;
	}
}
