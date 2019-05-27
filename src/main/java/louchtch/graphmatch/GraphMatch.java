package louchtch.graphmatch;

import louchtch.graphmatch.matching.Matching;
import louchtch.graphmatch.matching.MaxFlowMatching;
import louchtch.graphmatch.model.*;

public class GraphMatch
{
	public <T> Matching match(Vertices<T> left, Vertices<T> right, DirectedWeightedEdges<T> edges, SearchType searchType)
	{
		MaxFlowGraph<T> graph = new MaxFlowGraph<>(left, right, edges);

		return new MaxFlowMatching(graph, searchType);
	}
}
