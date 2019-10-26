package cc.darhao.undertow_demo;

import com.jfinal.server.undertow.UndertowServer;
import sun.misc.Signal;
import sun.misc.SignalHandler;

public class App implements SignalHandler {
	
	private UndertowServer undertowServer;
	
	
	public static void main(String[] args) {
		App app = new App();
		Signal.handle(new Signal("INT"), app);//对应Ctrl+C
		Signal.handle(new Signal("TERM"), app);//对应kill
		//详见->Linux信号：https://www.jianshu.com/p/f445bfeea40a
		app.start();
	}
	

	private void start() {
		//开启数据源插件
		//...
		
		//开启Web服务器
		undertowServer = UndertowServer.create(DemoConfig.class);
		undertowServer.setPort(10007);
		//Tips: Dev模式能提供代码热部署功能，不用重启就可以更新。但会有一个监控线程在运行，建议上线关闭该模式
		undertowServer.setDevMode(true);
		//配置websocket（可选）
		undertowServer.configWeb( builder ->{
	         builder.addWebSocketEndpoint(MyWebSocket.class);
		} );
		undertowServer.start();
		//配置Pasta
		//...
		
		
		//开启定时线程
		//...
		
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
			
			//关闭Web服务器
			undertowServer.stop();
			
			//关闭数据源插件
			//...
			
			System.out.println("程序正常结束");
		} catch (Exception e){
			System.err.println("程序异常结束");
			System.exit(1);
		}
		
	}
}
