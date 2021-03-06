package dalclassbagger;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.swing.*;
import com.alee.laf.WebLookAndFeel;
import com.github.lgooddatepicker.*;
import gov.loc.repository.bagit.hash.StandardSupportedAlgorithms;

public class Baggui implements ActionListener,WindowListener, WindowFocusListener,WindowStateListener{

	static JButton open; //= new JButton("Select Folder");
	static JButton save;// = new JButton("Save as");
	static JButton start;// = new JButton("Bag it!");
	static JButton back;
	static JCheckBox zipped;// = new JCheckBox("zip the bag");
	static String[] algorithms = {"MD5","SHA-1","SHA224","SHA256","SHA512"};
	static String[] profiles;
	static JComboBox<String> algorithmSelect ;//= new JComboBox<String>(algorithms); 
	static JComboBox<String> profileSelect;
	static JTextField saveField;// = new JTextField(22);
	static JTextField openField;// = new JTextField(22); 
	static JPanel mainPanel;
	static JPanel p;
	static JPanel metaPanel;
	static JScrollPane bottemPanel;
	JFrame frame = new JFrame("DalClass Transfer Tool");
	Profile profile;
	Map<String, Component> customFields; 
	final JFileChooser fc= new JFileChooser();
	Baglady baglady=null;
	boolean profilepicked;
	boolean maximized;
	boolean resized = false;
	String profileName;
	
	 //constructor, adds action listeners
	public Baggui(Baglady baglady){
		//so it's not hideous 
		WebLookAndFeel.install();
		open = new JButton("Select folder");
		save = new JButton("Save as");
		start = new JButton("Bag it!");
		back = new JButton("Pick another profile");
		zipped = new JCheckBox("Zip the bag");
		algorithmSelect = new JComboBox<String>(algorithms); 
		saveField = new JTextField(22);
		openField = new JTextField(22); 

		mainPanel=new JPanel(new GridBagLayout())
		//creates the gradient background
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
		
		//various initialization stuff
		try {profiles=getProfiles();} 
		catch (IOException e) {e.printStackTrace();}
		
		profileSelect=new JComboBox<String>(profiles);
		metaPanel=new JPanel(new GridBagLayout());
		bottemPanel=new JScrollPane();
		open.addActionListener(this);
		save.addActionListener(this);
		start.addActionListener(this);
		back.addActionListener(this);
		profileSelect.addActionListener(this);
		this.baglady=baglady;
		fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		algorithmSelect.setSelectedIndex(0);
		algorithmSelect.addActionListener(this);
		profilepicked=false;
		frame.addWindowStateListener(this);
		frame.setMinimumSize(mainPanel.getPreferredSize());;
		maximized=false;	
	}	
	
	//asks what profile the user would like before anything else happens.
	public void askProfile() {
		ImageIcon icon = new ImageIcon(this.getClass().getClassLoader().getResource("images/Dalfavicon.png"));
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		
		
	    String s = (String)JOptionPane.showInputDialog(
				null,
		        "Please Select a Profile",
		        null,
		        JOptionPane.QUESTION_MESSAGE,
		        icon,
		        profiles,
		        profiles[0]);
	    //setting what profile was picked.
		if ((s != null) && (s.length() > 0)) {
		   profileName=s;
		   profilepicked=true;
		   profile=new Profile(profileName, this);
		   if(!profileName.equals("No profile"))
			   this.customFields=profile.getMetaFields();
		   return;
		}
		return;
	}
	
