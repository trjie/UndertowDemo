package cc.darhao.undertow_demo;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import com.jfinal.core.Controller;

public class HelloController extends Controller {
   
	public void list() {
       File file = new File(File.separator + "file");
       if (!file.exists()) {
		file.mkdirs();
       }
       String[] list = file.list();
       List<String> files = Arrays.asList(list);
       renderJson(files);
    }
	
	
	public void download(String fileName) {
		File file = new File(File.separator + "file" + File.separator + fileName );
		if (file.exists()) {
    		renderFile(file);
		}else {
			renderNull();
		}
		
	}

}