package cc.darhao.undertow_demo;

import com.jfinal.kit.PropKit;
import com.jfinal.plugin.cron4j.Cron4jPlugin;
import com.jfinal.plugin.redis.Cache;
import com.jfinal.plugin.redis.Redis;
import com.jfinal.plugin.redis.RedisPlugin;
import com.jfinal.server.undertow.UndertowServer;

import cc.darhao.undertow_demo.util.VisualSerializer;
import sun.misc.Signal;
import sun.misc.SignalHandler;

public class App implements SignalHandler {
	
	private UndertowServer undertowServer;
	
	private RedisPlugin rp;
	
	private Cron4jPlugin cp;
	
	public static void main(String[] args) {
		App app = new App();
		
		Signal.handle(new Signal("INT"), app);//对应Ctrl+C 
		Signal.handle(new Signal("TERM"), app);//对应kill //详见->Linux信号：https://www.jianshu.com/p/f445bfeea40a
		app.start();
	}
	

	private void start() {
		//开启数据源插件
		System.out.println(Redis.use());
		rp = new RedisPlugin("chat", "localhost", 6379);
		rp.setSerializer(new VisualSerializer());
    	rp.start();
    	System.out.println(Redis.use());
		//开启Web服务器
		System.out.println(getClass().getClassLoader());
		undertowServer = UndertowServer.create(DemoConfig.class);
		undertowServer.setPort(8080);
		//Tips: Dev模式能提供代码热部署功能，不用重启就可以更新。但会有一个监控线程在运行，建议上线关闭该模式
		undertowServer.setDevMode(false);
		//配置websocket（可选）
		undertowServer.configWeb( builder ->{
	         builder.addWebSocketEndpoint(MyWebSocket.class);
		} );
		undertowServer.start();
		//配置Pasta
		cp = new Cron4jPlugin(PropKit.use("cron4config.txt"), "cron4j");
		cp.start();
		//开启Netty
		//...
		
		//打印Log4j2（如果日志写到数据库，一定要在数据源开启后打印）
		//...
	}

	
	@Override
	public void handle(Signal arg0) {
		try {
			//打印Log4j2（如果日志写到数据库，一定要在数据源关闭前打印）
			//...
			
			//开启Netty
			//...
			
			//开启定时线程
			//...
			if (cp != null) {
				cp.stop();
			}
			
			//关闭Web服务器
			undertowServer.stop();
			new ChatManagerTask().run();
			//关闭数据源插件
			if (rp != null) {
				rp.stop();
			}
			
			
			System.out.println("程序正常结束");
		} catch (Exception e){
			System.err.println("程序异常结束");
			System.exit(1);
		}
		
	}
}
