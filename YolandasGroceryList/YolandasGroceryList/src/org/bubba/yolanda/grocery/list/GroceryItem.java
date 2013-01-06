package org.bubba.yolanda.grocery.list;

public class GroceryItem
{
	private long id;
	private String item;
	private int quantity;

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
	public int getQuantity()
	{
		return quantity;
	}
	public void setQuantity(int quantity)
	{
		this.quantity = quantity;
	}
	// Will be used by the ArrayAdapter in the ListView
	@Override
	public String toString()
	{
		return (item + "                                                  ").substring(0, 40) + "  " + quantity;
	}
}