	//creates and styles gui
	public void createAndShowGUI() {
		Color slategrey = new Color(112,115,114);
		Color dalgold =new Color(192,140,12);
		p = new JPanel(new GridBagLayout());
		
		//misc window setup
		SwingUtilities.updateComponentTreeUI(frame);	
		frame.setIconImage(new ImageIcon(this.getClass().getClassLoader().getResource("images/Dalfavicon.png")).getImage());
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    
	    Font f = new Font("serif", Font.PLAIN, 16);
	 /*   
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
	    open.setForeground(slategrey);
	    save.setForeground(slategrey);
	    zipped.setForeground(slategrey);
	    start.setForeground(slategrey);
	    profileSelect.setForeground(Color.WHITE);
	    algorithmSelect.setForeground(slategrey);
	    saveField.setMinimumSize(new Dimension(22, 27));
*/
	    //configuring spacing
	    GridBagConstraints c = new GridBagConstraints();
	    c.ipadx=3;
	    c.ipady=3;
	    c.insets=new Insets(3,11,3,14);
	    open.setPreferredSize(new Dimension(120,27));
	    save.setPreferredSize(new Dimension(120,27));
	    back.setPreferredSize(new Dimension(125,27));
	    profileSelect.setPreferredSize(new Dimension(120,22));
	    algorithmSelect.setPreferredSize(new Dimension(120,22));
	    
	    saveField.setFont(f);
	    openField.setFont(f);
	    
	  //  positioning components
	  //  c.gridx=0;
	  //  c.gridy=0;
	  //  p.add(profileSelect,c);    
	    
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
	    
	    c.gridy=5;
	    c.gridx=4;
	    p.add(start,c);
	    
	    c.gridy=5;
	    c.gridx=0;
	    p.add(back,c);
	    
	    c.gridx=0;
	    c.gridy=6;
	    c.weightx=0.0;
	    c.fill=GridBagConstraints.HORIZONTAL;
	    c.gridwidth=6;
	    
	    
	    JSeparator sep = new JSeparator(JSeparator.HORIZONTAL);
	    p.add(sep,c);
	    
	    drawMetaFields();
	    //building window
	    c.gridx=0;
	    c.gridy=0;
	    p.setOpaque(false);
	    mainPanel.add(p,c);
	    c.gridy=1;
	    frame.getContentPane().add(mainPanel); 
	    frame.setLocationRelativeTo(null);
	    frame.pack();
	    frame.setVisible(true);
	    frame.setMinimumSize(frame.getSize());
	    resize();
	    refresh();
	}
	
