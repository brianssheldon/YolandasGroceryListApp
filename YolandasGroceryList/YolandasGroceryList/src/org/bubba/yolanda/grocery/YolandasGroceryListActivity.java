package org.bubba.yolanda.grocery;

import java.util.Iterator;
import java.util.List;

import org.bubba.yolanda.grocery.knownitems.KnownItem;
import org.bubba.yolanda.grocery.knownitems.KnownItemsDao;
import org.bubba.yolanda.grocery.list.GroceryItem;
import org.bubba.yolanda.grocery.list.GroceryListDao;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

public class YolandasGroceryListActivity extends ListActivity
{
	private KnownItemsDao knownItemsDao;
	private GroceryListDao groceryListDao;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		loadKnownItemsView();
		
		loadGroceryItems();
		
		getListView().setOnItemClickListener(getItemClickListener());
	}

	private OnItemClickListener getItemClickListener()
	{
		OnItemClickListener listViewOnClickListener = new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
			{
				final AdapterView av = arg0;
				List<GroceryItem> allItems = groceryListDao.getAllItems();
				
				if(allItems == null || arg2 >= allItems.size() || arg2 < 0)
				{
					return;
				}
				
				final GroceryItem item = allItems.get(arg2);
				
				if(item == null) return;
				
		        new AlertDialog.Builder(arg0.getContext())
			        .setIcon(android.R.drawable.ic_dialog_alert)
			        .setTitle("Delete Item?")
			        .setMessage("Do you want to delete\n\n" + item.getItem() + "?")
			        .setPositiveButton("Delete", new DialogInterface.OnClickListener() 
			        {
			            @Override
			            public void onClick(DialogInterface dialog, int which)
			            {
							groceryListDao.deleteItem(item);
							List<GroceryItem> groceryItems = getGroceryList();
							ArrayAdapter<GroceryItem> adapter = new ArrayAdapter<GroceryItem>(
									av.getContext(), 
									android.R.layout.simple_list_item_1, 
									groceryItems);
							setListAdapter(adapter);
			            }
			        })
			        .setNegativeButton("cancel", null)
			        .show();
			}
		};
		return listViewOnClickListener;
	}

	private void loadGroceryItems()
	{
		List<GroceryItem> groceryItems = getGroceryList();
		ArrayAdapter<GroceryItem> adapter = new ArrayAdapter<GroceryItem>(
				this, android.R.layout.simple_list_item_1, groceryItems);
		setListAdapter(adapter);
	}

	private void loadKnownItemsView()
	{
		List<KnownItem> knownItems = getKnownItems();
		String[] knownArray = new String[knownItems.size()];
		int i = 0;
		for (Iterator<KnownItem> iterator = knownItems.iterator(); iterator.hasNext();)
		{
			knownArray[i] = ((KnownItem) iterator.next()).getItem();
			i ++;
		}

	   ArrayAdapter<String> adapter2 = 
	         new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, knownArray);
	   
	   AutoCompleteTextView actvDev = (AutoCompleteTextView)findViewById(R.id.actv);
	   actvDev.setThreshold(1);
	   actvDev.setAdapter(adapter2);

	   actvDev.setOnItemClickListener(new AutoCompleteListener());
	}

    private final class AutoCompleteListener implements OnItemClickListener
	{
		public void onItemClick(AdapterView<?> parent, View textView, int position, long id)
		{	// they have selected an item from the dropdown list. add it to the grocery list
			String name = ((TextView)textView).getText().toString(); // get selected item
			
			if(name == null || "".equals(name))
			{
				return;
			}

			groceryListDao.createItem(name);

			((AutoCompleteTextView)findViewById(R.id.actv)).setText("");

			List<GroceryItem> groceryItems = getGroceryList();
			ArrayAdapter<GroceryItem> adapter = new ArrayAdapter<GroceryItem>(parent.getContext(), android.R.layout.simple_list_item_1, groceryItems);
			setListAdapter(adapter);
		}
	}
    
	private List<KnownItem> getKnownItems()
	{
		knownItemsDao = new KnownItemsDao(this);
		knownItemsDao.open();
		List<KnownItem> values = knownItemsDao.getAllItems();
		
		if(values != null && values.size() > 0) 
		{
			return values; // the table is already loaded. don't load it again.
		}
		
		String[] hardCodedItems = getResources().getStringArray(R.array.food_array);
		
		for (int i = 0; i < hardCodedItems.length; i++)
		{
			knownItemsDao.createKnownItem(hardCodedItems[i]);
		}
		
		values = knownItemsDao.getAllItems();
		return values;
	}

	private List<GroceryItem> getGroceryList()
	{
		groceryListDao = new GroceryListDao(this);
		groceryListDao.open();
		return groceryListDao.getAllItems();
	}

	// Will be called via the onClick attribute of the buttons in main.xml
	public void onClick(View view)
	{
		@SuppressWarnings("unchecked")
		ArrayAdapter<KnownItem> adapter = (ArrayAdapter<KnownItem>) getListAdapter();
		KnownItem item = null;
		
		switch (view.getId())
		{
			case R.id.add:
				String text = ((AutoCompleteTextView)findViewById(R.id.actv)).getText().toString();

				item = knownItemsDao.createKnownItem(text);
				loadKnownItemsView();
				
				groceryListDao.createItem(text);
				
				((AutoCompleteTextView)findViewById(R.id.actv)).setText("");
	
				List<GroceryItem> groceryItems = getGroceryList();
				ArrayAdapter<GroceryItem> adapter2 = new ArrayAdapter<GroceryItem>(view.getContext(), android.R.layout.simple_list_item_1, groceryItems);
				setListAdapter(adapter2);
				break;
			}
			adapter.notifyDataSetChanged();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{	// only called once - creates the menu
	    MenuInflater inflater = getMenuInflater();

	    inflater.inflate(R.menu.mainmenu, menu);
	    
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{	// called when they have selected a menu option

	    int itemId = item.getItemId();
	    
		switch (itemId)
	    {	
		    
		    case R.id.addFromBigList:	// go to screen to select items from big list
		    	Intent bigListIntent = new Intent(this, BigListActivity.class);
		    	startActivityForResult(bigListIntent, 101);
		    	return true;
		    	
		    default:
		        return super.onOptionsItemSelected(item);
	    }
	}

	@Override
    public void onActivityResult(int requestCode,int resultCode,Intent data)
    {	// after we get back from BigListActivity etc, reload page
	     super.onActivityResult(requestCode, resultCode, data);

	     switch(resultCode) 
	     {
	     	case 92:
	     		Toast.makeText(this, "barcode not found. sorry", Toast.LENGTH_SHORT).show();
	     		break;
	     	case 93:
//	     		addTextView();
	     		break;
	     }
	     
	     loadGroceryItems();
    }
	
	@Override
	protected void onResume()
	{
		knownItemsDao.open();
		groceryListDao.open();
		super.onResume();
	}

	@Override
	protected void onPause()
	{
		knownItemsDao.close();
		groceryListDao.close();
		super.onPause();
	}
}