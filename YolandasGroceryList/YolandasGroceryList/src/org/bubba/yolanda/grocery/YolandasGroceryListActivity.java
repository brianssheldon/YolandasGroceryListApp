package org.bubba.yolanda.grocery;

import java.util.Iterator;
import java.util.List;

import org.bubba.yolanda.edittextphonenbrs.EditTextMsgNumbersActivity;
import org.bubba.yolanda.edittextphonenbrs.TextMsgUtils;
import org.bubba.yolanda.grocery.knownitems.KnownItem;
import org.bubba.yolanda.grocery.knownitems.KnownItemsDao;
import org.bubba.yolanda.grocery.list.GroceryItem;
import org.bubba.yolanda.grocery.list.GroceryListDao;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

public class YolandasGroceryListActivity extends ListActivity
{
	private KnownItemsDao knownItemsDao = null;
	private GroceryListDao groceryListDao = null;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		opendbs();
		loadKnownItemsView();
		
		loadGroceryItems();
		
		getListView().setOnItemClickListener(getItemClickListener());
	}

	private void opendbs()
	{
		if(groceryListDao == null)
		{
			groceryListDao = new GroceryListDao(this);
			groceryListDao.open();
		}
		
		if(knownItemsDao == null)
		{
			knownItemsDao = new KnownItemsDao(this);
			knownItemsDao.open();
		}
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
				
		        CharSequence[] items = new CharSequence[103];
		        items[0] = "Delete Item   '" + item.getItem() + "' ?";
		        items[1] = "Cancel";
		        for (int i = 0; i < 101; i++) items[i+2]=""+(i+1);
				
		        
		        AlertDialog.Builder builder = new AlertDialog.Builder(arg0.getContext());
		        builder.setIcon(android.R.drawable.ic_dialog_alert);
		        builder.setTitle("Delete Item   '" + item.getItem() + "'");
		        builder.setItems(items, new OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						if(which == 0)
						{
							groceryListDao.deleteItem(item);
							loadGroceryItems();
						}
						else if(which == 1)
						{
							return;
						}
						else if(which > 1)
						{
							item.setQuantity(which - 1);
							saveItem(item);
							loadGroceryItems();
						}
					}
				});
		        AlertDialog alert = builder.create();
		        alert.show();
		        alert.getWindow().setLayout(400, 600);
			}
		};
		return listViewOnClickListener;
	}
	
	private void loadGroceryItems()
	{
		List<GroceryItem> groceryItems = getGroceryList();
		ArrayAdapter<GroceryItem> adapter = new ArrayAdapter<GroceryItem>(
				this, R.layout.list_item, groceryItems);
		setListAdapter(adapter);
	}

	@SuppressLint("NewApi")
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
			if(textView == null
				|| ((TextView)textView).getText() == null
				|| ((TextView)textView).getText().toString() == "") return;
			
			String name = ((TextView)textView).getText().toString(); // get selected item
			
			groceryListDao.createItem(name, 1);

			((AutoCompleteTextView)findViewById(R.id.actv)).setText("");

			loadGroceryItems();
		}
	}
    
	private List<KnownItem> getKnownItems()
	{
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
		opendbs();
		return groceryListDao.getAllItems();
	}

	private void saveItem(GroceryItem item)
	{
		opendbs();
		groceryListDao.updateItem(item);
	}

	// Will be called via the onClick attribute of the buttons in main.xml
	public void onClick(View view)
	{
		@SuppressWarnings("unchecked")
		ArrayAdapter<KnownItem> adapter = (ArrayAdapter<KnownItem>) getListAdapter();
		
		switch (view.getId())
		{
			case R.id.add:
				String text = ((AutoCompleteTextView)findViewById(R.id.actv)).getText().toString();
				if(text == null || text.trim().length() == 0) return;
				knownItemsDao.createKnownItem(text);
				loadKnownItemsView();
				
				groceryListDao.createItem(text, 1);
				
				((AutoCompleteTextView)findViewById(R.id.actv)).setText("");
	
				List<GroceryItem> groceryItems = getGroceryList();
				ArrayAdapter<GroceryItem> adapter2 = new ArrayAdapter<GroceryItem>(view.getContext(), android.R.layout.simple_list_item_1, groceryItems);
				setListAdapter(adapter2);
				loadGroceryItems();
				break;
			}
			adapter.notifyDataSetChanged();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{	// only called once - creates the menu
	    MenuInflater inflater = getMenuInflater();

	    addToMenu(menu);
	    
	    inflater.inflate(R.menu.mainmenu, menu);
	    
	    return true;
	}
	
	@SuppressLint("NewApi")
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{	// called when they have selected a menu option
    	SendTextMessage stm = new SendTextMessage();

	    int itemId = item.getItemId();
	    
		switch (itemId)
	    {	

		    case 1:
		    case 2:
		    case 3:
		    	stm.sendTextMsg(item.getTitle().toString(), this, getGroceryList());
		    	return true;
	    	
		    case R.id.addFromBigList:	// go to screen to select items from big list
		    	Intent bigListIntent = new Intent(this, BigListActivity.class);
		    	startActivityForResult(bigListIntent, 101);
		    	return true;
		    case R.id.editTextMsgNbrlist:
	            Intent myIntent = new Intent(this, EditTextMsgNumbersActivity.class);
	            startActivityForResult(myIntent, 100);
		    	return true;
		    
		    case R.id.clearGroceryList:
		    	
		        new AlertDialog.Builder(this)
		        .setIcon(android.R.drawable.ic_dialog_alert)
		        .setTitle("Delete All Items?")
		        .setMessage("Do you want to delete all items?")
		        .setPositiveButton("Delete", new DialogInterface.OnClickListener() 
		        {
		            @Override
		            public void onClick(DialogInterface dialog, int which)
		            {
				    	groceryListDao.deleteAllItems();
				    	loadGroceryItems();
		            }
		        })
		        .setNegativeButton("cancel", null)
		        .show();
		    	
		    	return true;
		    	
		    default:
		        return super.onOptionsItemSelected(item);
	    }
	}
	
	public boolean onPrepareOptionsMenu (Menu menu)
	{	// called everytime menu is launched. If textMsg nbrs have changed, 
		// I need to repopulate the text.
		menu.removeGroup(9); // remove existing textMsg Nbrs from menu
		    
	    addToMenu(menu); // add textMsg Nbrs to menu
	    
	    return true;
	}

	void addToMenu(Menu menu)
	{
		TextMsgUtils textUtils = new TextMsgUtils();
	    
	    String textNbrsString = textUtils.readTextMsgNumbersFile(getApplicationContext()).toString();
	    String[] textNbrs = textUtils.parseTxtMsgNbrs(textNbrsString);
	    
	    for (int i = 0; i < textNbrs.length; i++)
	    {
	    	if(textNbrs[i] == null || textNbrs[i].trim().length() < 1 )
	    	{
	    		break;
	    	}
			menu.add(9, i + 1, i, textNbrs[i]);
		}
	}

	@Override
    public void onActivityResult(int requestCode,int resultCode,Intent data)
    {	// after we get back from BigListActivity etc, reload page
	     super.onActivityResult(requestCode, resultCode, data);
	     
	     switch(requestCode)
	     {
		     case 191:
		    	 Uri data2 = data.getData();
				String asdf = data2.getPath();
		    	 break;
	     }
	     
	     
//	     switch(resultCode) 
//	     {
//	     	case 93:
////	     		addTextView();
//	     		break;
//	     	case 191:
////	     		addTextView();
//	     		break;
//	     }
	     onResume();
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