package PingTuDao;

import PingTuEntity.RankingEntry;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class HandleDB {

    private Connection conn;
    private PreparedStatement stat;
    private ResultSet rs;

    public ArrayList<RankingEntry> selectInfo() {
        ArrayList<RankingEntry> al = new ArrayList();

        String sql = "select * from ranking order by time asc limit 5";

        conn = DBUtil.getConn();
        try {
            stat = conn.prepareStatement(sql);
            rs = stat.executeQuery();
            while (rs.next()) {
                String player = rs.getString("Player");
                int time = rs.getInt("Time");

                RankingEntry rankingEntry = new RankingEntry(player, time);
                al.add(rankingEntry);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return al;
    }

    public void insertInfo(String name, int time) {
        conn = DBUtil.getConn();
        String sql = "select count(*) from ranking where time <" + time;

        try {
            stat = conn.prepareStatement(sql);
            rs = stat.executeQuery();
            if (rs.next()) {
                int n = rs.getInt(1);
                if (n < 5) {
                    sql = "insert into ranking " +
                            "(player, time) " +
                            "values('" + name + "'," + time + ")";
                    stat = conn.prepareStatement(sql);
                    stat.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
