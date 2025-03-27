package com.library;

import java.sql.PreparedStatement;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.Label;

public class ControllerUpdateBook
{
    @FXML private TextField ID;        // Book ID
    @FXML private TextField title;     // Book Title
    @FXML private TextField author;    // Book Author
    @FXML private TextField genre;     // Book Genre
    
    @FXML private Button updateButton;
    @FXML private Button deleteButton;
    
    @FXML private Label bookIDLabel;
    @FXML private Label bookTitleLabel;
    @FXML private Label bookAuthorLabel;
    @FXML private Label bookGenreLabel;

    private Runnable updateTableCallback;

    public void setBook(Books book, Runnable updateTableCallback) // book is the book to be updated, updateTableCallback is the callback for updating the table
    {
        this.updateTableCallback = updateTableCallback;
        ID.setText(book.getID());
        title.setText(book.getTitle());
        author.setText(book.getAuthor());
        genre.setText(book.getGenre());
    }

    @FXML
    private void updateBook()
    {
        String sql = "UPDATE books SET Title = ?, Author = ?, Genre = ? WHERE ID = ?";
        String updatedID = ID.getText();
        String updatedTitle = title.getText();
        String updatedAuthor = author.getText();
        String updatedGenre = genre.getText();

        try
        {
            Config.getConn().createStatement().executeUpdate("USE " + Config.getDbNAME());
            PreparedStatement statement = Config.getConn().prepareStatement(sql);
            statement.setString(1, updatedTitle);
            statement.setString(2, updatedAuthor);
            statement.setString(3, updatedGenre);
            statement.setString(4, updatedID);
            statement.executeUpdate();
        }
        catch (Exception e) { e.printStackTrace(); }
        closeWindow();
    }

    @FXML
    private void deleteBook()
    {
        String sql = "DELETE FROM books WHERE ID = ?";
        try
        {
            Config.getConn().createStatement().executeUpdate("USE " + Config.getDbNAME());
            PreparedStatement statement = Config.getConn().prepareStatement(sql);
            statement.setString(1, ID.getText());
            statement.executeUpdate();
        }
        catch (Exception e) { e.printStackTrace(); }
        closeWindow();
    }

    @FXML
    private void closeWindow()
    {
        updateTableCallback.run(); // updates the table in the main window
        Stage currentStage = (Stage) deleteButton.getScene().getWindow();
        currentStage.close();
    }
}
