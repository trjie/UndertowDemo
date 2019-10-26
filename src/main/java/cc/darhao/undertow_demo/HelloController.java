package cc.darhao.undertow_demo;

import com.jfinal.core.Controller;

public class HelloController extends Controller {
   
	public void hi() {
       renderText("Hello JFinal World.");
    }

}