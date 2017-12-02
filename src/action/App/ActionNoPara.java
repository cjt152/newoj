package action.App;

import action.BaseAction;
import dao.Discuss.DiscussSQL;
import entity.Contest;
import entity.Discuss.Discuss;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.struts2.ServletActionContext;
import org.jsoup.nodes.Document;
import servise.ContestMain;
import util.HTML.contestListHTML;
import util.JSON.AppJson;
import util.JSON.JSON;
import util.Main;
import util.MyClient;
import util.Tool;

import java.sql.Timestamp;
import java.util.List;

/**
 * 没有参数的请求
 * Created by syimlzhu on 2016/9/18.
 */
public class ActionNoPara extends BaseAction{

    public String getSelfInfo(){
        //Tool.log("getSelfInfo");
        out.print(AppJson.getSelfUserInfo());
        return NONE;
    }
    public String getOtherOjsContest(){
        out.print(JSON.getOtherOjsContest());
        return NONE;
    }
    public String getRecentlyContest(){
        List<Contest> list = ContestMain.getContests(0,6,-1,null,-1,-1);
        JSONArray ja = new JSONArray();
        for(Contest c:list){
            ja.add(JSON.getJSONObject(
                    "cid",c.getCid()+"",
                    "name",c.getName(),
                    "begin",c.getBeginDate()+"",
                    "end",c.getEndTime()+"",
                    "type",c.getType()+"",
                    "kind",c.getKind()+""
                    ));
        }
        out.print(ja.toString());
        return NONE;
    }

    public String getIndexDiscuss(){
        List<Discuss> list= DiscussSQL.getDiscussTOP(false);
        JSONObject ret = new JSONObject();
        JSONArray data = new JSONArray();
        for(Discuss d:list){
            if(d.getId() == 1 || d.getId() == 27 || d.getId() == 33 || d.getId() == 4 || d.getId() == 69) continue;
            data.add(d.toJSON());
        }
        ret.put("data",data);
        out.print(ret.toString());
        return NONE;
    }
}
