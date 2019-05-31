import louchtch.graphmatch.matching.MaxFlowMatching;
import louchtch.graphmatch.model.*;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.*;

public class MaxFlowMatchingTest
{
	@Test
	public void simpleMatchingScenario()
	{
		DirectedWeightedEdges<String> edges = new DirectedWeightedEdges<>();

		// people
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

		// A can do 1, 2
		edges.add(p_a.makeEdgeTo(t_1, 1));
		edges.add(p_a.makeEdgeTo(t_2, 1));

		// B only 1
		edges.add(p_b.makeEdgeTo(t_1, 1));

		// C can do 2, 3
		edges.add(p_c.makeEdgeTo(t_2, 1));
		edges.add(p_c.makeEdgeTo(t_3, 1));

		// D can do 3, 4, 5
		edges.add(p_d.makeEdgeTo(t_3, 1));
		edges.add(p_d.makeEdgeTo(t_4, 1));
		edges.add(p_d.makeEdgeTo(t_5, 1));

		// E only 5
		edges.add(p_e.makeEdgeTo(t_5, 1));


		Vertices<String> left = new Vertices<String>().with(p_a).with(p_b).with(p_c).with(p_d).with(p_e);
		Vertices<String> right = new Vertices<String>().with(t_1).with(t_2).with(t_3).with(t_4).with(t_5);

		MaxFlowGraph<String> graph = new MaxFlowGraph<>(left, right, edges);

		MaxFlowMatching<String> matching = new MaxFlowMatching<>(graph, SearchType.MinCost);
		List<Edge<String>> matchingAslist = matching.asListOfEdges();

		Assert.assertThat(matchingAslist, hasItems(
			Edge.between(p_b, t_1),
			Edge.between(p_a, t_2),
			Edge.between(p_c, t_3),
			Edge.between(p_d, t_4),
			Edge.between(p_e, t_5)
		));

		Assert.assertEquals(5, matchingAslist.size());
	}
}