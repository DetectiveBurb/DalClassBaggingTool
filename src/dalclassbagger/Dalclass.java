package dalclassbagger;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class Dalclass {


	public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
		Baglady baglady=new Baglady();
		Baggui gui=new Baggui(baglady);
		gui.askProfile();
		gui.createAndShowGUI();
	}
}
