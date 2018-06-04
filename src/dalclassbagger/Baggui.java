package dalclassbagger;

import java.awt.FileDialog;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Paths;

import javax.swing.*;
import javax.swing.plaf.FileChooserUI;

import com.alee.laf.WebLookAndFeel;

import gov.loc.repository.bagit.hash.StandardSupportedAlgorithms;

public class Baggui implements ActionListener{

	 static JButton open = new JButton("Select Folder");
	 static JButton save = new JButton("Save as");
	 static JButton start = new JButton("Bag!");
	 static JCheckBox zipped = new JCheckBox("zip the bag");
	 static String[] algorithms = {"MD5","SHA-1","SHA224","SHA256","SHA512"};
	 static String[] profiles = {"test", "Hello", "Out", "Of","Time"};
	 static JComboBox algorithmSelect = new JComboBox(algorithms); 
	 static JComboBox profileSelect = new JComboBox(profiles);
	 static JButton metaDataB = new JButton("Save Meta Data as");
	 static JTextField saveField = new JTextField(10);
	 static JTextField openField = new JTextField(10);
	 static JTextField metaField = new JTextField(10);

	 
	 
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
	    p.add(zipped,c);
	    
	    c.gridx=0;
	    c.gridy=5;
	    p.add(algorithmSelect,c);
	    
	   
	    
	    c.gridy=5;
	    c.gridx=4;
	    p.add(start,c);
	    
	    frame.getContentPane().add(p); 
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
        	int returnVal = fc.showSaveDialog(metaDataB); 	
        	if (returnVal == JFileChooser.APPROVE_OPTION)
        	{
        		baglady.setMetaoutput(fc.getSelectedFile().toPath());
        		metaField.setText(fc.getSelectedFile().toString());
        	}
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


