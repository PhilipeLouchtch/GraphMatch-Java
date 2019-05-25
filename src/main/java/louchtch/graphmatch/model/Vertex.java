package louchtch.graphmatch.model;

public class Vertex<TContent>
{
	private TContent content;

	public Vertex(TContent content)
	{
		this.content = content;
	}

	public TContent content()
	{
		return content;
	}

	/* Singleton types */
	private static final Vertex<?> sourceVertex = new Vertex<>(null);
	private static final Vertex<?> sinkVertex = new Vertex<>(null);

	/**
	 * The Source vertex
	 * @return
	 */
	public static <T> Vertex<T> source()
	{
		return (Vertex<T>) sourceVertex;
	}

	/**
	 * The Sink vertex
	 * @return
	 */
	public static <T> Vertex<T> sink()
	{
		return (Vertex<T>) sinkVertex;
	}
}
