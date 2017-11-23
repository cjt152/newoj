package util.TimerTasks;

import action.addcontest;
import entity.Contest;
import entity.Enmu.AcbOrderType;
import entity.Result;
import entity.Status;
import servise.ContestMain;
import servise.GvMain;
import servise.MessageMain;
import util.Main;
import com.fjutacm.common.sql.SQL;

import java.util.*;

/**
 * 每日一题改成每日补一题
 * 从题库随机抽取一题
 * Created by QAQ on 2016/9/22.
 */
public class OneProblemEveryDay extends MyTimer{
    private static final Set<String> awardsUser = Collections.synchronizedSet(new HashSet<String>());
    @Override
    public void run() {
        Calendar ca = Calendar.getInstance();
        String contestName = "【每日一题】"+ca.get(Calendar.YEAR)+"年"+(ca.get(Calendar.MONTH)+1)+"月";
        int cid = GvMain.getOneProblemEveryDayCid();
        String gvName = GvMain.getOneProblemEveryDayName();
        if(cid == -1 || !contestName.equals(gvName)){
            cid = addContest(contestName);
        }
        if(cid != -1){
            int pid = Main.problems.getRandomPid();
            Contest c = ContestMain.getContest(cid);
            String pidstring = c.getProblems();
            if(pidstring.length() == 0){
                pidstring += pid;
            }else{
                pidstring += ","+pid;
            }
            ContestMain.ResetContestProblmes(cid,pidstring);
            GvMain.setOneProblemEveryDayPid(pid);
            awardsUser.clear();
        }
    }

    private int addContest(String name){
        Calendar time = Calendar.getInstance();
        //每个月的一号创建一场比赛
        addcontest contest = new addcontest();
        contest.setName(name);

        time.set(Calendar.DAY_OF_MONTH,1);
        String month = (time.get(Calendar.MONTH) + 1) +"";
        if(month.length()==1) month = "0"+month;
        contest.setInfo("每日补一题！在当日内通过题目，可获得100ACB奖励噢！");
        contest.setBegintime_d(time.get(Calendar.YEAR)+"-"+month+"-01");
        contest.setBegintime_m("0");
        contest.setBegintime_s("0");

        time.add(Calendar.MONTH,1);
        month = (time.get(Calendar.MONTH) + 1) +"";
        if(month.length()==1) month = "0"+month;
        contest.setEndtime_d(time.get(Calendar.YEAR)+"-"+month+"-01");
        contest.setEndtime_m("0");
        contest.setEndtime_s("0");

        contest.setType(0);
        contest.setKind(0);
        contest.setProblems("");
        contest.setProblemCanPutTag("true");
        contest.setStatusReadOut("true");
        contest.setRank(2);
        contest.setTraining_m1_s("10");//金奖个数
        contest.setTraining_m2_s("30");//银奖个数
        contest.setTraining_m3_s("60");//铜奖个数
        contest.setTraining_m1_t("1");
        contest.setTraining_m2_t("1");
        contest.setTraining_m3_t("1");
        int cid = ContestMain.addContestReturnResult(contest);
        //////-------------//////
        GvMain.setOneProblemEveryDayCid(cid);
        GvMain.setOneProblemEveryDayName(name);
        return cid;
    }
    public static void onUserAcProblem(Status ps,Status s){
        if(awardsUser.contains(s.getUser())){
            return ;
        }
        int pid = GvMain.getOneProblemEveryDayPid();
        if(s.getPid() != pid) return ;
        if(ps.getResult()!= Result.AC && s.getResult() == Result.AC) {
            //之前没有AC过
            int acNum = new SQL("SELECT COUNT(*) FROM statu WHERE pid=? AND ruser=? AND result=? AND id<?",pid,s.getUser(),Result.AC.getValue(),s.getRid()).queryNum();
            if(acNum == 0){
                //addacb
                awardsUser.add(s.getUser());
                Main.users.addACB(s.getUser(),100, AcbOrderType.ONE_PROBLEM_EVERYDAY,"pid:"+pid);
                MessageMain.addMessageOneProblemEveryDay(s.getUser(),pid,100);
            }
        }
    }
    @Override
    public void getTimer() throws Exception {
        setEveryDay(4,0,0);
        new Timer().scheduleAtFixedRate(this, date, period);
    }
}
