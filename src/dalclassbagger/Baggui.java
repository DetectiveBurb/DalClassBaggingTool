package dalclassbagger;

import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Paths;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.FileChooserUI;

import com.alee.laf.WebLookAndFeel;

import gov.loc.repository.bagit.hash.StandardSupportedAlgorithms;

public class Baggui implements ActionListener{

	 static JButton open = new JButton("Select Folder");
	 static JButton save = new JButton("Save as");
	 static JButton start = new JButton("Bag!");
	 static JCheckBox zipped = new JCheckBox("zip the bag");
	 static String[] algorithms = {"MD5","SHA-1","SHA224","SHA256","SHA512"};
	 static String[] profiles = {"No Profile", "Hello", "Out", "Of","Time"};
	 static JComboBox<String> algorithmSelect = new JComboBox<String>(algorithms); 
	 static JComboBox<String> profileSelect = new JComboBox<String>(profiles);
	 static JButton metaDataB = new JButton("Save Meta Data");
	 static JTextField saveField = new JTextField(17);
	 static JTextField openField = new JTextField(17);
	 static JTextField metaField = new JTextField(17);

	 
	 
	 final JFileChooser fc = new JFileChooser();
	 Baglady baglady=null;
	 
	public Baggui(Baglady baglady){
		open.addActionListener(this);
		save.addActionListener(this);
		start.addActionListener(this);
		metaDataB.addActionListener(this);
		saveField.addActionListener(this);
		openField.addActionListener(this);
		metaField.addActionListener(this);
		this.baglady=baglady;
		fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		algorithmSelect.setSelectedIndex(0);
	//	algorithmSelect.addActionListener(this);
	}
	 
	public static JButton getMetaDataB() {
		return metaDataB;
	}

	public static void setMetaDataB(JButton metaDataB) {
		Baggui.metaDataB = metaDataB;
	}

	public void createAndShowGUI() {
		JPanel p = new JPanel(new GridBagLayout());	
		
		  WebLookAndFeel.install();
		//JFrame.setDefaultLookAndFeelDecorated(true);        
		/*  try {
		    //  UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		
		    } catch (ClassNotFoundException e) {
		      e.printStackTrace();
		    } catch (InstantiationException e) {
		      e.printStackTrace();
		    } catch (IllegalAccessException e) {
		      e.printStackTrace();
		    } catch (UnsupportedLookAndFeelException e) {
		      e.printStackTrace();
		    }*/
		  
		JFrame frame = new JFrame("Dal Class Bagger Tool");  
		SwingUtilities.updateComponentTreeUI(frame);	
		
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    GridBagConstraints c = new GridBagConstraints();
	    c.ipadx=4;
	    c.ipady=4;
	    c.insets=new Insets(4,12,4,15);

	
	    open.setPreferredSize(new Dimension(120,35));
	    save.setPreferredSize(new Dimension(120,35));
	    profileSelect.setPreferredSize(new Dimension(120,22));
	    metaDataB.setPreferredSize(new Dimension(120,35));
	    algorithmSelect.setPreferredSize(new Dimension(120,22));
	    
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
	    p.add(metaDataB,c);
	    c.gridx=1;
	    p.add(metaField,c);
	    
	    c.gridx=0;
	    c.gridy=4;
	    p.add(algorithmSelect,c);
	    
	    c.gridx=0;
	    c.gridy=5;
	    p.add(zipped,c);
	    
	    c.gridy=5;
	    c.gridx=4;
	    p.add(start,c);
	    
	    //saveField.setText(baglady.getOutput().toString());
	    
	    frame.getContentPane().add(p); 
	    frame.setLocationRelativeTo(null);
	    frame.pack();
	    frame.setVisible(true);
	}
	
	
    

    public void actionPerformed(ActionEvent e) {
    	if (e.getSource().equals(open)) {    	
    		int returnVal = fc.showOpenDialog(open); 	
    		if (returnVal == JFileChooser.APPROVE_OPTION)
    		{
    			baglady.setInput(fc.getSelectedFile().toPath());
    			openField.setText(fc.getSelectedFile().toString());
    			saveField.setText(System.getProperty("user.home")+baglady.delimeter+"Desktop"+baglady.delimeter+baglady.getInput().getFileName()+baglady.delimeter);
    		}
    	}
    	
    	else if (e.getSource().equals(save)) {    	
    		int returnVal = fc.showSaveDialog(save); 	
    		if (returnVal == JFileChooser.APPROVE_OPTION)
    		{
    			baglady.setOutput(fc.getSelectedFile().toPath());
    			saveField.setText(fc.getSelectedFile().toString());
    		}
    	}
    	
    	else if (e.getSource().equals(metaDataB)) {
    		FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES", "txt", "text");
    		fc.setFileFilter(filter);
        	int returnVal = fc.showSaveDialog(metaDataB); 	
        	if (returnVal == JFileChooser.APPROVE_OPTION)
        	{
        		baglady.setMetaoutput(fc.getSelectedFile().toPath());
        		metaField.setText(fc.getSelectedFile().toString());
        	}
        	fc.setFileFilter(null);
    	}	
    	  	
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
    		
    		if (baglady.getOutput()==null && !saveField.getText().equals(""))
    			baglady.setOutput(Paths.get(saveField.getText()));    
    	
    		if (baglady.getMetaoutput() != null)
    			if (baglady.getMetaoutput().toString().indexOf(".txt")==-1 || baglady.getMetaoutput().toString().indexOf(".text")==-1)
    				baglady.setMetaoutput(Paths.get(baglady.getMetaoutput().toString()+".txt"));
   
    		baglady.doeverything(zipped.isSelected());
    		
    		if (baglady.getSuccess())
    		{
    			JOptionPane.showMessageDialog(null, "Bag Created Successfully");
    		}
    		else
    		{
    			JOptionPane.showMessageDialog(null, "there was an error creating the bag");
    		}
    	}
    	
    	else if (e.getSource().equals(saveField))
    	{
    		baglady.setOutput(Paths.get(saveField.getText()));
    	}
    	
    	else if (e.getSource().equals(openField))
    	{
    		baglady.setInput(Paths.get(openField.getText()));

    	}
    	
    	else if (e.getSource().equals(metaField))
    	{
    		baglady.setMetaoutput(Paths.get(openField.getText()));
    	}
    	
    	
    }

}


