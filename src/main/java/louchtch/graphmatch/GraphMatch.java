package louchtch.graphmatch;

import louchtch.graphmatch.matching.Matching;
import louchtch.graphmatch.matching.MaxFlowMatching;
import louchtch.graphmatch.model.*;

public class GraphMatch
{
	public <T> Matching match(Vertices<T> left, Vertices<T> right, DirectedEdges<T> edges, SearchType searchType)
	{
		Vertex<T> source = Vertex.source();
		Vertex<T> sink = Vertex.sink();

		Vertices<T> allVertices = left.with(right).with(sink).with(source);

		edges = edges.with(DirectedEdges.make(source, left));
		edges = edges.with(DirectedEdges.make(right, sink));

		Graph<T> graph = new Graph<>(allVertices, edges);

		return new MaxFlowMatching(graph, searchType);
	}
}
