package com.jackgharris.uber_eats_console.core;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Database {

    private static Database instance;
    private String driver;
    private String host;
    private int port;
    private String name;
    private String user;
    private String password;
    private String file;


    public Database(String driver, List<String> connectionDetails){

        this.driver = driver;

        switch (driver) {
            case "mysql" -> {

                this.host = connectionDetails.get(0);
                this.port = Integer.parseInt(connectionDetails.get(1));
                this.name = connectionDetails.get(2);
                this.user = connectionDetails.get(3);
                this.password = connectionDetails.get(4);
                this.file = "";

            } case "sqlite" -> {

                this.host = "";
                this.port = 0;
                this.name = null;
                this.user = "";
                this.password = "";
                this.file = connectionDetails.get(0);

            }
        }

    }

    public static List<HashMap<String, String>> query(String query){

        //check if the driver class is loaded
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");

        }catch (ClassNotFoundException exception){
            exception.printStackTrace();
        }

        List<HashMap<String, String>> data =  new ArrayList<>();

        //check for SQL connection error
        try{

            String url = "";
            Connection con = null;

            switch (Database.instance.driver) {
                case "mysql" -> {
                    url = Database.instance.host+":"+Database.instance.port+"/"+Database.instance.name;
                    con = DriverManager.getConnection("jdbc:"+Database.instance.driver+"://"+url,Database.instance.user,Database.instance.password);
                }
                case "sqlite" -> {
                    url = Database.instance.file;
                    con = DriverManager.getConnection("jdbc:"+Database.instance.driver+"://"+Database.instance.file);
                }
            }

            Statement statement = con.createStatement();
            ResultSet result = statement.executeQuery(query);

            int columns  = result.getMetaData().getColumnCount();


            while(result.next()){
                HashMap<String, String> entry = new HashMap<String, String>();

                int i = 1;
                while(i <= columns){
                    entry.put(result.getMetaData().getColumnLabel(i),result.getString(i));
                    i++;
                }

                data.add(entry);
            }

        }catch (SQLException exception){
            exception.printStackTrace();
        }

        return data;

    }

    public static void boot(String driver, List<String> connectionDetails){
        Database.instance = new Database(driver, connectionDetails);
    }

    public static Database instance(){
        return Database.instance;
    }

}
