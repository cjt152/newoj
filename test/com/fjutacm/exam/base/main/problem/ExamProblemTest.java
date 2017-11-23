package com.fjutacm.exam.base.main.problem;

import org.junit.Test;
import util.Tool;

import static org.junit.Assert.*;

/**
 * Created by sck on 2017/11/15.
 */
public class ExamProblemTest {
    @Test
    public void getExamProblemByProblemid() throws Exception {
        ExamProblem test=ExamProblem.getExamProblemByProblemid(1,0);
        assertNotNull(test.content);
        Tool.log(test.content);
        test.setContent(test.content+"0");
        test.write();

        test=ExamProblem.getExamProblemByProblemid(1,1);
        assertNotNull(test.content);
        test.setContent(test.content+"1");
        test.write();

        test=ExamProblem.getExamProblemByProblemid(1,2);
        assertNotNull(test.content);
        test.setContent(test.content+"2");
        test.write();
    }

}