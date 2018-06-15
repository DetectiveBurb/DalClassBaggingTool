package dalclassbagger;

import java.awt.Component;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

public class Profile {
	
	String name;
	Map<String, Component> map;
	
	public Profile() {
		map=new HashMap<String, Component>();
	}
	
	public Profile(String name) {
		this.name=name;
		map=new HashMap<String, Component>();
	}
	
	public Map<String, Component> getMetaFields()
	{
		switch(name) {
		
		case "DalClass":{
			String[] functions={"",
					"AD Admninistration",
	                "CS Campus Services",
	                "ER External Relations and Communicatons",
	                "FP Facilities and Property",
	                "FN Finance",
	                "GV Governance",
	                "HL Health and Human Safety",
	                "HR Human Resources",
	                "IM Information Management and Technology",
	                "RS Research",
	                "ST Students",
	                "TL Teaching and Learning"};
			String[] recordSeries= {  "AD01 Adminstration General",
                    "AD10 Advice and Inquiries",
                    "AD20 Appreciation and Complaints"};
			String[] digitalOriginal= {"???",
                    "Original (Born Digital)",
                    "Original Surrogate (Digitized/Migrated & Physical/Original Destroyed)",
                    "Surrogate (Digitized/Migrated & Physical/Original Kept)",
                    "Not-Yet-Known"};
			String[] personalInfo= { "unknown",  
					"Yes",
                    "No"};
			
			
			
			RequiredTextField transferNumber = new RequiredTextField(17);
			RequiredTextField sessionNumber = new RequiredTextField(17);
			RequiredTextField receivingDepartment = new RequiredTextField(17);
			RequiredTextField summary = new RequiredTextField(17);
			RequiredComboBox containsPersonalInformation = new RequiredComboBox(personalInfo);
			
			transferNumber.setRequired(true);
			sessionNumber.setRequired(true);
			receivingDepartment.setRequired(true);
			summary.setRequired(true);
			containsPersonalInformation.setRequired(true);
			
			map.put("Transfer Number", transferNumber);
			map.put("Session Number",sessionNumber);
			map.put("Transferring Employee",new RequiredTextField(17));
			map.put("Receiving Department",receivingDepartment);
			map.put("Business Function",new RequiredComboBox(functions));
			map.put("DalClass Record Series",new RequiredComboBox(recordSeries));
			map.put("Summary of records",summary);
			map.put("Records Creation Start Date",new RequiredTextField(17));
			map.put("Records Creation End Date",new RequiredTextField(17));
			map.put("Digital Originality",new RequiredComboBox(digitalOriginal));
			map.put("Location of Physical Records (if digitized and kept)",new RequiredTextField(17));	
			map.put("Contains personal information",containsPersonalInformation);
			map.put("Notes",new RequiredTextField(17));
			
				
	/*		((RequiredTextField) map.get("Transfer Number")).setRequired(true);
			((RequiredTextField) map.get("Session Number")).setRequired(true);
			((RequiredTextField) map.get("Receiving Department")).setRequired(true);
			((RequiredTextField) map.get("Summary of records")).setRequired(true);
			((RequiredComboBox) map.get("Contains personal information")).setRequired(true);  */




			
			((RequiredTextField) map.get("Receiving Department")).setText("Dalhousie University Archives");
		}
		
		}
		
		return map;
	}
	
}