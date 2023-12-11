package com.example.bgtc;

import javafx.animation.FadeTransition;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
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
    String ipAddress;
    String url = "jdbc:mysql://192.168.0.179:3306/BGTC";
    //    String url = "jdbc:mysql://IP_ADDRESS:3306/BGTC";
    String user = "Danilas";
    String password = "p@ssw0rd";
    public Connection connection;

    @FXML
    private Label ErrorLogin, Date, AdministrirovanieLabel, AutoPark, StatusCreate, ExitAutoPark, AddAuto, UpdateTable, OrdersLabel, OrderAdd, ChangeLabel;
    @FXML
    private TextField AuthLoginField, AuthPassField, CreateUserLogin, CreateUserMail, CreateUserPass, AutoAdd, GRZAdd, FioAdd, AdressOtAdd, AdressToAdd, PhoneNumAdd;
    @FXML
    private Pane Auth, LeftPanel, AdminUserAdd, AutoParkPane, OrdersPane;
    @FXML
    private Button LoginButton, CreateUser;
    @FXML
    private ComboBox idCombo;
    @FXML
    private CheckBox StatusCheck;

    @FXML
    private TableView<AutoPark> TableAutoPark;
    @FXML
    private TableColumn<AutoPark, String> GRZCol;
    @FXML
    private TableColumn<AutoPark, Integer> IdCol;
    @FXML
    private TableColumn<AutoPark, String> AutoCol;

    @FXML
    private TableView<Orders> TableOrdrs;
    @FXML
    private TableColumn<Orders, Integer> idColZ;
    @FXML
    private TableColumn<Orders, String> fioColZ;
    @FXML
    private TableColumn<Orders, String> adresStartColZ;
    @FXML
    private TableColumn<Orders, String> adresFinishColZ;
    @FXML
    private TableColumn<Orders, String> numPhoneColZ;
    @FXML
    private TableColumn<Orders, String> statusColZ;

    ObservableList<AutoPark> listA;
    ObservableList<Orders> listO;

    boolean statusAutoAdd = false;
    boolean statusOrderAdd = false;
    boolean StatusOrderChange = false;
    Alert alert = new Alert(Alert.AlertType.INFORMATION);

    @FXML
    protected void Login() throws InterruptedException {
//        String query = "SELECT login, password FROM users";
        String query = "SELECT login, password FROM users WHERE login LIKE '"+AuthLoginField.getText()+"'";
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
        TableAutoPark.setVisible(true);
        try {
            IdCol.setCellValueFactory(new PropertyValueFactory<AutoPark, Integer>("id"));
            AutoCol.setCellValueFactory(new PropertyValueFactory<AutoPark, String>("auto"));
            GRZCol.setCellValueFactory(new PropertyValueFactory<AutoPark, String>("grz"));

            listA = mysqlConnect.listA;
            TableAutoPark.setItems(listA);

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
    protected void Exit() {
        LeftPanel.setVisible(true);
        AdminUserAdd.setVisible(false);
        AutoParkPane.setVisible(false);
        OrdersPane.setVisible(false);
        TableAutoPark.getItems().clear();
        TableOrdrs.getItems().clear();

        AutoAdd.clear();
        GRZAdd.clear();
        AutoAdd.setDisable(true);
        GRZAdd.setDisable(true);

        FioAdd.setDisable(true);
        AdressOtAdd.setDisable(true);
        AdressToAdd.setDisable(true);
        PhoneNumAdd.setDisable(true);
        idCombo.setDisable(true);
        FioAdd.clear();
        AdressOtAdd.clear();
        AdressToAdd.clear();
        PhoneNumAdd.clear();
        StatusCheck.setSelected(false);

        statusAutoAdd = false;
        statusOrderAdd = false;
    }

    @FXML
    protected void AddAutoA() {

        if (statusAutoAdd == false) {
            AutoAdd.setDisable(false);
            GRZAdd.setDisable(false);
            statusAutoAdd = true;
        } else {
            if (AutoAdd.getText().equals("") | GRZAdd.getText().equals("")) {
                alert.setTitle("Ошибка");
                alert.setHeaderText(null);
                alert.setContentText("Заполните пустые поля!");
                alert.showAndWait();
            } else {
                String autoadd = AutoAdd.getText();
                String grzadd = GRZAdd.getText();
                String query = "INSERT INTO `BGTC`.`AutoPark` (`auto`, `grz`) VALUES ('" + autoadd + "', '" + grzadd + "');";

                try {
                    Statement statement = connection.createStatement();
                    statement.executeUpdate(query);
                    alert.setTitle("Информация");
                    alert.setHeaderText(null);
                    alert.setContentText("Траснпорт добавлен!");
                    alert.showAndWait();
                } catch (Exception ex) {
                    System.out.println("Connection failed...");
                    System.out.println(ex);
                    StatusCreate.setText("Ошибка!");
                }
            }
        }
    }

    @FXML
    protected void Orders() {
        HelloApplication ha = new HelloApplication();
        OrdersPane.setVisible(true);
        LeftPanel.setVisible(false);
        AdminUserAdd.setVisible(false);
        AutoParkPane.setVisible(false);
        mysqlConnect.dataOrders();

        try {
            idColZ.setCellValueFactory(new PropertyValueFactory<Orders, Integer>("id"));
            fioColZ.setCellValueFactory(new PropertyValueFactory<Orders, String>("fio"));
            adresStartColZ.setCellValueFactory(new PropertyValueFactory<Orders, String>("adressot"));
            adresFinishColZ.setCellValueFactory(new PropertyValueFactory<Orders, String>("adressto"));
            numPhoneColZ.setCellValueFactory(new PropertyValueFactory<Orders, String>("phonenum"));
            statusColZ.setCellValueFactory(new PropertyValueFactory<Orders, String>("status"));

            listO = mysqlConnect.listO;
            TableOrdrs.setItems(listO);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        for (int i = 0; i < listO.size(); i++){
            idCombo.getItems().addAll(listO.get(i).id);
        }

    }

    @FXML
    protected void OrderAdd() {
        if (statusOrderAdd==false){
            FioAdd.setDisable(false);
            AdressOtAdd.setDisable(false);
            AdressToAdd.setDisable(false);
            PhoneNumAdd.setDisable(false);
            statusOrderAdd = true;
        } else if (FioAdd.equals("")|AdressOtAdd.equals("")|AdressToAdd.equals("")|PhoneNumAdd.equals("")) {
            alert.setTitle("Ошибка");
            alert.setHeaderText(null);
            alert.setContentText("Заполните пустые поля!");
            alert.showAndWait();
        } else {
            String FioAddOrder = FioAdd.getText();
            String Adressot = AdressOtAdd.getText();
            String Adressto = AdressToAdd.getText();
            String Phonenum = PhoneNumAdd.getText();
            String query = "INSERT INTO `BGTC`.`Orders` (`fio`, `adressot`,`adressto`, `phonenum`, `status` ) VALUES ('" + FioAddOrder + "', '" + Adressot + "', '"
                    + Adressto + "', '" + Phonenum + "', 'Выполняется');";
            try {
                Statement statement = connection.createStatement();
                statement.executeUpdate(query);
                alert.setTitle("Информация");
                alert.setHeaderText(null);
                alert.setContentText("Заказ создан");
                alert.showAndWait();
            } catch (Exception ex) {
                System.out.println("Connection failed...");
                System.out.println(ex);
                StatusCreate.setText("Ошибка!");
            }

        }
    }

    @FXML
    public void OrdersChange(){
        FioAdd.setDisable(false);
        AdressOtAdd.setDisable(false);
        AdressToAdd.setDisable(false);
        PhoneNumAdd.setDisable(false);
        idCombo.setDisable(false);
        String FioA = FioAdd.getText();
        String AdressOtA = AdressOtAdd.getText();
        String AdressToA = AdressToAdd.getText();
        String PhoneNumA = PhoneNumAdd.getText();
        String StatusA = "";
        if (StatusCheck.isSelected()){
            StatusA = "Выполнен";
        }else {
            StatusA = "Выполняется";
        }
        if (StatusOrderChange==true) {
            String query = "UPDATE `BGTC`.`Orders` SET `fio` = '"+FioA+"', `adressot` = '"+AdressOtA+"', `adressto` = '"+AdressToA+"', `phonenum` = '"+PhoneNumA+"', `status` = '"+StatusA+"' WHERE (`id` = '"+idCombo.getValue()+"')";

            try {
                Statement statement = connection.createStatement();
                statement.executeUpdate(query);
                alert.setTitle("Информация");
                alert.setHeaderText(null);
                alert.setContentText("Данные обновлены");
                alert.showAndWait();
            } catch (Exception ex) {
                System.out.println("Connection failed...");
                System.out.println(ex);
            }
        }
        StatusOrderChange = true;
    }


    @FXML
    public void IdComboChange(){
       for (int i = 0; i<listO.size(); i++){
           if(idCombo.getValue().equals(listO.get(i).id)){
               FioAdd.setText(listO.get(i).fio);
               AdressOtAdd.setText(listO.get(i).adressot);
               AdressToAdd.setText(listO.get(i).adressto);
               PhoneNumAdd.setText(listO.get(i).phonenum);
           }
       }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ConnectBd();
        FadeTransition leftpaneFade = new FadeTransition(Duration.seconds(0.001), LeftPanel);
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