package entity;

import entity.Title.TitleSet;
import net.sf.json.JSONObject;
import util.Main;
import util.HTML.HTML;
import action.register;
import util.Tool;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Map;

/**
 * Created by Administrator on 2015/6/3.
 */
public class User implements IBeanResultSetCreate, IBeanCanCatch,ICanToJSON{
    public static final int V_TEAM = 1; //集训队员
    public static final int V_RETIRED = 2; //退役队员
    public static final int V_ASSOCIATION = 3; //协会成员
    public static final int V_SCHOOL = 4; //校内人员
    public static final int V_NONE = 0; //未认证
    String username;
    String password;
    String nick;
    String school;
    String email;
    String motto;//签名
    Timestamp registertime;
    int type;
    int submissions;
    String mark;
    int acnum;
    Permission permission = null;
    int rating;//if rating == -100000    is null
    int ratingnum=0;
    int rank;//rating 的rank
    int acb;
    int inTeamLv;//队员等级
    int inTeamStatus;//队员状态 0非队员 1现役队员 2退役队员 3协会成员 4校内人员
    //详细信息：姓名，性别，学校，院系，专业班级，学号，手机
    String name;
    int    gender;//性别
    String faculty;//学院
    String major;//专业
    String cla;//班级
    String no;//学号
    String phone;//联系方式
    Timestamp graduationTime;//毕业时间
    private Timestamp catch_time;
    public TitleSet titleSet = null;
    public Timestamp autoClockInTime = null;
    private Map<Integer,Integer> acProblem = null; //每道题的AC次数
    public User(){}

    public User(register r){
        username=r.username;
        password=r.password;
        nick=r.nick;
        gender=r.gender;
        school=r.school;
        email=r.email;
        motto=r.motto;
        registertime=new Timestamp(System.currentTimeMillis());
        type=0;
        submissions=0;
        mark="";
        acnum=-1;
        //acproblems=new HashSet<Integer>();
        acb=0;
        this.inTeamLv=0;
        this.inTeamStatus=0;
        this.graduationTime = null;
    }

    public static int getShowRating(int ratingnum,int rating){
        int z=700;
        double f=0.6;
        if(ratingnum==0) return -100000;
        return (int)((rating-z)*(1-Math.pow(f,ratingnum))+z+0.5);
    }

//    public void countAcProblem(){
//        acproblems=Main.status.getAcProblems(username);
//    }
    public static String ratingColor(int rating){
        if(rating==-100000) return "#000000";
        if(rating<1000) return "#808080";
        if(rating<1200) return "#40C040";
        if(rating<1400) return "#00FF00";
        if(rating<1500) return "#00C0FF";
        if(rating<1600) return "#0000FF";
        if(rating<1700) return "#C000FF";
        if(rating<1900) return "#FF00FF";
        if(rating<2000) return "#FF0080";
        if(rating<2200) return "#FF0000";
        if(rating<2600) return "#FF8000";
        return "#FFD700";
    }

    public static String ratingToHTML(int rating){
        if(rating==-100000) return"-";
        return HTML.textb(rating+"",ratingColor(rating));
    }

    public  int getSubAcbInWeekContest(){
        if(inTeamStatus != User.V_TEAM) return 0;
        switch (inTeamLv){
            case -1:
                return 50;
            case 0:
                return 50;
            case 1:
                return 100;
            case 2:
                return 150;
            case 3:
                return 200;
            case 4:
                return 300;
            case 5:
                return 400;
            case 6:
                return 500;
            default:
                return 0;
        }

    }

