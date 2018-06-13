package dalclassbagger;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import com.alee.laf.WebLookAndFeel;

import gov.loc.repository.bagit.hash.StandardSupportedAlgorithms;

public class Baggui implements ActionListener{

	static JButton open = new JButton("Select Folder");
	static JButton save = new JButton("Save as");
	static JButton start = new JButton("Bag it!");
	static JCheckBox zipped = new JCheckBox("zip the bag");
	static String[] algorithms = {"MD5","SHA-1","SHA224","SHA256","SHA512"};
	static String[] profiles = {"No Profile", "Hello", "Out", "Of","Time"};
	static JComboBox<String> algorithmSelect = new JComboBox<String>(algorithms); 
	static JComboBox<String> profileSelect = new JComboBox<String>(profiles);
//	static JButton metaDataB = new JButton("Save metadata");
	static JTextField saveField = new JTextField(22);// \
	static JTextField openField = new JTextField(22);//  *was 17 
//	static JTextField metaField = new JTextField(22);// /
	static JPanel p = new JPanel(new GridBagLayout());
	JFrame frame = new JFrame("DalClass Transfer Tool");
	Profile profile;
	Map<String, JTextField> customFields; 
	final JFileChooser fc = new JFileChooser();
	Baglady baglady=null;
	
	
	
	 //constructor, adds action listeners
	public Baggui(Baglady baglady){
		open.addActionListener(this);
		save.addActionListener(this);
		start.addActionListener(this);
	//	metaDataB.addActionListener(this);
		profileSelect.addActionListener(this);
		this.baglady=baglady;
		fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		algorithmSelect.setSelectedIndex(0);
		algorithmSelect.addActionListener(this);
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
		try {frame.setIconImage(ImageIO.read(new File("images"+baglady.delimeter+"Dalfavicon.png")));} 
		catch (IOException e) {e.printStackTrace();}
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    
	    //coloring buttons
	    p.setBackground(Color.WHITE);
	    open.setBackground(Color.WHITE);
	    save.setBackground(Color.WHITE);
	  //  metaDataB.setBackground(Color.WHITE);
	    zipped.setBackground(Color.WHITE);
	    start.setBackground(Color.WHITE);
	    profileSelect.setBackground(Color.WHITE);
	    algorithmSelect.setBackground(Color.WHITE); 
	   
	    
	    
	    //coloring text
	    open.setForeground(dalgold);
	    save.setForeground(dalgold);
	  //  metaDataB.setForeground(dalgold);
	    zipped.setForeground(dalgold);
	    start.setForeground(dalgold);
	    profileSelect.setForeground(dalgold);
	    algorithmSelect.setForeground(dalgold);
	  /*  saveField.setForeground(dalgold);
	    openField.setForeground(dalgold);
	    metaField.setForeground(dalgold); */

	    //configuring spacing
	    GridBagConstraints c = new GridBagConstraints();
	    c.ipadx=3;
	    c.ipady=3;
	    c.insets=new Insets(3,11,3,14);
	    open.setPreferredSize(new Dimension(120,20));
	    save.setPreferredSize(new Dimension(120,20));
	    profileSelect.setPreferredSize(new Dimension(120,22));
	 //   metaDataB.setPreferredSize(new Dimension(120,35));
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
	//    p.add(metaDataB,c);
	    c.gridx=1;
	 //   p.add(metaField,c);
	    
	    c.gridx=0;
	    c.gridy=3;
	    p.add(algorithmSelect,c);
	    
	    c.gridx=0;
	    c.gridy=4;
	    p.add(zipped,c);
	    
	    c.gridy=4;
	    c.gridx=4;
	    p.add(start,c);
	    
	    c.gridx=5;
	    c.gridy=0;
	    c.weighty=0.0;
	    c.fill=GridBagConstraints.VERTICAL;
	    c.gridheight=6;
	    
	    
	    JSeparator sep = new JSeparator(JSeparator.VERTICAL);
	    p.add(sep,c);
	    //building window
	    frame.getContentPane().add(p); 
	    frame.setLocationRelativeTo(null);
	    frame.pack();
	    frame.setVisible(true);
	}
	
	private void drawMetaFields() {
		GridBagConstraints c = new GridBagConstraints();
		c.ipadx=4;
		c.ipady=4;
		c.insets=new Insets(4,12,4,15);
		c.gridx=6;
		c.gridy=0;
		
		for (Map.Entry<String, JTextField> entry : customFields.entrySet()) 
		{
		    String key = entry.getKey();
		    JTextField field = entry.getValue();
		    JLabel label = new JLabel(key+":");
		    
		    p.add(label,c);
		    c.gridx++;
		    
		    p.add(field,c);
		    c.gridx--;
		    
		    c.gridy++;
		    if (c.gridy%7 == 0) 
		   	{
		    	c.gridx+=2;
		    	c.gridy=0;
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
    	
    	//"bag!" gets selected algorithm, as well as captures
    	//data if it was entered into the fields manually
    	else if (e.getSource().equals(start)){
    		switch (algorithmSelect.getSelectedItem().toString()) {
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
    	
    		if (baglady.getMetaoutput()==null)
    			baglady.setMetaoutput(Paths.get(baglady.getOutput().toString()+".txt"));
   
    		//makes the bag with options enabled
    		if (fieldsFilled())
    		{
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
    		else 
    			JOptionPane.showMessageDialog(null, "Please make sure all fields on the left are filled");

    		
    		//determines if successful and notify's the user
    		
    	}
    	
    	
    	else if (e.getSource().equals(profileSelect))
    	{
    		profile=new Profile(profileSelect.getSelectedItem().toString());
    		this.customFields=profile.getMetaFields();
    		drawMetaFields();
    	}
    	
    }

	private boolean fieldsFilled() {
		System.out.println("test");
		JOptionPane.showMessageDialog(null, "test");

		for (Map.Entry<String, JTextField> entry : customFields.entrySet()) 
		{
			JOptionPane.showMessageDialog(null, "test1");
			if (entry.getValue().getText().length() == 0)
				return false;
		}
		return true;
	}

	
}


