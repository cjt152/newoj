package com.fjutacm.exam.base.main.problem;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * Created by sck on 2017/11/16.
 */
public class ExamCodeDesignTest {
    private ExamProblem test;
    @Test
    public void writeWay() throws Exception {
        test=new ExamCodeDesign("title" , true , new Date(), "creator" , "content" , "sourcecode" , "judgedata" );
        System.out.println(test.getProblemid());
        assertNotNull(test.getContent());
    }

}