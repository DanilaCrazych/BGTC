package com.example.bgtc;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class HelloController implements Initializable {


    String url = "jdbc:mysql://192.168.0.179:3306/BGTC";
    //    String url = "jdbc:mysql://213.167.217.126:3306/BGTC";
    private String user = "Danilas";
    private String password = "p@ssw0rd";
    private Connection connection;

    @FXML
    private Label ErrorLogin;
    @FXML
    private TextField AuthLoginField, AuthPassField;
    @FXML
    private Pane Auth, LeftPanel, WorkPanel;
    @FXML
    private Button LoginButton;


   @FXML
    protected void login() throws InterruptedException {
       String query = "SELECT Login, Password FROM users";
       String loginAuh = "";
       String passAuh = "";

       try {
           Statement statement = connection.createStatement();
           ResultSet resultSet = statement.executeQuery("SELECT * FROM users");
           while (resultSet.next()) {
               int id = resultSet.getInt(1);
               loginAuh = resultSet.getString(2);
               passAuh = resultSet.getString(3);
           }
       } catch (Exception ex) {
           System.out.println("Connection failed...");
           System.out.println(ex);
       }
        if (AuthLoginField.getText().equals("")|AuthPassField.getText().equals("")){
            ErrorLogin.setVisible(true);
            ErrorLogin.setText("Введите логин и пароль!");
        }
        else if (AuthLoginField.getText().equals(loginAuh)&&AuthPassField.getText().equals(passAuh)){
            FadeTransition auth = new FadeTransition(Duration.seconds(1), Auth);
            FadeTransition leftpaneFade = new FadeTransition(Duration.seconds(1), LeftPanel);
            auth.setByValue(1.0);
            auth.setToValue(0);
            auth.play();

            leftpaneFade.setByValue(1.0);
            leftpaneFade.setToValue(0);
            leftpaneFade.play();

        }
        else{
            ErrorLogin.setText("Неверный логин или пароль!");
            ErrorLogin.setVisible(true);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Подключение к базе данных успешно установлено!");
        } catch (SQLException e) {
            System.out.println("Ошибка при подключении к базе данных:");
            printSQLException(e);
        }
    }

    public static void printSQLException(SQLException ex) {
        for (Throwable e : ex) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                System.err.println("SQLException: " + ((SQLException) e).getSQLState());
                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
    }
}