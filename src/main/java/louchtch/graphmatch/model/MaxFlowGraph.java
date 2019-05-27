package louchtch.graphmatch.model;

import louchtch.graphmatch.matching.MaxFlowMatching;

public class MaxFlowGraph<T>
{
	public final Vertices<T> vertices;
	public final DirectedWeightedEdges<T> edges;

	public final Vertex<T> source;
	public final Vertex<T> sink;

	public MaxFlowGraph(Vertices<T> left, Vertices<T> right, DirectedWeightedEdges<T> edges)
	{
		this.source = new Vertex<>(null);
		this.sink = new Vertex<>(null);

		Vertices<T> allVertices = left.with(right).with(sink).with(source);
		this.vertices = allVertices;

		DirectedWeightedEdges<T> edgesFromSourceToLeftVertices = source.makeEdgesTo(left);
		DirectedWeightedEdges<T> edgesFromRightVerticesToSink = right.makeEdgesTo(sink);
		this.edges = edges;

		// fixme: make edges immutable
		this.edges.add(edgesFromSourceToLeftVertices);
		this.edges.add(edgesFromRightVerticesToSink);
	}

	// for when edges are immuatble again
	private MaxFlowGraph(Vertices<T> all, DirectedWeightedEdges<T> edges, Vertex<T> source, Vertex<T> sink)
	{
		this.source = source;
		this.sink = sink;

		this.vertices = all;
		this.edges = edges;
	}

	public void augmentAlong(MaxFlowMatching.BellmanFordAugmentingPath<T> path)
	{
		path.forEach(edge -> {
			this.edges.turnResidiual(edge);
		});
	}
}
