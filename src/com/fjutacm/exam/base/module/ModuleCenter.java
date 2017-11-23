package com.fjutacm.exam.base.module;

import com.fjutacm.exam.base.module.answermodule.AnswerApi;
import com.fjutacm.exam.base.module.answermodule.AnswerApiLocal;
import com.fjutacm.exam.base.module.exammodule.ExamApi;
import com.fjutacm.exam.base.module.exammodule.ExamApiLocal;
import com.fjutacm.exam.base.module.messagemodule.MessageApi;
import com.fjutacm.exam.base.module.messagemodule.MessageApiLocal;
import com.fjutacm.exam.base.module.problemmodule.ProblemApi;
import com.fjutacm.exam.base.module.problemmodule.ProblemApiLocal;
import com.fjutacm.exam.base.module.usermodule.UserApi;
import com.fjutacm.exam.base.module.usermodule.UserApiLocal;

/**
 * Created by sck on 2017/11/13.
 */
public class ModuleCenter {

    public static AnswerApi answerApi=new AnswerApiLocal();
    public static ExamApi examApi=new ExamApiLocal();
    public static MessageApi messageApi=new MessageApiLocal();
    public static ProblemApi problemApi=new ProblemApiLocal();
    public static UserApi userApi=new UserApiLocal();


}
