package com.library;

import java.sql.ResultSet;
import java.sql.Statement;

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

public class ControllerMemberMain {
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
    private TextField limit;

    @FXML
    private Button searchButton;
    @FXML
    private Button borrowButton;
    
    @FXML
    private String userId;

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
        String stringStatus;
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
                if (status) {
                    stringStatus = "Available"; // Book is available
                } else {
                    stringStatus = "busy"; // Book is not available
                }
                booksList.add(new Books(id, title, author, genre, stringStatus)); // add book to list
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
            boolean matchesID = Utils.InjectionPreventer(searchID.getText()).isEmpty() || book.getID().toLowerCase().contains(searchID.getText().toLowerCase());
            boolean matchesTitle = Utils.InjectionPreventer(searchTitle.getText()).isEmpty() || book.getTitle().toLowerCase().contains(searchTitle.getText().toLowerCase());
            boolean matchesAuthor = Utils.InjectionPreventer(searchAuthor.getText()).isEmpty() || book.getAuthor().toLowerCase().contains(searchAuthor.getText().toLowerCase());
            boolean matchesGenre =Utils.InjectionPreventer(searchGenre.getText()).isEmpty() || book.getGenre().toLowerCase().contains(searchGenre.getText().toLowerCase());
            return matchesID && matchesTitle && matchesAuthor && matchesGenre;
        });

        tableView.setItems(filteredData);
        tableView.refresh();
    }

    void updateTable() {
        booksList.clear();
        tableView.refresh();
        getBooksDB();
    }

    @FXML
    void logOut()
    {
        Stage currentStage = (Stage) searchButton.getScene().getWindow();
        Utils.redirect(currentStage, "/com/library/main.fxml", "Library Management System");
    }

    @FXML
    void toProfile()
    {
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/library/MemberProfile.fxml"));
        StackPane root2;
        try
        {
            Stage currentStage = (Stage) searchAuthor.getScene().getWindow();
            userId = currentStage.getTitle();
            root2 = loader.load();
            Stage stage = new Stage();
            stage.setTitle(userId);
            stage.setResizable(false);
            stage.setScene(new Scene(root2));
            stage.show();
        }
        catch (Exception e){e.printStackTrace();}
    }

    @FXML
    void borrow()
    {
        Stage currentStage = (Stage) searchAuthor.getScene().getWindow();
        userId = currentStage.getTitle();

        if (tableView.getSelectionModel().getSelectedItem() != null)
        {
            Books selectedBook = tableView.getSelectionModel().getSelectedItem();
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/library/BorrowRequest.fxml"));
                StackPane root = loader.load();
                ControllerRequest controller = loader.getController();
                controller.setBookID(selectedBook.getID());
                controller.setBookTitle(selectedBook.getTitle());
                controller.setMemberID(userId);
                controller.setBookStatus(selectedBook.getStatus());
                controller.setBorrowLimit(checklimit(userId));
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setResizable(false);
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setTitle("Error");
                alert.setContentText("Failed to open borrow request window.");
                alert.showAndWait();
            }
        }    
        else
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle("Error");
            alert.setContentText("Please select a book to borrow.");
            alert.showAndWait();
        }
    }

    int checklimit(String userId)
    {
        String sql = "SELECT books_left FROM members WHERE ID = '" +userId + "'";
        try {
            Config.getConn().createStatement().executeUpdate("USE " + Config.getDbNAME());
            ResultSet rs = Config.getConn().createStatement().executeQuery(sql);
            if (rs.next()) {
                return (rs.getInt("books_left"));
            }
            rs.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

}
