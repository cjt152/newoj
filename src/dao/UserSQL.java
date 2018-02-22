package dao;

import action.TeamAward;
import entity.*;
import entity.Enmu.AcbOrderType;
import entity.Title.TitleSet;
import util.Event.EventMain;
import util.Event.Events.EventAcbChg;
import util.Event.Events.EventVerify;
import util.Main;
import servise.MessageMain;
import util.HTML.HTML;
import util.MyTime;
import util.SQL.SQL;
import util.Tool;

import java.sql.*;
import java.sql.Date;
import java.util.*;

/**
 * Created by Administrator on 2015/6/3.
 */
public class UserSQL extends BaseCacheLRU<String,User> {
    /*
    * users(username,password,nick,gender,Email,motto,registertime,type,solved,submissions,Mark)
    * permission(id,name)
    * userper(username,perid)
    * */
    public UserSQL(){
        super(500);
    }

    public static int getUsersNum(int cid){
        return new SQL("select count(*) from v_contestuser where cid=?",cid)
                .queryNum();
    }

    public static int getUsersNum(int cid,int st){
        return new SQL("select count(*) from contestuser where cid=? and statu=?",cid,st).queryNum();
    }

    public int getUsersNum(String search){
        if(search==null||search.equals("")){
            return new SQL("select count(*) from users").queryNum();
        }else {
            return new SQL("select count(*) from users where (username like ? or nick like ?)", "%" + search + "%", "%" + search + "%").queryNum();
        }
    }

    public int register(User u){
        SQL sql1=new SQL("select * from users where username=?",u.getUsername());
        try {
            ResultSet s=sql1.query();
            s.next();
            if(s.isLast()) return -1;//已存在
        }catch (SQLException e){
            return 0;
        }finally {
            sql1.close();
        }
        new SQL("insert into users values(?,md5(?),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
                , u.getUsername()
                , u.getPassword()
                , HTML.HTMLtoString(u.getNick())
                , u.getGender()
                , HTML.HTMLtoString(u.getSchool())
                , HTML.HTMLtoString(u.getEmail())
                , HTML.HTMLtoString(u.getMotto())
                , u.getRegistertime()
                , u.getType()
                , HTML.HTMLtoString(u.getMark())
                , -100000
                , 0,0,"","","","","","",0,0,0,1000000,null).update();
        new SQL("UPDATE users SET rank=(select rank+1 FROM v_user WHERE username=?) WHERE username=?",u.getUsername(),u.getUsername()).update();
        MessageMain.addMessageWelcome(u);
        return 1;
    }

    public void updateAllUserRank(){
        new SQL("UPDATE users SET rank=(select rank+1 FROM v_user WHERE v_user.username=users.username)").update();
    }
//    public int getRank(String user){
//        SQL sql=new SQL("select rank+1 from v_user where username=?", user);
//        ResultSet rs=sql.query();
//        try {
//            rs.next();
//            return rs.getInt(1);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }finally {
//            sql.close();
//        }
//        return -1;
//    }

    public Permission getPermission(String username){
        if(username==null) return null;
        SQL sql=new SQL("select perid from userper where username=?",username);
        try {
            ResultSet rs=sql.query();
            return new Permission(rs);
        }finally {
            sql.close();
        }
    }
    public TitleSet getTitle(String username){
        if(username == null) return null;
        TitleSet ts = new SQL("SELECT id,jd,endtime FROM t_title WHERE username=?",username).queryBean(TitleSet.class);
        if(ts == null){
            ts = new TitleSet();
        }
        SQL sql = new SQL("SELECT * FROM t_title_config where username=?",username);
        ResultSet rs = sql.query();
        try {
            if(rs.next()){
                ts.isShow = rs.getBoolean("isShow");
                ts.setOrder(rs.getString("config"));
                ts.n = rs.getInt("n");
                ts.adj = rs.getInt("adj");
            }
        } catch (SQLException e) {
        } finally {
            sql.close();
        }
        ts.setOrder(ts.getOrder());
        return ts;
    }
    public void addTitle(String username,int id,int jd,Timestamp endTime){
        new SQL("REPLACE INTO t_title VALUES(?,?,?,?)",username,id,jd,endTime).update();
    }
    public void setTitleConfig(String username,boolean isShow,String config,int adj,int n)
    {
        new SQL("REPLACE INTO t_title_config VALUES(?,?,?,?,?)",username,isShow,config,adj,n).update();
    }

