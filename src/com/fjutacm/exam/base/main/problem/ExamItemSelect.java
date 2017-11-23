package com.fjutacm.exam.base.main.problem;


import com.fjutacm.common.sql.SQL;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * Created by sck on 2017/11/13.
 */
public class ExamItemSelect extends ExamProblem {
    private String selectitems;
    private String answer;

    private final  static String TABLE ="t_exam_itemselect";
    protected ExamItemSelect(Integer problemid){
        super(problemid, 2,null, null, null, null, null);
    }

    public ExamItemSelect(String title, Boolean ispublic, Date creationdate, String creator, String content, String selectitems, String answer) {
        super(0,2, title, ispublic, creationdate, creator, content);
        this.selectitems = selectitems;
        this.answer = answer;
        writeWay();
    }

    @Override
    protected void writeWay() {
        if(this.problemid==0){
            this.problemid=new SQL("insert into t_exam_itemselect values(?,?,?,?,?,?,?,?,?)",
                    this.title,
                    this.ispublic,
                    this.creationdate,
                    this.creator,
                    this.content,
                    this.selectitems,
                    this.answer,
                    this.info,
                    this.problemid).insertGetLastInsertId();
        }
        else{
            new SQL("delete from t_exam_itemselect where problemid=?",this.problemid).update();
            new SQL("insert into t_exam_itemselect values(?,?,?,?,?,?,?,?,?)",
                    this.title,
                    this.ispublic,
                    this.creationdate,
                    this.creator,
                    this.content,
                    this.selectitems,
                    this.answer,
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
                this.selectitems=rs.getString(6);
                this.answer=rs.getString(7);
                this.info=rs.getString(8);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            sql.close();
        }
    }

    public String getSelectitems() {
        return selectitems;
    }

    public String getAnswer() {
        return answer;
    }

    public void setSelectitems(String selectitems) {
        this.isupate=true;
        this.selectitems = selectitems;
    }

    public void setAnswer(String answer) {
        this.isupate=true;
        this.answer = answer;
    }
}
