/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package playmusic;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ethan
 */
public class Database {

    public static String basePath;

    public Database() {
        basePath = new File("").getAbsolutePath();
    }

    public Connection connect() {
        Connection conn = null;
        try {

            String pathToDB = basePath.replace("\\", "/") + "/music.db";
            File file = new File(pathToDB);
            if (!file.exists()) {
                // need to create the database
                createNewDatabase("music.db");
            }

            String url = "jdbc:sqlite:" + pathToDB;

            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public void createNewDatabase(String fileName) {
        String url = "jdbc:sqlite:" + basePath.replace("\\", "/") + "/" + fileName;

        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");

                // create the tables
                createBaseDirTable();
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void createBaseDirTable() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException ex) {
            System.out.println("Exception (line 68): " + ex.getMessage());
        }

        // SQLite connection string
        String url = "jdbc:sqlite:" + basePath.replace("\\", "/") + "/music.db";

        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS base_dir (\n"
                + "	directory varchar PRIMARY KEY,\n"
                + "	datetime_chosen datetime DEFAULT NULL"
                + ");";

        try (Connection conn = DriverManager.getConnection(url);
                Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateBaseDirectory(String directory) {
        // check if a base dir was set. If not, set one. Otherwise, update the existing base directory.
        String sql = "SELECT COUNT(*) AS `count` FROM base_dir";

        try (Connection conn = this.connect();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            // loop through the result set
            while (rs.next()) {
                if (rs.getInt("count") > 0) { // update pre-existing base directory
                    String sqlUpdate = "UPDATE base_dir SET directory = ?, datetime_chosen = date('now')";

                    PreparedStatement pstmt = conn.prepareStatement(sqlUpdate);
                    pstmt.setString(1, directory);
                    pstmt.executeUpdate();
                } else { // make a brand new base directory
                    String sqlInsert = "INSERT INTO base_dir (directory, datetime_chosen) VALUES (?,date('now'))";

                    PreparedStatement pstmt = conn.prepareStatement(sqlInsert);
                    pstmt.setString(1, directory);
                    pstmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public String getBaseDirectory() {
        String directory = null;

        // check if a base dir was set. If not, set one. Otherwise, update the existing base directory.
        String sql = "SELECT directory FROM base_dir";

        try (Connection conn = this.connect();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                return rs.getString("directory");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return directory;
    }

    public void insertIntoDb(String table, Map<String, String> data) {

    }
}
