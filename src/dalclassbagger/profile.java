package dalclassbagger;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JTextField;

public class Profile {
	
	String name;
	Map<String, JTextField> map;
	
	public Profile() {
		map=new HashMap<String, JTextField>();
	}
	
	public Profile(String name) {
		this.name=name;
		map=new HashMap<String, JTextField>();
	}
	
	public Map<String, JTextField> getMetaFields()
	{
		switch(name) {
		
		}
		map.put("test",new JTextField(17));
		map.put("dsgsg",new JTextField(17));
		map.put("sdgsdg",new JTextField(17));
		map.put("hgjkg",new JTextField(17));
		map.put("tur",new JTextField(17));
		map.put("cvbc",new JTextField(17));
		map.put("fhgbn",new JTextField(17));
		map.put("gfjut",new JTextField(17));
		map.put("fguf",new JTextField(17));
		return map;
	}
	
}