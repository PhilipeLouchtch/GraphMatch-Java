import louchtch.graphmatch.matching.MaxFlowMatching;
import louchtch.graphmatch.model.*;
import org.junit.Test;

import java.util.List;

public class MaxFlowMatchingTest
{
	@Test
	public void asListOfEdges()
	{
		Vertices<String> left = new Vertices<>();
		Vertices<String> right = new Vertices<>();


		// poeple
		Vertex<String> p_a = new Vertex<>("a");
		Vertex<String> p_b = new Vertex<>("b");
		Vertex<String> p_c = new Vertex<>("c");
		Vertex<String> p_d = new Vertex<>("d");
		Vertex<String> p_e = new Vertex<>("e");

		// tasks
		Vertex<String> t_1 = new Vertex<>("1");
		Vertex<String> t_2 = new Vertex<>("2");
		Vertex<String> t_3 = new Vertex<>("3");
		Vertex<String> t_4 = new Vertex<>("4");
		Vertex<String> t_5 = new Vertex<>("5");

		DirectedWeightedEdges<String> edges = new DirectedWeightedEdges<>();

		edges.add(p_a.makeEdgeTo(t_1, 1));
		edges.add(p_a.makeEdgeTo(t_2, 1));

		edges.add(p_b.makeEdgeTo(t_1, 1));

		edges.add(p_c.makeEdgeTo(t_2, 1));
		edges.add(p_c.makeEdgeTo(t_3, 1));

		edges.add(p_d.makeEdgeTo(t_3, 1));
		edges.add(p_d.makeEdgeTo(t_4, 1));
		edges.add(p_d.makeEdgeTo(t_5, 1));

		edges.add(p_e.makeEdgeTo(t_5, 1));

		MaxFlowGraph<String> graph = new MaxFlowGraph<>(
			left.with(p_a).with(p_b).with(p_c).with(p_d).with(p_e),
			right.with(t_1).with(t_2).with(t_3).with(t_4).with(t_5),
			edges
		);

		MaxFlowMatching<String> matching = new MaxFlowMatching<>(graph, SearchType.MinCost);
		List<Edge<String>> matchingAslist = matching.asListOfEdges();

		System.out.println("done");
	}
}