package util.rating;

import entity.*;
import entity.Enmu.AcbOrderType;
import servise.ContestMain;
import servise.MessageMain;
import util.Event.EventMain;
import util.Event.Events.EventRating;
import util.Main;
import dao.ratingSQL;
import util.HTML.HTML;
import util.HTML.TableHTML;
import util.Tool;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Syiml on 2015/6/30 0030.
 */
public class Computer {
    public static final int maxk=600;
    public static final int mink=400;
    public static final int maxlen=80;
    public static final int minlen=10;
    _rank r;
    List<Integer> rating;
    List<Integer> ratingnum;
    Timestamp time;
    List<Integer> ans;//保存结果
    int cid;
    public Computer(Contest c){
        cid=c.getCid();
        r=c.getRank().get_rank();
        time=c.getEndTime();
        rating=new ArrayList<Integer>();
        ratingnum=new ArrayList<Integer>();
        for(String u:r.username){
//            System.out.println(u);
            User us=Main.users.getUser(u);
            if(us!=null){
                int r=us.getRating();
                if(r==-100000) r=1500;
                rating.add(r);
                ratingnum.add(us.getRatingnum());
            }
        }
        ans=new ArrayList<Integer>();
//        for(int i=0;i<r.rank.size();i++){
//            //ans.add(rating.get(i));
//        }
    }
    private double f(double Ra,double Rb){
        return 1/(1+Math.pow(10, (Rb - Ra) / 400));
    }
    public void comp(){
        int k;
        int LEN=rating.size();
        if(LEN>maxlen) k=maxk;
        else if(LEN<minlen) k=mink;
        else k=(int)(1.0*LEN-minlen)/(maxlen-minlen)*(maxk-mink)+mink;
        for(int i=0;i<LEN;i++){
            double q=0;
            for(int j=0;j<LEN;j++){
                if(r.rank.get(i)>r.rank.get(j)){
                    q+=(0-f(rating.get(i),rating.get(j)));
                }
                if(r.rank.get(i)<r.rank.get(j)){
                    q+=(1-f(rating.get(i),rating.get(j)));
                }
            }
            q=k*q/LEN;
            //printf("%3d %5d->%11.5lf  ->%.0lf\n",i+1,a[i],q,a[i]+q);
            ans.add(((int)(rating.get(i)+q+0.5)));
        }
    }
    public String HTML() {
        TableHTML table = new TableHTML();
        table.setClass("table");
        table.addColname("#", "用户", "T", "当前值", "预测值", "变化量", "acb返还");
        Contest c = ContestMain.getContest(cid);
        for (int i = 0; i < rating.size(); i++) {
            User u = Main.users.getUser(r.username.get(i));
            RegisterUser registerUser = c.getRegisterUser(u.getUsername());
            List<String> row = new ArrayList<String>();
            row.add(r.rank.get(i) + "");
            row.add(u.getUsernameHTML());
            int pr = User.getShowRating(ratingnum.get(i), rating.get(i));
            if (pr == -100000) pr = 700;
            row.add(ratingnum.get(i) + "");
            if (ratingnum.get(i) != 0) row.add(User.ratingToHTML(pr));
            else row.add("-");
            int newRating = User.getShowRating(ratingnum.get(i) + 1, ans.get(i));
            row.add(User.ratingToHTML(newRating));
            int ratingChange = newRating - pr;
            if (ratingnum.get(i) == 0) {
                row.add("-");
            } else if (ratingChange >= 0) {
                row.add(HTML.textb("+" + ratingChange, "green"));
            } else {
                row.add(HTML.textb("" + ratingChange, "red"));
            }
            if (registerUser != null && registerUser.getStatu() == RegisterUser.STATUS_TEAM_AUTO_APPENDED) {
                int totalSubAcb = u.getSubAcbInWeekContest();
                int realSubAcb = ratingChange >= 0 ? 0 : Math.min(-ratingChange, totalSubAcb);
                row.add(totalSubAcb - realSubAcb + "/" + totalSubAcb);
            } else {
                row.add("");
            }
            table.addRow(row);
        }
        return HTML.panelnobody("Forecast Rating" + (ContestMain.getContest(cid).isComputerating() ? HTML.a("comprating.action?cid=" + cid, "save") : ""), table.HTML());
    }
    public void save(){
        new Thread(){
            @Override
            public void run() {
                Contest c= ContestMain.getContest(cid);
                for(int i=0;i<rating.size();i++){
                    RatingCase ratingCase=new RatingCase(r.username.get(i),c.getEndTime(),c.getCid(),rating.get(i),ans.get(i),ratingnum.get(i)+1,r.rank.get(i),"");
                    ratingSQL.save(ratingCase);
                    //返还acb
                    RegisterUser registerUser = c.getRegisterUser(ratingCase.getUsername());
                    User u = Main.users.getUser(ratingCase.getUsername());
                    if(registerUser.getStatu() == RegisterUser.STATUS_TEAM_AUTO_APPENDED){
                        int totalSubAcb = u.getSubAcbInWeekContest();
                        int ratingChange = ratingCase.getTrueRating() - ratingCase.getTruePRating();
                        int realSubAcb = ratingChange >= 0 ? 0 : Math.min(-ratingChange, totalSubAcb);
                        int addAcb = totalSubAcb - realSubAcb;
                        Main.users.addACB(u.getUsername(),addAcb, AcbOrderType.CONTEST_AUTO_REGISTER_END,"比赛编号："+cid);
                        MessageMain.addMessageReturnACB(c,u,ratingCase,addAcb);
                    }else{
                        MessageMain.addMessageRatingChange(ratingCase.getCid(), ratingCase.getUsername(), ratingCase.getTruePRating(), ratingCase.getTrueRating());
                    }
                    EventMain.triggerEvent(new EventRating(u,ratingCase.getRank(),rating.size()));
                    Tool.log(ratingCase.getUsername() + ":" + ratingCase.getRating());
                }
                Main.users.updateAllUserRank();
            }
        }.start();
    }
}
