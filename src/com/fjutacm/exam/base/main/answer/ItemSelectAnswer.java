package com.fjutacm.exam.base.main.answer;

import java.util.Date;

/**
 * Created by sck on 2017/11/13.
 */
public class ItemSelectAnswer extends ExamAnswer {
    public ItemSelectAnswer( Integer problemid, String useranswer, String username, Date submitdate) {
        super(0, problemid, 2, useranswer, username, submitdate);
    }

    @Override
    public boolean judge() {
        return false;
    }
}
