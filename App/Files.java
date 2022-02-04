package app;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class Files {
	private byte[] data;
	private String rawContentDisposition;
	private String contentDisposition="";
	private String name="";
	private String filename="";
	public Files(String rawContentDisposition, byte[] data) throws IOException {
		this.data=data;
		this.rawContentDisposition=rawContentDisposition;
		parseRawContentDisposition();
		

	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString()+"---"+contentDisposition+"---"+name+"---"+filename;
	}

	
	private void parseRawContentDisposition() throws IOException {
		//Content-Disposition: form-data; name="upload_file"; filename="1.jpg"
		String[] vt = this.rawContentDisposition.split(";");
		for(String i:vt) {
			i=i.trim();
			if (i.toLowerCase().startsWith("content-disposition")) {
				String []pair=i.split(": ");
				if (pair.length!=2) {
					throw new IOException("Problem with content-disposition in content-disposition.");
				}
				contentDisposition=pair[1].trim();
			}else if(i.toLowerCase().startsWith("name")) {
				String []pair=i.split("=");
				if (pair.length!=2) {
					throw new IOException("Problem with content-disposition in content-disposition.");
				}
				String trimName=pair[1].trim();
				name=trimName.substring(1, trimName.length()-1);
				name=URLDecoder.decode(name, "UTF-8").trim();
				
			}else if (i.toLowerCase().startsWith("filename")) {
				String []pair=i.split("=");
				if (pair.length!=2) {
					throw new IOException("Problem with content-disposition in content-disposition.");
				}
				String trimFilename=pair[1].trim();
				filename=trimFilename.substring(1, trimFilename.length()-1);
				filename=URLDecoder.decode(filename, "UTF-8").trim();
			}
		}
	}
	public String getName() {
		return name;
	}
	public String getFilename() {
		return filename;
	}
	public String getContentDisposition() {
		return contentDisposition;
	}
	public byte[] getData() {
		return data;
	}
}
