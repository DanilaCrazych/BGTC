package com.example.bgtc;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class MysqlConnect {
    public ObservableList<AutoPark> listA = FXCollections.observableArrayList();
    public ObservableList<Orders> listO = FXCollections.observableArrayList();
    public ObservableList<Orders> listOC = FXCollections.observableArrayList();
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
        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                listA.add(new AutoPark(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3)));
            }
        } catch (Exception ex) {
            System.out.println("Connection failed...");
            System.out.println(ex);
        }
    }

    public void dataOrders() {
        ConnectBd();
        String query = "SELECT * FROM Orders";
        int id;
        String fio;
        String adressot;
        String adressto;
        String phonenum;
        String status;
        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                listO.add(new Orders(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3),
                        resultSet.getString(4), resultSet.getString(5), resultSet.getString(6)));
            }
        } catch (Exception ex) {
            System.out.println("Connection failed...");
            System.out.println(ex);
        }
    }
}
