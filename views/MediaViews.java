package views;

import app.Files;
import app.Request;
import app.Response;
import static app.Settings.*;

import java.io.BufferedWriter;
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

		for (int i = 0; i < req.getFiles().size(); ++i) {
			Files file = req.getFiles().get(i);
			byte[] data = file.getData();
			BufferedWriter writer = new BufferedWriter(
					new FileWriter(BASE_DIR + "\\audio\\" + timeStamp1 + "_" + i + ".wav"));
			for (byte b : data) {
				writer.write(b);
			}
			writer.close();
		}
		System.out.println("getAudioFromJs end");
	}
}
