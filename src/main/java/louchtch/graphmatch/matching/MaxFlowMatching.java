package louchtch.graphmatch.matching;

import louchtch.graphmatch.model.MaxFlowGraph;
import louchtch.graphmatch.model.SearchType;

import java.util.Optional;

public class MaxFlowMatching<T> implements Matching
{
	private final MaxFlowGraph<T> graph;
	private final SearchType searchType;

	public MaxFlowMatching(MaxFlowGraph<T> graph, SearchType searchType)
	{
		this.graph = graph;
		this.searchType = searchType;
	}

	@Override
	public Object stuff()
	{
		return null;
	}

	private Optional<Path> findAugmentingPath()
	{
		// searh type distinction -- todo: move to own implementations of MaxFlowMatching instead
		// todo: implement shortest path or remove from SearchType enum
//		if (searchType == SearchType.ShortestPath)
//		{
//
//		}

		//
		if (searchType == SearchType.MinCost)
		{

		}

		throw new RuntimeException("Unsupported search type: " + searchType);
	}

	private class Path
	{
	}
}