    public List<List<String>> getPermissionTable(){
        //user,per,admin
        SQL sql=new SQL("SELECT username,perid FROM userper");
        ResultSet rs=sql.query();
        List<List<String>> table=new ArrayList<List<String>>();
        try {
            while(rs.next()){
                int perId = rs.getInt("perid");
                List<String> row=new ArrayList<String>();
                row.add(rs.getString("username"));
                row.add(PermissionType.getPerByCode(perId).getName());
                row.add(HTML.a("delper.action?user="+rs.getString("username")+"&perid="+perId,"删除"));
                table.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            sql.close();
        }
        return table;
    }

    public void addPer(String user,int per){
        new SQL("INSERT INTO userper values(?,?)",user,per).update();
        remove_catch(user);
    }

    public void delPer(String user,int per){
        new SQL("delete from userper where username=? and perid=?",user,per).update();
        remove_catch(user);
    }

    public User getUser(String username){return getBeanByKey(username);}

    public List<User> getUsers(int from,int num,String search,String order,boolean desc){
        if(order==null||order.equals("")){
            order="rank";
        }
        if(search==null||search.equals("")){
            return new SQL("select *  from users  ORDER BY "+order+(desc?" desc ":" ")+" LIMIT ?,?",from,num).queryBeanList(User.class);
        }else{
            return new SQL("select * from users where  (username like ? or nick like ?)  " +
                    " ORDER BY "+order+(desc?" desc ":" ")+
                    " LIMIT ?,?","%"+search+"%","%"+search+"%",from,num).queryBeanList(User.class);
        }
    }

    public List<User> getUsersInTeam(int from,int num,String search,String order,boolean desc,int Status){
        if(order==null||order.equals("")){
            order="rank";
        }
        if(search==null||search.equals("")){
            return new SQL("select *  from users where inTeamStatus=?  ORDER BY "+order+(desc?" desc ":" ")+" LIMIT ?,?",Status,from,num).queryBeanList(User.class);
        }else{
            return new SQL("select * from users where  (username like ? or nick like ?) and inTeamStatus=?" +
                    " ORDER BY "+order+(desc?" desc ":" ")+
                    " LIMIT ?,?","%"+search+"%","%"+search+"%",Status,from,num).queryBeanList(User.class);
        }
    }

    public int getUsersNumInTeam(String search,int status){
        if(search==null||search.equals("")){
            return new SQL("select count(*) from users where inTeamStatus=?",status).queryNum();
        }else {
            return new SQL("select count(*) from users where (username like ? or nick like ?) and inTeamStatus=?", "%" + search + "%", "%" + search + "%",status).queryNum();
        }
    }
    public List<User> getUserByStatus(int Status){
          return new SQL("select *  from users where inTeamStatus=?",Status).queryBeanList(User.class);
    }
    public List<User> getRichTop10(){
        return new SQL("select * from users order by acb desc,rating desc " +
                "LIMIT 0,10").queryBeanList(User.class);
    }

    public List<User> getAcnumTop10(){
        return new SQL("select * from users order by acnum desc,rating desc LIMIT 0,10").queryBeanList(User.class);
    }

    public List<List<String>> getUsers(int cid,int from,int num,String search,boolean is3){
        List<List<String>> list=new ArrayList<List<String>>();
        SQL sql;
        if(search == null || search.equals("")) {
            sql = new SQL("select users.username,users.name,users.gender,users.school,users.faculty," +
                    "users.major,users.cla,users.no,users.nick,users.rating,users.ratingnum," +
                    "contestuser.statu,contestuser.time,contestuser.info " +
                    "from users left join contestuser on(users.username=contestuser.username) " +
                    "where cid=? ORDER BY time desc " +
                    "LIMIT ?,?", cid, from, num);
        }else {
            sql = new SQL("select users.username,users.name,users.gender,users.school,users.faculty," +
                    "users.major,users.cla,users.no,users.nick,users.rating,users.ratingnum," +
                    "contestuser.statu,contestuser.time,contestuser.info " +
                    "from users left join contestuser on(users.username=contestuser.username) " +
                    "where cid=? and (users.username like ? or users.nick like ?) ORDER BY time desc " +
                    "LIMIT ?,?", cid, "%" + search + "%", "%" + search + "%", from, num);
        }
        try {
            ResultSet rs=sql.query();
            while(rs.next()){
                List<String> s=new ArrayList<String>();
                String nick=rs.getString("nick");
                if(nick==null){
                    s.add(rs.getString("username"));
                    if(is3){
                        s.add("");
                        s.add("");
                        s.add("");
                        s.add("");
                        s.add("");
                        s.add("");
                        s.add("");
                    }
                    s.add(HTML.textb("未注册","red"));
                    s.add("");
                }else{
                    //HTML.a("UserInfo.jsp?user=" + rs.getString("username"), rs.getString("username"));
                    s.add(rs.getString("username"));
                    if(is3){
                        s.add(rs.getString("name"));
                        s.add(rs.getInt("gender")+"");
                        s.add(rs.getString("school"));
                        s.add(rs.getString("faculty"));
                        s.add(rs.getString("major"));
                        s.add(rs.getString("cla"));
                        s.add(rs.getString("no"));
                    }
                    s.add(Main.users.getUser(rs.getString("username")).getTitleAndNick());
                    s.add(User.ratingToHTML(User.getShowRating(rs.getInt("ratingnum"), rs.getInt("rating"))));
                }
                if(rs.getInt("statu")==3){
                    s.add(
                            HTML.a("javascript:retry_show('" + rs.getString("username") +
                                    "','" +
                                    rs.getString("info") +
                                    "','" +
                                    cid +
                                    "'," +
                                    (Main.loginUser()!=null && Main.loginUser().getUsername().equals(rs.getString("username"))) +
                                    ")"
                                    , RegisterUser.statuToHTML(rs.getInt("statu"))));
                }
                else s.add(RegisterUser.statuToHTML(rs.getInt("statu")));
                Timestamp t=rs.getTimestamp("time");
                if(t==null){
                    s.add("");
                }else{
                    s.add(t.toString().substring(0, 19));
                }
                s.add(rs.getString("info"));
                list.add(s);
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            return list;
        }finally {
            sql.close();
        }
    }

    public List<User> getRegisterUsers(int cid){
        return new SQL("SELECT * FROM users WHERE username in (SELECT username FROM contestuser WHERE cid = ?)",cid).queryBeanList(User.class);
        /*SQL sql=new SQL("select * from v_contestuser where cid=?",cid);
        List<User> list=new ArrayList<User>();
        ResultSet rs=sql.query();
        try {
            while(rs.next()){
                User u=new User();
                u.setNick(rs.getString("nick"));
                u.setUsername(rs.getString("username"));
                u.setName(rs.getString("name"));
                u.setGender(rs.getInt("gender"));
                u.setSchool(rs.getString("school"));
                u.setFaculty(rs.getString("faculty"));
                u.setMajor(rs.getString("major"));
                u.setCla(rs.getString("cla"));
                u.setNo(rs.getString("no"));
                list.add(u);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            sql.close();
        }
        return list;*/
    }

    public String login(String user,String pass){
        SQL sql=new SQL("select username,password from users where username=? and password=md5(?)", user, pass);
        SQL sql2=new SQL("select * from users where username=?", user);
        try {
            ResultSet r=sql.query();
            if(r.next()){
                return "LoginSuccess";
            }else{
                ResultSet rs=sql2.query();
                if(rs.next()){
                    return "WrongPassword";
                }else{
                    return "NoSuchUser";
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            sql.close();
            sql2.close();
        }
            return "SystemError";
    }
    public boolean update(User u){
        //Tool.log("Edit:"+u.getUsername());
        String sql="UPDATE users set ";
        if(u.getPassword()!=null) sql+="password = md5(?),";
        sql+=" nick = ?,name = ?,gender = ?,school = ?,faculty = ?,major = ?,cla = ?";
        sql+=",no = ?,phone = ?,Email = ?,motto = ?,inTeamLv = ?,inTeamStatus = ? WHERE username=?";

        SQL s = new SQL(sql);
        if(u.getPassword()!=null) s.addArgs(u.getPassword());
        s.addArgs(u.getNick(),u.getName(),u.getGender(),u.getSchool(),u.getFaculty(),u.getMajor(),u.getCla(),u.getNo(),
                    u.getPhone(),u.getEmail(),u.getMotto(),u.getInTeamLv(),u.getInTeamStatus(),u.getUsername());
        //Tool.log(sql);
        EventMain.triggerEvent(new EventVerify(u));
        boolean k=(s.update()==1);
        remove_catch(u.getUsername());
        return k;
    }
    public int updateByVerify(UserVerifyInfo userVerifyInfo){
        User u = Main.users.getUser(userVerifyInfo.username);
        int inTeamLv = u.getInTeamLv();
        int status = u.getInTeamStatus();
        if(userVerifyInfo.VerifyType == User.V_NONE){//修改资料
            status = u.getInTeamStatus();
        }else{
            status = userVerifyInfo.VerifyType;
            if(userVerifyInfo.VerifyType == User.V_TEAM){
                inTeamLv = -1;
            }else if(userVerifyInfo.VerifyType == User.V_RETIRED){
                if(u.getInTeamLv() <= 0){
                    status = User.V_ASSOCIATION;//集训队等级不足，退为协会成员
                }else{
                    status = User.V_RETIRED;
                }
            }
        }
        int ret = new SQL("UPDATE users SET " +
                "name=?,school=?,gender=?,faculty=?,major=?,cla=?,no=?,phone=?,email=?,graduationTime=?,inTeamStatus=?,inTeamLv=? " +
                "WHERE username=?",
                userVerifyInfo.name,userVerifyInfo.school,userVerifyInfo.gender,
                userVerifyInfo.faculty,userVerifyInfo.major,userVerifyInfo.cla,userVerifyInfo.no,
                userVerifyInfo.phone,userVerifyInfo.email,userVerifyInfo.graduationTime,status,inTeamLv,userVerifyInfo.username
        ).update();
        remove_catch(userVerifyInfo.username);
        return ret;
    }
    public TeamMemberAwardInfo getTeamMemberAwardInfo(int id){
        return new SQL("SELECT * FROM t_team_member_info WHERE id = ?",id).queryBean(TeamMemberAwardInfo.class);
    }
    public List<TeamMemberAwardInfo> getTeamMemberAwardInfoList(String username) {
        return new SQL("SELECT * FROM t_team_member_info WHERE username1 = ? OR username2 = ? OR username3 = ? ORDER BY time",username,username,username).queryBeanList(TeamMemberAwardInfo.class);
    }
    public List<TeamMemberAwardInfo> getTeamMemberAwardInfoList(int from,int num,boolean admin){
        if(admin)
            return new SQL("SELECT * FROM t_team_member_info ORDER BY time DESC LIMIT ?,?",from,num).queryBeanList(TeamMemberAwardInfo.class);
        else
            return new SQL("SELECT * FROM t_team_member_info WHERE contest_level!=-1 ORDER BY time DESC LIMIT ?,?",from,num).queryBeanList(TeamMemberAwardInfo.class);
    }
    public int updateTeamMemberAwardInfo(TeamAward info){
        return new SQL("REPLACE t_team_member_info values(?,?,?,?,?,?,?,?,?,?,?)",
                info.getId(),
                Date.valueOf(info.getAwardTime_d()),
                info.getUsername1(),
                info.getUsername2(),
                info.getUsername3(),
                info.getName1(),
                info.getName2(),
                info.getName3(),
                info.getContestLevel(),
                info.getAwardLevel(),
                info.getText()
        ).update();
    }
    public int addTeamMemberAwardInfo(TeamAward info){
        return new SQL("INSERT INTO t_team_member_info(time,username1,username2,username3,name1,name2,name3,contest_level,awards_level,text) values(?,?,?,?,?,?,?,?,?,?)",
                Date.valueOf(info.getAwardTime_d()),
                info.getUsername1(),
                info.getUsername2(),
                info.getUsername3(),
                info.getName1(),
                info.getName2(),
                info.getName3(),
                info.getContestLevel(),
                info.getAwardLevel(),
                info.getText()
                ).update();
    }
    public int delTeamMemberAwardInfo(int id){
        return new SQL("DELETE FROM t_team_member_info WHERE id=?",id).update();
    }
    public int getTeamMemberAwardInfoListNum(){
        return new SQL("SELECT COUNT(*) FROM t_team_member_info").queryNum();
    }

    private static int ViewCodeType_ViewCode = 0;
    private static int ViewCodeType_DownloadData = 1;

    public boolean haveViewCode(String user,int pid){
        return new SQL("SELECT COUNT(*) FROM t_viewcode WHERE username=? AND pid=? AND `type`=?", user, pid,ViewCodeType_ViewCode).queryNum() != 0;
    }
    public Set<Integer> canViewCode(String user){
        return new SQL("SELECT pid FROM t_viewcode WHERE username=? AND `type`=?",user,ViewCodeType_ViewCode).querySet();
    }
    public int addViewCode(String user,int pid){
        return new SQL("INSERT INTO t_viewcode VALUES(?,?,?)",user,pid,ViewCodeType_ViewCode).update();
    }

    public boolean haveDownloadData(String user,int pid){
        return new SQL("SELECT COUNT(*) FROM t_viewcode WHERE username=? AND pid=? AND `type`=?", user, pid,ViewCodeType_DownloadData).queryNum() != 0;
    }
    public int addDownloadData(String user,int pid){
        return new SQL("INSERT INTO t_viewcode VALUES(?,?,?)",user,pid,ViewCodeType_DownloadData).update();
    }

    public int awardACB(String user,int num,String text){
        if(num<0){
            num=-num;
            int ret=subACB(user,num,AcbOrderType.ADMIN,text);
            if(ret>0){
                MessageMain.addMessageSubACB(user,num,text);
                Tool.log("管理员扣去"+user+" "+num+"ACB"+" 备注："+text);
            }
            return ret;
        }
        addACB(user,num,AcbOrderType.ADMIN,text);
        int x=MessageMain.addMessageAwardACB(user,num,text);
        Tool.log("管理员给"+user+" "+num+"ACB"+" 备注："+text);
        return x;
    }
    public int addACB(String user, int num, AcbOrderType orderType,String mark){
        int ret= new SQL("UPDATE users SET acb=acb+? WHERE username=?",num,user).update();
        User u  = getBeanFromCatch(user);
        if(u!=null){
            u.setAcb(u.getAcb() + num);
        }
        AcbOrder acbOrder = new AcbOrder();
        acbOrder.username = user;
        acbOrder.change = num;
        acbOrder.reason = orderType;
        acbOrder.mark = mark;
        acbOrder.time = Tool.now();
        Main.acbOrderSQL.addAcbOrder(acbOrder);
        EventMain.triggerEvent(new EventAcbChg(getUser(user),acbOrder));
        return ret;
    }
    public int subACB(String user,int num, AcbOrderType orderType,String mark) {
        int ret = new SQL("UPDATE users SET acb=acb-? WHERE username=? AND acb>=?", num, user, num).update();
        if(ret != 0) {
            User u  = getBeanFromCatch(user);
            if(u!=null){
                u.setAcb(u.getAcb() - num);
            }
            AcbOrder acbOrder = new AcbOrder();
            acbOrder.username = user;
            acbOrder.change = -num;
            acbOrder.reason = orderType;
            acbOrder.mark = mark;
            acbOrder.time = Tool.now();
            Main.acbOrderSQL.addAcbOrder(acbOrder);
            EventMain.triggerEvent(new EventAcbChg(getUser(user),acbOrder));
        }
        return ret;
    }
    public void updateUserAcnum(String username){
        new SQL("UPDATE users SET acnum = (select sum(t_usersolve.status) from t_usersolve where t_usersolve.username=?) where username=?",username,username).update();
        remove_catch(username);
    }
    public void updateAllUserAcnum(){
        new SQL("UPDATE users SET acnum = IFNULL((select sum(t_usersolve.status) from t_usersolve where t_usersolve.username=users.username),0)").update();
    }
    @Override
    protected User getByKeyFromSQL(String username) {
        if(username == null) return null;
        User ret = new SQL("SELECT * from users where username=?",username).queryBean(User.class);
        if(ret!=null){
            ret.setPermission(getPermission(username));
            ret.titleSet = getTitle(username);
        }
        return ret;
    }
}
