package util.SQL;

import entity.IBeanResultSetCreate;
import util.FileLog;
import util.Main;
import util.Pair;
import util.Tool;

import java.sql.*;
import java.util.*;

/**
 * SQL工具类，在query后务必调用close()来归还连接池
 * Created by Syiml on 2015/7/10 0010.
 */
public class SQL {
    public static DBConnectionPool conns = new DBConnectionPool();
    protected ResultSet rs=null;
    private String sql;
    private Object[] args;
    private PreparedStatement p=null;
    private Connection conn;
    private boolean log=true;
    public SQL(String sql, Object... args){
        this.sql=sql;
        this.args=args;
        conn= conns.getConn();
    }
    public SQL(String sql, List<Object> args){
        this.sql = sql;
        this.args = args.toArray();
        conn = conns.getConn();
    }
    public void addArgs(Object... args){
        Object[] a3 = new Object[this.args.length + args.length];
        System.arraycopy(this.args, 0, a3, 0, this.args.length);
        System.arraycopy(args, 0, a3, this.args.length, args.length);
        this.args = a3;
    }
    public SQL setLog(boolean log){
        this.log=log;
        return this;
    }
    private ResultSet doQuery() throws SQLException {
        Timestamp t = Tool.now();
        ResultSet rs = p.executeQuery();
        String sql =p.toString();
        //Tool.debug("{"+(Tool.now().getTime() - t.getTime()) +"}"+ sql.substring(sql.indexOf(':')+1) , SQL.class.getName());
        Tool.SQLDebug(Tool.now().getTime() - t.getTime(),sql.substring(sql.indexOf(':')+1));
        return rs;
    }
    public ResultSet query(){
        try {
            p = conn.prepareStatement(sql);
            for(int i=0;i<args.length;i++){
                p.setObject(i+1,args[i]);
            }
            return rs=doQuery();
        } catch (SQLException e) {
            if(log)Tool.log(e);
        }
        return null;
    }
    public ResultSet query(int from,int num){
        try {
            p=conn.prepareStatement(sql+" LIMIT ?,?");
            int i;
            for(i=0;i<args.length;i++){
                p.setObject(i+1,args[i]);
            }
            p.setObject(i+1,from);
            p.setObject(i+2,num);
            return doQuery();
        } catch (SQLException e) {
            if(log)Tool.log(e);
        }
        return null;
    }

    /**
     * 使用queryList、queryMap、querySet、queryPairList方法时，要重写本方法来正确获取指定类型的数据
     * @param i 第i个参数
     * @return 返回指定类型的数据
     * @throws SQLException
     */
    protected Object getObject(int i) throws SQLException {
//        Main.log("必须重写SQL类的getObject方法");
        return rs.getObject(i);
    }
    public <K,V> Map<K,V> queryMap(){
        Map<K,V> ret=new TreeMap<K, V>();
        try {
            p=conn.prepareStatement(sql);
            int i;
            for(i=0;i<args.length;i++){
                p.setObject(i+1,args[i]);
            }
            rs=doQuery();
            while(rs.next()){
                Object key=getObject(1);
                Object value=getObject(2);
                ret.put((K)key,(V)value);
            }
        } catch (SQLException e) {
            if(log)Tool.log(e);
        } finally {
            close();
        }
        return ret;
    }
    public <K,V> List<Pair<K,V>> queryPairList(){
        List<Pair<K,V>> ret=new ArrayList<Pair<K,V>>();
        try {
            p=conn.prepareStatement(sql);
            int i;
            for(i=0;i<args.length;i++){
                p.setObject(i+1,args[i]);
            }
            rs=doQuery();
            while(rs.next()){
                Object key=getObject(1);
                Object value=getObject(2);
                ret.add(new Pair<K, V>((K)key,(V)value));
            }
        } catch (SQLException e) {
            if(log)Tool.log(e);
        } finally {
            close();
        }
        return ret;
    }
    public <T> void queryCollection(Collection c){
        try {
            p=conn.prepareStatement(sql);
            int i;
            for(i=0;i<args.length;i++){
                p.setObject(i+1,args[i]);
            }
            rs=doQuery();
            while(rs.next()){
                c.add((T)getObject(1));
            }
        } catch (SQLException e) {
            if(log)Tool.log(e);
        } finally {
            close();
        }
    }
    public <T> List<T> queryList(){
        List<T> ret=new ArrayList<T>();
        queryCollection(ret);
        return ret;
    }
    public <T> Set<T> querySet(){
        Set<T> ret=new TreeSet<T>();
        queryCollection(ret);
        return ret;
    }
    public <T> T queryObj(){
        rs=query();
        try {
            if(rs != null && rs.next()){
                return (T)rs.getObject(1);
            }else return null;
        } catch (SQLException e) {
            if(log)Tool.log(e);
        } finally {
            close();
        }
        return null;
    }

