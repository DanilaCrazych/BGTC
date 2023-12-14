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
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

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
    private Label ErrorLogin, Date, AdministrirovanieLabel, AutoPark, StatusCreate, ExitAutoPark, AddAuto, UpdateTable, OrdersLabel, OrderAdd, ChangeLabel, ChangeLabel1, DelOrders, Print, UpdateBut, UpdateBut1;
    @FXML
    private TextField AuthLoginField, AuthPassField, CreateUserLogin, CreateUserMail, CreateUserPass, AutoAdd, GRZAdd, FioAdd, AdressOtAdd, PhoneNumAdd, PrintName,FioSotrudnikiAdd;
    @FXML
    private Pane Auth, LeftPanel, AdminUserAdd, AutoParkPane,SotrudnikiPane, OrdersPane;
    @FXML
    private Button LoginButton, CreateUser;
    @FXML
    private ComboBox idCombo, idCombo1,AutoCombo,GRZCombo,idCombo11, Driver;
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

    @FXML
    private TableView<Sotrudniki> TableSotrudniki;
    @FXML
    private TableColumn<Sotrudniki, String> AutoCol1;
    @FXML
    private TableColumn<Sotrudniki, String> FioSotrudniki;
    @FXML
    private TableColumn<Sotrudniki, Integer> IdCol1;
    @FXML
    private TableColumn<Sotrudniki, String> GRZCol1;

    ObservableList<AutoPark> listA;
    ObservableList<Orders> listO;
    ObservableList<Sotrudniki> listS;
    ArrayList<Integer> listDelPark;

    boolean statusAutoAdd = false;
    boolean statusOrderAdd = false;
    boolean StatusOrderChange = false;
    boolean StatusAutoChange = false;
    boolean StatusAutoDel = false;
    boolean StatusOrderDel = false;
    boolean StatusPrint = false;
    boolean SotrudnikiStatusAdd = false;
    boolean SotrudnikiStatusChange = false;
    boolean SotrudnikiStatusDel = false;
    Alert alert = new Alert(Alert.AlertType.INFORMATION);


    //Логин
    @FXML
    protected void Login() throws InterruptedException {
        String query = "SELECT login, password FROM users WHERE login LIKE '" + AuthLoginField.getText() + "'";
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
            if (AuthLoginField.getText().equals("asd")|AuthLoginField.getText().equals("admin")){
                AdministrirovanieLabel.setVisible(true);
            }else {
                AdministrirovanieLabel.setVisible(false);
            }
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

    //Кнопка "Администрирование"
    @FXML
    protected void AdministrirovanieLabel() {
        SotrudnikiPane.setVisible(false);
        AutoParkPane.setVisible(false);
        OrdersPane.setVisible(false);
        AdminUserAdd.setVisible(true);

    }

    //Создание нового пользователя
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

    //Выход
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


        idCombo1.setDisable(true);

        idCombo.getItems().clear();
        idCombo1.getItems().clear();

    }

    //Форма заказов
    @FXML
    protected void Orders() {
        HelloApplication ha = new HelloApplication();
        TableOrdrs.getItems().clear();
        mysqlConnect.dataSotrudniki();
//        listS= mysqlConnect.listS;

        FioAdd.setDisable(true);
        AdressOtAdd.setDisable(true);
        Driver.setDisable(true);
        PhoneNumAdd.setDisable(true);
        idCombo.setDisable(true);
        FioAdd.clear();
        AdressOtAdd.clear();

        PhoneNumAdd.clear();
        StatusCheck.setSelected(false);
        StatusPrint = false;
        statusOrderAdd = false;
        StatusOrderDel = false;
        StatusOrderChange = false;
        idCombo1.setDisable(true);

        OrdersPane.setVisible(true);
        AdminUserAdd.setVisible(false);
        AutoParkPane.setVisible(false);
        SotrudnikiPane.setVisible(false);
        mysqlConnect.dataOrders();


        try {
            idColZ.setCellValueFactory(new PropertyValueFactory<Orders, Integer>("id"));
            fioColZ.setCellValueFactory(new PropertyValueFactory<Orders, String>("fio"));
            adresStartColZ.setCellValueFactory(new PropertyValueFactory<Orders, String>("adressot"));
            adresFinishColZ.setCellValueFactory(new PropertyValueFactory<Orders, String>("adressto"));
            numPhoneColZ.setCellValueFactory(new PropertyValueFactory<Orders, String>("phonenum"));
            statusColZ.setCellValueFactory(new PropertyValueFactory<Orders, String>("status"));

//            listO = mysqlConnect.listO;
            TableOrdrs.setItems(listO);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        idCombo.getItems().clear();
        for (int i = 0; i < listO.size(); i++) {
            idCombo.getItems().addAll(listO.get(i).id);
        }
        idCombo.setPromptText("id");
        Driver.getItems().clear();
        for (int i = 0; i < listS.size(); i++) {
            Driver.getItems().addAll(listS.get(i).fio);
        }

    }
    //Создание заказа
    @FXML
    protected void OrderAdd() {
        if (statusOrderAdd == false) {
            FioAdd.setDisable(false);
            Driver.setDisable(false);
            AdressOtAdd.setDisable(false);
            PhoneNumAdd.setDisable(false);
            mysqlConnect.dataSotrudniki();
//            listS= mysqlConnect.listS;
            statusOrderAdd = true;
        } else if (FioAdd.equals("") | AdressOtAdd.equals("") | Driver.getValue().toString().equals("") | PhoneNumAdd.equals("")) {
            alert.setTitle("Ошибка");
            alert.setHeaderText(null);
            alert.setContentText("Заполните пустые поля!");
            alert.showAndWait();
        } else {
            String FioAddOrder = FioAdd.getText();
            String Adressot = AdressOtAdd.getText();
            String Phonenum = PhoneNumAdd.getText();
            String query = "INSERT INTO `BGTC`.`Orders` (`fio`, `adressot`,`adressto`, `phonenum`, `status` ) VALUES ('" + FioAddOrder + "', '" + Adressot + "', '"
                    + Driver.getValue().toString() + "', '" + Phonenum + "', 'Выполняется');";
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
    //Изменение заказа
    @FXML
    public void OrdersChange() {

        FioAdd.setDisable(false);
        Driver.setDisable(false);
        AdressOtAdd.setDisable(false);
        PhoneNumAdd.setDisable(false);
        idCombo.setDisable(false);
//        Driver.getItems().clear();
//        for (int i = 0; i < listS.size(); i++) {
//            Driver.getItems().addAll(listS.get(i).fio);
//        }

        StatusCheck.setDisable(false);
        String FioA = FioAdd.getText();
        String AdressOtA = AdressOtAdd.getText();
        String PhoneNumA = PhoneNumAdd.getText();
        String StatusA = "";
        if (StatusCheck.isSelected()) {
            StatusA = "Выполнен";
        } else {
            StatusA = "Выполняется";
        }
        if (StatusOrderChange == true) {
            String query = "UPDATE `BGTC`.`Orders` SET `fio` = '" + FioA + "', `adressot` = '" + AdressOtA + "', `adressto` = '" + Driver.getValue().toString() + "', `phonenum` = '" + PhoneNumA + "', `status` = '" + StatusA + "' WHERE (`id` = '" + idCombo.getValue() + "')";
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
    //Удаление заказа
    @FXML
    public void DelOrder(){
        idCombo.setDisable(false);
        if (StatusOrderDel == true) {
            String query = "DELETE FROM `BGTC`.`Orders` WHERE (`id` = '" + idCombo.getValue() + "');";
            List<Object> IdComboInt = new ArrayList<Object>();
            IdComboInt.add(idCombo.getValue());
            int idComboBox = Integer.parseInt(IdComboInt.get(0).toString());
            try {
                try {
                    Statement statement = connection.createStatement();
                    statement.executeUpdate(query);

                } catch (Exception ex) {
                    System.out.println("Connection failed...");
                    System.out.println(ex);
                }
                try {
                    int Increment = listO.size();
                    Statement statement = connection.createStatement();
                    statement.executeUpdate("ALTER TABLE Orders AUTO_INCREMENT = " + Increment + "");
                } catch (Exception ex) {
                    System.out.println("Connection failed...");
                    System.out.println(ex);
                }
            } catch (Exception exception) {
                System.out.println("Connection failed...");
                System.out.println(exception);
            }
        }
        StatusOrderDel = true;
        alert.setTitle("Информация");
        alert.setHeaderText(null);
        alert.setContentText("Данные удалены");
        alert.showAndWait();
    }
    //Обновить таблицу
    @FXML
    public void UpdateOrders(){
        TableOrdrs.getItems().clear();
        mysqlConnect.dataOrders();
        try {
            idColZ.setCellValueFactory(new PropertyValueFactory<Orders, Integer>("id"));
            fioColZ.setCellValueFactory(new PropertyValueFactory<Orders, String>("fio"));
            adresStartColZ.setCellValueFactory(new PropertyValueFactory<Orders, String>("adressot"));
            adresFinishColZ.setCellValueFactory(new PropertyValueFactory<Orders, String>("adressto"));
            numPhoneColZ.setCellValueFactory(new PropertyValueFactory<Orders, String>("phonenum"));
            statusColZ.setCellValueFactory(new PropertyValueFactory<Orders, String>("status"));

//            listO = mysqlConnect.listO;
            TableOrdrs.setItems(listO);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
    //Вывод Таблицы автопарка
    @FXML
    protected void AutoParkPane() {
        statusAutoAdd = false;
        StatusAutoChange = false;
        StatusAutoDel = false;


        AdminUserAdd.setVisible(false);
        OrdersPane.setVisible(false);
        AutoParkPane.setVisible(true);
        SotrudnikiPane.setVisible(false);
        TableAutoPark.getItems().clear();
        mysqlConnect.dataAutoPark();

        try {
            IdCol.setCellValueFactory(new PropertyValueFactory<AutoPark, Integer>("id"));
            AutoCol.setCellValueFactory(new PropertyValueFactory<AutoPark, String>("auto"));
            GRZCol.setCellValueFactory(new PropertyValueFactory<AutoPark, String>("grz"));

//            listA = mysqlConnect.listA;
            TableAutoPark.setItems(listA);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        idCombo1.getItems().clear();
        for (int i = 0; i < listA.size(); i++) {
            idCombo1.getItems().addAll(listA.get(i).id);
        }
        idCombo1.setPromptText("id");

    }
    //Добавление Авто
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
    //Изменение авто
    @FXML
    public void AutoChange() {
        AutoAdd.setDisable(false);
        GRZAdd.setDisable(false);
        idCombo1.setDisable(false);


        String AutoA = AutoAdd.getText();
        String GRZA = GRZAdd.getText();

        if (StatusOrderChange == true) {
            String query = "UPDATE `BGTC`.`AutoPark` SET `auto` = '" + AutoA + "', `grz` = '" + GRZA + "' WHERE (`id` = '" + idCombo1.getValue() + "');";
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
    //Удалить авто
    @FXML
    public void DeleteAuto() {
        idCombo1.setDisable(false);
        if (StatusAutoDel == true) {
            String query = "DELETE FROM `BGTC`.`AutoPark` WHERE (`id` = '" + idCombo1.getValue() + "');";
            List<Object> IdComboInt = new ArrayList<Object>();
            IdComboInt.add(idCombo1.getValue());
            int idComboBox = Integer.parseInt(IdComboInt.get(0).toString());
            try {
                try {
                    Statement statement = connection.createStatement();
                    statement.executeUpdate(query);
                    alert.setTitle("Информация");
                    alert.setHeaderText(null);
                    alert.setContentText("Данные удалены");
                    alert.showAndWait();
                } catch (Exception ex) {
                    System.out.println("Connection failed...");
                    System.out.println(ex);
                }

                try {
                    int Increment = listA.size();
                    Statement statement = connection.createStatement();
                    statement.executeUpdate("ALTER TABLE AutoPark AUTO_INCREMENT = " + Increment + "");
                } catch (Exception ex) {
                    System.out.println("Connection failed...");
                    System.out.println(ex);
                }
            } catch (Exception exception) {
                System.out.println("Connection failed...");
                System.out.println(exception);
            }
        }
        StatusAutoDel = true;
    }
    //Обновить авто
    @FXML
    public void UpdateAuto(){
        TableAutoPark.getItems().clear();
        mysqlConnect.dataAutoPark();
        try {
            IdCol.setCellValueFactory(new PropertyValueFactory<AutoPark, Integer>("id"));
            AutoCol.setCellValueFactory(new PropertyValueFactory<AutoPark, String>("auto"));
            GRZCol.setCellValueFactory(new PropertyValueFactory<AutoPark, String>("grz"));

//            listA = mysqlConnect.listA;
            TableAutoPark.setItems(listA);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void SotrudnikiPane(){

        AdminUserAdd.setVisible(false);
        OrdersPane.setVisible(false);
        AutoParkPane.setVisible(false);
        SotrudnikiPane.setVisible(true);

        idCombo11.setDisable(true);
        FioSotrudnikiAdd.setDisable(true);
        AutoCombo.setDisable(true);
        GRZCombo.setDisable(true);
        idCombo11.getItems().clear();
        FioSotrudnikiAdd.clear();
        AutoCombo.getItems().clear();
        GRZCombo.getItems().clear();
        SotrudnikiCombo();

        try {
            TableSotrudniki.getItems().clear();
            IdCol1.setCellValueFactory(new PropertyValueFactory<Sotrudniki, Integer>("id"));
            FioSotrudniki.setCellValueFactory(new PropertyValueFactory<Sotrudniki, String>("fio"));
            AutoCol1.setCellValueFactory(new PropertyValueFactory<Sotrudniki, String>("auto"));
            GRZCol1.setCellValueFactory(new PropertyValueFactory<Sotrudniki, String>("grz"));
            mysqlConnect.dataSotrudniki();
            TableSotrudniki.setItems(listS);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
    @FXML
    public void SotrudnikiAdd(){

        if (SotrudnikiStatusAdd==false){
            FioSotrudnikiAdd.setDisable(false);
            AutoCombo.setDisable(false);
            GRZCombo.setDisable(false);
            SotrudnikiStatusAdd=true;
        } else if (FioSotrudnikiAdd.getText().equals("")) {
            alert.setTitle("Ошибка");
            alert.setHeaderText(null);
            alert.setContentText("Заполните пустые поля!");
            alert.showAndWait();
        } else {
            String FioAdd = FioSotrudnikiAdd.getText();
            String autoAdd = AutoCombo.getValue().toString();
            String grzAdd = GRZCombo.getValue().toString();
            String query = "INSERT INTO `BGTC`.`Sotrudniki` (`fio`, `auto`, `grz`) VALUES ('"+FioAdd+"', '"+autoAdd+"', '"+grzAdd+"');\n";
            try {
                Statement statement = connection.createStatement();
                statement.executeUpdate(query);
                alert.setTitle("Информация");
                alert.setHeaderText(null);
                alert.setContentText("Сотрудник добавлен!");
                alert.showAndWait();
            } catch (Exception ex) {
                System.out.println("Connection failed...");
                System.out.println(ex);
                StatusCreate.setText("Ошибка!");
            }
        }
    }

    @FXML
    public void SotrudnikiChange(){
        idCombo11.setDisable(false);
        FioSotrudnikiAdd.setDisable(false);
        AutoCombo.setDisable(false);
        GRZCombo.setDisable(false);
        if (StatusOrderChange == true) {
            String FioChange = FioSotrudnikiAdd.getText();
            String AutoChange = AutoCombo.getValue().toString();
            String GrzChange = GRZCombo.getValue().toString();
            String query = "UPDATE `BGTC`.`Sotrudniki` SET `fio` = '"+FioChange+"', `auto` = '"+AutoChange+"', `grz` = '"+GrzChange+"' WHERE (`id` = '"+ idCombo11.getValue()+"');\n";
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
        StatusOrderChange=true;
    }

    @FXML
    public void SotrudnikiDel(){
        idCombo11.setDisable(false);
        if (SotrudnikiStatusDel == true) {
            String query = "DELETE FROM `BGTC`.`Sotrudniki` WHERE (`id` = '" + idCombo11.getValue() + "');";
            List<Object> IdComboInt = new ArrayList<Object>();
            IdComboInt.add(idCombo11.getValue());
            int idComboBox = Integer.parseInt(IdComboInt.get(0).toString());
            try {
                try {
                    Statement statement = connection.createStatement();
                    statement.executeUpdate(query);
                    alert.setTitle("Информация");
                    alert.setHeaderText(null);
                    alert.setContentText("Данные удалены");
                    alert.showAndWait();
                } catch (Exception ex) {
                    System.out.println("Connection failed...");
                    System.out.println(ex);
                }

                try {
                    int Increment = listS.size();
                    Statement statement = connection.createStatement();
                    statement.executeUpdate("ALTER TABLE AutoPark AUTO_INCREMENT = " + Increment + "");
                } catch (Exception ex) {
                    System.out.println("Connection failed...");
                    System.out.println(ex);
                }
            } catch (Exception exception) {
                System.out.println("Connection failed...");
                System.out.println(exception);
            }
        }
        SotrudnikiStatusDel = true;
    }

    //Добавление в ComboBox
    @FXML
    public void IdComboChange() {
        try {
            for (int i = 0; i < listO.size(); i++) {
                if (idCombo.getValue().equals(listO.get(i).id)) {
                    FioAdd.setText(listO.get(i).fio);
                    AdressOtAdd.setText(listO.get(i).adressot);
                    PhoneNumAdd.setText(listO.get(i).phonenum);
                }
            }
        } catch (Exception e) {
            System.out.println("Ненужная надпись :)");
            throw new RuntimeException(e);
        }
    }
    @FXML
    public void IdComboChangePark() {
        for (int i = 0; i < listA.size(); i++) {
            if (idCombo1.getValue().equals(listA.get(i).id)) {
                AutoAdd.setText(listA.get(i).auto);
                GRZAdd.setText(listA.get(i).grz);
            }
        }
    }

    @FXML
    public void IdCombiChangeSotr(){
        for (int i = 0; i < listS.size(); i++) {
            if (idCombo11.getValue().equals(listS.get(i).id)) {
                FioSotrudnikiAdd.setText(listS.get(i).fio);
            }
            AutoGrzSet();
        }
    }

    public void AutoGrzSet(){
        mysqlConnect.dataAutoPark();
//        listA = mysqlConnect.listA;
        for (int i = 0; i < listA.size(); i++) {
            AutoCombo.getItems().addAll(listA.get(i).auto);
            GRZCombo.getItems().addAll(listA.get(i).grz);
        }
    }
    @FXML
    public void SotrudnikiCombo(){
        mysqlConnect.dataAutoPark();
//        listA = mysqlConnect.listA;
        mysqlConnect.dataSotrudniki();
//        listS= mysqlConnect.listS;
        for (int i = 0; i < listS.size(); i++) {
            idCombo11.getItems().addAll(listS.get(i).id);
            AutoCombo.getItems().addAll(listA.get(i).auto);
            GRZCombo.getItems().addAll(listA.get(i).grz);
        }


    }
    //Вывод на печать
    @FXML
    public void CreateExcel() {
            if (StatusPrint == true) {
                String printName = PrintName.getText();
                try {
//                    listO = mysqlConnect.listO;
                    TableOrdrs.setItems(listO);
                    DirectoryChooser directoryChooser = new DirectoryChooser();
                    directoryChooser.setTitle("Создание Excel");
                    String direct = directoryChooser.showDialog(null).toString();
                    String path = direct + "\\"+printName+".xls";

                    HSSFWorkbook workbook = new HSSFWorkbook();
                    HSSFSheet sheet = workbook.createSheet("Лист1");

                    HSSFRow rowhead = sheet.createRow((short) 0);
                    rowhead.createCell(0).setCellValue("Номер");
                    rowhead.createCell(1).setCellValue("ФИО");
                    rowhead.createCell(2).setCellValue("Адрес начальный");
                    rowhead.createCell(3).setCellValue("Адрес конечный");
                    rowhead.createCell(4).setCellValue("Телефон");


                    int j = 1;
                    for (int i = 0; i < listO.size(); i++) {
                        HSSFRow row = sheet.createRow((short) j);
                        row.createCell(0).setCellValue(listO.get(i).id);
                        row.createCell(1).setCellValue(listO.get(i).fio);
                        row.createCell(2).setCellValue(listO.get(i).adressot);
                        row.createCell(3).setCellValue(listO.get(i).adressto);
                        row.createCell(4).setCellValue(listO.get(i).phonenum);
                        j++;
                    }
                    FileOutputStream fileOut = new FileOutputStream(path);
                    workbook.write(fileOut);
                    fileOut.close();
                    workbook.close();
                    System.out.println("Your excel file has been generated!");

                } catch (Exception ex) {
                    System.out.println(ex);
                }
            }
            PrintName.setVisible(true);
            StatusPrint=true;
        }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ConnectBd();
        listS = mysqlConnect.listS;
        listO = mysqlConnect.listO;
        listA = mysqlConnect.listA;
        FadeTransition leftpaneFade = new FadeTransition(Duration.seconds(0.001), LeftPanel);
        leftpaneFade.setByValue(1.0);
        leftpaneFade.setToValue(0);
        leftpaneFade.play();

        Date.setText("Дата: " + formatter.format(date));

    }
    //Подключение к БД
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