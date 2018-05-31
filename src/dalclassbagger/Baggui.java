package dalclassbagger;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.plaf.FileChooserUI;

public class Baggui implements ActionListener{

	 static JButton b1 = new JButton("Select Folder to Bag");
	 final JFileChooser fc = new JFileChooser();
	 
	public Baggui(){
		b1.addActionListener(this);
	}
	 
	public static void createAndShowGUI() {
	       //Make sure we have nice window decorations.
		
		JFrame.setDefaultLookAndFeelDecorated(true);
        
		//Create and set up the window.
        JFrame frame = new JFrame("HelloWorldSwing");
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	    //Add the ubiquitous "Hello World" label.
	    JLabel label = new JLabel("Hello World");
	    frame.getContentPane().add(label);
	    
	    
	    frame.getContentPane().add(b1);
	    //Display the window.
	    frame.pack();
	    frame.setVisible(true);
	}
	
	
    

    public void actionPerformed(ActionEvent e) {
    	int returnVal = fc.showOpenDialog(b1);
    	if (returnVal == JFileChooser.APPROVE_OPTION)
    	{
    		
    	}
    }

}


