package org.bubba.yolanda.grocery;

import java.math.BigDecimal;
import java.math.RoundingMode;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class CalculatorActivity extends Activity
{
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calculator);

		View mainView = findViewById(R.id.rlCalculator);
		final EditText priceET1 = (EditText) mainView.findViewById(R.id.price1);
		final EditText quantityET1 = (EditText) mainView.findViewById(R.id.quantity1);
		final TextView resultET1 = (TextView) mainView.findViewById(R.id.result1);
		makeTextWatcher(priceET1, quantityET1, resultET1);

		final EditText priceET2 = (EditText) mainView.findViewById(R.id.price2);
		final EditText quantityET2 = (EditText) mainView.findViewById(R.id.quantity2);
		final TextView resultET2 = (TextView) mainView.findViewById(R.id.result2);
		makeTextWatcher(priceET2, quantityET2, resultET2);

		final EditText priceET3 = (EditText) mainView.findViewById(R.id.price3);
		final EditText quantityET3 = (EditText) mainView.findViewById(R.id.quantity3);
		final TextView resultET3 = (TextView) mainView.findViewById(R.id.result3);
		makeTextWatcher(priceET3, quantityET3, resultET3);

	    Button clearButton = (Button) findViewById(R.id.clearCalc);
		clearButton.setOnClickListener(btnClearOnClick); 
		
		Button exitButton = (Button) findViewById(R.id.exitCalc);
		exitButton.setOnClickListener(btnExitOnClick);
	}

	private void makeTextWatcher(final EditText priceET, final EditText quantityET, final TextView resultET)
	{
		TextWatcher fieldTextWatcher = new TextWatcher()
		{
			public void afterTextChanged(Editable s){}
			public void beforeTextChanged(CharSequence s, int start, int count, int after){}

			public void onTextChanged(CharSequence s, int start, int before, int count)
			{
				String priceString = priceET.getText().toString();
				String quantityString = quantityET.getText().toString();
				
				if(quantityString == null || quantityString.length() < 1
					|| priceString == null || priceString.length() < 1) return;

				BigDecimal quantity = new BigDecimal(quantityString);//s.toString());
				BigDecimal price = new BigDecimal(priceString);
				
				resultET.setText("" + price.divide(quantity, 2, RoundingMode.HALF_EVEN).toString());
			}
		};
		
		quantityET.addTextChangedListener(fieldTextWatcher);
		priceET.addTextChangedListener(fieldTextWatcher);
	}

    private final Button.OnClickListener btnClearOnClick = new Button.OnClickListener() 
    {
        public void onClick(View v) 
        {
        	View mainView = findViewById(R.id.rlCalculator);
			((EditText) mainView.findViewById(R.id.price1)).setText("");
			((EditText) mainView.findViewById(R.id.quantity1)).setText("");
			((TextView) mainView.findViewById(R.id.result1)).setText("");

			((EditText) mainView.findViewById(R.id.price2)).setText("");
			((EditText) mainView.findViewById(R.id.quantity2)).setText("");
			((TextView) mainView.findViewById(R.id.result2)).setText("");

			((EditText) mainView.findViewById(R.id.price3)).setText("");
			((EditText) mainView.findViewById(R.id.quantity3)).setText("");
			((TextView) mainView.findViewById(R.id.result3)).setText("");
        }
    };
    
    private final Button.OnClickListener btnExitOnClick = new Button.OnClickListener() 
    {
        public void onClick(View v) 
        {
	    	Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
        }
    };
}