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
import java.util.LinkedList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ethan
 */
public class Database {

    public static String basePath;
    public Connection conn;

    public Database() {
        basePath = new File("").getAbsolutePath();
        conn = connect();
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

        Statement stmt;
        try {
            stmt = conn.createStatement();
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void createSongFiles() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException ex) {
            System.out.println("Exception (line 68): " + ex.getMessage());
        }

        // SQLite connection string
        String url = "jdbc:sqlite:" + basePath.replace("\\", "/") + "/music.db";

        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS song_files (\n"
                + "	file_name varchar PRIMARY KEY,\n"
                + "	number_of_plays int DEFAULT 0,\n"
                + "	datetime_updated datetime DEFAULT NULL,\n"
                + "	rating int DEFAULT NULL"
                + ");";
        Statement stmt;
        try {
            stmt = conn.createStatement();
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateBaseDirectory(String directory) {
        // check if a base dir was set. If not, set one. Otherwise, update the existing base directory.
        String sql = "SELECT COUNT(*) AS `count` FROM base_dir";

        Statement stmt;
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

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

    public void closeConnection(Connection conn) {
        try {
            conn.close();
        } catch (SQLException ex) {
            System.out.println("Error closing the connection.");
        }
    }

    public boolean updateSong(String oldFileName, String fileName, int rating) {
        if (rating == -1) { // N/A rating - remove it from DB
            String sql = "DELETE FROM song_files WHERE file_name = ?";

            PreparedStatement pstmt;
            try {
                pstmt = conn.prepareStatement(sql);

                // set the corresponding param
                pstmt.setString(1, oldFileName);
                // execute the delete statement
                pstmt.executeUpdate();
                return true;

            } catch (SQLException e) {
                System.out.println("Error 166: " + e.getMessage());
            }
            return false;
        } else {

            // check if a base dir was set. If not, set one. Otherwise, update the existing base directory.
            String sql = "SELECT COUNT(*) AS `count` FROM song_files WHERE file_name = ?";

            PreparedStatement pstmt;
            try {
                pstmt = conn.prepareStatement(sql);

                try {
                    pstmt.setString(1, oldFileName);
                } catch (SQLException ex) {
                    System.out.println("Error 164: " + ex.getMessage());
                }
                ResultSet rs;
                try {
                    rs = pstmt.executeQuery();
                    // loop through the result set
                    while (rs.next()) {
                        if (rs.getInt("count") > 0) { // update pre-existing base directory
                            String sqlUpdate = "UPDATE song_files SET file_name = ?, datetime_updated = date('now'), rating = ? WHERE file_name = ?";

                            PreparedStatement pstmt2 = conn.prepareStatement(sqlUpdate);
                            pstmt2.setString(1, fileName);
                            pstmt2.setInt(2, rating);
                            pstmt2.setString(3, oldFileName);

                            pstmt2.executeUpdate();
                            return true;

                        } else { // make a brand new base directory
                            String sqlInsert = "INSERT INTO song_files (file_name, datetime_updated, rating, number_of_plays) VALUES (?,date('now'), ?, 0)";

                            PreparedStatement pstmt2 = conn.prepareStatement(sqlInsert);
                            pstmt2.setString(1, oldFileName);
                            pstmt2.setInt(2, rating);

                            pstmt2.executeUpdate();
                            return true;

                        }
                    }
                } catch (SQLException ex) {
                    System.out.println("Error 190: " + ex.getMessage());
                }
            } catch (SQLException ex) {
                System.out.println("Error 193: " + ex.getMessage());
            }
            return false;
        }
    }

    public boolean resetSongRatings(boolean resetForAllDirectories) {
        if (resetForAllDirectories) {
            String sql = "DELETE FROM song_files";

            PreparedStatement pstmt;
            try {
                pstmt = conn.prepareStatement(sql);

                // execute the delete statement
                pstmt.executeUpdate();
                return true;

            } catch (SQLException e) {
                System.out.println("Error 244: " + e.getMessage());
            }
        } else {  // only reset the ratings for the current directory
            String sql = "DELETE FROM song_files WHERE file_name LIKE ?";

            PreparedStatement pstmt;
            try {
                pstmt = conn.prepareStatement(sql);

                // set the corresponding param
                pstmt.setString(1, getBaseDirectory() + "%");
                // execute the delete statement
                pstmt.executeUpdate();
                return true;

            } catch (SQLException e) {
                System.out.println("Error 244: " + e.getMessage());
            }
        }
        return false;
    }

    /*
    doesTableExist
    Checks if a table exists.
     */
    public boolean doesTableExist(String tableName) {

        String sql = "SELECT name FROM sqlite_master WHERE type='table' AND name = ?";

        PreparedStatement pstmt;
        try {
            pstmt = conn.prepareStatement(sql);

            try {
                pstmt.setString(1, tableName);
            } catch (SQLException ex) {
                System.out.println("Error 231: " + ex.getMessage());
            }
            ResultSet rs;
            try {
                rs = pstmt.executeQuery();

                while (rs.next()) {
                    System.out.println();
                }
            } catch (SQLException ex) {
                System.out.println("Error 241: " + ex.getMessage());
            }
        } catch (SQLException ex) {
            System.out.println("Error 244: " + ex.getMessage());
        }

        return false;
    }

    public String getBaseDirectory() {
        String directory = null;

        // check if a base dir was set. If not, set one. Otherwise, update the existing base directory.
        String sql = "SELECT directory FROM base_dir";

        Statement stmt;
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                return rs.getString("directory");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return directory;
    }

    /*
    getSongFiles
    Gets all the files saved to the database for a particular directory.
     */
    public LinkedList<Song> getSongFiles(String directory) {

        // check if a base dir was set. If not, set one. Otherwise, update the existing base directory.
        String sql = "SELECT * FROM song_files WHERE file_name LIKE ?";

        PreparedStatement pstmt;
        LinkedList<Song> songs = new LinkedList<Song>();
        try {
            pstmt = conn.prepareStatement(sql);

            try {
                pstmt.setString(1, directory + "%");
            } catch (SQLException ex) {
                System.out.println("Error 160: " + ex.getMessage());
            }
            ResultSet rs;
            try {
                rs = pstmt.executeQuery();

                while (rs.next()) {
                    Song s = new Song(rs.getString("file_name"), rs.getInt("number_of_plays"), rs.getInt("rating"));
                    songs.add(s);
                }
            } catch (SQLException ex) {
                System.out.println("Error 170: " + ex.getMessage());
            }
        } catch (SQLException ex) {
            System.out.println("Error 173: " + ex.getMessage());
            if (ex.getMessage().contains("no such table: song_files")) {
                // build the song_files table
                createSongFiles();
            }
        }

        return songs;
    }
}
