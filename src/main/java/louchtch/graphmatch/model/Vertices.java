package louchtch.graphmatch.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Vertices<VertexContent>
{
	private List<Vertex<VertexContent>> listOfVertices;

	public Vertices()
	{
		listOfVertices = new ArrayList<>();
	}

	private Vertices(List<Vertex<VertexContent>> vertices)
	{
		listOfVertices = vertices;
	}

	public Stream<Vertex<VertexContent>> stream()
	{
		return listOfVertices.stream();
	}

	public Vertices<VertexContent> with(Vertices<VertexContent> others)
	{
		List<Vertex<VertexContent>> combined = new ArrayList<>(this.listOfVertices.size() + others.listOfVertices.size());
		combined.addAll(this.listOfVertices);
		combined.addAll(others.listOfVertices);

		return new Vertices<>(combined);
	}

	public Vertices<VertexContent> with(Vertex<VertexContent> other)
	{
		List<Vertex<VertexContent>> combined = new ArrayList<>(this.listOfVertices);
		combined.add(other);

		return new Vertices<>(combined);
	}
}
