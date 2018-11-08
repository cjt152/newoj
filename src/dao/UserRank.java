package dao;

import entity.IBeanResultSetCreate;
import util.SQL.SQL;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

class UseRankNode implements IBeanResultSetCreate {
    public String username;
    public int rating;
    public int acnum;
    @Override
    public void init(ResultSet rs) throws SQLException {
        rs.getInt("");
    }
    String getKey(){
        return username;
    }
}

/**
 * 排行榜
 * Created by QAQ on 2018/10/28.
 */
public class UserRank {
    public static int max_rank_number = 1000;
    public Map<String,UseRankNode> map;
    public List<UseRankNode> list;

    /**
     * Read From DB
     */
    public void Init()
    {
        list = new SQL("").queryBeanList(UseRankNode.class);
        for(UseRankNode node: list){

        }
    }
}
