package dao;

import entity.Contest;
import servise.ContestMain;
import util.Main;
import entity.User;
import entity.Problem;
import util.HTML.HTML;
import util.HTML.problemHTML;
import util.HTML.problemListHTML.problemView;
import util.Pair;
import com.fjutacm.common.sql.SQL;
import action.editproblem;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Administrator on 2015/5/23.
 */
public class ProblemSQL extends BaseCache<Integer,Problem> {
    /*
    * problem(pid,ptype,title,ojid,ojspid)
    * */
    public ProblemSQL(){
        cachTime = 60*60*1000;
        maxSize = 100;
    }
    public Problem getProblem(int pid){
        return getBeanByKey(pid);
    }
    public List<problemView> getProblems(int pid1,int pid2,boolean showhide,String owner){
        String sql="select pid,title,visiable,totalAcUser,totalSubmit from problem where pid>=? and pid<=?";
        if(!showhide){
            if(owner!=null && !owner.equals("")){
                sql+= " and (visiable=1 or owner=?)";
                return new SQL(sql,pid1,pid2,owner).queryBeanList(problemView.class);
            }else {
                sql += " and visiable=1";
            }
        }
        return new SQL(sql,pid1,pid2).queryBeanList(problemView.class);
    }
    public List<problemView> getProblems(int from,int num,String search){
        if(search==null||search.equals("")){
            return new SQL("SELECT pid,title,visiable,totalAcUser,totalSubmit FROM  problem  WHERE visiable=1 LIMIT ?,?",from,num).queryBeanList(problemView.class);
        }else{
            return new SQL("SELECT pid,title,visiable,totalAcUser,totalSubmit FROM  problem  WHERE visiable=1 AND (pid=? OR title like ?) LIMIT ?,?",search,"%"+search+"%",from,num).queryBeanList(problemView.class);
        }
    }
    public int getProblemsNum(String search){
        if(search==null||search.equals("")) {
            return new SQL("SELECT COUNT(*) FROM  problem  WHERE visiable=1").queryNum();
        }else{
            return new SQL("SELECT COUNT(*) FROM  problem  WHERE visiable=1 AND (pid=? OR title like ?)",search,"%"+search+"%").queryNum();
        }
    }
    public List<problemView> getProblems(int cid){
        Contest c = ContestMain.getContest(cid);
        if(c.isStatusReadOut()){
            String sql="SELECT contestproblems.tpid,title,1,totalAcUser,totalSubmit FROM contestproblems LEFT JOIN problem ON contestproblems.tpid=problem.pid WHERE cid=?";
            //String sql="SELECT tpid,(select title from problem where problem.pid=tpid) as title,1,(select count(distinct ruser) from statu where statu.cid=? and statu.pid=tpid and result=1)as acnum,(select count(*) from statu where statu.cid=? and statu.pid=tpid)as submitnum FROM `contestproblems` WHERE cid=?";
            return new SQL(sql,cid).queryBeanList(problemView.class);
        }else{
            //pid,title,visiable,ac,submit
            String sql="SELECT tpid,(select title from problem where problem.pid=tpid) as title,1,(select count(distinct ruser) from statu where statu.cid=? and statu.pid=tpid and result=1)as acnum,(select count(*) from statu where statu.cid=? and statu.pid=tpid)as submitnum FROM `contestproblems` WHERE cid=?";
            return new SQL(sql,cid,cid,cid).queryBeanList(problemView.class);
        }
    }
    public int getPageNum(int num,boolean showHide){
        String sql="select MAX(pid) from problem where pid<10000";
        if(!showHide) {
            sql+=" and visiable=1";
        }
        int maxpid=new SQL(sql).queryNum();
        if(maxpid==0) return 1;
        return (maxpid-1000)/num+1;//没有任何题目默认有一页为空
    }
    public void editProblem(int pid,Problem pro){
        new SQL("UPDATE problem SET ptype=?,title=?,ojid=?,ojspid=?,author=?,spj=? WHERE pid=?",pro.getType(), pro.getTitle(),pro.getOjid(),pro.getOjspid(),pro.getAuthor(),pro.isSpj(),pid).update();
        removeCatch(pid);
    }
    public int addProblem(int pid,Problem pro){
        int newpid;
        if(pid==-1){
            newpid=new SQL("select MAX(pid)+1 from problem").queryNum();
            newpid=newpid==0?1000:newpid;
        }else{
            editProblem(pid,pro);
            return pid;
        }
        User u = Main.loginUser();
        assert u!=null;
        new SQL("Insert into problem values(?,?,?,?,?,?,?,?,0,0,0,0,?)",newpid,pro.getType(),pro.getTitle(),pro.getOjid(),pro.getOjspid()
                ,0,pro.getAuthor(),pro.isSpj(),u.getUsername()).update();
        return newpid;
    }
    public String setProblemVisiable(int pid){
        if(pid!=-1){
            new SQL("update problem set visiable=1-visiable where pid=?",pid).update();
            removeCatch(pid);
            return "success";
        }else return "error";
    }
    public boolean setProblemVisiable(int pid,int z){
        SQL sql=new SQL("update problem set visiable=? where pid=?",z,pid);
        boolean ret=(sql.update()==1);
        sql.close();
        removeCatch(pid);
        return ret;
    }
    public void setContestProblemVisiable(int cid,int z){
        new SQL("update problem set visiable=? where pid in (select tpid from contestproblems where cid=?)",z,cid).update();
        clearCatch();
    }
    public List<Integer> getProblemsByOjPid(int oj,String ojspid){
        SQL sql=new SQL("SELECT pid FROM problem WHERE ojid=? AND ojspid=?",oj,ojspid){
            @Override
            protected Object getObject(int i) throws SQLException {
                return rs.getInt(i);
            }
        };
        return sql.queryList();
    }

