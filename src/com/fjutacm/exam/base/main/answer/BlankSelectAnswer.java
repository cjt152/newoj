package com.fjutacm.exam.base.main.answer;

import java.util.Date;

/**
 * Created by sck on 2017/11/13.
 */
public class BlankSelectAnswer extends ExamAnswer {

    public BlankSelectAnswer(Integer problemid, String useranswer, String username, Date submitdate) {
        super(0, problemid, 0, useranswer, username, submitdate);
    }

    @Override
    public boolean judge() {
        return false;
    }
}
