package dalclassbagger;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.text.JTextComponent;
import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;

//import javafx.scene.paint.Color;


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
			Border border=BorderFactory.createLineBorder(Color.red);
			
			DatePickerSettings dateSettings = new DatePickerSettings();
			dateSettings.setFormatForDatesCommonEra("yyyy-MM-dd");
			DatePickerSettings dateSettings2 = new DatePickerSettings();
			dateSettings2.setFormatForDatesCommonEra("yyyy-MM-dd");

			transferNumber.setRequired(true);
			transferNumber.setBorder(border);
			transferNumber.setSize(new Dimension(1,4));
			sessionNumber.setRequired(true);
			sessionNumber.setBorder(border);
			receivingDepartment.setRequired(true);
			receivingDepartment.setBorder(border);
			summary.setRequired(true);
			summary.setBorder(border);
			containsPersonalInformation.setRequired(true);
			containsPersonalInformation.setBorder(border);
			
			map.put("Transfer Number", transferNumber);
			map.put("Session Number",sessionNumber);
			map.put("Transferring Employee",new RequiredTextField(17));
			map.put("Receiving Department",receivingDepartment);
			map.put("Business Function",new RequiredComboBox(functions));
			map.put("DalClass Record Series",new RequiredComboBox(recordSeries));
			map.put("Summary of records",summary);
		
			//	map.put("Records Creation Start Date",new RequiredTextField(17));
			map.put("Records Creation Start Date",new DatePicker(dateSettings));
			
			//map.put("Records Creation End Date",new RequiredTextField(17));
			map.put("Records Creation End Date",new DatePicker(dateSettings2));
			
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
		
		case "Digital Records Accession Generic":{
			
			String[] recordsRetentionType= { "???",
                    "Permanent (Keep Forever)",
                    "Non-Permanent (Limited)",
					"Not-Yet-Known "};
			String[] yesNo= { "???",
	                    "YES",
	                    "NO"};
			String[] hashSignatureType= {"???",
                    "MD5",
                    "SHA1",
                    "SHA224",
                    "SHA256",
                    "SHA-512",
                    "SHA-384",
                    "SHA-512",
                    "SHA3-224",
                    "SHA3-256",
                    "SHA3-384",
					"SHA3-512"};
			String[] recordsMedium= {"???",
                    "CD",
                    "DVD",
                    "Blu-Ray",
                    "HDD/Hard-Drive",
                    "USB: Flash/Thumb-Drive",
                    "Floppy: 3.5in",
                    "Floppy: 5.25in",
                    "Floppy: 8in",
                    "Zip Disk",
                    "Jazz Drive",
                    "Tape/Disk Cartridge",
                    "Memory Card (SD/HC/XC/UHS/MS PRO/PRO Duo/Compact Flash/etc.)",
                    "Magneto-Optical Disc (1990s)",
                    "Mobile Device (Phone/Tablet/etc.)",
                    "Computer",
                    "Folder-on-Computer",
                    "Email/Attachment",
                    "Content Systems (RMS/DMS/RDMS/CMS Repository)",
                    "Network Storage (NAS/SAN/FTP/SFTP)",
                    "Cloud Storage",
					"Other(Describe in notes)"};
			String[] digitalOriginality= { "???",
                    "Original (Born Digital)",
                    "Original Surrogate (Digitized/Migrated & Physical/Original Destroyed)",
                    "Surrogate (Digitized/Migrated & Physical/Original Kept)",
					"Not-Yet-Known"};
			String[] access= { "???",
                    "Open/Public",
                    "Open/Redacted",
                    "Semi-Confidential",
                    "Confidential/Sensitive",
                    "Classified/Top-Secret",
					"Not-Yet-Known"};
			String[] priority = { "???",
                    "Vital (Birth/Death/Marriage/Land/Rights)",
                    "Critical (Essential or Indispensible for Business)",
                    "High Value (Re-Appraised or Perceived as)",
					"Not-Yet-Known"};
			String[] agency = {   "???",
                    "Information on the set-up and origin of the agency",
                    "Document important decision-making processes",
                    "Document functions and accomplishment of agency",
                    "Document key operations of agency",
                    "Document key decision making process of top and mid level management",
					"Not-Yet-Known"};
			String[] residents = {"???",
                    "Document rights and obligations of the Government",
                    "Documents rights of residents/citizens",
					"Not-Yet-Known"};
			String[] locality= {   "???",
                    "Information on Federal/State/Local government sovereignty",
                    "Information on Federal/State/Local government security",
                    "Information on relations with other Federal/State/Local government",
                    "Information on locality/nation building  efforts",
					"Not-Yet-Known"};
			String[] general = { "???",
                    "Intrinsic value",
                    "Future research",
					"Not-Yet-Known"};
			String[] digitalContentStructure = { "???",
                    "Compound (Multiple Types)",
                    "Word Processing",
                    "Plain Text",
                    "Text With Markup",
                    "Spreadsheet",
                    "Presentation",
                    "Database",
                    "AUDIO",
                    "Audio: Mono",
                    "Audio: Stereo",
                    "VIDEO",
                    "Video: High Quality/Professional",
                    "Video: Medium Quality/Amateur",
                    "Video: Animation/Interactive",
                    "IMAGE",
                    "Image: Raster (for Tiff,PNG,Jpeg,jpeg2000,etc.)",
                    "Image: Vector/CAD ",
                    "Image: Raw/Native ",
                    "Image: Geospatial Raster ",
                    "Image: Geospatial Vector ",
                    "Email",
                    "Web Content: Site/Page/SocialMedia/Blog/etc.",
                    "Software: Game/Application/Virtual Machine/Code/etc.",
                    "Disk Image/Forensic Image",
                    "Archive: Zip/Tar/Arc/Warc/SIRF/VEO/etc.",
                    "Encrypted (Key-Available/Accessible-Content)",
                    "Encrypted (No-Key/Inaccessible-Content))",
					"Not-Yet-Known"};
			String[] digitalFormatOpenness= {   "???",
                    "Open/Standard",
                    "Ubiquitous/DeFacto",
                    "Proprietary",
                    "Mixed",
					"Not-Yet-Known"};
			String[] OAISdigitalCurationLifecycle= {  "???",
                    "SIP",
                    "AIP",
                    "AIPe (encapsulated AIP)",
					"DIP"};
			
			
			map.put("TransferNumber",new RequiredTextField(17));
			map.put("Transfering Institution",new RequiredTextField(17));
			map.put("Creating Institution",new RequiredTextField(17));
			map.put("Creating Institution Subdivision",new RequiredTextField(17));
			map.put("Creating Insitution Address",new RequiredTextField(17));
			map.put("Transferring Employee",new RequiredTextField(17));
			map.put("Receiving Institution Address",new RequiredTextField(17));
			map.put("Receiving Institution Authority",new RequiredTextField(17));
			map.put("Receiving Employee",new RequiredTextField(17));
			map.put("Records Series Number",new RequiredTextField(17));
			map.put("Records Series Title",new RequiredTextField(17));
			map.put("Records Retention Type",new RequiredComboBox(recordsRetentionType));
			map.put("Records Disposition Number",new RequiredTextField(17));
			map.put("Records Title/Description",new RequiredTextField(17));
			map.put("Records Creation Date",new DatePicker());
			map.put("Records Ceation End Date",new DatePicker());
			map.put("Records Hashed Before Tansfer",new RequiredComboBox(yesNo));
			map.put("Hash Signature Type",new RequiredComboBox(hashSignatureType));
			map.put("Records Medium/Carrier #1",new RequiredComboBox(recordsMedium));
			map.put("How Many Media Received #1",new RequiredTextField(17));
			map.put("Records Medim/Carrier #2",new RequiredComboBox(recordsMedium));
			map.put("How Many Media Received #2",new RequiredTextField(17));
			map.put("Records Medium/Carrier #3",new RequiredComboBox(recordsMedium));
			map.put("How Many Media Received #3",new RequiredTextField(17));
			map.put("Records Medium/Carrier #4",new RequiredComboBox(recordsMedium));
			map.put("How Many Media Received #4",new RequiredTextField(17));
			map.put("Records Medium/Carrier #5",new RequiredComboBox(recordsMedium));
			map.put("How Many Media Received #5",new RequiredTextField(17));
			map.put("Transer Media Kept",new RequiredComboBox(yesNo));
			map.put("Media Location (if kept)",new RequiredTextField(17));
			map.put("Records Received Date",new DatePicker());
			map.put("Mailing/Tracking Reference Number (if available)",new RequiredTextField(17));
			map.put("Courier",new RequiredTextField(17));
			map.put("Digital Orginality",new RequiredComboBox(digitalOriginality));
			map.put("Physical Record Location (if Digitized or Kept)",new RequiredTextField(17));
			map.put("Confidentail Classification",new RequiredComboBox(access));
			map.put("Priority Classification",new RequiredComboBox(priority));
			map.put("Value for Agency Classification",new RequiredComboBox(agency));
			map.put("Value for Residents Classification",new RequiredComboBox(residents));
			map.put("Value for locality Classification",new RequiredComboBox(locality));
			map.put("General Value Classification",new RequiredComboBox(general));
			map.put("Digital Content Structure",new RequiredComboBox(digitalContentStructure));
			map.put("Digital Format Openness",new RequiredComboBox(digitalFormatOpenness));
			map.put("OAIS Digital Curation Lifecycle",new RequiredComboBox(OAISdigitalCurationLifecycle));
			map.put("Notes",new RequiredTextField(17));
			map.put("Accession Bag Creator",new RequiredTextField(17));
			
		}
		
		}
		
		return map;
	}
	
}