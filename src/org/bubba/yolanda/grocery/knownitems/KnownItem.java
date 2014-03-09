package org.bubba.yolanda.grocery.knownitems;

public class KnownItem
{
	private long id;
	private String item;

	public KnownItem(String item)
	{
		this.item = item;
	}
	
	public KnownItem()
	{
		this.item = "";
	}
	
	public long getId()
	{
		return id;
	}
	public void setId(long id)
	{
		this.id = id;
	}
	public String getItem()
	{
		return item;
	}
	public void setItem(String item)
	{
		this.item = item;
	}
	
	// Will be used by the ArrayAdapter in the ListView
	@Override
	public String toString()
	{
		return item;
	}
}