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

public class ControllerBookManagement {

    private ObservableList<Books> booksList = FXCollections.observableArrayList();

    @FXML
    private TableView<Books> tableView;

    @FXML
    private TableColumn<Books, String> ID;
    @FXML
    private TableColumn<Books, String> title;
    @FXML
    private TableColumn<Books, String> author;
    @FXML
    private TableColumn<Books, String> genre;
    @FXML
    private TableColumn<Books, Boolean> status;

    @FXML
    private TextField searchID;
    @FXML
    private TextField searchTitle;
    @FXML
    private TextField searchAuthor;
    @FXML
    private TextField searchGenre;
    @FXML
    private Button searchButton;
    @FXML
    private Button editButton;
    @FXML
    private Button addButton;

    @FXML
    void initialize() {
        // Initialize the table columns
        ID.setCellValueFactory(new PropertyValueFactory<>("ID"));
        title.setCellValueFactory(new PropertyValueFactory<>("title"));
        author.setCellValueFactory(new PropertyValueFactory<>("author"));
        genre.setCellValueFactory(new PropertyValueFactory<>("genre"));
        status.setCellValueFactory(new PropertyValueFactory<>("status"));
        getBooksDB();
    }

    void getBooksDB() {
        String sql = "SELECT ID, title, author, genre, status FROM books";
        try {
            Config.getConn().createStatement().executeUpdate("USE " + Config.getDbNAME());
            Statement statement = Config.getConn().createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                String id = resultSet.getString("ID");
                String title = resultSet.getString("title");
                String author = resultSet.getString("author");
                String genre = resultSet.getString("genre");
                boolean status = resultSet.getBoolean("status");
                booksList.add(new Books(id, title, author, genre, status)); // add book to list
            }
            statement.close();

            tableView.setItems(booksList);
            tableView.refresh();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void search() { // Filter book by ID, Title, Author, Genre in tableView
        FilteredList<Books> filteredData = new FilteredList<>(booksList, p -> true);

        filteredData.setPredicate(book -> {
            boolean matchesID = searchID.getText().isEmpty() || book.getID().contains(searchID.getText());
            boolean matchesTitle = searchTitle.getText().isEmpty() || book.getTitle().contains(searchTitle.getText());
            boolean matchesAuthor = searchAuthor.getText().isEmpty() || book.getAuthor().contains(searchAuthor.getText());
            boolean matchesGenre = searchGenre.getText().isEmpty() || book.getGenre().contains(searchGenre.getText());
            return matchesID && matchesTitle && matchesAuthor && matchesGenre;
        });

        tableView.setItems(filteredData);
        tableView.refresh();
    }

    @FXML
    void updateBook() { // Update book data in database
        if(tableView.getSelectionModel().getSelectedItem() != null)
        {
            Books book = tableView.getSelectionModel().getSelectedItem();
            /* */
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/library/StaffUpdateBook.fxml"));
            StackPane root2;
            try 
            {
                root2 = loader.load();
                Stage stage = new Stage();
                stage.setTitle("Update Book");
                ControllerUpdateBook controller = loader.getController(); // get controller 
                controller.setBook(book,this::updateTable); // Pass the selected member and the method to update the table
                stage.setScene(new Scene(root2));
                stage.show();
            } catch (Exception e){}

        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle("Error");
            alert.setContentText("Please select a book to update.");
            alert.showAndWait();
        }
    }

    void updateTable() {
        booksList.clear();
        tableView.refresh();
        getBooksDB();
    }

    @FXML
    void addBook() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/library/StaffAddBook.fxml"));
        StackPane root2;
        try
        {
            root2 = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Add Book");
            ControllerAddBook controller = loader.getController(); // get controller 
            controller.setController(this::updateTable); // Pass the selected book and the method to update the table
            stage.setScene(new Scene(root2));
            stage.show();
        }
        catch (Exception e){e.printStackTrace();}
    }
}
