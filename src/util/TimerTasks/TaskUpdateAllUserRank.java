package util.TimerTasks;

import servise.MallMain;
import util.Main;
import util.MyTime;
import util.Tool;

import java.sql.Timestamp;
import java.util.Timer;

/**
 * Created by QAQ on 2016/10/14.
 */
public class TaskUpdateAllUserRank extends MyTimer {
    public static Timestamp nextUpdateTime = null;
    @Override
    public void run() {
        if(nextUpdateTime!=null && Tool.now().after(nextUpdateTime)) {
            Main.users.updateAllUserAcnum();
            Main.users.updateAllUserRank();
            nextUpdateTime = null;
        }
    }

    @Override
    public void getTimer() throws Exception {
        setEveryDay(5,30,0);
        new Timer().scheduleAtFixedRate(this, date, period);

        //每5分钟更新一次
        //new Timer().scheduleAtFixedRate(this, 5 * MyTime.MINUTE, 5 * MyTime.MINUTE);
    }
}
