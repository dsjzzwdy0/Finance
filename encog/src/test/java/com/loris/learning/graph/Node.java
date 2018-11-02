package com.loris.learning.graph;

import java.util.UUID;

/**
 * Node
 * @author dsj
 *
 */
public class Node
{
	private String id;					//ID值
	protected String name;				//名称
	protected int index;				//序号
	
	/**
	 * Create a new Node.
	 */
	public Node()
	{
		id = UUID.randomUUID().toString();
	}
	
	public Node(String id, String name)
	{
		this.id = id;
		this.name = name;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public int getIndex()
	{
		return index;
	}

	public void setIndex(int index)
	{
		this.index = index;
	}

	@Override
	public boolean equals(Object obj)
	{
		if(this == obj)
		{
			return true;
		}
		if(!(obj instanceof Node))
		{
			return false;
		}
		else
		{
			Node other = (Node)obj;
			return equals(other);
		}
	}
	
	public boolean equals(Node other)
	{
		if(this == other)
		{
			return true;
		}
		if(id.equals(other.getId()))
		{
			return true;
		}
		if(index == other.getIndex())
		{
			return true;
		}
		return false;
	}
}
