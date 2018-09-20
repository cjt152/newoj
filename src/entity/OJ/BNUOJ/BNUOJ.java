package entity.OJ.BNUOJ;

import entity.OJ.CodeLanguage;
import entity.OJ.OTHOJ;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import util.Main;
import util.Pair;
import util.Tool;
import util.Vjudge.VjSubmitter;
import entity.RES;
import entity.Result;
import util.MyClient;
import util.HTML.problemHTML;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/6/9.
 */
public class BNUOJ extends OTHOJ {
    private static String URL= Main.GV.getJSONObject("bnuoj").getString("URL");
    private static String submitURL="//ajax/problem_submit.php";//ajax/problem_submit.php
    private static String problemURL="//problem_show.php";
    private static String TitleSelect="h2";
    private static String DesSelect=".content-wrapper";
    private static String loginURL="//ajax/login.php";
    private static MyClient hc = MyClient.getMyClient();

    private static Map<String,Result> resultMap;
    //private static Map<String,String>  languageMap;
    private static List<Pair<Integer,CodeLanguage>> languageList;
    static{
        resultMap = new HashMap<String, Result>();

        resultMap.put("Waiting",Result.PENDING);
        resultMap.put("Judging",Result.PENDING);
        resultMap.put("Accepted",Result.AC);
        resultMap.put("Wrong Answer",Result.WA);
        resultMap.put("Runtime Error",Result.RE);
        resultMap.put("Time Limit Exceed",Result.TLE);
        resultMap.put("Memory Limit Exceed",Result.MLE);
        resultMap.put("Output Limit Exceed",Result.OLE);
        resultMap.put("Presentation Error",Result.PE);
        resultMap.put("Compile Error",Result.CE);
        resultMap.put("Restricted Function",Result.DANGER);

        /*languageMap=new HashMap<String,String>();
        languageMap.put("0","1");//C++
        languageMap.put("1","2");//C
        languageMap.put("2","3");//JAVA*/

        languageList = new ArrayList<>();
        languageList.add(new Pair<>(1,CodeLanguage.GPP));
        languageList.add(new Pair<>(2,CodeLanguage.GCC));
        languageList.add(new Pair<>(3,CodeLanguage.JAVA));
    }

    private static String getSubmitURL(){
        return URL+submitURL;
    }

    private static String getLoginURL() {
        return URL+loginURL;
    }

    public String getName(){
        return "BNUOJ";
    }

    @Override
    public String get64IO(String pid) {
        return getProblemHTML(pid).getInt64();
    }

    @Override
    public List<Pair<Integer, CodeLanguage>> getLanguageList(String pid) {
        return languageList;
    }

    public String getProblemURL(String pid){ return URL+problemURL+"?pid="+pid; }
    private Result getResultMap(String v){
        return resultMap.get(v);
    }

