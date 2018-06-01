package dalclassbagger;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.plaf.FileChooserUI;

import gov.loc.repository.bagit.hash.StandardSupportedAlgorithms;

public class Baggui implements ActionListener{

	 static JButton open = new JButton("Select Folder");
	 static JButton save = new JButton("Save as");
	 static JButton start = new JButton("Bag!");
	 static JCheckBox zipped = new JCheckBox("zip the bag");
	 static String[] algorithms = {"MD5","SHA-1","SHA224","SHA256","SHA512"};
	 static JComboBox algorithmSelect = new JComboBox(algorithms); 
	 
	 
	 final JFileChooser fc = new JFileChooser();
	 Baglady baglady=null;
	 
	public Baggui(Baglady baglady){
		open.addActionListener(this);
		save.addActionListener(this);
		start.addActionListener(this);
		this.baglady=baglady;
		fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		algorithmSelect.setSelectedIndex(0);
	//	algorithmSelect.addActionListener(this);
	}
	 
	public void createAndShowGUI() {
		JPanel p = new JPanel(new GridBagLayout());
		
		JFrame.setDefaultLookAndFeelDecorated(true);
        
        JFrame frame = new JFrame("HelloWorldSwing");
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	  //  JLabel label = new JLabel("Hello World");
	    //frame.getContentPane().add(label);
	    GridBagConstraints c = new GridBagConstraints();
	
	    
	    c.gridx=0;
	    c.gridy=0;
	    
	    p.add(open,c);
	    
	    c.gridx=1;
	    p.add(save,c);
	    
	    c.gridy=1;
	    p.add(start,c);
	    
	    c.gridx=2;
	    c.gridy=0;
	    p.add(zipped,c);
	    
	    c.gridx=0;
	    c.gridy=1;
	    p.add(algorithmSelect,c);
	    
	    
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
    		}
    	}
    	
    	else if (e.getSource().equals(save)) {    	
    		int returnVal = fc.showSaveDialog(save); 	
    		if (returnVal == JFileChooser.APPROVE_OPTION)
    		{
    			baglady.setOutput(fc.getSelectedFile().toPath());
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
    	}
    	
    	
    }

}


