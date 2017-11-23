package com.fjutacm.exam.base.module.usermodule;

/**
 * Created by sck on 2017/11/13.
 */
public class UserApiNet implements UserApi {
    @Override
    public boolean isTeacher(String name) {
        return false;
    }

    @Override
    public Integer[] listAllReferenceExam(String username) {
        return new Integer[0];
    }

    @Override
    public boolean isReferenceExam(String username, Integer examid) {
        return false;
    }

    @Override
    public String[] listAllPermission(String username) {
        return new String[0];
    }

    @Override
    public boolean havePermission(String username, String permission) {
        return false;
    }
}
