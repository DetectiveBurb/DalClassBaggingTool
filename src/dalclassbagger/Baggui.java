package dalclassbagger;

import java.awt.Color;
import java.awt.Component;
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
import java.net.URL;
import java.nio.file.Paths;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.imageio.ImageIO;
import javax.swing.*;
import com.alee.laf.WebLookAndFeel;
import com.github.lgooddatepicker.*;
import com.sun.jndi.toolkit.url.Uri;

import gov.loc.repository.bagit.hash.StandardSupportedAlgorithms;

public class Baggui implements ActionListener,WindowListener, WindowFocusListener,WindowStateListener{

	static JButton open = new JButton("Select Folder");
	static JButton save = new JButton("Save as");
	static JButton start = new JButton("Bag it!");
	static JCheckBox zipped = new JCheckBox("zip the bag");
	static String[] algorithms = {"MD5","SHA-1","SHA224","SHA256","SHA512"};
	static String[] profiles;
	static JComboBox<String> algorithmSelect = new JComboBox<String>(algorithms); 
	static JComboBox<String> profileSelect;
	static JTextField saveField = new JTextField(22);
	static JTextField openField = new JTextField(22); 
	static JPanel mainPanel;
	static JPanel p;
	static JPanel metaPanel;
	static JScrollPane bottemPanel;
	JFrame frame = new JFrame("DalClass Transfer Tool");
	Profile profile;
	Map<String, Component> customFields; 
	final JFileChooser fc = new JFileChooser();
	Baglady baglady=null;
	boolean profilepicked;
	boolean maximized;
	boolean resized = false;
	