    @Override
    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username",username);
        jsonObject.put("nick",nick);
        jsonObject.put("motto",motto);
        jsonObject.put("submissions",submissions);
        jsonObject.put("acnum",acnum);
        jsonObject.put("acb",acb);
        jsonObject.put("rating",getShowRating());
        jsonObject.put("ratingnum",getRatingnum());
        jsonObject.put("rank",rank);
        return jsonObject;
    }

    public void init(ResultSet rs) throws SQLException {
        //username,nick,gender,school,Email,motto,registertime,type,Mark,rating,rank,ratingnum,acnum,acb
        this.username=rs.getString("username");
        this.nick=rs.getString("nick");
        this.gender=rs.getInt("gender");
        this.school=rs.getString("school");
        this.email=rs.getString("Email");
        this.motto=rs.getString("motto");
        this.registertime=rs.getTimestamp("registertime");
        this.type=rs.getInt("type");
        this.mark=rs.getString("Mark");
        this.rating=rs.getInt("rating");
        this.ratingnum=rs.getInt("ratingnum");
        this.acb=rs.getInt("acb");
        this.name=rs.getString("name");
        this.faculty=rs.getString("faculty");
        this.major=rs.getString("major");
        this.cla=rs.getString("cla");
        this.no=rs.getString("no");
        this.phone=rs.getString("phone");
        this.inTeamLv=rs.getInt("inTeamLv");
        this.inTeamStatus=rs.getInt("inTeamStatus");
        this.acnum=rs.getInt("acnum");
        this.graduationTime = rs.getTimestamp("graduationTime");
        try{
            this.rank=rs.getInt("rank");
        }catch(SQLException e){
            this.rank=-1;
        }
        //permission=Main.getPermission(username);
    }

    public int getShowRating(){
        return getShowRating(ratingnum,rating);
    }

    public String getRatingHTML() {
        return ratingToHTML(getShowRating());
    }

    public String getUsernameHTMLNoA(){
        return HTML.textb(username, ratingColor(getShowRating())) + getIcon();
    }
    /////get set////

    public String getIcon(){
        if(inTeamStatus==1){//集训队员标记
            return "<em class='inTeam inTeam-"+inTeamLv+"'></em>";
        }else if(inTeamStatus==2){//退役队员
            return "<em class='inTeam levTeam-"+inTeamLv+"'></em>";
        }else if(inTeamStatus == User.V_ASSOCIATION){//协会成员
            if(getGraduationTime()!=null && Tool.now().before(getGraduationTime())) {//未毕业
                return "<em class='inTeam association'></em>";
            }else{
                return "<em class='inTeam graduated'></em>";
            }
        }else if(inTeamStatus == User.V_SCHOOL){//校内人员
            if(getGraduationTime()!=null && Tool.now().before(getGraduationTime())) {//未毕业
                return "<em class='inTeam school'></em>";
            }else{
                return "<em class='inTeam graduated'></em>";
            }
        }
        return "";
    }

    public String getUsernameHTML(){
        return HTML.a("UserInfo.jsp?user="+username,getUsernameHTMLNoA());
    }

    public String getUsernameAndNickHTML(){
        return HTML.a("UserInfo.jsp?user="+username,getUsernameHTMLNoA())+"("+nick+")";
    }

    public String getTitleAndNickNoA(){
        if(titleSet == null){
            return Main.users.getUser(username).getTitleAndNickNoA();
        }
        return "<font class='title'>"+titleSet.getNickTitle()+"</font>"+HTML.textb(nick, ratingColor(getShowRating())) + getIcon();
    }
    public String getTitleAndNick(){
        if(titleSet == null){
            return Main.users.getUser(username).getTitleAndNick();
        }
        return "<font class='title'>"+titleSet.getNickTitle()+"</font>"+HTML.a("UserInfo.jsp?user="+username,HTML.textb(nick, ratingColor(getShowRating())) + getIcon());
        //return HTML.a("UserInfo.jsp?user="+username,getTitleAndNickNoA());
    }

    public boolean canRegisterOfficalContest(){
        return gender!=0&&!name.equals("")&&!faculty.equals("")&&!major.equals("")&&!cla.equals("")&&!no.equals("")&&!phone.equals("");
    }

    /**
     * @return 返回是否是现役队员
     */
    public boolean isInTeam(){
        if(inTeamStatus==V_TEAM)
            return true;
        else
            return false;
    }

    public int getRank(){return rank;}

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getRating(){return rating;}

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getRatingnum(){return ratingnum;}

    public void setRatingnum(int ratingnum) {
        this.ratingnum = ratingnum;
    }

    public Timestamp getRegistertime() {
        return registertime;
    }

    public void setRegistertime(Timestamp registertime) {
        this.registertime = registertime;
    }

    public Permission getPermission(){
        if(permission == null){
            permission = Main.getPermission(getUsername());
        }
        return permission;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getCla() {
        return cla;
    }

    public void setCla(String cla) {
        this.cla = cla;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getAcnum(){return acnum;}

    public void setAcnum(int acnum) {
        this.acnum = acnum;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMotto() {
        return motto;
    }

    public void setMotto(String motto) {
        this.motto = motto;
    }

    public int getSubmissions() {
        return submissions;
    }

    public void setSubmissions(int submissions) {
        this.submissions = submissions;
    }

    public int getAcb() {
        return acb;
    }

    public void setAcb(int acb) {
        this.acb = acb;
    }

    public int getInTeamLv() {
        return inTeamLv;
    }

    public void setInTeamLv(int inTeamLv) {
        this.inTeamLv = inTeamLv;
    }

    public int getInTeamStatus() {
        return inTeamStatus;
    }

    public void setInTeamStatus(int inTeamStatus) {
        this.inTeamStatus = inTeamStatus;
    }

    public Timestamp getGraduationTime() {
        return graduationTime;
    }

    public void setGraduationTime(Timestamp graduationTime) {
        this.graduationTime = graduationTime;
    }

    public Timestamp getCatch_time() {

        return catch_time;
    }

    public void setCatch_time(Timestamp catch_time) {
        this.catch_time = catch_time;
    }


    @Override
    public Timestamp getExpired() {
        return getCatch_time();
    }

    @Override
    public void setExpired(Timestamp t) {
        setCatch_time(t);
    }

    public String getVerifyText(){
        switch (inTeamStatus){
            case V_TEAM: return "集训队员";
            case V_RETIRED: return "退役队员";
            case V_ASSOCIATION: return "协会成员";
            case V_SCHOOL: return "校内学生";
            case V_NONE: return "未认证";
            default: return "ERROR";
        }
    }

    public static String getGenderText(int gender){
        if(gender == 1) return "男";
        else if(gender == 2) return "女";
        else return "未选择";
    }

    synchronized public Map<Integer,Integer> getAcProblem()
    {
        if(acProblem == null)
        {
            acProblem = Main.users.getAcProblem(username);
            acnum = acProblem.size();
        }
        return acProblem;
    }
}
