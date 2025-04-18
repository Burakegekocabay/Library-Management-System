package com.library;

import java.sql.Statement;
import java.sql.ResultSet;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

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
    private TableColumn<Members, Integer> Borrow_rights;
    @FXML
    private TableColumn<Members, String> Status;


    @FXML
    private TextField searchID;
    @FXML
    private TextField searchName;
    @FXML
    private TextField searchMail;
    @FXML
    private TextField searchPhone;
    @FXML
    private TextField searchStatus;
    @FXML
    private Button searchButton;
    @FXML
    private Button editButton;
    @FXML
    private Button AddButton;
    @FXML
    private Button mainMenuButton;

    @FXML
    void initialize()
    {
        // Initialize the table columns
        ID.setCellValueFactory(new PropertyValueFactory<>("ID"));
        Name.setCellValueFactory(new PropertyValueFactory<>("Name"));
        Mail.setCellValueFactory(new PropertyValueFactory<>("Mail"));
        Phone.setCellValueFactory(new PropertyValueFactory<>("Phone"));
        Borrow_rights.setCellValueFactory(new PropertyValueFactory<>("BooksLeft"));
        Status.setCellValueFactory(new PropertyValueFactory<>("status"));

        getUsersDB();
    }

    void getUsersDB() // get user data from database
    {
        String sql = "SELECT ID, Name, Mail, Phone, books_left, status FROM members";
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
                int books_left = resultSet.getInt("books_left");
                String status = resultSet.getString("status");
                userList.add(new Members(id, Name, Mail, Phone, books_left, status)); // add user to list
            }
            statement.close();

            tableView.setItems(userList); 
            tableView.refresh();
        } catch (Exception e) {e.printStackTrace();}
    }

    @FXML
    public void search() { // Filter user by ID, Name, Mail, Phone in tableView 
        FilteredList<Members> filteredData = new FilteredList<>(userList, p -> true);

        filteredData.setPredicate(Members -> {
            boolean matchesID = searchID.getText().isEmpty() || Members.getID().contains(searchID.getText());
            boolean matchesName = searchName.getText().isEmpty() || Members.getName().toLowerCase().contains(searchName.getText().toLowerCase());
            boolean matchesMail = searchMail.getText().isEmpty() || Members.getMail().toLowerCase().contains(searchMail.getText().toLowerCase());
            boolean matchesPhone = searchPhone.getText().isEmpty() || Members.getPhone().toLowerCase().contains(searchPhone.getText().toLowerCase());
            boolean matchesStatus = searchStatus.getText().isEmpty() || Members.getStatus().toLowerCase().contains(searchStatus.getText().toLowerCase());
            return matchesID && matchesName && matchesMail && matchesPhone && matchesStatus;
        });

        tableView.setItems(filteredData);
        tableView.refresh();
    }

    @FXML
    void updateUser() // Update user data in database
    {
        if(tableView.getSelectionModel().getSelectedItem() != null)
        {
            Members member = tableView.getSelectionModel().getSelectedItem();
            /* */
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/library/StaffUpdateMember.fxml"));
            StackPane root2;
            try 
            {
                root2 = loader.load();
                Stage stage = new Stage();
                stage.setTitle("Update Member");
                ControllerUpdateMember controller = loader.getController(); // get controller 
                controller.setMember(member,this::updateTable); // Pass the selected member and the method to update the table
                stage.setScene(new Scene(root2));
                stage.setResizable(false);
                stage.show();
            } catch (Exception e){}

        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle("Error");
            alert.setContentText("Please select a user to update.");
            alert.showAndWait();
        }
    }

    void updateTable()
    {
        userList.clear();
        tableView.refresh();
        getUsersDB();
    }

    @FXML
    void addMember()
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/library/StaffAddMember.fxml"));
        StackPane root2;
        try
        {
            root2 = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Add Member");
            ControllerAddMember controller = loader.getController(); // get controller 
            controller.setController(this::updateTable); // Pass the selected member and the method to update the table
            stage.setScene(new Scene(root2));
            stage.setResizable(false);
            stage.show();
        }
        catch (Exception e){e.printStackTrace();}
    }

    @FXML
    void toMainMenu() {
        Stage currentStage = (Stage) mainMenuButton.getScene().getWindow();
        Utils.redirect(currentStage,"/com/library/StaffMainPage.fxml"," Library Management System");
    }
}
