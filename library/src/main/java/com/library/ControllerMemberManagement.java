package com.library;

import java.sql.Statement;
import java.sql.ResultSet;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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
    private TextField searchID;
    @FXML
    private TextField searchName;
    @FXML
    private TextField searchMail;
    @FXML
    private TextField searchPhone;
    @FXML
    private Button searchButton;


    @FXML
    void initialize()
    {
        ID.setCellValueFactory(new PropertyValueFactory<>("ID"));
        Name.setCellValueFactory(new PropertyValueFactory<>("Name"));
        Mail.setCellValueFactory(new PropertyValueFactory<>("Mail"));
        Phone.setCellValueFactory(new PropertyValueFactory<>("Phone"));
        getUsersDB();
    }

    void getUsersDB()
    {
        String sql = "SELECT ID, Name, Mail, Phone FROM users";
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
                userList.add(new Members(id, Name, Mail, Phone));
            }
            statement.close();

            tableView.setItems(userList);
            tableView.refresh();
        } catch (Exception e) {e.printStackTrace();}
    }

    @FXML
    public void search() {
        FilteredList<Members> filteredData = new FilteredList<>(userList, p -> true);

        filteredData.setPredicate(Members -> {
            boolean matchesID = searchID.getText().isEmpty() || Members.getID().contains(searchID.getText());
            boolean matchesName = searchName.getText().isEmpty() || Members.getName().contains(searchName.getText());
            boolean matchesMail = searchMail.getText().isEmpty() || Members.getMail().contains(searchMail.getText());
            boolean matchesPhone = searchPhone.getText().isEmpty() || Members.getPhone().contains(searchPhone.getText());
            return matchesID && matchesName && matchesMail && matchesPhone;
        });

        tableView.setItems(filteredData);
        tableView.refresh();
    }
}
