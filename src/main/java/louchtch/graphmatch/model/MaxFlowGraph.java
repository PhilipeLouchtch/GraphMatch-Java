package louchtch.graphmatch.model;

import louchtch.graphmatch.matching.MaxFlowMatching;

public class MaxFlowGraph<T>
{
	public final Vertices<T> vertices;
	public final DirectedWeightedEdges<T> edges;

	public final Vertex<T> source;
	public final Vertex<T> sink;

	public final Vertices<T> left;
	public final Vertices<T> right;

	public MaxFlowGraph(Vertices<T> left, Vertices<T> right, DirectedWeightedEdges<T> edges)
	{
		this.source = new Vertex<>(null);
		this.sink = new Vertex<>(null);

		Vertices<T> allVertices = left.with(right).with(sink).with(source);
		this.vertices = allVertices;

		this.left = left;
		this.right = right;

		DirectedWeightedEdges<T> edgesSourceToLeft = new DirectedWeightedEdges<>();
		left.forEach(leftVertex -> edgesSourceToLeft.add(source.makeEdgeTo(leftVertex, 1))); // todo proper weight

		DirectedWeightedEdges<T> edgesRightToSink = new DirectedWeightedEdges<>();
		right.forEach(rightVertex -> edgesRightToSink.add(rightVertex.makeEdgeTo(sink, 1))); // todo proper weight?

		this.edges = new DirectedWeightedEdges<>();

		// fixme: make edges immutable
		this.edges.add(edges);
		this.edges.add(edgesSourceToLeft);
		this.edges.add(edgesRightToSink);
	}

	// for when edges are immuatble again
//	private MaxFlowGraph(Vertices<T> all, DirectedWeightedEdges<T> edges, Vertex<T> source, Vertex<T> sink)
//	{
//		this.source = source;
//		this.sink = sink;
//
//		this.vertices = all;
//		this.edges = edges;
//	}

	public void augmentAlong(MaxFlowMatching.BellmanFordAugmentingPath<T> path)
	{
		path.forEach(edge -> {
			this.edges.turnResidiual(edge);
		});
	}
}
