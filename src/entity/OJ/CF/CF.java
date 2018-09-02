package entity.OJ.CF;

import entity.OJ.CodeLanguage;
import util.Main;
import entity.OJ.OTHOJ;
import util.Pair;
import util.Tool;
import util.Vjudge.VjSubmitter;
import entity.RES;
import entity.Result;
import util.HTML.problemHTML;
import util.MyClient;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static entity.OJ.CodeLanguage.PYTHON3;

/**
 * Created by Syiml on 2015/10/9 0009.
 */
public class CF extends OTHOJ {
    private static String URL=Main.GV.getJSONObject("cf").getString("URL");

    @Override
    public String getRid(String user,VjSubmitter s) {
        Document doc;
        try {
            doc = Jsoup.connect(URL+"/submissions/"+user).get();
        } catch (IOException e) {
            Tool.log(e);
            return "error";
        }
        Element e=doc.select(".status-frame-datatable tr").get(1);
        if(e!=null)
        {
            Element cell = e.select("td").get(0);
            return cell.text();
        }
        return "error";
    }

    @Override
    public problemHTML getProblemHTML(String pid) {
        Document doc;
        problemHTML p=new problemHTML();
        try {
            doc = Jsoup.connect(getProblemURL(pid)).get();
        } catch (IOException e) {
            return null;
        }
//        Elements es=doc.select("img");
//        for(Element e:es){
//            String link=e.attr("src");
//            e.attr("src",URL+"/"+link);
//        }
        Elements e=doc.select(".problem-statement");
//        e.attr("abs:href");
        p.setTitle(e.select(".title").get(0).text().substring(3));
        p.setDis(e.select(".problem-statement>div:nth-child(2)").html());
        e.select(".input-specification").select(".section-title").remove();
        p.setInput(e.select(".input-specification").html());
        e.select(".output-specification").select(".section-title").remove();
        p.setOutput(e.select(".output-specification").html());

        //Elements sampleoutput=e.select(".sample-test").select(".output");
        //Elements sampleinput=e.select(".sample-test").select(".input");

        Elements sample=e.select(".sample-test").get(0).children();

        for(int i=0;i<sample.size();i+=2){
            String in="<pre class='sample'>"+sample.get(i).select("pre").html()+"</pre>";
            String out="<pre class='sample'>"+sample.get(i+1).select("pre").html()+"</pre>";
            if(i==sample.size()-2){
                Elements note=e.select(".note");
                if(note!=null&&note.size()!=0){
                    out=out.concat("<pre>"+note.html()+"</pre>");
                }
            }
            p.addSample(in,out);
        }
        p.setInt64("%I64d");
        e.select(".time-limit .property-title").remove();
        String s=e.select(".time-limit").html();
        p.setTimeLimit((int)(Double.parseDouble(s.substring(0,s.indexOf("second")-1))*1000)+"MS");
        e.select(".memory-limit .property-title").remove();
        s=e.select(".memory-limit").text();
        p.setMenoryLimit(s.substring(0,s.indexOf("megabytes")-1)+"MB");
        return p;
    }

    @Override
    public String getTitle(String pid) {
        Document doc;
        try {
            doc = Jsoup.connect(getProblemURL(pid)).get();
        } catch (IOException e) {
            return GET_TITLE_ERROR;
        }
        return doc.select(".problem-statement").select(".title").get(0).text().substring(3);
    }

