package com.fjutacm.exam.base.main.problem;

import com.fjutacm.common.sql.SQL;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * Created by sck on 2017/11/13.
 */
public class ExamBlankFill extends ExamProblem {

    private String answer;

    protected ExamBlankFill(Integer problemid){
        super(problemid, 0,null, null, null, null, null);
    }

    public ExamBlankFill(String title, Boolean ispublic, Date creationdate, String creator, String content, String answer) {
        super(0,0, title, ispublic, creationdate, creator, content);
        this.answer = answer;
        writeWay();
    }

    @Override
    protected void writeWay() {
        if(this.problemid==0){
            this.problemid=new SQL("insert into t_exam_blankfill values(?,?,?,?,?,?,?,?)",
                    this.title,
                    this.ispublic,
                    this.creationdate,
                    this.creator,
                    this.content,
                    this.answer,
                    this.info,
                    this.problemid).insertGetLastInsertId();
        }
        else{
            new SQL("delete from t_exam_blankfill where problemid=?",this.problemid).update();
            new SQL("insert into t_exam_blankfill values(?,?,?,?,?,?,?,?)",
                    this.title,
                    this.ispublic,
                    this.creationdate,
                    this.creator,
                    this.content,
                    this.answer,
                    this.info,
                    this.problemid).update();
        }
    }

    @Override
    protected void readWay() {
        SQL sql=new SQL("select * from t_exam_blankfill where problemid=?",this.problemid);
        ResultSet rs=sql.query();
        try {
            if(rs.next()){
                this.title=rs.getString(1);
                this.ispublic=rs.getBoolean(2);
                this.creationdate=rs.getDate(3);
                this.creator=rs.getString(4);
                this.content=rs.getString(5);
                this.answer=rs.getString(6);
                this.info=rs.getString(7);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            sql.close();
        }
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.isupate=true;
        this.answer = answer;
    }
}
