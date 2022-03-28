package app.forms.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import app.forms.annotations.FormField.FormTypes;

@Retention(RUNTIME)
@Target(FIELD)
public @interface FormField {
	boolean isReadOnly() default false;
	String typeInput() default "text";
	FormTypes typeGetData() default FormTypes.typeStr;
	
	public enum FormTypes{
		typeStr,typeInt,typeDouble
	}
}
