package com.fjutacm.exam.base.module.problemmodule;

import com.fjutacm.exam.base.main.problem.ExamBlankFill;
import com.fjutacm.exam.base.main.problem.ExamItemSelect;
import com.fjutacm.exam.base.main.problem.ExamProblem;

/**
 * Created by sck on 2017/11/13.
 */
public class ProblemApiLocal implements ProblemApi {

    private ExamProblem getExamProblemByProblemid(Integer promblemid,Integer type){
        return ExamProblem.getExamProblemByProblemid(promblemid,type);
    }

    @Override
    public String getItemSelectAnswerById(Integer promblemid,Integer type) {
        return ((ExamItemSelect)getExamProblemByProblemid(promblemid,type)).getAnswer();
    }

    @Override
    public String getBlankFillAnswerById(Integer promblemid,Integer type) {
        return ((ExamBlankFill)getExamProblemByProblemid(promblemid,type)).getAnswer();
    }
}
