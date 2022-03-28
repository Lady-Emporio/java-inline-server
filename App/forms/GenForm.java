package app.forms;

import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Base64;

import com.j256.ormlite.field.DatabaseField;

import app.forms.annotations.FormField;

public class GenForm {

	// https://commons.apache.org/proper/commons-lang/apidocs/src-html/org/apache/commons/lang3/text/translate/EntityArrays.html
	private static final String[][] BASIC_ESCAPE = { { "\"", "&quot;" }, // " - double-quote
			{ "&", "&amp;" }, // & - ampersand
			{ "<", "&lt;" }, // < - less-than
			{ ">", "&gt;" }, // > - greater-than
	};

	public static String generateHtml(Class<?> form, Object instance) throws Exception {
		Field[] classFields = form.getDeclaredFields();

		ArrayList<TestGen1> fields = new ArrayList<TestGen1>();
		for (Field clssField : classFields) {
			DatabaseField formField = clssField.getAnnotation(DatabaseField.class);
			if (null == formField) {
				continue;
			}

			Field instField = instance.getClass().getField(clssField.getName());
			String val;
			if (null == instField) {
				throw new Exception("Field not found." + instance.getClass().getName() + "-" + clssField.getName());
			}
			//https://ormlite.com/data_types.shtml
			switch (formField.dataType()) {
			case STRING: {
				val = "" + getDouble(instField, instance);
			}
				break;

			case LONG_STRING: {
				val = getString(instField, instance);;
			}
				break;
			case INTEGER: {
				val = "" +getInt(instField, instance);
			}
				break;
			case BYTE_ARRAY: {
				val = getString(instField, instance);
				// byte[] decoded = Base64.getDecoder().decode(val.getBytes());
				// String decodedStr=new String(decoded);
			}
				break;
			default: {
				throw new Exception("Type field not support.");
			}
			}
			fields.add(new TestGen1(formField, val));

		}

		StringBuilder rawHtml = new StringBuilder();
		rawHtml.append("<form method='post'>");
		
		String sign;
		String value;
		TestGen1 data;
		boolean needReplace;
		for (int i = 0; i < fields.size(); ++i) {
			data = fields.get(i);
			value=data.val;
			StringBuilder  escapeHtml=new StringBuilder();
			for(int charI=0;charI<value.length();++charI) {
				sign=value.substring(charI,charI+1);
				for(String[] excape:BASIC_ESCAPE) {
					if (sign.equals(excape[0])){
						sign=excape[1];
						break;
					}
				}
				escapeHtml.append(sign);
			}
			rawHtml.append("<input type='").append("TEXT").append("'")
					.append(data.formField.generatedId() ? " readonly " : "")
					.append("value='").append(escapeHtml.toString()).append("'")
					.append(" >");
		}
		rawHtml.append("<input type='submit' value='Записать'>");
		rawHtml.append("</form>");
		return rawHtml.toString();
	}

	public static int getInt(Field instField, Object instance) throws Exception {
		int val = instField.getInt(instance);
		return val;
	}

	public static double getDouble(Field instField, Object instance) throws Exception {
		double val;
		Class<?> classField = instField.getType();
		if (classField.equals(float.class)) {
			val = (double) instField.getFloat(instance);
		} else if (classField.equals(double.class)) {
			val = instField.getDouble(instance);
		} else {
			throw new Exception(
					"Can not cast type: '" + instance.getClass().getName() + "' '" + instField.getName() + "'.");
		}
		return val;
	}

	public static String getString(Field instField, Object instance) throws Exception {
		String val;
		Class<?> classField = instField.getType();
		if (classField.equals(byte[].class)) {
			byte[] valVt = (byte[]) instField.get(instance);
			byte[] encoded = Base64.getEncoder().encode(valVt);
			val = new String(encoded);

		} else if (classField.equals(String.class)) {
			val = (String) instField.get(instance);
		} else {
			throw new Exception(
					"Can not cast type: '" + instance.getClass().getName() + "' '" + instField.getName() + "'.");
		}
		return val;
	}
}
