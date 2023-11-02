package com.example.bgtc;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.net.URL;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class HelloController implements Initializable {

    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
    Date date = new Date(System.currentTimeMillis());
    String url = "jdbc:mysql://192.168.0.179:3306/BGTC";
    //    String url = "jdbc:mysql://213.167.217.126:3306/BGTC";
    private String user = "Danilas";
    private String password = "p@ssw0rd";
    private Connection connection;

    @FXML
    private Label ErrorLogin, Date, AdministrirovanieLabel, StatusCreate;
    @FXML
    private TextField AuthLoginField, AuthPassField, CreateUserLogin, CreateUserMail, CreateUserPass;
    @FXML
    private Pane Auth, LeftPanel, AdminUserAdd;
    @FXML
    private Button LoginButton, CreateUser;



    @FXML
    protected void Login() throws InterruptedException {
        String query = "SELECT login, password FROM users";
        String loginAuth = "";
        String passAuth = "";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                loginAuth = resultSet.getString(1);
                passAuth = resultSet.getString(2);
            }
        } catch (Exception ex) {
            System.out.println("Connection failed...");
            System.out.println(ex);
        }
        if (AuthLoginField.getText().equals("") | AuthPassField.getText().equals("")) {
            ErrorLogin.setVisible(true);
            ErrorLogin.setText("Введите логин и пароль!");
        } else if (AuthLoginField.getText().equals(loginAuth) && AuthPassField.getText().equals(passAuth)) {
            FadeTransition auth = new FadeTransition(Duration.seconds(1), Auth);
            FadeTransition leftpaneFade = new FadeTransition(Duration.seconds(1), LeftPanel);
            auth.setByValue(1.0);
            auth.setToValue(0);
            auth.play();

            leftpaneFade.setByValue(0);
            leftpaneFade.setToValue(1.0);
            leftpaneFade.play();
            Date.setText("Дата: " + formatter.format(date));
        } else {
            ErrorLogin.setText("Неверный логин или пароль!");
            ErrorLogin.setVisible(true);
        }
    }

    @FXML
    protected  void AdministrirovanieLabel() {
        AdminUserAdd.setVisible(true);
    }
    @FXML
    protected  void CreateUser() {
        String LoginCreate = CreateUserLogin.getText();
        String MailCreate = CreateUserMail.getText();
        String PassCreate = CreateUserPass.getText();
        String query = "INSERT INTO `BGTC`.`users` (`login`, `password`, `mail`) VALUES ('"+LoginCreate+"', '"+PassCreate+"', '"+MailCreate+"');";

        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
            StatusCreate.setText("Пользователь создан!");
        } catch (Exception ex) {
            System.out.println("Connection failed...");
            System.out.println(ex);
            StatusCreate.setText("Ошибка!");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        FadeTransition leftpaneFade = new FadeTransition(Duration.seconds(1), LeftPanel);
        leftpaneFade.setByValue(1.0);
        leftpaneFade.setToValue(0);
        leftpaneFade.play();
        Date.setText("Дата: " + formatter.format(date));
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