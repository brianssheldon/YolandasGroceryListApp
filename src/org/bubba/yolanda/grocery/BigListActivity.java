package org.bubba.yolanda.grocery;

import java.util.List;

import org.bubba.yolanda.grocery.knownitems.KnownItem;
import org.bubba.yolanda.grocery.knownitems.KnownItemsDao;
import org.bubba.yolanda.grocery.list.GroceryListDao;

import android.app.Activity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.CompoundButton.OnCheckedChangeListener;


public class BigListActivity extends Activity
{
	@Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    	KnownItemsDao knownItemsDao = new KnownItemsDao(this);
    	knownItemsDao.open();
    	
        ScrollView sv = new ScrollView(this); 

        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        sv.addView(ll, 0);
        
        List<KnownItem> knownItems = knownItemsDao.getAllItems();
        knownItemsDao.close();
        
        for (int i = 0; i < knownItems.size(); i++)
        {
        	if(null == knownItems.get(i)
        		|| "".equals(knownItems.get(i).getItem())) continue;
        		
			CheckBox cb = new CheckBox(this);
			cb.setId(i);
            cb.setText(knownItems.get(i).getItem());
			cb.setOnCheckedChangeListener(new AddRowListenerx());
			
			ll.addView(cb);
		}

        this.setContentView(sv);
    }
	
	class AddRowListenerx implements OnCheckedChangeListener
	{
		public void onCheckedChanged(CompoundButton arg0, boolean arg1) 
		{
	    	GroceryListDao groceryListDao = new GroceryListDao(arg0.getContext());
	    	groceryListDao.open();
			String text = arg0.getText().toString();
			if("".equals(text)) return;
			
			groceryListDao.createItem(text, 1);
			groceryListDao.close();
		}
	}
}