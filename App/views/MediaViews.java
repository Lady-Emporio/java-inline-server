package app.views;

import app.ClearSky;
import app.server.HttpFiles;
import app.server.Request;
import app.server.Response;

import static app.server.Settings.*;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MediaViews {
	public static void getAudioFromJs(Request req, Response resp) throws IOException {
		System.out.println("getAudioFromJs begin");

		String timeStamp1 = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());

		{
			BufferedWriter writer = new BufferedWriter(new FileWriter(BASE_DIR + "wtf.hex"));
			for (byte b : req.rawBody) {
				writer.write(b);
			}
			writer.close();
		}
		ClearSky.log("MediaViews get files: "+req.getFiles().size()+".");
		for (int i = 0; i < req.getFiles().size(); ++i) {
			HttpFiles file = req.getFiles().get(i);
			byte[] data = file.getData();
			String fileName = BASE_DIR + "\\audio\\" + timeStamp1 + "_" + i + ".wav";
//			BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
//			for (byte b : data) {
//				writer.write(b);
//			}
//			writer.close();
			
			int unsignedByte;
			DataOutputStream dos = new DataOutputStream(new FileOutputStream(fileName));
			for (byte b : data) {
				unsignedByte = ((int)b) & 0xff;
				dos.writeByte(unsignedByte);
			}
			dos.close();
					
					
		}
		System.out.println("getAudioFromJs end");
	}
}
