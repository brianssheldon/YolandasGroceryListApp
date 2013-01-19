package org.bubba.yolanda.edittextphonenbrs;

import java.io.FileInputStream;
import java.util.StringTokenizer;

import android.content.Context;

public class TextMsgUtils
{
	public String[] parseTxtMsgNbrs(String readTxtMsgNbrs)
	{
		String[] nbrs = new String[]{" ", " ", " "};
		
		if(null == readTxtMsgNbrs || "".equals(readTxtMsgNbrs.trim()))
		{
			return nbrs;
		}
		
		StringTokenizer st = new StringTokenizer(readTxtMsgNbrs, "|");
		
		for (int i = 0; i < 3; i++)
		{
			if(st.hasMoreElements())
			{
				nbrs[i] = st.nextToken().trim();
			}
			else
			{
				nbrs[i] = " ";
			}
		}
				
		return nbrs;
	}
	
	public CharSequence readTextMsgNumbersFile(Context context)
	{
		StringBuffer record = new StringBuffer();
		
		try
		{
	    	int ch = 0;
			FileInputStream fis = context.openFileInput("tstMsgNbrlist.txt");
			while( (ch = fis.read()) != -1)
	        {
	        	record.append((char)ch);
	        }
	    	fis.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		if(record.length() ==0)
		{
			record.append(" | | ");
		}
		
    	return record;
	}
}