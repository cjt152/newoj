package com.fjutacm.exam.base.main.problem;

import com.fjutacm.exam.base.main.CacheObj;

import java.util.Date;

/**
 * Created by sck on 2017/11/13.
 */
public abstract class ExamProblem extends CacheObj {

    protected Integer problemid;
    private Integer type;
    protected String title;
    protected Boolean ispublic;
    protected Date creationdate;
    protected String creator;
    protected String content;
    protected String info;

    public static ExamProblem getExamProblemByProblemid(Integer problemid,Integer type){
        ExamProblem problem=null;
        if(type==0){
            problem=new ExamBlankFill(problemid);
            problem.readWay();
        }
        else if(type==1){
            problem=new ExamCodeDesign(problemid);
            problem.readWay();
        }
        else if(type==2){
            problem=new ExamItemSelect(problemid);
            problem.readWay();
        }
        return problem;
    }

    protected ExamProblem(Integer problemid,Integer type, String title, Boolean ispublic, Date creationdate, String creator, String content) {
        this.problemid = problemid;
        this.type=type;
        this.title = title;
        this.ispublic = ispublic;
        this.creationdate = creationdate;
        this.creator = creator;
        this.content = content;
        this.info=null;
    }

    public Integer getProblemid() {
        return problemid;
    }

    public String getTitle() {
        return title;
    }

    public Boolean getIspublic() {
        return ispublic;
    }

    public Date getCreationdate() {
        return creationdate;
    }

    public String getCreator() {
        return creator;
    }

    public String getContent() {
        return content;
    }

    public void setProblemid(Integer problemid) {
        this.isupate=true;
        this.problemid = problemid;
    }

    public void setTitle(String title) {
        this.isupate=true;
        this.title = title;
    }

    public void setIspublic(Boolean ispublic) {
        this.isupate=true;
        this.ispublic = ispublic;
    }

    public void setContent(String content) {
        this.isupate=true;
        this.content = content;
    }

    public void setInfo(String info) {
        this.isupate=true;
        this.info = info;
    }
}
