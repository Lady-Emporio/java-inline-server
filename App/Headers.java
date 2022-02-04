package app;

import java.util.ArrayList;
import java.util.Map;



public class Headers {
	public static class Header {
		private final String name;
		private final String value;

		public Header(String name, String value) {
			this.name = name.trim();
			this.value = value.trim();
			// RFC2616#14.23 - header can have an empty value (e.g. Host)
			if (this.name.length() == 0) { // but name cannot be empty
				throw new IllegalArgumentException("name cannot be empty");
			}
		}

		public String getName() {
			return name;
		}

		public String getValue() {
			return value;
		}
		@Override
		public String toString() {
			return super.toString()+": "+name+" : "+value;
		}
	}
	private ArrayList<Header> data=new ArrayList<Header>();
	
	public String get(String name) {
		for (int i = 0; i < data.size(); i++) {
			if (data.get(i).getName().equalsIgnoreCase(name)) {
				return data.get(i).getValue();
			}
		}
		return null;
	}
	public Header replace(String name, String value) {
		for (int i = 0; i < data.size(); i++) {
			if (data.get(i).getName().equalsIgnoreCase(name)) {
				Header prev=data.get(i);
				data.set(i, new Header(name,value));
				return prev;
			}
		}
		data.add(new Header(name,value));
		return null;
    }
	
	@Override
	public String toString() {
		StringBuffer w=new StringBuffer();
		for(Header i : data) {
			w.append(i.toString());
			w.append('\n');
		}
		return w.toString();
	}
}