    /**
     * 通过sql语句查询出一个浮点数
     * @return 查询结果
     */
    public Float queryFloat(){
        rs=query();
        try {
            if(rs != null && rs.next()){
                return rs.getFloat(1);
            }else return 0f;
        } catch (SQLException e) {
            if(log)Tool.log(e);
        } finally {
            close();
        }
        return 0f;
    }
    /**
     * 通过sql语句查询出一个整数
     * @return 查询结果
     */
    public int queryNum(){
        rs=query();
        try {
            if(rs != null && rs.next()){
                return rs.getInt(1);
            }else return 0;
        } catch (SQLException e) {
            if(log)Tool.log(e);
        } finally {
            close();
        }
        return 0;
    }
    public String queryString(){
        rs=query();
        try {
            if(rs != null && rs.next()){
                return rs.getString(1);
            }else return "";
        } catch (SQLException e) {
            if(log)Tool.log(e);
        } finally {
            close();
        }
        return "";
    }

    public <T extends IBeanResultSetCreate> T queryBean(Class<T> cls){
        rs=query();
        T ret=null;
        try {
            if(rs != null && rs.next()){
                ret=cls.newInstance();
                ret.init(rs);
            }
        } catch (Exception e) {
            if(log) Tool.log(e);
        }  finally {
            close();
        }
        return ret;
    }
    public <T extends IBeanResultSetCreate> List<T> queryBeanList(Class<T> cls){
        List<T> list=new ArrayList<T>();
        rs=query();
        try {
            while(rs.next()){
                T aBean=cls.newInstance();
                aBean.init(rs);
                list.add(aBean);
            }
        } catch (Exception e) {
            if(log)Tool.log(e);
        }finally {
            close();
        }
        return list;
    }
    public int update(){
        return update(true);
    }

    public int isnertGetLastInsertId(){
        p=null;
        try {
            p= conn.prepareStatement(sql);
            for(int i=0;i<args.length;i++){
                p.setObject(i+1,args[i]);
            }
            String sql =p.toString();
            Timestamp t = Tool.now();
            int ret = p.executeUpdate();
            Tool.SQLDebug((Tool.now().getTime() - t.getTime())," "+ sql.substring(sql.indexOf(':')+1));
            if(ret <= 0) return ret;
            sql = "SELECT LAST_INSERT_ID()";
            p= conn.prepareStatement(sql);
            rs = p.executeQuery();
            if (rs != null && rs.next()) {
                return rs.getInt(1);
            } else return -1;
        } catch (SQLException e) {
            if(log)Tool.log(e);
            return -1;
        } finally {
            close();
        }
    }
    public int update(boolean _log){
        p=null;
        try {
            p= conn.prepareStatement(sql);
            for(int i=0;i<args.length;i++){
                p.setObject(i+1,args[i]);
            }
            String sql =p.toString();
            Timestamp t = Tool.now();
            int ret = p.executeUpdate();
            String sqlString = sql.substring(sql.indexOf(':')+1);
            Tool.SQLDebug((Tool.now().getTime() - t.getTime())," "+ sqlString);
            FileLog.updateSqlLog(Tool.now()+": "+sqlString);
            return ret;
        } catch (SQLException e) {
            if(Main.config.isDebug) e.printStackTrace();
            if(log&&_log)Tool.log(e);
            return -1;
        } finally {
            close();
        }
    }
    public void updateOneThread(){
        Main.sqlUpdateThread.putSql(this);
    }
    public void close(){
        pClose();
        cClose();
        conns.putCondition(conn);
    }

    private void cClose(){
        try {
            if(rs!=null)rs.close();
        } catch (SQLException ignored) {
            if(log)Tool.log(ignored);
        }
    }
    private void pClose(){
        try {
            if(p!=null) p.close();
        } catch (SQLException ignored) {
            if(log)Tool.log(ignored);
        }
    }
}
