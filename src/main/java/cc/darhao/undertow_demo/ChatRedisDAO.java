package cc.darhao.undertow_demo;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.jfinal.plugin.redis.Cache;
import com.jfinal.plugin.redis.Redis;

public class ChatRedisDAO {
	private static ChatRedisDAO dao;
	private Cache cache;
	private String key = "CHAT_MESSAGE";

	public static ChatRedisDAO getInstance() {
		if (dao == null) {
			synchronized (ChatRedisDAO.class) {
				if (dao == null) {
					dao = new ChatRedisDAO();
				}
			}
		}
		return dao;
	}
	
	private ChatRedisDAO() {
		cache = Redis.use();
	}
	public synchronized void put(String message) {
		cache.rpush(key, message);
	}
	
	@SuppressWarnings("unchecked")
	public synchronized List<String> get() {
		List<String> messages = (List<String>) cache.lrange(key, 0, -1);
		return  messages;
	}
	
	
	public synchronized void remove() {
		cache.del(key);
	}
	
	public synchronized List<String> getAndRemove() {
		List<String> messages = get();
		cache.del(key);
		return messages;
	}
	
	
	public static void main(String[] args) {
		ExecutorService executorService = Executors.newFixedThreadPool(10);
		for (int i = 0; i < 100; i++) {
			executorService.submit(()->{
				System.out.println(ChatRedisDAO.getInstance());
			});
		}
	}
}


