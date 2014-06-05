package disruptor.dbreadwrite;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.concurrent.Executors;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.EventHandlerGroup;

class RowEvent {
	private long id;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
}

/**
 * disruptor的测试类
 * 参考：https://github.com/LMAX-Exchange/disruptor/wiki/Getting-Started
 * 		http://lavasoft.blog.51cto.com/62575/238613
 * 
 * 简介：生产者不断往表里写入记录，消费者不断从表里删除记录
 * @author Longli
 * @since 2014-6-3 17:48:49
 */
public class LongliTestMain {
	
	static Connection con = null;
	static {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://localhost:4050/test?charactorEncoding=utf-8";
			con = DriverManager.getConnection(url , "root", "123456");
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	@SuppressWarnings({ "unchecked", "unused" })
	public static void main(String[] args) {
		Disruptor<RowEvent> disruptor = new Disruptor<RowEvent>(new EventFactory<RowEvent>() {
			@Override
			public RowEvent newInstance() {
				return new RowEvent();
			}
		}, 32/* ringBuffer容量 */, Executors.newCachedThreadPool()/*线程池，线程池最后应该shutdown的*/);
		
		// 消费者行为
		EventHandlerGroup<RowEvent> handlerGroup = disruptor.handleEventsWith(new EventHandler<RowEvent>() {
			@Override
			public void onEvent(RowEvent event, long sequence, boolean endOfBatch) throws Exception {
				long rowId = event.getId();
				try {
					Statement delStatement = con.createStatement();
					System.out.println("be to delete row with id=" + rowId);
					int delCount = delStatement.executeUpdate("delete from disruptor_test where id = " + rowId);
					System.out.println("end delete row with id=" + rowId + ", delCount=" + delCount);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});
		
		disruptor.start();
		
		// 生产者行为
		RingBuffer<RowEvent> ringBuffer = disruptor.getRingBuffer();
		for (int n=0; n<10000/*生产行为的次数*/; ++n) {
			long sequence = ringBuffer.next();
			RowEvent event = ringBuffer.get(sequence);
			try {
				// insert_and_get_id是我自己写的存储过程：插入一条记录，然后获取刚刚插入记录的自增id
				// 参考：http://lavasoft.blog.51cto.com/62575/238613
				CallableStatement prepareCall = con.prepareCall("call insert_and_get_id(?,?)");
				prepareCall.setString(1, "nLoop=" + n);
				prepareCall.registerOutParameter(2, Types.BIGINT);
				prepareCall.executeUpdate();
				long lastInsertId = prepareCall.getLong(2);
				event.setId(lastInsertId);
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				ringBuffer.publish(sequence);
			}
		}
	}
}

