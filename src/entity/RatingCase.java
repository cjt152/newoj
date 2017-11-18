package entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * Created by Syiml on 2015/7/3 0003.
 */
public class RatingCase implements IBeanResultSetCreate{
    public String getUsername() {
        return username;
    }
    public Timestamp getTime() {
        return new Timestamp(time);
    }
    public int getCid() {
        return cid;
    }
    public int getPrating() {
        return prating;
    }
    public int getRating() {
        return rating;
    }
    public int getRatingnum() {
        return ratingnum;
    }
    public int getRank() {
        return rank;
    }

    String username;
    long time;
    int cid;
    int prating;
    int rating;
    int ratingnum;
    int rank;
    int num;
    String text;

    public RatingCase(){}
    public RatingCase(String u,Timestamp t,int cid,int prating,int rating,int ratingnum,int rank,String cname){
        username=u;
        time=t.getTime();
        this.cid=cid;
        this.prating=prating;
        this.rating=rating;
        this.ratingnum=ratingnum;
        this.rank=rank;
        this.text= cname + " rank:" + rank + "/"+ num;
    }

    @Override
    public void init(ResultSet rs) throws SQLException {
        //username,time,cid,prating,rating,ratingnum,rank,(select name from contest where id=cid) as cname
        username=rs.getString("username");
        time=rs.getTimestamp("time").getTime();
        cid=rs.getInt("cid");
        prating=rs.getInt("prating");
        rating=rs.getInt("rating");
        ratingnum=rs.getInt("ratingnum");
        rank=rs.getInt("rank");
        num = rs.getInt("num");
        text=rs.getString("cname")+ " rank:" + rank + "/" + num;
    }

    public String getText() {
        return text;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public void setPrating(int prating) {
        this.prating = prating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setRatingnum(int ratingnum) {
        this.ratingnum = ratingnum;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getTrueRating(){
        return User.getShowRating(ratingnum,rating);
    }
    public int getTruePRating(){
        return User.getShowRating(ratingnum-1,prating);
    }
}
