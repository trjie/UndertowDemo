package cc.darhao.undertow_demo;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.jfinal.plugin.redis.Redis;

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
	static Map<String, Session> userSessionMap = new ConcurrentHashMap<>();
	static Map<Session, String> sessionUserMap = new ConcurrentHashMap<>();
	
    @OnMessage
    public void message(String message, Session session) throws IOException {
    	System.out.println(getClass().getClassLoader());
    	if (message.contains(":")) {
			if (message.startsWith("Login:")) {
				String nickName = message.substring(message.indexOf(":") + 1, message.length());
				if (nickName.trim().equals("")) {
					session.getBasicRemote().sendText("服务器：登录失败，昵称无效！");
				}else if (userSessionMap.containsKey(nickName)) {
					session.getBasicRemote().sendText("服务器：登录失败，昵称重复！");
				}else {
					userSessionMap.put(nickName, session);
					sessionUserMap.put(session, nickName);
					session.getBasicRemote().sendText("服务器：登录成功！");
					String record = "用户"+ nickName + "加入群聊！";
					for (Entry<String, Session> entry : userSessionMap.entrySet()) {
						if (entry.getKey().equals(nickName)) {
							continue;
						}
						if (entry.getValue().isOpen()) {
							entry.getValue().getBasicRemote().sendText(record);
						}
					}
					ChatRedisDAO.getInstance().put(record);
						
				}
			}else if (message.startsWith("Say:")) {
				String content = message.substring(message.indexOf(":") + 1, message.length());
				 if (!userSessionMap.containsValue(session)) {
					session.getBasicRemote().sendText("服务器：发送失败，用户未登录！");
				} else if (content.equals("")) {
					session.getBasicRemote().sendText("服务器：发送失败，内容不能为空！");
				}else {
					String record = "用户"+ sessionUserMap.get(session) + "：" + content;
					for (Entry<String, Session> entry : userSessionMap.entrySet()) {
						if (entry.getValue().equals(session)) {
							continue;
						}
						if (entry.getValue().isOpen()) {
							entry.getValue().getBasicRemote().sendText(record);
						}
					}
					ChatRedisDAO.getInstance().put(record);
				}
			}else {
				session.getBasicRemote().sendText("发送失败，非法指令！");
			}
		}else {
			session.getBasicRemote().sendText("发送失败，非法指令！");
		}
    }
    
    @OnClose
    public void onClose(CloseReason closeReason, Session session) {
    	String nickName = sessionUserMap.remove(session);
    	if (nickName != null) {
			userSessionMap.remove(nickName);
		}
    }
}