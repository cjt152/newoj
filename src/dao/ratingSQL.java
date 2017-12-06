package dao;

import entity.User;

import java.util.List;

import entity.RatingCase;
import util.Main;
import util.SQL.SQL;
import com.google.gson.Gson;

/**
 * Created by Syiml on 2015/7/3 0003.
 */
public class ratingSQL {
    public static void save(RatingCase r){
        new SQL("INSERT INTO t_rating VALUES(?,?,?,?,?,?,?) ", r.getUsername(),r.getTime(), r.getCid(), r.getPrating(), r.getRating(), r.getRatingnum(), r.getRank()).update();
        new SQL("UPDATE users set ratingnum=ratingnum+1,rating=? WHERE username=?", r.getRating(), r.getUsername()).update();
        Main.users.remove_catch(r.getUsername());
    }
    public static List<RatingCase> getRating(int cid){
        return new SQL("SELECT username,time,cid,prating,rating,ratingnum,rank,(select name from contest where id=cid) as cname,0 as num FROM t_rating WHERE cid=? order by rank",cid)
                .queryBeanList(RatingCase.class);
    }
    public static List<RatingCase> getRating(String username){
        return new SQL("SELECT username,time,cid,prating,rating,ratingnum,rank,(select name from contest where id=cid) as cname,(SELECT COUNT(*) FROM t_rating a where a.cid=b.cid) as num FROM t_rating b WHERE username=? order by ratingnum desc",username)
                .queryBeanList(RatingCase.class);
    }
    public static String getJson(String username,boolean t){
        List<RatingCase> list=getRating(username);
        for(RatingCase r:list){
            if(t){
                r.setRating(r.getRating());
            }else{
                r.setRating(User.getShowRating(r.getRatingnum(), r.getRating()));
            }
        }
        Gson g=new Gson();
        return g.toJson(list);
    }
}
