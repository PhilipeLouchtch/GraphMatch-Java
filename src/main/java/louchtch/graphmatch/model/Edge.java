package louchtch.graphmatch.model;

import java.util.Objects;

public class Edge<T>
{
	public final Vertex<T> from;
	public final Vertex<T> to;

	protected Edge(Vertex<T> from, Vertex<T> to)
	{
		this.from = from;
		this.to = to;
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o)
		{
			return true;
		}
		if (o == null || getClass() != o.getClass())
		{
			return false;
		}
		Edge<?> other = (Edge<?>) o;
		return from == other.from && to == other.to;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(from, to);
	}

	public static <T> Edge<T> between(Vertex<T> from, Vertex<T> to)
	{
		return new Edge<>(from, to);
	}
}
