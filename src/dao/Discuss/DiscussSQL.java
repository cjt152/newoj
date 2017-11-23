package dao.Discuss;

import entity.Discuss.Discuss;
import entity.Discuss.ReplyReply;
import entity.Permission;
import entity.User;
import util.Main;
import entity.Discuss.DiscussReply;
import servise.MessageMain;
import util.HTML.HTML;
import com.fjutacm.common.sql.SQL;
import util.Tool;

import java.util.List;

/**
 * Created by Syiml on 2015/7/3 0003.
 */
public class DiscussSQL {
    public static Discuss getDiscuss(int id){
        return new SQL("SELECT *,(select count(*) from t_discussreply where did=t_discuss.id )as replynum FROM t_discuss WHERE id=?",id)
                .queryBean(Discuss.class);
    }
    public static List<Discuss> getDiscussTOP(boolean all){
        String sql="SELECT *,0 as replynum FROM t_discuss WHERE top=1"+
                (all?"":" AND visiable=1")+
                " ORDER BY priority DESC";
        return new SQL(sql).queryBeanList(Discuss.class);
    }
    public static List<Discuss> getDiscussList(int cid,int from,int num,boolean all,String seach,String user){//admin is all
        String sql="SELECT *,(select count(*) from t_discussreply where did=t_discuss.id )as replynum FROM t_discuss WHERE cid="+cid;
        if(!all){
            sql+=" AND visiable=1";
        }
        if(seach!=null&&!seach.equals("")){
            sql+=" AND title like '%"+seach+"%'";
        }
        if(user!=null&&!user.equals("")){
            sql+=" AND username='"+user+"'";
            if(!all){
                sql+=" AND showauthor=1";
            }
        }
        sql+=" ORDER BY priority DESC";
        sql+=" LIMIT "+from+","+num;
        return new SQL(sql).queryBeanList(Discuss.class);
    }
    public static int getDiscussListNum(int cid,boolean all,String seach,String user){//admin is all
        String sql="SELECT count(*) FROM t_discuss WHERE cid="+cid;
        if(!all){
            sql+=" AND visiable=1";
        }
        if(seach!=null&&!seach.equals("")){
            sql+=" AND title like '%"+seach+"%'";
        }
        if(user!=null&&!user.equals("")){
            sql+=" AND username='"+user+"'";
            if(!all){
                sql+=" AND showauthor=1";
            }
        }
        return new SQL(sql).queryNum();
    }
    public static int newid(){
        return new SQL("SELECT max(id)+1 FROM t_discuss").queryNum();
    }
    public static int addDiscuss(Discuss d){
        int id=newid();
        new SQL("INSERT INTO t_discuss values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
                ,id
                , d.getCid()
                , d.isadmin()? d.getTitle() :HTML.HTMLtoString(d.getTitle())
                , d.getPanelclass()
                , d.getUsername()
                , d.getTime()
                , d.getText()
                , d.getPriority() ==-1?id: d.getPriority()
                , d.isTop()
                , d.isVisiable()
                , d.isReply()
                , d.getShownum()
                , d.isPanelnobody()
                , d.isShowauthor()
                , d.isShowtime()
                , d.isReplyHidden()).update();
        return id;
    }
    public static void append(Discuss d){
        String text= getDiscuss(d.getId()).getText();
        text+="<hr/><p>以下内容于["+HTML.textb(Tool.now().toString().substring(0,19),"blue")+"]补充</p>";
        text+=d.getText();
        new SQL("UPDATE t_discuss SET text=? WHERE id=?",text, d.getId()).update();
    }
    public static void editDiscuss(Discuss d){
        String sql="UPDATE t_discuss SET "+
                " title=?"+
                ",panelclass=?"+
                ",text=?"+
                ",priority=?"+
                ",top=?"+
                ",visiable=?"+
                ",reply=?"+
                ",shownum=?"+
                ",panelnobody=?"+
                ",showauthor=?"+
                ",showtime=?"+
                ",replyhidden=?"+
                " WHERE id=?";
        new SQL(sql
                ,d.isadmin()? d.getTitle() :HTML.HTMLtoString(d.getTitle())
                , d.getPanelclass()
                ,d.isadmin()? d.getText() :HTML.pre(HTML.HTMLtoString(d.getText()))
                , d.getPriority() ==-1? d.getId() : d.getPriority()
                , d.isTop()
                , d.isVisiable()
                , d.isReply()
                , d.getShownum()
                , d.isPanelnobody()
                , d.isShowauthor()
                , d.isShowtime()
                , d.isReplyHidden()
                , d.getId()).update();
    }
    ////////////////discuss replay///////////////////
    public static List<DiscussReply> getDiscussReplay(int did, int from, int num){
        return new SQL("SELECT * FROM t_discussreply WHERE did=? LIMIT ?,?",did,from,num)
                .queryBeanList(DiscussReply.class);
    }
    public static int getDiscussReplayNum(int did){
        return new SQL("SELECT COUNT(*) FROM t_discussreply WHERE did=?",did)
                .queryNum();
    }
    public static DiscussReply getDiscussReply(int did,int rid){
        return new SQL("SELECT * FROM t_discussreply WHERE did=? AND rid=?",did,rid)
                .queryBean(DiscussReply.class);
    }
    private static int getNewReplyId(int did){
        return new SQL("SELECT MAX(rid) FROM t_discussreply WHERE did=?",did).queryNum()+1;
    }
    public static synchronized String reply(User loginuser,int did,String text){
        if(loginuser==null) return "error";
        Discuss d=getDiscuss(did);
        if(d==null) return "error";
        int newId=getNewReplyId(did);
        new SQL("INSERT INTO t_discussreply VALUES(?,?,?,?,?,?,0,null)",newId,did,loginuser.getUsername(),Tool.now(),text,!d.isReplyHidden()).update();
        DiscussReply dr = getDiscussReply(did,newId);
        if(dr != null){
            MessageMain.addMessageDisscussReply(dr);
        }
        return "success";
    }
    public static String hideshow(int did,int rid){
        Permission per=Main.loginUserPermission();
        if(!per.getAddDiscuss())return "error";
        new SQL("UPDATE t_discussreply SET visiable=not visiable WHERE did=? AND rid=?",did,rid).update();
        return "success";
    }
    public static String adminReply(int did,int rid,String text){
        Permission per=Main.loginUserPermission();
        if(!per.getAddDiscuss())return "error";
        if(text.equals("")) text=null;
        String sql="UPDATE t_discussreply SET adminreplay=? WHERE did=? AND rid=?";
        new SQL(sql,text,did,rid).update();
        MessageMain.addMessageDiscussReplyAdmin(did, rid, text);
        return "success";
    }
    ////////////////ReplyReply//////////////
    public static List<ReplyReply> getReplyReply(int did, int rid, int from, int num){
        return new SQL("SELECT * FROM t_replyreply WHERE did=? AND rid=? LIMIT ?,?",did,rid,from,num).queryBeanList(ReplyReply.class);
    }
    public static int getReplyReplyNum(int did, int rid) {
        return new SQL("SELECT COUNT(*) FROM t_replyreply WHERE did=? AND rid=?",did,rid).queryNum();
    }
    public static synchronized int addReplyReply(int did,int rid,int replyRid,User loginUser,String text){
        int newId=getNewReplyReplyID(did,rid);
        new SQL("INSERT INTO t_replyreply VALUES(?,?,?,?,?,?,?,?)",did,rid,newId,replyRid,loginUser.getUsername(),Tool.now(),text,true).update();
        return newId;
    }
    private static int getNewReplyReplyID(int did,int rid){
        return new SQL("SELECT MAX(rrid) FROM t_replyreply WHERE did=? AND rid=?",did,rid).queryNum()+1;
    }
    public static ReplyReply getReplyReply(int did, int rid, int rrid){
        return new SQL("SELECT * FROM t_replyreply WHERE did=? AND rid=? AND rrid=?",did,rid,rrid).queryBean(ReplyReply.class);
    }
}
