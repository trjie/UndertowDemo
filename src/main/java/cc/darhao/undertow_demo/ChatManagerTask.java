package cc.darhao.undertow_demo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.jfinal.plugin.redis.Redis;

public class ChatManagerTask implements Runnable {

	public static Object Lock = new Object();
	
	@Override
	public void run() {
		synchronized (Lock) {
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
			String time = format.format(new Date());
			File folder = new File(File.separator + "file");
			if (!folder.exists()) {
				folder.mkdirs();
			}
			File file = new File(File.separator + "file" + File.separator + "chat_" + time);
			System.out.println(file.getAbsolutePath());
			if (!file.exists()) {
				try {
					file.createNewFile();
					try (FileOutputStream outputStream = new FileOutputStream(file, true);){
						
						List<String> messages = ChatRedisDAO.getInstance().getAndRemove();
						if (messages != null && !messages.isEmpty()) {
							for (String message : messages) {
								outputStream.write((message + "\n").getBytes(Charset.forName("utf-8")));
							}
							outputStream.flush();
						}
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

}
