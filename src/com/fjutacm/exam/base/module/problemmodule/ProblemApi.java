package com.fjutacm.exam.base.module.problemmodule;

/**
 * Created by sck on 2017/11/13.
 */
public interface ProblemApi {
    String getItemSelectAnswerById(Integer promblemid,Integer type);
    String getBlankFillAnswerById(Integer promblemid,Integer type);
}
