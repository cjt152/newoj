package entity.rank;

import entity.rank.RankICPC.RankICPC;
import entity.rank.RankNOIP.RankNOIP;
import entity.rank.RankShortCode.RankShortCode;
import entity.Contest;
import entity.rank.RankTraining.RankTraining;
import entity.Status;
import com.fjutacm.common.sql.SQL;
import action.addcontest;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Syiml on 2015/6/23 0023.
 */
public class RankSQL {
    public static RankICPC ICPCRank(int cid,RankICPC r){
        SQL sql=new SQL("SELECT cid,penalty,mtype_1,m1,mtype_2,m2,mtype_3,m3 FROM t_rank_icpc WHERE cid=?",cid);
        try {
            ResultSet rs=sql.query();
            if(rs.next()){
                r.setPenalty(rs.getInt(2));
                r.setType_1(rs.getInt(3));
                r.setM1(rs.getInt(4));
                r.setType_2(rs.getInt(5));
                r.setM2(rs.getInt(6));
                r.setType_3(rs.getInt(7));
                r.setM3(rs.getInt(8));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            sql.close();
        }
        return r;
    }
    public static RankShortCode RankShortCode(int cid,RankShortCode rank){
        SQL sql=new SQL("SELECT * FROM t_rank_shortcode WHERE cid=?", cid);
        ResultSet rs=sql.query();
        try {
            if(rs.next()){
                rank.type_1=rs.getInt("mtype_1");
                rank.type_2=rs.getInt("mtype_2");
                rank.type_3=rs.getInt("mtype_3");
                rank.m1=rs.getInt("m1");
                rank.m2=rs.getInt("m2");
                rank.m3=rs.getInt("m3");
                rank.chengfa=rs.getInt("chengfa");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            sql.close();
        }
        return rank;
    }
    public static RankTraining RankTraining(int cid,RankTraining rank){
        SQL sql=new SQL("SELECT * FROM t_rank_training WHERE cid=?", cid);
        ResultSet rs=sql.query();
        try {
            if(rs.next()){
                rank.type_1=rs.getInt("mtype_1");
                rank.type_2=rs.getInt("mtype_2");
                rank.type_3=rs.getInt("mtype_3");
                rank.m1=rs.getInt("m1");
                rank.m2=rs.getInt("m2");
                rank.m3=rs.getInt("m3");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            sql.close();
        }
        return rank;
    }
    public static RankNOIP RankNOIP(int cid,RankNOIP rank) {
        rank.type_1=0;
        rank.type_2=0;
        rank.type_3=0;
        rank.m1=1;
        rank.m2=3;
        rank.m3=6;
        return rank;
    }
    public static Rank getRank(Contest c,ResultSet rs){
        //rs: id,ruser,pid,cid,lang,submittime,result,timeused,memoryUsed,code,codelen
        Rank rank;
        switch(c.getRankType()){
            case 0:
                rank=ICPCRank(c.getCid(), new RankICPC(c));
                break;
            case 1:
                rank=RankShortCode(c.getCid(),new RankShortCode(c));
                break;
            case 2:
                rank=RankTraining(c.getCid(),new RankTraining(c));
                break;
            case 3:
                rank=RankNOIP(c.getCid(),new RankNOIP(c));
                break;
            default:
                rank=new RankICPC(c);
        }
        try {
            while(rs.next()){
                //status.add(rs.getInt(1));
                Status s=new Status(rs,9);
    //                status.add(s.getRid());
                rank._add(s,c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("没有result");
        }
        return rank;
    }
    public static String addIcpcRank(int cid, addcontest a){
        try {
            new SQL("INSERT INTO t_rank_icpc values(?,?,?,?,?,?,?,?)"
                ,cid
                ,Integer.parseInt(a.getIcpc_penalty())
                ,Integer.parseInt(a.getIcpc_m1_s())
                ,Integer.parseInt(a.getIcpc_m1_t())
                ,Integer.parseInt(a.getIcpc_m2_s())
                ,Integer.parseInt(a.getIcpc_m2_t())
                ,Integer.parseInt(a.getIcpc_m3_s())
                ,Integer.parseInt(a.getIcpc_m3_t())).update();
            return "success";
        }catch (NumberFormatException e){
            e.printStackTrace();
            return "error";
        }
    }
    public static String addShortCodeRank(int cid, addcontest a){
        try {
            new SQL("INSERT INTO t_rank_shortcode values(?,?,?,?,?,?,?,?)"
                    ,cid
                    ,Integer.parseInt(a.getIcpc_m1_s())
                    ,Integer.parseInt(a.getIcpc_m1_t())
                    ,Integer.parseInt(a.getIcpc_m2_s())
                    ,Integer.parseInt(a.getIcpc_m2_t())
                    ,Integer.parseInt(a.getIcpc_m3_s())
                    ,Integer.parseInt(a.getIcpc_m3_t())
                    ,Integer.parseInt(a.getShortcode_chengfa())).update();
            return "success";
        }catch (NumberFormatException e){
            e.printStackTrace();
            return "error";
        }
    }
    public static String addTrainingRank(int cid,addcontest a){
        try {
            new SQL("INSERT INTO t_rank_training values(?,?,?,?,?,?,?)"
                    ,cid
                    ,Integer.parseInt(a.getTraining_m1_s())
                    ,Integer.parseInt(a.getTraining_m1_t())
                    ,Integer.parseInt(a.getTraining_m2_s())
                    ,Integer.parseInt(a.getTraining_m2_t())
                    ,Integer.parseInt(a.getTraining_m3_s())
                    ,Integer.parseInt(a.getTraining_m3_t())).update();
            return "success";
        }catch (NumberFormatException e){
            e.printStackTrace();
            return "error";
        }
    }
    public static String addRank(int cid, addcontest a){
        if(a.getRank()==0){
            return addIcpcRank(cid,a);
        }else if(a.getRank()==1){
            return addShortCodeRank(cid,a);
        }else if(a.getRank()==2){
            return addTrainingRank(cid,a);
        }else if(a.getRank()==3){
            return "success";
        }
        return "error";
    }
    public static String editRank(int cid, addcontest a){
        String s="t_rank_icpc";
        if(a.getRank()==0){
            s="t_rank_icpc";
        }else if(a.getRank()==1){
            s="t_rank_shortcode";
        }else if(a.getRank()==2){
            s="t_rank_training";
        }
        new SQL("delete from "+s+" where cid=?",cid).update();
        return addRank(cid,a);
    }
}
