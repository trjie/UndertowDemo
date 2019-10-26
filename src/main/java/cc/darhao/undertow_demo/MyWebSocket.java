package cc.darhao.undertow_demo;

import java.io.IOException;

import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 *  注意：<br>
 *  由于 JFinalFilter 会接管所有不带 "." 字符的 URL 请求<br>
 *  所以 @ServerEndpoint 注解中的 URL 参数值建议以 ".ws" 结尾，<br>
 *  否则请求会响应 404 找不到资源<br>
 * <br>
 * <b>2019年10月26日</b>
 * @author <a href="https://github.com/darhao">鲁智深</a>
 */
@ServerEndpoint("/myapp.ws")
public class MyWebSocket { 
	
    @OnMessage
    public void message(String message, Session session) throws IOException {
        session.getBasicRemote().sendText("你好世界");
    }
    
}