	//event listeners 
    @SuppressWarnings("unchecked")
	public void actionPerformed(ActionEvent e) {
    	//for bag selection
    	if (e.getSource().equals(open)) {    	
    		int returnVal = fc.showOpenDialog(open); 	
    		if (returnVal == JFileChooser.APPROVE_OPTION)
    		{
    			baglady.setInput(fc.getSelectedFile().toPath());
    			//files in fields, also defaults output to desktop with the same name
    			openField.setText(fc.getSelectedFile().toString());
    			saveField.setText(System.getProperty("user.home")+baglady.delimeter+"Desktop"+baglady.delimeter+baglady.getInput().getFileName()+"-bagged"+baglady.delimeter);
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
    		
    		if (!saveField.getText().equals("")) 
    		{
    			baglady.setOutput(Paths.get(saveField.getText()));
    			baglady.setMetaoutput(Paths.get(saveField.getText().toString().substring(0, saveField.getText().toString().length()-1)+"-metadata.txt"));
    		}
    	
    		//if (baglady.getMetaoutput()==null && baglady.getOutput() != null)
    		//	baglady.setMetaoutput(Paths.get(baglady.getOutput().toString()+".txt"));
    	
    		int fields = fieldsFilled();
    		//checking that all required fields are filled
    		if (fields==1)
    			JOptionPane.showMessageDialog(null, "Please select a profile");
    		
    		else if (fields == 2)
    			JOptionPane.showMessageDialog(null, "Please make sure all fields are filled. \r\nRequired fields are outlined in red.");
    		
    		else if (fields == 3){
        		//makes the bag with options enabled

    			if (profilepicked) {
    		        frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    				baglady.doeverything(zipped.isSelected(), customFields);
    			}
    			else {
    		        frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    				baglady.doeverything(zipped.isSelected());
    			}
    			
    			if (baglady.getSuccess()) {
    				JOptionPane.showMessageDialog(null, "Bag Created Successfully");
    	            frame.setCursor(Cursor.getDefaultCursor());
    			}
    			else
    			{
    				baglady.cleanFolder(zipped.isSelected());
    				JOptionPane.showMessageDialog(null, "Sorry, There was an error creating the bag");
    	            frame.setCursor(Cursor.getDefaultCursor());
    				baglady.setSuccess(true);
    			}
    		}		
    	}    		
    	
    	//updates meta fields displayed when a profile is selected
    	else if (e.getSource().equals(profileSelect))
    	{
    		drawMetaFields();		    		
    	}
    	else if (e.getSource().equals(back))
    	{
    		int confirmed = 5;
    		
    		//displays a option making sure the user want to change profile as it will make them
    		//lose any info they have entered into the metafields
    		
    		confirmed = JOptionPane.showConfirmDialog(
    				null,
    	/*Text in box*/		"Changing profiles will delete all entered metadata. Do you wish to continue?",
    	/*Title of box*/	"Confirm profile change",
    				JOptionPane.YES_NO_OPTION
    		        ); 
    		//then displays the initial pop-up to select profiles.k
    		if (confirmed == JOptionPane.YES_OPTION) 
    		{
    			askProfile();
    			drawMetaFields();
    		}
    	}
    	//updates the record series when a function is selected.
    	else if(customFields != null) {
    		if (e.getSource().equals(customFields.get("Business Function")))
    			{
    				String businessFunc = ((JComboBox<String>) customFields.get("Business Function")).getSelectedItem().toString();
    				String[] newOptions = null;
    			
    				try {newOptions = profile.updateComboBox(businessFunc);}
    				catch (IOException e1) {e1.printStackTrace();}
    			
    				((RequiredComboBox)customFields.get("DalClass Record Series")).removeAllItems();
    				for (int i =0;i<newOptions.length;i++)
    				{
    					((JComboBox<String>)customFields.get("DalClass Record Series")).addItem(newOptions[i]);
    				}
    				((JComboBox<String>)customFields.get("DalClass Record Series")).setSelectedIndex(0);
    			}
    	}
    }
    
	//fills the profile combo box
    //if you are adding you're own JSON profile you'll need to add it to this array
    //make sure it's the exact file name minus ".json"
    //so if your json profile is exAmp1e.json, add "exAmp1e" to this array. 
	private String[] getProfiles() throws IOException {		
		String [] returnable= {"No profile","DalClass profile","Digital records accession generic profile"};
		return returnable;
	}
	
	//called once a profile is selected
	private void drawMetaFields() {
		
		if (profileName.equals("No profile"))
		{
			mainPanel.remove(bottemPanel);
			profilepicked=false;
			resize();
			refresh();
		}
		else 
		{
			profilepicked=true;
			//creating the panel
			metaPanel = new JPanel(new GridBagLayout());	
			mainPanel.remove(bottemPanel);
			//configuring spacing
			GridBagConstraints c = new GridBagConstraints();
			c.ipadx=2;
			c.ipady=4;
			c.insets=new Insets(0,4,4,4);
			c.gridx=0;
			c.gridy=0;
			c.anchor = (c.gridx == 0) ? GridBagConstraints.WEST : GridBagConstraints.EAST;
			c.fill = (c.gridx == 0) ? GridBagConstraints.BOTH : GridBagConstraints.HORIZONTAL;
			//int colnum = (maximized) ? 4 : 2;
			//colnum divided by two is how many columns will be drawn in the metafield panel 
			int colnum = 2;
			for (Entry<String, Component> entry : customFields.entrySet()) 
			{
				String key = entry.getKey();
				Component field =  entry.getValue();
				JLabel label = new JLabel(key+":");
				metaPanel.add(label,c);
				c.gridx++;
				
				metaPanel.add(field,c);
				c.gridx++;
				
				if (c.gridx%colnum == 0) 
				{
					c.gridy++;
					c.gridx=0;
				}		     
			}
			
			//some misc window setup
			bottemPanel=new JScrollPane(metaPanel);
			bottemPanel.setVisible(true);
			bottemPanel.getVerticalScrollBar().setUnitIncrement(16);
			c = new GridBagConstraints();
			c.gridy=1;
			c.gridx=0;
	    	resize();
			mainPanel.add(bottemPanel,c);
			frame.getContentPane().add(mainPanel);
			refresh();
		}

	}
  
	private void resize() {
		//deals with sizing the panel
		if (!maximized && !resized && profilepicked) 
		{
			resized=true;
			bottemPanel.setPreferredSize(new Dimension(821,435));
			mainPanel.setPreferredSize(new Dimension(821, 635));
			frame.pack();
			frame.setMinimumSize(frame.getSize());

		}
		else if (!maximized && profilepicked && resized) 
		{
			bottemPanel.setPreferredSize(new Dimension(821,435));
			mainPanel.setPreferredSize(new Dimension(821,635));
			frame.pack();
			frame.setMinimumSize(frame.getSize());
		}
		else if (!maximized && !profilepicked)
		{
			resized=false;
			mainPanel.setPreferredSize(new Dimension(570,200));
			frame.pack();
			frame.setMinimumSize(new Dimension(571,231));
		}
		else if (maximized && profilepicked) 
		{
			mainPanel.setSize(frame.getSize());
			bottemPanel.setPreferredSize(new Dimension(mainPanel.getWidth()-50,mainPanel.getHeight()-250));
		}		
	}

	//function to determine if all fields are filled.
	private int fieldsFilled() {
		if(saveField.getText().equals("") || openField.getText().equals(""))
			return 2;
		//else if (profilepicked==false || profileSelect.getSelectedIndex()==0)
			//return 1;
		else if (profilepicked == true)
			for (Entry<String, Component> entry : customFields.entrySet()) 
			{
				if (isFilled(entry.getValue())==false && isRequired(entry.getValue())==true)
					return 2;
			}
		return 3;
	}

	//checks if a specific field is filled
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
			if (((RequiredTextField) value).getText().toString().isEmpty())
				return false;
		} 
		return true;
	}
	
	//checks if a field is a required field
	private boolean isRequired(Component value) {
		RequiredComboBox box =new RequiredComboBox();
		RequiredTextField field= new RequiredTextField();
		
		if (value.getClass().equals(box.getClass())) 
			return ((RequiredComboBox) value).isRequired();
		else if (value.getClass().equals(field.getClass()))
			return ((RequiredTextField) value).isRequired();
		
		return false;
	}

	//so I don't need to write these six lines all the time
	private void refresh() {
		metaPanel.revalidate();
		bottemPanel.revalidate();
		mainPanel.revalidate();

		metaPanel.repaint();
		bottemPanel.repaint();
		mainPanel.repaint();	
		
		if(!maximized) 
    		frame.pack();
	}
	
	//easier to know if the fame is maximized or iconfied this way.
	@Override
	public void windowStateChanged(WindowEvent e) {
		if ((e.getNewState() & Frame.MAXIMIZED_BOTH) != Frame.MAXIMIZED_BOTH) {
			maximized=false;
			drawMetaFields();
		}
		else if ((e.getNewState() & Frame.MAXIMIZED_BOTH) == Frame.MAXIMIZED_BOTH) {
			maximized=true;
			drawMetaFields();
		}

	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		maximized=false;
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		maximized=true;
	}

	@Override
	public void windowGainedFocus(WindowEvent arg0) {}

	@Override
	public void windowLostFocus(WindowEvent arg0) {}

	@Override
	public void windowActivated(WindowEvent arg0) {}

	@Override
	public void windowClosed(WindowEvent arg0) {}

	@Override
	public void windowClosing(WindowEvent arg0) {}

	@Override
	public void windowDeactivated(WindowEvent arg0) {}

	@Override
	public void windowOpened(WindowEvent arg0) {}
}


