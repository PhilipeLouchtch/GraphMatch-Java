package louchtch.graphmatch.model;

public class MaxFlowGraph<T>
{
	private final Vertices<T> vertices;
	private final DirectedEdges<T> edges;

	private final Vertex<T> source;
	private final Vertex<T> sink;

	public MaxFlowGraph(Vertices<T> left, Vertices<T> right, DirectedEdges<T> edges)
	{
		this.source = Vertex.source();
		this.sink = Vertex.sink();

		Vertices<T> allVertices = left.with(right).with(sink).with(source);
		this.vertices = allVertices;

		DirectedEdges<T> edgesFromSourceToLeftVertices = source.makeEdgesTo(left);
		DirectedEdges<T> edgesFromRightVerticesToSink = right.makeEdgesTo(sink);
		this.edges = edges.with(edgesFromSourceToLeftVertices).with(edgesFromRightVerticesToSink);
	}
}
