package com.fjutacm.exam.base.module.usermodule;

import com.fjutacm.exam.base.main.user.ExamUser;


/**
 * Created by sck on 2017/11/13.
 */
public class UserApiLocal implements UserApi {

    private static ExamUser getExamUserByUserName(String username){
        return ExamUser.getExamUserByUsername(username);
    }

    @Override
    public boolean isTeacher(String username){
        return getExamUserByUserName(username).getIdentity()==0;
    }

    @Override
    public Integer[] listAllReferenceExam(String username){
        return (Integer[])getExamUserByUserName(username).getReferenceexam().toArray();
    }

    @Override
    public boolean isReferenceExam(String username, Integer examid) {
        return getExamUserByUserName(username).getReferenceexam().contains(examid);
    }

    @Override
    public String[] listAllPermission(String username) {
        return (String[])getExamUserByUserName(username).getPermission().toArray();
    }

    @Override
    public boolean havePermission(String username, String permission) {
        return getExamUserByUserName(username).getPermission().contains(permission);
    }
}