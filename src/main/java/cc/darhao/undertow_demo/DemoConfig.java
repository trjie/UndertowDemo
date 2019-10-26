package cc.darhao.undertow_demo;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.template.Engine;

public class DemoConfig extends JFinalConfig {
 
    public void configConstant(Constants me) {
       me.setDevMode(true);
    }
    
    public void configRoute(Routes me) {
       me.add("/hello", HelloController.class);
    }
    
    public void configEngine(Engine me) {}
    public void configPlugin(Plugins me) {
    	//特别注意：所有插件会在这里配置，onStart方法是所有插件启动后回调，onStop方法是所有插件关闭后回调
    	//所以不要在onStop后才去关闭Netty、定时线程等，否则有很大可能会抛异常（除非你的Netty、定时线程根本没有用到Redis、Db）
    	//如果你的项目有Netty和定时线程，那么共用的插件（如Druid、Redis等）需要在main方法里开启，并且使用信号量回调方法进行关闭
    }
    public void configInterceptor(Interceptors me) {}
    public void configHandler(Handlers me) {}
    
    
    @Override
    public void onStart() {
    	System.out.println("web服务已正常开启");
    }
    
    
    /* 
     * 该方法使用kill指令（除 Kill -9 指令外）或者Ctrl + C均可触发
     */
    @Override
    public void onStop() {
    	System.out.println("web服务已安全关闭");
    }
}
