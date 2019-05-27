package louchtch.graphmatch.model;

import java.util.List;
import java.util.stream.Collectors;

public class Vertex<T>
{
	public boolean isVisited;
	private T content;

	public Vertex(T content)
	{
		this.content = content;
	}

	public T content()
	{
		return content;
	}

	public DirectedWeightedEdges<T> makeEdgesTo(Vertices<T> toVertices)
	{
		List<DirectedWeightedEdges.DirectedWeightedEdge<T>> edges = toVertices.listOfVertices.stream()
			.map(toVertex -> DirectedWeightedEdges.DirectedWeightedEdge.between(this, toVertex, 1)) // TODO
			.collect(Collectors.toList());

		return new DirectedWeightedEdges<>(edges);
	}
}
