package util;

import entity.OJ.Acdream;
import entity.OJ.BNUOJ.BNUOJ;
import entity.OJ.BZOJ.BZOJ;
import entity.OJ.CF.CF;
import entity.OJ.CF.CFGym;
import entity.OJ.CodeVS.CodeVS;
import entity.OJ.DistributedJudgeSystem.DistributedJudgeSystem;
import entity.OJ.FOJ.FOJ;
import entity.OJ.HDU.HDU;
import entity.OJ.HUST.HUST;
import entity.OJ.JudgeSystem.JudgeSystem;
import entity.OJ.NBUT.NBUT;
import entity.OJ.OTHOJ;
import entity.OJ.PKU.PKU;
import entity.OJ.SPOJ.SPOJ;
import entity.OJ.ZOJ.ZOJ;
import entity.Status;
import util.Vjudge.VJudge;

import java.sql.Timestamp;

/**
 * Created by Administrator on 2015/12/14 0014.
 */
public interface Submitter {
    OTHOJ[] ojs ={
            new HDU(),
            new BNUOJ(),
            new NBUT(),
            new PKU(),
            new HUST(),
            new CF(),
            new CodeVS(),
            new JudgeSystem(),
            new Acdream(),
            new JudgeSystem("judge_system_game"),
            new CFGym(),
            new FOJ(),
            new DistributedJudgeSystem(),
            new BZOJ(),
            new ZOJ(),
            new SPOJ(),
    };
    String oj_keys[]={"hdu","bnuoj","nbut","pku","hust","cf","codevs",
            "judge_system","acdream","judge_system_game","cf_gym","foj",
            "new_judge","bzoj","zoj","spoj"};
    //OJ列表。判题OJ顺序不能改变，否则导致已有题目的OJ不正确
    VJudge m=new VJudge();

    int doSubmit(String user,int pid,int cid,int language,String code,Timestamp submittime);
    void onSubmitDone(Status s);
    int reJudge(int rid);

    /**
     * 批量重判
     * @param pid 题目id
     * @param fromRid 开始rid，限制重判的范围
     * @param status status==1 表示 只重判ac代码，status==2 表示重判除了CE以外的所有代码，status==3表示全部
     * @return
     */
    int reJudge(int pid,int fromRid,int status);
}
