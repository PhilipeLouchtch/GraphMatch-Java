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

	public DirectedWeightedEdges.DirectedWeightedEdge<T> makeEdgeTo(Vertex<? extends T> toVertex, int weight)
	{
		return DirectedWeightedEdges.DirectedWeightedEdge.between(this, toVertex, weight);
	}
}
