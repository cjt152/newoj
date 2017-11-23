package dao;

import entity.Condition;
import entity.Block;
import servise.ChallengeMain;
import util.HTML.HTML;
import util.HTML.TableHTML;
import com.fjutacm.common.sql.SQL;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * 挑战模式dao层
 * 涉及表：
 * t_challenge_block
 * t_challenge_condition
 * t_challenge_problem
 * Created by Syiml on 2015/10/10 0010.
 */
public class ChallengeSQL {
    /**
     * 初始化，获取Block列表
     * @return Blcok列表，key是id，方便通过id查找Block
     */
    public static Map<Integer, Block> init(){
        Map<Integer,Block> ret=new TreeMap<Integer, Block>();
        SQL sql1=new SQL("SELECT * FROM t_challenge_block order by id");
        SQL sql2=new SQL("SELECT * FROM t_challenge_condition");
        SQL sql3=new SQL("SELECT id,sum(score) as score FROM t_challenge_problem GROUP BY id");
        ResultSet rsBlock = sql1.query();
        ResultSet rsCondition = sql2.query();
        ResultSet rsScore = sql3.query();
        try {
            while(rsBlock.next()){
                ret.put(rsBlock.getInt("id"),new Block(rsBlock.getInt("id"),rsBlock.getString("name"),rsBlock.getInt("gro"),rsBlock.getInt("isEditing")));
            }
            while(rsCondition.next()){
                int belongBlockId= rsCondition.getInt("belongBlockId");
                ret.get(belongBlockId).addCondition(new Condition(belongBlockId,rsCondition.getInt("type"),rsCondition.getInt("par"),rsCondition.getInt("num"),rsCondition.getInt("id")));
            }
            while(rsScore.next()){
                int id = rsScore.getInt("id");
                ret.get(id).setScore(rsScore.getInt("score"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            sql1.close();
            sql2.close();
            sql3.close();
        }
        return ret;
    }

    /**
     * 获得某user每个Block的得分
     * @param user 用户名
     * @return key是BlockId，value是得分
     */
    public static Map<Integer,Integer> getUserScore(String user){
        SQL sql=new SQL("" +
                "SELECT id,sum(score) as score " +
                "FROM t_challenge_problem " +
                "JOIN t_usersolve " +
                "ON t_challenge_problem.tpid = t_usersolve.pid AND username = ? AND status=1 " +
                "GROUP BY id",user){
            protected Integer getObject(int i) throws SQLException {
                return rs.getInt(i);
            }
        };
        return sql.queryMap();
    }
    public static Set<Integer> getOpenBlocks(String user){
        return new SQL("Select block from t_challenge_openblock where username=?",user).querySet();
    }
    public static void addOpenBlock(String user, int block){
        new SQL("insert into t_challenge_openblock values(?,?)",user,block).update();
    }
    static boolean isOpen(String user,int block){
        return new SQL("SELECT * from t_challenge_openblock where username=? and block=?",user,block).queryObj();
    }
    public static JSONArray getProblems(String user, int id){
        SQL sql=new SQL("select status+1 as solved,t_challenge_problem.pid,tpid,(select title from problem where pid=t_challenge_problem.tpid) as title,score " +
                "from t_challenge_problem left join t_usersolve on t_usersolve.pid=t_challenge_problem.tpid and username=? " +
                "where t_challenge_problem.id=?",user,id);
        JSONArray problemList=new JSONArray();
        ResultSet ps= sql.query();
        try {
            while(ps.next()){
                JSONObject aProblem=new JSONObject();
                aProblem.put("solved",ps.getInt("solved"));
                aProblem.put("pid",ps.getInt("pid"));
                aProblem.put("tpid",ps.getInt("tpid"));
                aProblem.put("title",ps.getString("title"));
                aProblem.put("score",ps.getString("score"));
                problemList.add(aProblem);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            sql.close();
        }
        return problemList;
    }
    public static TableHTML getProblems(int id){
        TableHTML problemList=new TableHTML();
        problemList.setClass("table table-bordered");
        problemList.addColname("#","pid","标题","积分","删除");
        SQL sql=new SQL("select pid,tpid,(select title from problem where pid=t_challenge_problem.tpid) as title,score " +
                "from t_challenge_problem " +
                "where id=?",id);
        ResultSet rs = sql.query();
        try {
            while(rs.next()){
                List<String> row=new ArrayList<String>();
                row.add(rs.getInt("pid")+"");
                row.add(HTML.aNew("Problem.jsp?pid="+rs.getInt("tpid"),rs.getInt("tpid")+""));
                row.add(rs.getString("title"));
                row.add(rs.getInt("score")+"");
                row.add(HTML.a("delPorblem.action?block="+id+"&pos="+rs.getInt("pid"),"删除"));
                problemList.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            sql.close();
        }
        return problemList;
    }
    public static int getUserScore(String user, int id){
        return new SQL("SELECT sum(score) as score " +
                "FROM t_challenge_problem " +
                "JOIN t_usersolve " +
                "ON t_challenge_problem.tpid = t_usersolve.pid AND username = ? AND status=1 " +
                "AND id=? " +
                "GROUP BY id",user,id).queryNum();
    }
    public static String getText(int id){
        return new SQL("select text from t_challenge_block where id=?", id).queryString();
    }
    public static String editBlockText(int id,String text){
        if(new SQL("update t_challenge_block set text=? where id=? ",text,id).update()!=0){
            return "success";
        }else{
            return "error";
        }
    }
    public static String addCondition(int block,int type,int par,int num){
        if(new SQL("insert into t_challenge_condition values(?,?,?,?,?)",block,type,par,num,null).update()==1){
            ChallengeMain.init();
            return "success";
        }
        return "error";
    }
    public static String delCondition(int id){
        if(new SQL("delete from t_challenge_condition where id=?",id).update()==1){
            ChallengeMain.init();
            return "success";
        }
        return "error";
    }
    public static String addProblem(int pos,int bid,int pid,int score){
        new SQL("update t_challenge_problem set pid=pid+1 where pid>=? and id=? ORDER BY pid DESC",pos,bid).update();
        new SQL("insert into t_challenge_problem values(?,?,?,?)",bid,pos,pid,score).update();
        ChallengeMain.init();
        return "success";
    }
    public static String delProblem(int pos,int bid){
        new SQL("delete from t_challenge_problem where id=? and pid=?",bid,pos).update();
        new SQL("update t_challenge_problem set pid=pid-1 where pid>? and id=?",pos,bid).update();
        ChallengeMain.init();
        return "success";
    }
    public static String setEditing(int id,int isEditing){
        if(new SQL("UPDATE t_challenge_block SET isEditing=? WHERE id=?",isEditing,id).update()!=0){
            ChallengeMain.blocks.get(id).setIsEditing(isEditing);
            return "success";
        }else{
            return "error";
        }
    }
    public static void addBlock(int id,String name){
        new SQL("INSERT INTO t_challenge_block VALUES(?,?,?,?,?)",id,name,0,"",1).update();
        ChallengeMain.blocks= ChallengeSQL.init();
    }
    public static String modBlockType(int id,int gro ){
         if (new SQL("UPDATE t_challenge_block SET gro = ? WHERE id = ?",gro,id).update() !=0 ){
             ChallengeMain.blocks= ChallengeSQL.init();
             return "success";
         }else {
             return "error";
         }
    }
}
