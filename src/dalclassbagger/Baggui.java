package dalclassbagger;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.JTextComponent;

import com.alee.laf.WebLookAndFeel;

import gov.loc.repository.bagit.hash.StandardSupportedAlgorithms;

public class Baggui implements ActionListener{

	static JButton open = new JButton("Select Folder");
	static JButton save = new JButton("Save as");
	static JButton start = new JButton("Bag it!");
	static JCheckBox zipped = new JCheckBox("zip the bag");
	static String[] algorithms = {"MD5","SHA-1","SHA224","SHA256","SHA512"};
	static String[] profiles = {"No Profile", "DalClass", "Out", "Of","Time"};
	static JComboBox<String> algorithmSelect = new JComboBox<String>(algorithms); 
	static JComboBox<String> profileSelect = new JComboBox<String>(profiles);
//	static JButton metaDataB = new JButton("Save metadata");
	static JTextField saveField = new JTextField(22);// \
	static JTextField openField = new JTextField(22);//  *was 17 
//	static JTextField metaField = new JTextField(22);// /
	static JPanel p;
	JFrame frame = new JFrame("DalClass Transfer Tool");
	Profile profile;
	Map<String, Component> customFields; 
	final JFileChooser fc = new JFileChooser();
	Baglady baglady=null;
	boolean profilepicked;
	
	
	
	 //constructor, adds action listeners
	public Baggui(Baglady baglady){
		
		p=new JPanel(new GridBagLayout()) 
		{
	        @Override
	        protected void paintComponent(Graphics grphcs) {
	            super.paintComponent(grphcs);
	            Graphics2D g2d = (Graphics2D) grphcs;
	            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
	                    RenderingHints.VALUE_ANTIALIAS_ON);
	            GradientPaint gp = new GradientPaint(0, 0,
	            		Color.WHITE, getWidth(), 0,
	            		new Color(112,115,114).darker().darker());
	            g2d.setPaint(gp);
	            g2d.fillRect(0, 0, getWidth(), getHeight()); 

	        }
		};
		
