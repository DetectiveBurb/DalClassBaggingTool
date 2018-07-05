package dalclassbagger;

import java.awt.Component;
import java.awt.Dimension;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.net.ssl.HttpsURLConnection;
import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.border.Border;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;

import java.security.*;
import java.security.cert.*;

import javax.net.ssl.*;

public class Profile{
	
	String selected;
	Baggui gui;
	Map<String, Component> map;
	String[] records;
	
	public Profile() {
		map=new HashMap<String, Component>();
	}
	
	public Profile(String selected, Baggui baggui) {
		this.selected=selected;
		map=new LinkedHashMap<String, Component>();
		gui = baggui;
	}
	
	public Map<String, Component> getMetaFields()
	{
		
			String file = "profiles/"+selected+".json";
			InputStream in = this.getClass().getClassLoader().getResourceAsStream(file);
			JSONTokener tokener = new JSONTokener(in);
			JSONObject json = new JSONObject(tokener);	
			JSONArray jsons = (JSONArray) json.get("ordered");
			Border border=BorderFactory.createLineBorder(Color.red);		
			RequiredTextField textField = new RequiredTextField(); 
			RequiredComboBox box = new RequiredComboBox();
			DatePickerSettings dateSettings = new DatePickerSettings();
			dateSettings.setFormatForDatesCommonEra(json.getString("dateformat"));
			
			
			for(int i=0;i<jsons.length();i++)
			{
				JSONObject curr = (JSONObject) jsons.get(i);
				Iterator<String> keys = curr.keys();
				String first = keys.next();
				curr=(JSONObject) curr.get(first);
				if (curr.getString("type").equals("text"))
					map.put(first, new RequiredTextField(17));
				else if (curr.getString("type").equals("date"))
					map.put(first, new DatePicker(dateSettings.copySettings()));
				else if (curr.getString("type").equals("list"))
					{
					JSONArray valueList = (JSONArray) curr.get("valueList");
					String[] valueListarray = JArray2Array(valueList);
					map.put(first, new RequiredComboBox(valueListarray));
					}

				if (curr.getBoolean("fieldRequired"))
				{
					if (map.get(first).getClass().equals(textField.getClass()))
						{
							((RequiredTextField) map.get(first)).setRequired(true);
							((RequiredTextField) map.get(first)).setBorder(border);
						}
					else if (map.get(first).getClass().equals(box.getClass()))
						{
						((RequiredComboBox) map.get(first)).setRequired(true);			
						((RequiredComboBox) map.get(first)).setBorder(border);
						}		
				}
			if (first.equals("Business Function"))
				((RequiredComboBox) map.get("Business Function")).addActionListener(gui);
			else if (first.equals("DalClass Record Series"))				
				records=JArray2Array(curr.getJSONArray("valueList"));
			}
		
		return map;
	}
	
	
	private String[] JArray2Array(JSONArray valueList) {
		String[] list = new String[valueList.length()];
		for(int i=0; i<valueList.length();i++) 
			list[i] = (String) valueList.get(i);

		return list;
	}


	public String[] updateComboBox(String series) throws IOException 
	{		
		
		ArrayList<String> options=new ArrayList<String>();
		
		for(int i=0;i<records.length;i++)
		{
			String curr = records[i];
			String num=records[i].substring(0,2);
			if (num.substring(0,2).equals(series.substring(0, 2)))
				options.add(curr);
		}	
		return options.toArray(new String[options.size()]);
	}		   
	
}