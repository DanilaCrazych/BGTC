package com.example.bgtc;

import javafx.animation.FadeTransition;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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
    MysqlConnect mysqlConnect = new MysqlConnect();
    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
    Date date = new Date(System.currentTimeMillis());
    String url = "jdbc:mysql://192.168.0.179:3306/BGTC";
    //   public String url = "jdbc:mysql://IP_ADDR:3306/BGTC";
    String user = "Danilas";
    String password = "p@ssw0rd";
    public Connection connection;

    @FXML
    private Label ErrorLogin, Date, AdministrirovanieLabel, AutoPark, StatusCreate, ExitAutoPark;
    @FXML
    private TextField AuthLoginField, AuthPassField, CreateUserLogin, CreateUserMail, CreateUserPass;
    @FXML
    private Pane Auth, LeftPanel, AdminUserAdd, AutoParkPane;
    @FXML
    private Button LoginButton, CreateUser;
    @FXML
    private TableColumn<AutoPark, String> GRZCol;
    @FXML
    private TableColumn<AutoPark, Integer> IdCol;
    @FXML
    private TableColumn<AutoPark, String> AutoCol;
    @FXML
    private TableView<AutoPark> TableAutoPark;
    ObservableList<AutoPark> listM;

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
    protected void AdministrirovanieLabel() {
        TableAutoPark.setVisible(false);
        AdminUserAdd.setVisible(true);
    }

    @FXML
    protected void AutoParkPane() {
        mysqlConnect.dataAutoPark();
        LeftPanel.setVisible(false);
        AdminUserAdd.setVisible(false);
        AutoParkPane.setVisible(true);
        try {
            IdCol.setCellValueFactory(new PropertyValueFactory<AutoPark, Integer>("id"));
            AutoCol.setCellValueFactory(new PropertyValueFactory<AutoPark, String>("auto"));
            GRZCol.setCellValueFactory(new PropertyValueFactory<AutoPark, String>("grz"));

            listM = mysqlConnect.list;
            TableAutoPark.setItems(listM);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void CreateUser() {
        String LoginCreate = CreateUserLogin.getText();
        String MailCreate = CreateUserMail.getText();
        String PassCreate = CreateUserPass.getText();
        String query = "INSERT INTO `BGTC`.`users` (`login`, `password`, `mail`) VALUES ('" + LoginCreate + "', '" + PassCreate + "', '" + MailCreate + "');";

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

    @FXML
    protected void Exit(){
        LeftPanel.setVisible(true);
        AdminUserAdd.setVisible(false);
        AutoParkPane.setVisible(false);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ConnectBd();
        FadeTransition leftpaneFade = new FadeTransition(Duration.seconds(1), LeftPanel);
        leftpaneFade.setByValue(1.0);
        leftpaneFade.setToValue(0);
        leftpaneFade.play();
        Date.setText("Дата: " + formatter.format(date));
    }

    public void ConnectBd() {
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