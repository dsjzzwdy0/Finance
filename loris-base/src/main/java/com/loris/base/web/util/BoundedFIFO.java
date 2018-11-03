package com.loris.base.web.util;


public class BoundedFIFO
{
	/** Default resize increment value. */
	private static final int RESIZE_INCREMENT = 500;

	private Object[] buf;
	private int numElements = 0;
	private int first = 0;
	private int next = 0;
	private int maxSize;
	private int resize_increment;
	private Object current;

	//private static Logger log = Logger.getLogger(BoundedFIFO.class);

	/**
	 *
	 * @param maxSize
	 * @param resize_increment
	 *            If the capacity of the buffer reaches its maximum
	 *            it will resize automatically. RESIZE_VOLUME indicates the
	 *            increment by
	 *            which the buffer wil enlarge
	 */
	public BoundedFIFO(int maxSize, int resize_increment)
	{

		this.resize_increment = resize_increment;

		if (maxSize < 1)
		{
			throw new IllegalArgumentException("The maxSize argument (" + maxSize + ") is not a positive integer.");
		}

		this.maxSize = maxSize;
		buf = new Object[maxSize];
	}

	public BoundedFIFO(int maxSize)
	{
		this(maxSize, BoundedFIFO.RESIZE_INCREMENT);
	}

	/**
	 * Get the first element in the buffer. Returns <code>null</code> if
	 * there are no elements in the buffer.
	 */

	public Object get()
	{
		synchronized (this.buf)
		{
			if (numElements == 0)
			{
				current = null;
				return null;
			}

			Object r = buf[first];
			buf[first] = null; // help garbage collection

			if (++first == maxSize)
			{
				first = 0;
			}
			numElements--;
			current = r;
			return r;
		}
	}
	
	/**
	 * 获得当前的对象	 * 
	 * @return 返回当前的对象
	 */
	public Object getCurrent()
	{
		return current;
	}
	
	/**
	 * Clear the buffer.
	 * 
	 */
	public void clear()
	{
		for(int i = 0; i < numElements; i ++)
		{
			buf[i] = null;
		}
		first = 0;
		next = 0;
		numElements = 0;
	}

	/**
	 * Place a {@link LoggingEvent} in the buffer. If the buffer is full
	 * it will be automatically resized.
	 */
	public void put(Object o)
	{
		synchronized (this.buf)
		{
			if ((numElements + 1) == maxSize)
			{
				this.resize();
			}

			if (numElements != maxSize)
			{
				buf[next] = o;
				if (++next == maxSize)
				{
					next = 0;
				}
				numElements++;
			}
		}
	}

	/**
	 * Resize the bounded fifo.
	 */
	public void resize()
	{
		// synchronized (this.buf) {
		Object[] buf2 = new Object[maxSize + resize_increment];
		System.arraycopy(buf, 0, buf2, 0, maxSize);
		this.buf = buf2;
		this.maxSize = this.maxSize + resize_increment;
		// }
		//log.debug(" BoundedFIFO buffer resized to: " + this.maxSize);
	}

	/**
	 * Get the maximum size of the buffer.
	 */

	public int getMaxSize()
	{
		return maxSize;
	}

	/**
	 * Return <code>true</code> if the buffer is full, i.e. of the
	 * number of elements in the buffer equals the buffer size.
	 */

	public boolean isFull()
	{
		return numElements == maxSize;
	}

	/**
	 * Get the number of elements in the buffer. This number is
	 * guaranteed to be in the range 0 to <code>maxSize</code>
	 * (inclusive).
	 */

	public int length()
	{
		return numElements;
	}

	/**
	 * For Debug purposes ONLY
	 */
	public void printBuffer()
	{
		synchronized (this.buf)
		{
			for (int i = 0; i < buf.length; i++)
			{
				System.out.print(this.buf[i] + " ");
			}
			System.out.println("");
		}
	}

}
