package com.fjutacm.exam.base.main.problem;

import org.junit.Test;
import util.Init;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * Created by sck on 2017/11/15.
 */
public class ExamBlankFillTest {
    private ExamProblem test;
    static {
        new Init().init();
    }
    @Test
    public void writeInMysql() throws Exception {
        test=new ExamBlankFill("title" , true , new Date(), "creator" , "content" , "answer" );
        System.out.println(test.getProblemid());
        assertNotNull(test.getContent());
    }

}