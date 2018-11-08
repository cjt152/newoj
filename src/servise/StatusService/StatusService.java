package servise.StatusService;

import entity.User;
import util.Main;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 分线程处理所有的statu表的内容和user的ac相关内容（暂时，最好把所有都放进去，保证同一个玩家的数据在单个线程上修改，其他线程只读）
 * Created by QAQ on 2018/10/31.
 */

abstract class UserEvent {
    public String username;
    abstract void run(User u);
}

class EveryThread implements Runnable{
    public BlockingQueue<UserEvent> queue;
    EveryThread()
    {
        queue = new LinkedBlockingQueue<>();
    }
    @Override
    public void run() {
        while(true)
        {
            UserEvent event = null;
            try {
                event = queue.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(event == null) continue;
            event.run(Main.users.getUser(event.username));
        }
    }
}
public class StatusService {
    private static int serviceThreadNumber = 11;
    public List<EveryThread> threads;
    public void init()
    {
        threads = new ArrayList<>();
        for(int i = 0 ; i< serviceThreadNumber; i ++){
            EveryThread thread = new EveryThread();
            threads.add(thread);
            new Thread(thread).start();
        }
    }

    public void AddEvent(String username,UserEvent event){
        event.username = username;
        try {
            int z = username.hashCode();
            if(z < 0) z = -z ;
            threads.get(z  % serviceThreadNumber).queue.put(event);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
