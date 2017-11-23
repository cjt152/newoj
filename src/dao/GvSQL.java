package dao;

import com.fjutacm.common.sql.SQL;

/**
 * Created by QAQ on 2016/6/26 0026.
 */
public class GvSQL extends BaseCacheLRU<String,String>{
    public GvSQL() {
        super(100);
    }

    public String get(String key){
        return getBeanByKey(key);
    }
    public void set(String key,String value){
        set_catch(key,value);
        new SQL("REPLACE INTO t_gv VALUES(?,?)",key,value).update();
    }

    @Override
    protected String getByKeyFromSQL(String key) {
        return new SQL("SELECT value FROM t_gv WHERE `key`=?",key).queryString();
    }
}
