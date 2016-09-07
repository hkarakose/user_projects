package com.kodfarki.ml;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import org.apache.mahout.cf.taste.impl.model.jdbc.MySQLJDBCDataModel;
import org.apache.mahout.cf.taste.model.DataModel;

/**
 * User: Halil Karakose
 * Date: 10/07/16
 * Time: 19:35
 */
public class Chap0602Mysql {
    public static void main(String[] args) {
        MysqlDataSource dbsource = new MysqlDataSource();
        dbsource.setUser("root");
        dbsource.setPassword("root");
        dbsource.setServerName("localhost");
        dbsource.setDatabaseName("scotchbox");

        DataModel dataModel =
                new MySQLJDBCDataModel(dbsource, "taste_preferences",
                "user_id", "item_id", "preference", "timestamp");
    }
}
