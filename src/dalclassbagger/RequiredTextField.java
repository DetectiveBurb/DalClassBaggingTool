package dalclassbagger;

import javax.swing.JTextField;
import javax.swing.text.Document;

public class RequiredTextField extends JTextField {

	boolean required=false;
	
	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public RequiredTextField() {
		// TODO Auto-generated constructor stub
	}

	public RequiredTextField(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public RequiredTextField(int arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public RequiredTextField(String arg0, int arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	public RequiredTextField(Document arg0, String arg1, int arg2) {
		super(arg0, arg1, arg2);
		// TODO Auto-generated constructor stub
	}

}
