package dalclassbagger;

import java.util.Vector;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;

public class RequiredComboBox extends JComboBox<Object> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	boolean required=false;
	
	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public RequiredComboBox() {
		// TODO Auto-generated constructor stub
	}

	public RequiredComboBox(ComboBoxModel aModel) {
		super(aModel);
		// TODO Auto-generated constructor stub
	}

	public RequiredComboBox(Object[] items) {
		super(items);
		// TODO Auto-generated constructor stub
	}

	public RequiredComboBox(Vector items) {
		super(items);
		// TODO Auto-generated constructor stub
	}

}
