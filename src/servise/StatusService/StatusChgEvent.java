package servise.StatusService;

import entity.Result;
import entity.Status;
import entity.User;
import util.Event.EventMain;
import util.Event.Events.EventJudge;
import util.Event.Events.EventStatusChange;
import util.Main;
import util.MyTime;
import util.TimerTasks.TaskUpdateAllUserRank;
import util.Tool;

import java.util.Map;

/**
 * Created by QAQ on 2018/10/31.
 */
public class StatusChgEvent extends UserEvent{
    public int rid;
    public Result res;
    public String time;
    public String memory;
    public String CEInfo;
    public int score = -1;

    @Override
    void run(User user) {
        Status ps = Main.status.getStatu(rid).clone();
        Map<Integer,Integer> map = user.getAcProblem();

        Status s = Main.status.setStatusResult(rid, res, time, memory, CEInfo,score);

        Main.submitter.onSubmitDone(s);
        EventMain.triggerEvent(new EventStatusChange(ps,s));
        EventMain.triggerEvent(new EventJudge(Main.users.getUser(s.getUser()),s));

        Main.status.onStatusChange(ps,s);

        //Main.users.updateUserAcnum(s.getUser());
        if(!ps.getResult().isAc() && s.getResult().isAc()){
            if(map.containsKey(ps.getPid()))
            {
                map.put(ps.getPid(),map.get(ps.getPid())+1);
            }else{
                map.put(ps.getPid(),1);
                user.setAcnum(map.size());
                Main.users.updateUserAcnum(username,user.getAcnum());
            }
        }else if(ps.getResult().isAc() && !s.getResult().isAc()){
            if(map.containsKey(ps.getPid()))
            {
                int pid = ps.getPid();
                int number = map.get(pid);
                map.put(ps.getPid(),number-1);
                if(number-1 == 0)
                {
                    map.remove(ps.getPid());
                    user.setAcnum(map.size());
                    Main.users.updateUserAcnum(username,user.getAcnum());
                }
            }
        }

        //此时标记下次要更新用户排名
        //TODO: 更新排名
        TaskUpdateAllUserRank.nextUpdateTime = MyTime.addTimestamp(Tool.now(), MyTime.MINUTE*5);
    }
}