    public boolean isProblemLocal(int pid){
        return getProblem(pid).isLocal();
    }
    public int getOJid(int pid){
        return getProblem(pid).getOjid();
    }
    public String getOjspid(int pid){
        return getProblem(pid).getOjspid();
    }
    public String getTitle(int pid) { return getProblem(pid).getTitle();}
    public void saveProblemHTML(int pid,problemHTML ph){
        if(ph==null) return ;
        new SQL("INSERT INTO t_problemview VALUES(?,?,?,?,?,?,?,?)"
                ,pid,ph.getTimeLimit(),ph.getMenoryLimit(),ph.getInt64(),ph.getSpj(),ph.getDis(),ph.getInput(),ph.getOutput()).update();
        List<String> in=ph.getSampleInput();
        List<String> out=ph.getSampleOutput();
        for(int i=0;i<in.size();i++){
            new SQL("INSERT INTO t_problem_sample VALUES(?,?,?,?)",pid,i,in.get(i),out.get(i)).update();
        }
    }
    public String getP(int pid,String edit,int num){
        problemHTML p=getProblemHTML(pid);
        if(edit.equals("dis")) return p.getDis();
        if(edit.equals("input")) return p.getInput();
        if(edit.equals("output")) return p.getOutput();
        if(edit.equals("sampleinput")){
            List<String> s=p.getSampleInput();
            if(num<s.size())
                return s.get(num);
            else return "";
        }
        if(edit.equals("sampleoutput")){
            List<String> s=p.getSampleOutput();
            if(num<s.size())
                return s.get(num);
            else return "";
        }
        return "";
    }
    public String editProlem(editproblem ep){
        if(ep.getEdit().equals("dis")){
            new SQL("update t_problemview set Dis=? WHERE pid=?",ep.getS(),Integer.parseInt(ep.getPid())).update();
        }
        if(ep.getEdit().equals("input")){
            new SQL("update t_problemview set Input=? WHERE pid=?",ep.getS(),Integer.parseInt(ep.getPid())).update();
        }
        if(ep.getEdit().equals("output")){
            new SQL("update t_problemview set Output=? WHERE pid=?",ep.getS(),Integer.parseInt(ep.getPid())).update();
        }
        if(ep.getEdit().equals("sampleinput")){
            new SQL("update t_problem_sample set input=? WHERE pid=? AND id=?",ep.getS(),Integer.parseInt(ep.getPid()),Integer.parseInt(ep.getNum())).update();
        }
        if(ep.getEdit().equals("sampleoutput")){
            new SQL("update t_problem_sample set output=? WHERE pid=? AND id=?",ep.getS(),Integer.parseInt(ep.getPid()),Integer.parseInt(ep.getNum())).update();
        }
        return "success";
    }
    public boolean delProblemDis(int pid){
        new SQL("delete from t_problemview where pid=?",pid).update();
        new SQL("delete from t_problem_sample where pid=?",pid).update();
        return true;
    }
    public boolean addSample(int pid){
        SQL sql=new SQL("insert into t_problem_sample " +
                "values(?,0,'<pre class=\"sample\"></pre>'" +
                ",'<pre class=\"sample\"></pre>')",pid);
        boolean ret=(sql.update()==1);
        sql.close();
        return ret;
    }
    public problemHTML getProblemHTML(int pid){
        Problem p = Main.problems.getProblem(pid);
        problemHTML ph=new problemHTML(pid);
        SQL sql=new SQL("SELECT pid,timelimit,MenoryLimit,Int64,spj,Dis,Input,Output FROM t_problemview WHERE pid= ?",pid);
        SQL sql1 = null;
        try {
            ResultSet s=sql.query();
            if(s.next()){
                ph.setTitle(getTitle(pid));
                ph.setTimeLimit(s.getString(2));
                ph.setMenoryLimit(s.getString(3));
                ph.setInt64(s.getString(4));
                if(p.isLocal() || p.getOjid() == 7){
                    ph.setSpj(p.isSpj()?1:0);
                }else{
                    ph.setSpj(s.getInt(5));
                }
                ph.setDis(s.getString(6));
                ph.setInput(s.getString(7));
                ph.setOutput(s.getString(8));
                sql1 = new SQL("SELECT input,output FROM t_problem_sample WHERE pid=? ORDER BY id",pid);
                s=sql1.query();
                while(s.next()) {
                    ph.addSample(s.getString(1), s.getString(2));
                }
                return ph;
            }else{
                return null;
            }
        } catch (SQLException ignored) {
            return null;
        }finally {
            sql.close();
            if(sql1 != null) sql1.close();
        }
    }
    public Pair<Integer,Integer> getProblemLimit(int pid){
        SQL sql=new SQL("SELECT timelimit,MenoryLimit FROM t_problemview WHERE pid=?",pid);
        ResultSet rs=sql.query();
        try {
            if(rs.next()){
                String timeLimitStr=rs.getString("timelimit");
                String memoryLimitStr=rs.getString("MenoryLimit");
                int timeLimit=Integer.parseInt(timeLimitStr.substring(0, timeLimitStr.length() - 2));
                int memoryLimit=Integer.parseInt(memoryLimitStr.substring(0, memoryLimitStr.length() - 2));
                return new Pair<Integer, Integer>(timeLimit,memoryLimit);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            sql.close();
        }
        return new Pair<Integer, Integer>(0,0);
    }
    public String toHref(int pid,int cid){
        return "Contest.jsp?cid="+cid+"#Problem_"+pid;
    }
    public String toHTML(int pid) {
        return toHTML(pid,-1,Main.problems.getProblem(pid));
    }
    public String toHTML(int pid,int cid){
        return toHTML(pid,cid, ContestMain.getContest(cid).getProblem(pid));
    }
    private String toHTML(int pid,int cid,Problem p){
        if(cid!=-1){
            return "<tr><td>"+(char)(pid+'A')+"</td><td>"+HTML.a(toHref(pid,cid),p.getTitle())+"</td></tr>";
        }else{
            return "<tr><td>"+pid+"</td><td>"+HTML.a("Problem.jsp?pid="+pid,p.getTitle())+"</td></tr>";
        }
    }
    public SQL getProblemListByTag(int tagid,int from,int num) {
        String sql = "";
        User u = Main.loginUser();
        boolean vis;
        vis = ( u != null && u.getPermission().getShowHideProblem() );
        sql += "SELECT v_problem_tag.pid, ptype, title, ojid, ojspid, visiable, acusernum, submitnum, tagid, rating, status+1 as solved " +
                "FROM v_problem_tag " +
                "LEFT JOIN v_problem ON v_problem_tag.pid = v_problem.pid " +
                "LEFT JOIN t_usersolve ON username =  ? " +
                "AND    t_usersolve.pid = v_problem_tag.pid " +
                "WHERE tagid = ? " +
                (vis ? "" : " and status=1 ") +
                "ORDER BY rating DESC " +
                "LIMIT ?,?";
        String username;
        if (u != null) username = u.getUsername();
        else username = "";
        return new SQL(sql, username, tagid, from, num);
    }
    public void updateProblemTotals(int pid,int totalSubmit,int totalSubmitUser,int totalAc,int totalAcUser) {
        Problem p = getProblem(pid);
        p.totalSubmit = totalSubmit;
        p.totalSubmitUser = totalSubmitUser;
        p.totalAc = totalAc;
        p.totalAcUser = totalAcUser;
        new SQL("UPDATE problem SET totalSubmit=?,totalSubmitUser=?,totalAc=?,totalAcUser=? WHERE pid=?", totalSubmit, totalSubmitUser, totalAc, totalAcUser, pid).update();
    }
    public int getRandomPid(){
        return new SQL("SELECT pid FROM problem WHERE visiable=1 ORDER BY RAND() LIMIT 0,1").queryNum();
    }
    @Override
    protected Problem getByKeyFromSQL(Integer key) {
        return new SQL("SELECT * FROM problem WHERE pid=?",key).queryBean(Problem.class);
    }
}
