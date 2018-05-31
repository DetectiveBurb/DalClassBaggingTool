package dalclassbagger;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.plaf.FileChooserUI;

public class Baggui implements ActionListener{

	 static JButton open = new JButton("Select Folder to Bag");
	 static JButton save = new JButton("Save as");
	 static JButton start = new JButton("Bag!");
	 static JCheckBox zipped = new JCheckBox("zip the bag");
	 
	 final JFileChooser fc = new JFileChooser();
	 Baglady baglady=null;
	 
	public Baggui(Baglady baglady){
		open.addActionListener(this);
		save.addActionListener(this);
		start.addActionListener(this);
		this.baglady=baglady;
		fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
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
	    
	    p.add(open);
	    
	    c.gridx=1;
	    p.add(save);
	    
	    c.gridy=1;
	    p.add(start);
	    
	    c.gridx=0;
	    c.gridy=1;
	    p.add(zipped);
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
    		int returnVal = fc.showOpenDialog(save); 	
    		if (returnVal == JFileChooser.APPROVE_OPTION)
    		{
    			baglady.setOutput(fc.getSelectedFile().toPath());
    		}
    	}
    	
    	else if (e.getSource().equals(start)){
    		baglady.doeverything(zipped.isSelected());
    	}
    	
    	
    }

}


