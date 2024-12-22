package com.library;

import java.sql.Statement;
import java.sql.ResultSet;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class ControllerMemberManagement
{
    private ObservableList<Members> userList = FXCollections.observableArrayList();
    
    @FXML
    private TableView<Members> tableView;

    @FXML
    private TableColumn<Members, String> ID;
    @FXML
    private TableColumn<Members, String> Name;  
    @FXML
    private TableColumn<Members, String> Mail;  
    @FXML
    private TableColumn<Members, String> Phone;  
    @FXML
    private TableColumn<Members, String> Status;  


    @FXML
    void initialize()
    {
        ID.setCellValueFactory(new PropertyValueFactory<>("ID"));
        Name.setCellValueFactory(new PropertyValueFactory<>("Name"));
        Mail.setCellValueFactory(new PropertyValueFactory<>("Mail"));
        Phone.setCellValueFactory(new PropertyValueFactory<>("Phone"));
        Status.setCellValueFactory(new PropertyValueFactory<>("Status"));
        getUsersDB();
    }

    void getUsersDB()
    {
        String sql = "SELECT ID, Name, Mail, Phone, Status FROM users";
        try
        {
            Config.getConn().createStatement().executeUpdate("USE "+Config.getDbNAME());
            Statement statement = Config.getConn().createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next())
            {
                String id = resultSet.getString("ID");
                String Name = resultSet.getString("Name");
                String Mail = resultSet.getString("Mail");
                String Phone = resultSet.getString("Phone");
                String Status = resultSet.getString("Status");
                userList.add(new Members(id, Name, Mail, Phone, Status));
            }
            statement.close();

            tableView.setItems(userList);
            tableView.refresh();
        } catch (Exception e) {e.printStackTrace();}
    }

}
