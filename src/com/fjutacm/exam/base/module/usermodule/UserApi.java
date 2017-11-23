package com.fjutacm.exam.base.module.usermodule;

/**
 * Created by sck on 2017/11/13.
 */
public interface UserApi {
     boolean isTeacher(String name);
     Integer[] listAllReferenceExam(String username);
     boolean isReferenceExam(String username, Integer examid);
     String[] listAllPermission(String username);
     boolean havePermission(String username, String permission);
}