    private String get_csrf_token(MyClient hc,int z){
        if(z==1) return hc.get(URL + "/enter").select("#enterForm").select("input").get(0).attr("value");
        else return hc.get(URL + "/problemset/submit").select(".submit-form").select("input").get(0).attr("value");
    }
    private int login(VjSubmitter s, MyClient hc){
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("csrf_token",get_csrf_token(hc,1)));
        formparams.add(new BasicNameValuePair("action","enter"));
        formparams.add(new BasicNameValuePair("handleOrEmail",s.getUsername()));
        formparams.add(new BasicNameValuePair("password",s.getPassword()));
        formparams.add(new BasicNameValuePair("remember","1"));
        formparams.add(new BasicNameValuePair("_tta","451"));
        String ret = hc.Post(URL+"/enter",formparams);
        if(ret == null ) return 0;
        return 1;
    }

    /**
     * 由于CF会判断本次代码是否重复提交，因此添加一段时间的注释
     * @return 返回一段注释代码，里面包含了当前时间
     */
    String getRandomCode(CodeLanguage language){
        switch (language){
            case PYTHON3:
                return "#" + Tool.now();
            case PASCAL:
            case GPP:
            case GPP11:
            case CSHARP:
            case GO1_8:
            case JAVA:
            case JS:
                return "//" + Tool.now();
            case GCC:
            case GCC11:
            default:
                return "/*"+ Tool.now()+"*/";
        }
    }
    @Override
    public String submit(VjSubmitter s) {
        MyClient hc=new MyClient();
        //login
        login(s,hc);
        //submit
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        String csrf_token=get_csrf_token(hc,0);
        formparams.add(new BasicNameValuePair("csrf_token",csrf_token));
        formparams.add(new BasicNameValuePair("action","submitSolutionFormSubmitted"));
        formparams.add(new BasicNameValuePair("submittedProblemCode",s.getSubmitInfo().pid));
        formparams.add(new BasicNameValuePair("programTypeId",getTrueLanguage(s.getSubmitInfo().language,s.getSubmitInfo().pid)+""));
        formparams.add(new BasicNameValuePair("source",s.getSubmitInfo().code+ getRandomCode(getLanguage(s.getSubmitInfo().language,s.getSubmitInfo().pid))));
        if(hc.Post(URL+"/problemset/submit?csrf_token="+csrf_token, formparams)!=null) return "success";
        return "error";
    }
    protected Result getRes(String s){
        if(s.equals("Accepted")) return Result.AC;
        if(s.contains("Wrong answer")) return Result.WA;
        if(s.contains("Time limit exceeded")) return Result.TLE;
        if(s.contains("Runtime error")) return Result.RE;
        if(s.contains("Memory limit exceeded")) return Result.MLE;
        if(s.contains("Compilation error")) return Result.CE;
        if(s.contains("Idleness limit exceeded")) return Result.RE;
        if(s.contains("Skipped")) return Result.ERROR;
        if(s.contains("In queue")) return Result.PENDING;
        if(s.contains("Running")) return Result.RUNNING;
        return Result.ERROR;
    }
    @Override
    public RES getResult(VjSubmitter s) {
        RES res=new RES();
        res.setR(Result.PENDING);
        Document doc;
        try {
            doc = Jsoup.connect(URL+"/submissions/"+s.getUsername()).get();
        } catch (IOException e) {
            return res;
        }
        Element row=doc.select(".status-frame-datatable tr").get(1);
        if(row==null){
            return res;
        }
        res.setR(getRes(row.select(".status-verdict-cell").text()));
        if(res.getR()==Result.AC){
            res.setTime(row.select(".time-consumed-cell").text().replace(" ms", "MS"));
            res.setMemory(row.select(".memory-consumed-cell").text().replace(" KB", "KB"));
        }
        if(res.getR()==Result.CE){
            res.setCEInfo("无信息");
        }
        return res;
    }

    @Override
    public String getProblemURL(String pid) {
        String pid2=pid.substring(pid.length()-1);
        String pid1=pid.substring(0,pid.length()-1);
        return URL+"/problemset/problem/"+pid1+"/"+pid2;
    }

    @Override
    public String getName() {
        return "CF";
    }

    @Override
    public String get64IO(String pid) {
        return "%I64d";
    }

    private static List<Pair<Integer,CodeLanguage>> languageList;
    static{
        languageList = new ArrayList<>();
        languageList.add(new Pair<>(1,CodeLanguage.GPP));
        languageList.add(new Pair<>(42,CodeLanguage.GPP11));
        languageList.add(new Pair<>(10,CodeLanguage.GCC));
        languageList.add(new Pair<>(43,CodeLanguage.GCC11));
        //languageList.add(new Pair<>(2,CodeLanguage.VCPP));
        languageList.add(new Pair<>(4,CodeLanguage.PASCAL));
        languageList.add(new Pair<>(9,CodeLanguage.CSHARP));
        languageList.add(new Pair<>(32,CodeLanguage.GO1_8));
        languageList.add(new Pair<>(36,CodeLanguage.JAVA));
        languageList.add(new Pair<>(31, PYTHON3));
        languageList.add(new Pair<>(34,CodeLanguage.JS));
    }
    @Override
    public List<Pair<Integer, CodeLanguage>> getLanguageList(String pid) {
        return languageList;
    }
}
