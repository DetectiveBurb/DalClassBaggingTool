package dalclassbagger;

import java.awt.Component;
import java.io.IOException;
import java.io.InputStream;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.border.Border;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;

public class Profile{
	
	String selected;
	Baggui gui;
	Map<String, Component> map;
	String[] records;
	
	//constructors
	public Profile() {
		map=new HashMap<String, Component>();
	}
	
	public Profile(String selected, Baggui baggui) {
		this.selected=selected;
		map=new LinkedHashMap<String, Component>();
		gui = baggui;
	}
	
	//returns the metafield components map 
	//generates it from the jsons bundled with the software in the profiles dir
	public Map<String, Component> getMetaFields()
	{
		
			//get and parse the JSON
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
			
			//iterate through the JSON and add components to the map
			for(int i=0;i<jsons.length();i++)
			{
				JSONObject curr = (JSONObject) jsons.get(i);
				Iterator<String> keys = curr.keys();
				String first = keys.next();
				curr=(JSONObject) curr.get(first);
				if (curr.getString("type").equals("text"))
					map.put(first, new RequiredTextField(21));
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
				
			//some special conditions 
			if (first.equals("Business Function"))
				((RequiredComboBox) map.get("Business Function")).addActionListener(gui);
			else if (first.equals("DalClass Record Series"))				
				records=JArray2Array(curr.getJSONArray("valueList"));
			}
		
		return map;
	}
	
	//turns a JsonArray to a normal String[]
	private String[] JArray2Array(JSONArray valueList) {
		String[] list = new String[valueList.length()];
		for(int i=0; i<valueList.length();i++) 
			list[i] = (String) valueList.get(i);

		return list;
	}

	//updates the record series combobox when a business functions is selected.
	public String[] updateComboBox(String series) throws IOException 
	{		
		
		ArrayList<String> options=new ArrayList<String>();
		
		for(int i=1;i<records.length;i++)
		{
			String curr = records[i];
			String num=records[i].substring(0,2);
			if (num.substring(0,2).equals(series.substring(0, 2)))
				options.add(curr);
		}	
		return options.toArray(new String[options.size()]);
	}		   
	
}