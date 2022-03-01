package com.example.demo.Controller;

import com.example.demo.Connection.ConnectionClass;
import com.example.demo.Main;
import com.example.demo.Model.Department;
import com.example.demo.Model.Employee;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.DatagramPacket;
import java.sql.*;
import java.util.Date;

public class department{

    @FXML
    private TextField Dname;
    @FXML
    private TextField Dnumber;
    @FXML
    private TextField Mgr_ssn;
    @FXML
    private DatePicker Mgr_start_date;

    @FXML
    private TableColumn<Department, String> depName;
    @FXML
    private TableColumn<Department, String> depSSN;
    @FXML
    private TableColumn<Department, Date> depStartDate;
    @FXML
    private TableView<Department> table_department;


    
    @FXML
    private void initialize(){
        depName.setCellValueFactory(cellData -> cellData.getValue().dnameProperty());
        depSSN.setCellValueFactory(cellData -> cellData.getValue().mgr_ssnProperty());
        depStartDate.setCellValueFactory(cellData -> cellData.getValue().mgr_start_dateProperty());

        initializeTableValues();
    }

    ConnectionClass connectionClass = new ConnectionClass();
    Connection connection = connectionClass.getConnection();

    public ObservableList<Department> departmentList = FXCollections.observableArrayList();


    @FXML
    void Delete(ActionEvent event) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("DELETE FROM `department` WHERE `Dnumber` = '" + table_department.getSelectionModel().getSelectedItem().getDnumber() + "'");
        ps.execute();
        initialize();
    }

    public void initializeTableValues(){
        try {
            Statement statement = connection.createStatement();

            String sql="SELECT * FROM department;";

            ResultSet resultSet = statement.executeQuery(sql);

            ObservableList<Department> departmentList = FXCollections.observableArrayList();

            if (resultSet.next()){
                while (resultSet.next()) {
                    Department department = new Department(
                            resultSet.getString("Dname"),
                            resultSet.getString("Mgr_ssn"),
                            resultSet.getDate("Mgr_start_date"),
                            resultSet.getInt("Dnumber")
                    );
                    departmentList.add(department);
                }
                table_department.setItems(departmentList);
            }else {
                System.out.println("no data");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private Button submitButton1;

    @FXML
    void insertPerson(ActionEvent event) {
        try {
            Statement statement=connection.createStatement();
            String sql = "INSERT INTO " +
                    "department(Dname, Dnumber, Mgr_ssn, Mgr_start_date) " +
                    "VALUES ('"+Dname.getText()+"', '" + Dnumber.getText() + "','" +
                    ""+Mgr_ssn.getText()+"','" +
                    ""+Mgr_start_date.getValue()+"')";
            statement.executeUpdate(sql);
            System.out.println("Success!");

        } catch (SQLException e) {
            e.printStackTrace();
        }

        Dname.setText("");
        Mgr_ssn.setText("");

        initializeTableValues();
    }

    @FXML
    void Back(ActionEvent event) throws IOException {
        Main m = new Main();
        m.changeScene("Main.fxml");
    }
}
