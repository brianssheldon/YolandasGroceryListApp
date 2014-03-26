package org.bubba.yolanda.grocery.list;

public class ListItem
{
	private long id;
	private String tableName;
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
		if(item.length() > 24) return item + " " + quantity;
		
		return (item + "                                                  ").substring(0, 25) + " " + quantity;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
}