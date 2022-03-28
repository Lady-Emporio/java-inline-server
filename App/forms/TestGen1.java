package app.forms;

import com.j256.ormlite.field.DatabaseField;

import app.forms.annotations.FormField;

public class TestGen1 {
	public DatabaseField formField;
	public String val;
	public TestGen1(DatabaseField formField,String val) {
		this.formField=formField;
		this.val=val;
	}
	
}
