package entity.OJ.DistributedJudgeSystem;

import com.fjutacm.judge.service.JudgeInfo;
import com.fjutacm.judge.service.JudgeResultCallback;
import com.fjutacm.judge.service.JudgeService;
import entity.OJ.JudgeSystem.JudgeSystem;
import entity.OJ.OTHOJ;
import entity.RES;
import entity.Status;
import util.HTML.problemHTML;
import util.Main;
import util.Pair;
import util.Tool;
import util.Vjudge.VjSubmitter;

/**
 * Created by QAQ on 2017/10/29.
 */
class CallBack implements JudgeResultCallback {
    //DistributedJudgeSystem judgeSystem;
    CallBack(/*DistributedJudgeSystem judgeSystem*/){
        //this.judgeSystem = judgeSystem;
    }
    @Override
    public void doWork(String s) {
        //judgeSystem.setResult(s);
        Tool.log("结果返回："+s);
    }
}
public class DistributedJudgeSystem extends JudgeSystem {
    private CallBack cb;
    private JudgeService judgeService;

    public DistributedJudgeSystem(){
        cb = new CallBack();
        judgeService = new JudgeService(cb);
        judgeService.start();
        this.name = "分布式评测机";
    }

    @Override
    public String getTitle(String pid) {
        return "JudgeSystem不能获取题目";
    }

    @Override
    public String submit(VjSubmitter s) {
        Status status = Main.status.getStatu(s.getSubmitInfo().rid);
        Pair<Integer,Integer> limit=Main.problems.getProblemLimit(Integer.parseInt(s.getSubmitInfo().pid));
        JudgeInfo info = new JudgeInfo(
                Integer.parseInt(s.info.pid),
                s.info.rid+"",
                s.info.code,
                s.info.language,
                s.info.rejduge,
                status.getUser(),
                limit.getKey(),
                limit.getValue()
        );
        judgeService.judgeCode(info);
        s.rid = Math.random()+"";
        return "success";
    }

    @Override
    public RES getResultReturn(VjSubmitter s){
        return null;
    }

}