	 //constructor, adds action listeners
	public Baggui(Baglady baglady){
		
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
		try {
			profiles=getProfiles();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		profileSelect=new JComboBox<String>(profiles);
		metaPanel=new JPanel(new GridBagLayout());
		bottemPanel=new JScrollPane();
		open.addActionListener(this);
		save.addActionListener(this);
		start.addActionListener(this);
		profileSelect.addActionListener(this);
		this.baglady=baglady;
		fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		algorithmSelect.setSelectedIndex(0);
		algorithmSelect.addActionListener(this);
		profilepicked=false;
		frame.addWindowStateListener(this);
		maximized=false;
	}
	
	//fills the profileSelect combo box by reading what files are in the profiles dir
	private String[] getProfiles() throws IOException {
		ArrayList<String> list = new ArrayList<String>();
		list.add("No Profile");
		
		
		if (System.getProperty("java.class.path").indexOf(".jar") < 48) {
			CodeSource src = Baggui.class.getProtectionDomain().getCodeSource();
			if (src != null) {
				URL jar = src.getLocation();
				ZipInputStream zip = new ZipInputStream(jar.openStream());
				while(true) {
					ZipEntry e = zip.getNextEntry();
					if (e == null)
						break;
					String name = e.getName();
					if (name.startsWith("profiles/")) {
						if (name.length()>9)
							list.add(name.substring(9,name.length()-5));
					}
				}
			} 
			else {
				System.out.println("failed to read profile directory");
			}
		}
		
		else {
			File folder = new File("src/profiles");
			 for (final File fileEntry : folder.listFiles())		        
				 list.add(fileEntry.getName().substring(0, fileEntry.getName().length()-5));
		}
			
	    String[] returnable = new String[list.size()];
		returnable=list.toArray(returnable);
		return returnable;
	}
	
	
	
	//creates and styles gui
	public void createAndShowGUI() {
		//Color slategrey = new Color(112,115,114);
		Color dalgold =new Color(192,140,12);
		p = new JPanel(new GridBagLayout());
				
		//so it's not hideous 
		WebLookAndFeel.install();
		
		//misc window setup
		SwingUtilities.updateComponentTreeUI(frame);	
		try {frame.setIconImage(ImageIO.read(this.getClass().getClassLoader().getResource("images/dalFavIcon.ico")));} 
		catch (IOException e) {e.printStackTrace();}
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    
	    Font f = new Font("serif", Font.PLAIN, 16);
	    
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

	    //configuring spacing
	    GridBagConstraints c = new GridBagConstraints();
	    c.ipadx=3;
	    c.ipady=3;
	    c.insets=new Insets(3,11,3,14);
	    open.setPreferredSize(new Dimension(120,27));
	    save.setPreferredSize(new Dimension(120,27));
	    profileSelect.setPreferredSize(new Dimension(120,22));
	    algorithmSelect.setPreferredSize(new Dimension(120,22));
	    
	    saveField.setFont(f);
	    openField.setFont(f);
	    
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
	    c.gridx=0;
	    c.gridy=0;
	    p.setOpaque(false);
	    mainPanel.add(p,c);
	    c.gridy=1;
	    frame.getContentPane().add(mainPanel); 
	    frame.setLocationRelativeTo(null);
	    frame.pack();
	    frame.setVisible(true);
	}
	
	//called once a profile is selected
	private void drawMetaFields() {
		//creating the panel
		metaPanel = new JPanel(new GridBagLayout());
		metaPanel.setOpaque(false);
	
		refresh();
	
		mainPanel.remove(bottemPanel);
		//configuring spacing
		GridBagConstraints c = new GridBagConstraints();
		c.ipadx=2;
		c.ipady=4;
		c.insets=new Insets(0,4,4,4);
		c.gridx=0;
		c.gridy=0;
		
		int colnum;
		if(maximized)
			colnum=4;
		else
			colnum=2;
		//Positioning components 
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
		
		bottemPanel=new JScrollPane(metaPanel);
		bottemPanel.setVisible(true);
		
		//deals with sizing the panel
		if (!maximized && !resized) 
		{
			resized=true;
			bottemPanel.setPreferredSize(new Dimension(mainPanel.getWidth()+258,435));
		}
		else if (!maximized)
			bottemPanel.setPreferredSize(new Dimension(mainPanel.getWidth(),435));
		else
			bottemPanel.setPreferredSize(new Dimension(mainPanel.getWidth(),mainPanel.getHeight()-200));
		
		bottemPanel.getVerticalScrollBar().setUnitIncrement(16);
		
		metaPanel.setOpaque(false);
		bottemPanel.setOpaque(false);

		c = new GridBagConstraints();
		c.gridy=1;
		c.gridx=0;
		mainPanel.add(bottemPanel,c);
		refresh();
	    frame.getContentPane().add(mainPanel); 
	
	    if(!maximized)
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
    		
    		if (baglady.getOutput()==null && !saveField.getText().equals("")) 
    			baglady.setOutput(Paths.get(saveField.getText()));    
    	
    		if (baglady.getMetaoutput()==null && baglady.getOutput() != null)
    			baglady.setMetaoutput(Paths.get(baglady.getOutput().toString()+".txt"));
    		
    		//makes the bag with options enabled
    		int fields = fieldsFilled();
    		//checking that all required fields are filled
    		if (fields==1)
    			JOptionPane.showMessageDialog(null, "Please Select a profile");
    		
    		else if (fields == 2)
    			JOptionPane.showMessageDialog(null, "Please make sure all fields are filled");
    		
    		else if (fields == 3){
    			baglady.doeverything(zipped.isSelected(), customFields);
    			if (baglady.getSuccess())
    				{JOptionPane.showMessageDialog(null, "Bag Created Successfully");}
    			else
    			{
    				JOptionPane.showMessageDialog(null, "Sorry, There was an error creating the bag");
    				baglady.setSuccess(true);
    			}
    		}		
    	}    		
    	
    	//updates meta fields displayed when a profile is selected
    	else if (e.getSource().equals(profileSelect))
    	{
    		if (profileSelect.getSelectedIndex()==0)
    			{
    			mainPanel.remove(bottemPanel);
    			refresh();	
    			bottemPanel.setVisible(false);
    			
    			if (!maximized)
    				frame.pack();
    			
    			resized=false;
    			profilepicked=false;
    			}
    		else 
    		{
    			profile=new Profile((String)profileSelect.getSelectedItem(), this);
    			this.customFields=profile.getMetaFields();
    			drawMetaFields();
    			profilepicked=true;
    		} 		    		
    	}
    	//updates the record series when a function is selected.
    	else if (e.getSource().equals(customFields.get("Business Function")))
    		{
    			String businessFunc = ((JComboBox<String>) customFields.get("Business Function")).getSelectedItem().toString();
    			String[] test = null;
    			
				try {test = profile.updateComboBox(businessFunc);}
				catch (IOException e1) {e1.printStackTrace();}
    			
				((RequiredComboBox)customFields.get("DalClass Record Series")).removeAllItems();
    			for (int i =0;i<test.length;i++)
    			{
    				((JComboBox<String>)customFields.get("DalClass Record Series")).addItem(test[i]);
    			}
    			((JComboBox<String>)customFields.get("DalClass Record Series")).setSelectedIndex(0);
    		}	
    }
    //function to determine if all fields are filled.
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
		
	}
	
	//easier to know if the fame is maximized or iconfied this way.
	@Override
	public void windowStateChanged(WindowEvent e) {
		if ((e.getNewState() & frame.ICONIFIED) == frame.ICONIFIED)
			maximized=false;
		else if ((e.getNewState() & frame.MAXIMIZED_BOTH) == frame.MAXIMIZED_BOTH)
			maximized=true;
	}

	@Override
	public void windowGainedFocus(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowLostFocus(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
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
	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}


