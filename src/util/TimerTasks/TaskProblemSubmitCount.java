package util.TimerTasks;

import com.fjutacm.common.sql.SQL;
import util.Tool;

import java.util.Timer;

/**
 * Created by QAQ on 2016/8/26.
 */
public class TaskProblemSubmitCount extends MyTimer {

    @Override
    public void run() {
        Tool.log("TaskProblemSubmitCount run");
        new SQL("REPLACE INTO t_usersolve SELECT * FROM usersolve_view").update(true);

        new SQL("UPDATE problem SET " +
                "totalSubmit=(SELECT COUNT(*) FROM statu WHERE statu.pid=problem.pid)," +
                "totalSubmitUser=(SELECT COUNT(*) FROM t_usersolve WHERE t_usersolve.pid=problem.pid)," +
                "totalAc=(SELECT COUNT(*) FROM statu WHERE statu.pid=problem.pid AND statu.result=1)," +
                "totalAcUser=(SELECT COUNT(*) FROM t_usersolve WHERE t_usersolve.pid=problem.pid AND t_usersolve.status=1)").update();
    }

    @Override
    public void getTimer() throws Exception {
        setEveryDay(4,10,0);
        new Timer().scheduleAtFixedRate(this, date, period);
    }
}
