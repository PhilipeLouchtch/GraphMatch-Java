package louchtch.graphmatch.model;

public class Graph<T>
{
	private final Vertices<T> vertices;
	private final DirectedEdges<T> edges;

	public Graph(Vertices<T> vertices, DirectedEdges<T> edges)
	{
		this.vertices = vertices;
		this.edges = edges;
	}
}
