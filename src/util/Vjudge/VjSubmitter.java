package util.Vjudge;

import entity.Status;
import util.HTML.HTML;
import util.Main;
import entity.OJ.OTHOJ;
import entity.RES;
import entity.Result;
import util.MyClient;
import util.Submitter;
import util.Tool;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 提交器 info 里面保存了各种提交信息
 * Created by Administrator on 2015/5/21.
 */
public class VjSubmitter implements Runnable{
    public static final int BUSY=1;
    public static final int IDLE=0;
    public LinkedList<String> showstatus;
    public String rid = "";
    public MyClient client = new MyClient(); //用这个网络客户端进行登录、提交等操作
    public SubmitInfo info;//正在处理的info
    int status;//忙碌状态与否
    int ojid;
    int submitterID;
    VJudge vj;

    private Thread selfThread = null;
    private String username;
    private String password;
    private String ojsrid;
    public VjSubmitter(int ojid, String us, String pas, int id, VJudge vj){
        this.ojid=ojid;
        username=us;
        password=pas;
        status=IDLE;
        submitterID=id;
        this.vj=vj;
        showstatus = new LinkedList<>();
        showstatus.addLast("Begin");
    }

    public SubmitInfo getSubmitInfo(){return info;}

    public int getOjid() {
        return ojid;
    }

    public int getID(){return submitterID;}

    public boolean isBusy(){
        return status==BUSY;
    }

    public String getUsername(){
        return username;
    }

    public String getPassword(){
        return password;
    }

    public String getOjsrid(){return ojsrid;}

    public void setShowstatus(String status){
        this.showstatus.addLast("["+Tool.now()+"]"+status);
        if(this.showstatus.size()>30){
            this.showstatus.removeFirst();
        }
    }

    public void run(){//开始执行线程
        while(true){
            try {
                //读取队列执行
                //System.out.println(submitterID+"IDLE");
                this.info=vj.queue.get(ojid).take();
                this.status=BUSY;
                Main.status.setStatusResult(info.rid,Result.JUDGING,"-","-","");
                go();
                ojsrid="->"+ojsrid;
                this.status=IDLE;
            } catch (Exception e) {
                this.status=IDLE;
                Tool.log(e);
                setShowstatus("出错，10秒后重新运行");
                Tool.sleep(10000);
            }
        }
    }

    public void go() {
        try{
            //System.out.println(submitterID+":submitter go");
            setShowstatus("开始执行，正在第1次获取原rid");
            OTHOJ oj;
            oj = Submitter.ojs[ojid];
            String prid;
            int z=0;
            do{
                z++;
                if(z>=10){
                    //System.out.println(submitterID+":getPrid Error");
                    Main.status.setStatusResult(info.rid, Result.ERROR, "-", "-", "ERROR:获取原rid失败。可能是连接不上原oj导致的错误");
                    this.status=IDLE;
                    setShowstatus("获取原rid出错");
                    return;
                }
                prid = oj.getRid(username,this);//获得原来的rid
                //Tool.log("prdid = " + prid);
                Tool.sleep(1000);
                setShowstatus("第"+z+"次获取的原rid="+prid);
            }while(prid.equals("error"));
            setShowstatus("第"+z+"次获取的原rid="+prid+",开始提交");
            String nrid;
            RES r ;
            int num = 0;
            int k=2;
            do{
                while (oj.submit(this).equals("error")) {
                    num++;
                    if (num >= 10) {
                        //System.out.println(submitterID+":doSubmit out time");
                        Main.status.setStatusResult(info.rid, Result.ERROR, "-", "-", "ERROR:提交失败。可能是连接不上原oj导致的错误");
                        this.status=IDLE;
                        setShowstatus("第"+num+"次的提交出错，评测出错");
                        return;
                    }else{
                        setShowstatus("第"+num+"次的提交出错，开始重试");
                    }
                    Tool.sleep(1000);
                }
                setShowstatus("提交结束，开始获取评测结果");
                num = 0;
                do {
                    Tool.sleep(1000);
                    nrid = oj.getRid(username,this);
                    //System.out.println(submitterID+":get rid "+num+"=" + nrid);
                    setShowstatus("第"+num+"次获取rid="+nrid);
                    num++;
                    if (num == 5) break;//提交失败重新提交
                } while (nrid.equals("error")||nrid.equals(prid));
                k--;
            }while(num==5&&k!=0);
            if(k==0){
                Main.status.setStatusResult(info.rid, Result.ERROR, "-", "-", "ERROR:提交失败。提交时错误，检查64位整数格式是否正确");
                setShowstatus("提交失败");
            }else{
                ojsrid = nrid;
                r = oj.getResultReturn(this);
                if(r != null){
                    Status s = Main.status.setStatusResult(info.rid, r.getR(),r.getTime(),r.getMemory(),r.getCEInfo(),r.getScore());
                    if(r.getR() != Result.ERROR)
                        Main.submitter.onSubmitDone(s);
                }
            }
            this.status=IDLE;
        }catch(Exception e){
            setShowstatus("未知错误");
            Tool.log(e);
            Main.status.setStatusResult(info.rid, Result.ERROR, "-", "-", "");
            this.status=IDLE;
        }
    }

    public List<String> row(){
        //submitterID,ojid,status,username,info.rid,info.pid,ojsrid,show
        List<String> row=new ArrayList<String>();
        row.add(HTML.a("admin.jsp?page=SubmitterInfo&id="+submitterID,submitterID+""));
        if(submitterID == -1) row.add("local");
        else row.add(Submitter.ojs[ojid].getName());
        row.add(selfThread.getId()+"");
        row.add(status==1?"正在评测":"空闲");
        row.add(username);
        row.add(info==null?"":info.rid+"");
        row.add(info==null?"":info.pid+"");
        row.add(ojsrid+"");
        row.add(showstatus.getLast());
        return row;
    }
    void setSelfThread(Thread selfThread) {
        this.selfThread = selfThread;
    }
}
