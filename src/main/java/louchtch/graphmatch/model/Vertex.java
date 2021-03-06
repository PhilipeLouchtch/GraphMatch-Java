package louchtch.graphmatch.model;

public class Vertex<T>
{
	private static final Object lock = new Object();
	private static int globalVertexCount = 0;

	public boolean isVisited;
	private T content;
	public final int id;

	public Vertex(T content)
	{
		this.content = content;

		// used for array-based optimizations
		synchronized (lock) {
			this.id = globalVertexCount++;
		}
	}

	public T content()
	{
		return content;
	}

	@Override
	public String toString()
	{
		return "Vertex: " + content;
	}

	public DirectedWeightedEdges.DirectedWeightedEdge<T> makeEdgeTo(Vertex<? extends T> toVertex, int weight)
	{
		return DirectedWeightedEdges.DirectedWeightedEdge.between(this, toVertex, weight);
	}
}
