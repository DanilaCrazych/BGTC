package com.example.bgtc;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class MysqlConnect {
    public ObservableList<AutoPark> list = FXCollections.observableArrayList();
    static Connection conn;
    public static Connection ConnectBd() {
        HelloController hc = new HelloController();
        String url = hc.url;
        String user = hc.user;
        String password = hc.password;
        try {
            conn = DriverManager.getConnection(url, user, password);
//            System.out.println("Подключение к базе данных успешно установлено!");
        } catch (SQLException e) {
            System.out.println("Ошибка при подключении к базе данных:");
        }
        return null;
    }

    public void dataAutoPark() {
        ConnectBd();
        String query = "SELECT * FROM AutoPark";
        int id = 0;
        String auto = "";
        String grz = "";
        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                list.add(new AutoPark(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3)));
            }
        } catch (Exception ex) {
            System.out.println("Connection failed...");
            System.out.println(ex);
        }
    }
}
