package com.fjutacm.exam.base.main.problem;

import com.fjutacm.common.sql.SQL;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * Created by sck on 2017/11/13.
 */
public class ExamCodeDesign extends ExamProblem {

    private String sourcecode;
    private String judgedata;

    protected ExamCodeDesign(Integer problemid){
        super(problemid, 1,null, null, null, null, null);
    }

    public ExamCodeDesign(String title, Boolean ispublic, Date creationdate, String creator, String content, String sourcecode, String judgedata) {
        super(0, 1, title, ispublic, creationdate, creator, content);
        this.sourcecode = sourcecode;
        this.judgedata = judgedata;
        writeWay();
    }

    @Override
    protected void writeWay() {
        if(this.problemid==0){
            this.problemid=new SQL("insert into t_exam_codedesign values(?,?,?,?,?,?,?,?,?)",
                    this.title,
                    this.ispublic,
                    this.creationdate,
                    this.creator,
                    this.content,
                    this.sourcecode,
                    this.judgedata,
                    this.info,
                    this.problemid).insertGetLastInsertId();
        }
        else{
            new SQL("delete from t_exam_codedesign where problemid=?",this.problemid).update();
            new SQL("insert into t_exam_codedesign values(?,?,?,?,?,?,?,?,?)",
                    this.title,
                    this.ispublic,
                    this.creationdate,
                    this.creator,
                    this.content,
                    this.sourcecode,
                    this.judgedata,
                    this.info,
                    this.problemid).update();
        }
    }

    @Override
    protected void readWay() {
        SQL sql=new SQL("select * from t_exam_itemselect where problemid=?",this.problemid);
        ResultSet rs=sql.query();
        try {
            if(rs.next()){
                this.title=rs.getString(1);
                this.ispublic=rs.getBoolean(2);
                this.creationdate=rs.getDate(3);
                this.creator=rs.getString(4);
                this.content=rs.getString(5);
                this.sourcecode=rs.getString(6);
                this.judgedata=rs.getString(7);
                this.info=rs.getString(8);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            sql.close();
        }
    }

    public String getSourcecode() {
        return sourcecode;
    }

    public String getJudgedata() {
        return judgedata;
    }

    public void setSourcecode(String sourcecode) {
        this.sourcecode = sourcecode;
    }

    public void setJudgedata(String judgedata) {
        this.judgedata = judgedata;
    }
}
