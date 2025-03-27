package com.library;

import java.sql.PreparedStatement;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ControllerAddBook {
    @FXML private TextField ID;
    @FXML private TextField title;
    @FXML private TextField author;
    @FXML private TextField genre;
    @FXML private TextField status;
    
    @FXML private Button addButton;
    @FXML private Button cancelButton;

    private Runnable updateTableCallback;

    void setController(Runnable updateTableCallback) {
        this.updateTableCallback = updateTableCallback;
    }

    @FXML
    void addBook() {
        String sql = "INSERT INTO books (ID, title, author, genre) VALUES ( ?,?,?,?)";
        
        if (ID.getText().isEmpty() || title.getText().isEmpty() || author.getText().isEmpty() || genre.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please fill in all fields");
            alert.showAndWait();
            return;
        }

        if (controlID(ID.getText()) == false) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Book ID already exists");
            alert.showAndWait();
            return;
        }

        try {
            PreparedStatement statement = Config.getConn().prepareStatement(sql);
            statement.setString(1, ID.getText());
            statement.setString(2, title.getText());
            statement.setString(3, author.getText());
            statement.setString(4, genre.getText());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        closeWindow();
    }

    @FXML
    private void closeWindow()
    {
        updateTableCallback.run(); // updates the table in the main window
        Stage currentStage = (Stage) addButton.getScene().getWindow();
        currentStage.close();
    }

    private boolean controlID(String ID)
    {
        try {
            PreparedStatement statement = Config.getConn().prepareStatement("SELECT * FROM books WHERE ID = ?");
            statement.setString(1, ID);
            return !statement.executeQuery().next();
            }
        catch (Exception e) {e.printStackTrace();}
        return false;
    }
}
