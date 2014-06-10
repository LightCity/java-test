package lang.io;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisTest {
	public static void main(String[] argv) {
		JedisPool pool = new JedisPool(new JedisPoolConfig(), "localhost");
		Jedis jedis = pool.getResource();

		jedis.set("foo", "bar");
		String foobar = jedis.get("foo");
		System.out.println(foobar);
		assert foobar.equals("bar");

		pool.returnResource(jedis);
		pool.destroy();
	}
}