    public String getRid(String user,VjSubmitter s){
        Document d = MyClient.getMyClient().get(URL + "/v3/ajax/status_data.php?sEcho=27&iDisplayLength=1&bSearchable_0=true&iDisplayStart=0&sSearch_0=" + user);
        if(d==null){
            return "error";
        }
        JSONObject jo=JSONObject.fromObject(d.select("body").html());
        JSONArray aadata=jo.getJSONArray("aaData");
        if(aadata.size()==0){
            return "new";
        }else{
            return aadata.getJSONArray(0).getString(1);
        }
    }
    public String getTitle(String pid){
        Document doc;
        //doc = Jsoup.connect(getProblemURL(pid)).get();
        doc = MyClient.getMyClient().get(getProblemURL(pid));
        return doc.select(TitleSelect).get(0).text();
    }
    public problemHTML getProblemHTML(String pid){
        Document doc;
        problemHTML p=new problemHTML();
        doc = MyClient.getMyClient().get(getProblemURL(pid));
        if(doc == null) return null;
        Elements es=doc.select("img");
        for(Element e:es){
            String link=e.attr("src");
            e.attr("src",URL+"/"+link);
        }
        Elements e=doc.select("#showproblem");
        e.attr("abs:href");
        p.setTitle(e.select(TitleSelect).get(0).text());
        p.setDis(e.select(DesSelect).get(0).html());
        p.setInput(e.select(DesSelect).get(1).html());
        p.setOutput(e.select(DesSelect).get(2).html());
        String ss=e.toString();
        if (ss.contains("<span class=\"badge badge-info\">CodeForces</span>")){
            Elements e3=e.select(".sample-test pre");
            for(int i=1;i<e3.size();i+=2){
                p.addSample("<pre class='sample'>"
                        +e3.get(i-1).html()+"</pre>",
                        "<pre class='sample'>"
                        +e3.get(i).html()+"</pre>");
            }
        }else{
            Elements e2 = e.select("pre");
            int k=0;
            String in="",out;
            for(int i=0;i<e2.size();i++){
                if(e2.get(i).hasClass("content-wrapper")){
                    k++;
                    if(k%2==1){
                        in="<pre class='sample'>"
                                + e2.get(i).html() + "</pre>";
                    }else{
                        out="<pre class='sample'>"
                                + e2.get(i).html() + "</pre>";
                        p.addSample(in,out);
                    }
                }
            }
        }
        p.setInt64(doc.select(".badge-inverse").get(0).text());
        String s=doc.select(".span6").get(0).text();
        p.setTimeLimit(s.substring(11, s.length()));
        s=doc.select(".span6").get(1).text();
        p.setMenoryLimit(s.substring(14,s.length()));
        return p;
    }
    public String submit(VjSubmitter s){
        String ret=Login(s);
        if(ret.equals("error")) return "error";
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("problem_id",""+s.getSubmitInfo().pid));
        formparams.add(new BasicNameValuePair("language",getTrueLanguage(s.getSubmitInfo().language,s.getSubmitInfo().pid)+""));
        formparams.add(new BasicNameValuePair("source",s.getSubmitInfo().code));
        formparams.add(new BasicNameValuePair("isshare","0"));//share code
        formparams.add(new BasicNameValuePair("user_id",s.getUsername()));
        formparams.add(new BasicNameValuePair("login","Submit"));
        if(hc.Post(getSubmitURL(), formparams)==null) return "error";
        return "success";
    }
    public RES getResult(VjSubmitter s) {
        RES r=new RES();
        JSONObject jo=JSONObject.fromObject(MyClient.getMyClient().get(URL + "/v3/ajax/status_data.php?sEcho=27&iDisplayLength=1&bSearchable_0=true&iDisplayStart=0&sSearch_0=" + s.getUsername()).select("body").html());
        JSONArray aadata=jo.getJSONArray("aaData");
        if(aadata.size()==0){
            r.setR(Result.PENDING);
            return r;
        }else{
            JSONArray data=aadata.getJSONArray(0);
            r.setR(getResultMap(data.getString(3)));
            if(r.canReturn()){
                r.setTime(data.getString(5).replaceAll(" ms","MS"));
                r.setMemory(data.getString(6).replaceAll(" KB","KB"));
            }
            if(r.getR()==Result.CE){
                r.setCEInfo(getCEInfo(s));
            }
            return r;
        }
    }
    public String Login(VjSubmitter s){
        //登录
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("username",s.getUsername()));
        formparams.add(new BasicNameValuePair("password",s.getPassword()));
        formparams.add(new BasicNameValuePair("cksave","365"));//cookie save
        formparams.add(new BasicNameValuePair("login","Login"));
        if(hc.Post(getLoginURL(), formparams)==null) return "error";
        return "success";
    }
    public String getCEInfo(VjSubmitter s){
        try{
            JSONObject jo=JSONObject.fromObject(MyClient.getMyClient().get(URL + "/v3/ajax/get_ceinfo.php?runid=" + s.getOjsrid()).select("body").html());
            return jo.getString("msg");
        }catch (JSONException e){
            return "未获取到";
        }
    }
}
