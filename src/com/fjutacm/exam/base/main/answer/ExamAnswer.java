package com.fjutacm.exam.base.main.answer;

import com.fjutacm.common.sql.SQL;
import com.fjutacm.exam.base.main.CacheObj;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * Created by sck on 2017/11/13.
 */
public abstract class ExamAnswer extends CacheObj{

    protected Integer answerid;
    protected Integer problemid;
    protected Integer problemtype;
    protected String judgeinfo;
    protected String useranswer;
    protected String username;
    protected Date submitdate;

    public abstract boolean judge();

    protected ExamAnswer(Integer answerid, Integer problemid, Integer problemtype, String useranswer, String username, Date submitdate) {
        this.answerid = answerid;
        this.problemid = problemid;
        this.problemtype = problemtype;
        this.judgeinfo = null;
        this.useranswer = useranswer;
        this.username = username;
        this.submitdate = submitdate;
    }

    @Override
    protected void writeWay() {
        if(this.answerid==0){
            this.problemid=new SQL("insert into t_exam_answer values(?,?,?,?,?,?,?)",
                    this.problemid,
                    this.problemtype,
                    this.judgeinfo,
                    this.useranswer,
                    this.username,
                    this.submitdate,
                    this.answerid).insertGetLastInsertId();
        }
        else{
            new SQL("delete from t_exam_answer where answerid=?",this.answerid).update();
            new SQL("insert into t_exam_answer values(?,?,?,?,?,?,?)",
                    this.problemid,
                    this.problemtype,
                    this.judgeinfo,
                    this.useranswer,
                    this.username,
                    this.submitdate,
                    this.answerid).update();
        }
    }

    @Override
    protected void readWay() {
        SQL sql=new SQL("select * from t_exam_itemselect where answerid=?",this.answerid);
        ResultSet rs=sql.query();
        try {
            if(rs.next()){
                this.problemid=rs.getInt(1);
                this.problemtype=rs.getInt(2);
                this.judgeinfo=rs.getString(3);
                this.useranswer=rs.getString(4);
                this.username=rs.getString(5);
                this.submitdate=rs.getDate(6);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            sql.close();
        }
    }
}
