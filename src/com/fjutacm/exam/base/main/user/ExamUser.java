package com.fjutacm.exam.base.main.user;

import com.fjutacm.exam.base.main.CacheObj;

import java.util.HashSet;

/**
 * Created by sck on 2017/11/12.
 */
public class ExamUser extends CacheObj {
    private String username;
    private Integer identity;//0为教师
    private HashSet<String> permission;
    private HashSet<Integer> referenceexam;

    public static ExamUser  getExamUserByUsername(String username) {
       return new ExamUser(username,null,null,null);
        /*
        * 从数据库加载用户信息
        * */
    }

    private ExamUser(String username, Integer identity, String permission,String referenceexam) {
        this.username = username;
        this.identity = identity;
        this.permission=new HashSet<String>();
        this.referenceexam=new HashSet<Integer>();
        for (String str: permission.split("|")) {
            this.permission.add(str);
        }
        for (String str: referenceexam.split("|")) {
            this.referenceexam.add(Integer.parseInt(str));
        }
    }

    public String getUsername() {
        return username;
    }

    public int getIdentity() {
        return identity;
    }

    public HashSet<Integer> getReferenceexam() {
        return referenceexam;
    }

    public HashSet<String> getPermission() {
        return permission;
    }

    @Override
    protected void writeWay() {
    }

    @Override
    protected void readWay() {

    }
}