		open.addActionListener(this);
		save.addActionListener(this);
		start.addActionListener(this);
	//	metaDataB.addActionListener(this);
		profileSelect.addActionListener(this);
		this.baglady=baglady;
		fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		algorithmSelect.setSelectedIndex(0);
		algorithmSelect.addActionListener(this);
		profilepicked=false;
	}
	
	//creates and styles gui
	public void createAndShowGUI() {
		//Color slategrey = new Color(112,115,114);
		Color dalgold =new Color(192,140,12);
				
		//so it's not hideous 
		
		WebLookAndFeel.install();
		//misc window setup
		//JFrame frame = new JFrame("Dal Class Bagger Tool");  
		SwingUtilities.updateComponentTreeUI(frame);	
		frame.setResizable(false);
		try {frame.setIconImage(ImageIO.read(new File(baglady.delimeter+"DalClass Bagger"+baglady.delimeter+"src"+baglady.delimeter+"dalFavIcon.ico")));} 
		catch (IOException e) {e.printStackTrace();}
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    
	    //coloring buttons
	    p.setBackground(Color.WHITE);
	    open.setBackground(dalgold);
	    save.setBackground(dalgold);
	    zipped.setBackground(Color.WHITE);
	    zipped.setOpaque(false);
	    start.setBackground(dalgold);
	    profileSelect.setBackground(dalgold);
	    algorithmSelect.setBackground(dalgold); 
	   
	    
	    
	    //coloring text
	    open.setForeground(Color.WHITE);
	    save.setForeground(Color.WHITE);
	    zipped.setForeground(dalgold);
	    start.setForeground(Color.WHITE);
	    profileSelect.setForeground(Color.WHITE);
	    algorithmSelect.setForeground(Color.white);
	    saveField.setMinimumSize(new Dimension(22, 27));;
	   // openField.setForeground(dalgold);
	   // metaField.setForeground(dalgold);

	    //configuring spacing
	    GridBagConstraints c = new GridBagConstraints();
	    c.ipadx=3;
	    c.ipady=3;
	    c.insets=new Insets(3,11,3,14);
	    open.setPreferredSize(new Dimension(120,27));
	    save.setPreferredSize(new Dimension(120,27));
	    profileSelect.setPreferredSize(new Dimension(120,22));
	    algorithmSelect.setPreferredSize(new Dimension(120,22));
	    
	    
	    //positioning components
	    c.gridx=0;
	    c.gridy=0;
	    p.add(profileSelect,c);
	    
	    
	    c.gridx=0;
	    c.gridy=1;
	    p.add(open,c);
	    c.gridx=1;
	    p.add(openField,c);
	    
	    
	    c.gridx=0;
	    c.gridy=2;
	    p.add(save,c);
	    c.gridx=1;
	    p.add(saveField,c);
	        
	    c.gridx=0;
	    c.gridy=3;
	    p.add(algorithmSelect,c);
	    
	    c.gridx=0;
	    c.gridy=4;
	    p.add(zipped,c);
	    
	    c.gridy=4;
	    c.gridx=4;
	    p.add(start,c);
	    
	    c.gridx=0;
	    c.gridy=5;
	    c.weightx=0.0;
	    c.fill=GridBagConstraints.HORIZONTAL;
	    c.gridwidth=6;
	    
	    
	    JSeparator sep = new JSeparator(JSeparator.HORIZONTAL);
	    p.add(sep,c);
	    //building window
	    frame.getContentPane().add(p); 
	    frame.setLocationRelativeTo(null);
	    frame.pack();
	    frame.setVisible(true);
	}
	
	private void drawMetaFields() {
		GridBagConstraints c = new GridBagConstraints();
		c.ipadx=2;
		c.ipady=4;
		c.insets=new Insets(4,4,4,4);
		c.gridx=0;
		c.gridy=6;
		
		for (Entry<String, Component> entry : customFields.entrySet()) 
		{
		    String key = entry.getKey();
		    Component field =  entry.getValue();
		    JLabel label = new JLabel(key+":");
		    
		    p.add(label,c);
		    c.gridx++;
		    
		    p.add(field,c);
		    c.gridx++;
		    
		   // c.gridy++;
		    if (c.gridx%4 == 0) 
		   	{
		    	c.gridy++;
		    	c.gridx=0;
		   	}		     
		}
		
		 frame.pack();
		 frame.setVisible(true);
	}

    
	//event listeners 
    public void actionPerformed(ActionEvent e) {
    	//for bag selection
    	if (e.getSource().equals(open)) {    	
    		int returnVal = fc.showOpenDialog(open); 	
    		if (returnVal == JFileChooser.APPROVE_OPTION)
    		{
    			baglady.setInput(fc.getSelectedFile().toPath());
    			//files in fields, also defaults output to desktop with the same name
    			openField.setText(fc.getSelectedFile().toString());
    			saveField.setText(System.getProperty("user.home")+baglady.delimeter+"Desktop"+baglady.delimeter+baglady.getInput().getFileName()+baglady.delimeter);
    		}
    	}
    	//for output, updates field as well
    	else if (e.getSource().equals(save)) {    	
    		int returnVal = fc.showSaveDialog(save); 	
    		if (returnVal == JFileChooser.APPROVE_OPTION)
    		{
    			baglady.setOutput(fc.getSelectedFile().toPath());
    			saveField.setText(fc.getSelectedFile().toString());
    			baglady.setMetaoutput(Paths.get(baglady.getOutput().toString()+"-metadata.txt"));
    		}
    	}
    	
    	//for metadata.txt output, only accepts .txt 
    	//will add .txt to file is not typed in
  
    	/* 	else if (e.getSource().equals(metaDataB)) {
    		FileNameExtensionFilter filter = new FileNameExtensionFilter("text files", "txt", "text");
    		fc.setFileFilter(filter);
        	int returnVal = fc.showSaveDialog(metaDataB); 	
        	if (returnVal == JFileChooser.APPROVE_OPTION)
        	{
        		baglady.setMetaoutput(fc.getSelectedFile().toPath());
        		metaField.setText(fc.getSelectedFile().toString());
        	}
        	fc.setFileFilter(null);
    	}	
    	 */
    	
    	//"bag it!" gets selected algorithm, as well as captures
    	//data if it was entered into the fields manually
    	else if (e.getSource().equals(start)){
    		
    		switch (algorithmSelect.getSelectedItem().toString()) 
    		{
    			case "MD5":
    				baglady.setAlg(StandardSupportedAlgorithms.MD5);
    				break;
    			case "SHA-1":
    				baglady.setAlg(StandardSupportedAlgorithms.SHA1);
    				break;
    			case "SHA224":
    				baglady.setAlg(StandardSupportedAlgorithms.SHA224);
    				break;
    			case "SHA256":
    				baglady.setAlg(StandardSupportedAlgorithms.SHA256);
    				break;
    			case "SHA512":
    				baglady.setAlg(StandardSupportedAlgorithms.SHA512);
    				break;
    		}
    		
    		//getting manual field input
    		if (baglady.getInput()==null && !openField.getText().equals(""))
    			baglady.setInput(Paths.get(openField.getText()));
    		
    		if (baglady.getOutput()==null && !saveField.getText().equals("")) 
    			baglady.setOutput(Paths.get(saveField.getText()));    
    	
    		if (baglady.getMetaoutput()==null && baglady.getOutput() != null)
    			baglady.setMetaoutput(Paths.get(baglady.getOutput().toString()+".txt"));
    		
    		//makes the bag with options enabled
    		int fields = fieldsFilled();
    		
    			if (fields==1){
    				baglady.doeverything(zipped.isSelected());
    				if (baglady.getSuccess())
    				{
    					JOptionPane.showMessageDialog(null, "Bag Created Successfully");
    				}
    				else
    				{
    					JOptionPane.showMessageDialog(null, "Sorry, There was an error creating the bag");
    				}
    			}
    			
    			else if (fields == 2){
    				JOptionPane.showMessageDialog(null, "Please make sure all fields filled");
    			}
    			
    			else if (fields == 3){
    				baglady.doeverything(zipped.isSelected(), customFields);
    				
    				if (baglady.getSuccess())
    				{
    					JOptionPane.showMessageDialog(null, "Bag Created Successfully");
    				}
    				else
    				{
    					JOptionPane.showMessageDialog(null, "Sorry, There was an error creating the bag");
    				}

    			}
    			
    		}    		
    	
    	
    	
    	else if (e.getSource().equals(profileSelect))
    	{
    		profile=new Profile(profileSelect.getSelectedItem().toString());
    		this.customFields=profile.getMetaFields();
    		drawMetaFields();
    		profilepicked=true;
    	}
    	
    }

	private int fieldsFilled() {
		if(saveField.getText().equals("") || openField.getText().equals(""))
			return 2;
		
		else if (profilepicked==false || profileSelect.getSelectedIndex()==0)
			return 1;
		else 
			for (Entry<String, Component> entry : customFields.entrySet()) 
			{
				if (isFilled(entry.getValue())==false && isRequired(entry.getValue())==true)
					return 2;
			}
		return 3;
	}

	private boolean isFilled(Component value) {
		RequiredComboBox box = new RequiredComboBox();
		RequiredTextField field = new RequiredTextField();
		if (value.getClass().equals(box.getClass())) 
		{
			if (((RequiredComboBox) value).getSelectedItem().toString().isEmpty())
				return false;
		}
		else if (value.getClass().equals(field.getClass()))
		{
			System.out.print(((RequiredTextField) value).getText().toString());
			if (((RequiredTextField) value).getText().toString().isEmpty())
				return false;
		} 
		return true;
	}
	
	private String getFilled(Component value) {
		RequiredComboBox box =new RequiredComboBox();
		RequiredTextField field= new RequiredTextField();
		if (value.getClass().equals(box.getClass())) 
		{
			return ((RequiredComboBox) value).getSelectedItem().toString();
		}
		else if (value.getClass().equals(field.getClass()))
		{
			return ((RequiredTextField) value).getText();
		} 
		return "";
	}
	
	private boolean isRequired(Component value) {
		RequiredComboBox box =new RequiredComboBox();
		RequiredTextField field= new RequiredTextField();
		if (value.getClass().equals(box.getClass())) 
		{
			return ((RequiredComboBox) value).isRequired();
		}
		else if (value.getClass().equals(field.getClass()))
		{
			((RequiredTextField) value).isRequired();
		} 
		return false;
	}
}


