package com.example.csc325_firebase_webview_auth.view;//package modelview;

import com.example.csc325_firebase_webview_auth.model.Person;
import com.example.csc325_firebase_webview_auth.viewmodel.AccessDataViewModel;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class AccessFBView {


     @FXML
    private TextField nameField;
    @FXML
    private TextField majorField;
    @FXML
    private TextField ageField;
    @FXML
    private Button writeButton;
    @FXML
    private Button readButton;
    @FXML
    private TextArea outputField;
    @FXML
    private TableView<Person> outputTable;
    @FXML
    private TableColumn<Person,String> nameColumn;
    @FXML
    private TableColumn<Person,String> majorColumn;
    @FXML
    private TableColumn<Person,String> ageColumn;
    @FXML
    private MenuItem delete;
    @FXML
    private MenuItem close;
     private boolean key;
    private ObservableList<Person> listOfUsers = FXCollections.observableArrayList();
    private Person person;
    public ObservableList<Person> getListOfUsers() {
        return listOfUsers;
    }

    void initialize() {

        AccessDataViewModel accessDataViewModel = new AccessDataViewModel();
        nameField.textProperty().bindBidirectional(accessDataViewModel.userNameProperty());
        majorField.textProperty().bindBidirectional(accessDataViewModel.userMajorProperty());
        writeButton.disableProperty().bind(accessDataViewModel.isWritePossibleProperty().not());
    }

    @FXML
    private void addRecord(ActionEvent event) {
        addData();
    }

    @FXML
    private void deleteRecord(ActionEvent event) {
        deleteData();
    }

    @FXML
    private void closeWindow(ActionEvent event) {
        logout();
    }

    @FXML
    private void aboutWindow(ActionEvent event) {
        about();
    }

        @FXML
    private void readRecord(ActionEvent event) {
        readFirebase();
    }

            @FXML
    private void regRecord(ActionEvent event) {
        registerUser();
    }

     @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("/files/WebContainer.fxml");
    }

    public void addData() {

        //Check if all fields have values
        if(nameField.getText().isEmpty() || majorField.getText().isEmpty() || ageField.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Enter All Fields");
            alert.showAndWait();
        } else {
            //asynchronously retrieve all documents
            ApiFuture<QuerySnapshot> future =  App.fstore.collection("References").get();
            // future.get() blocks on response
            List<QueryDocumentSnapshot> documents;
            try {
                //Since this program is not pulling UID's to check uniqueness, it first checks if
                //data exists based on Name
                boolean dataExists = false;
                Optional<ButtonType> confirm = Optional.empty();
                documents = future.get().getDocuments();
                for (QueryDocumentSnapshot document : documents) {
                    if(document.getString("Name").equals(nameField.getText())) {
                        dataExists = true;
                        Alert alertConfirm = new Alert(Alert.AlertType.CONFIRMATION);
                        alertConfirm.setTitle("Add Confirmation");
                        alertConfirm.setHeaderText("Data With Name " +  nameField.getText() + " Exists. Continue?");
                        confirm = alertConfirm.showAndWait();
                    }
                }
                //If data name does not exist, or user confirms that data exists and wants to
                //continue anyway, adds data to database and updates table
                if((confirm.isPresent() && confirm.get() == ButtonType.OK) || !dataExists) {
                    DocumentReference docRef = App.fstore.collection("References").document(UUID.randomUUID().toString());
                    Map<String, Object> data = new HashMap<>();
                    data.put("Name", nameField.getText());
                    data.put("Major", majorField.getText());
                    data.put("Age", Integer.parseInt(ageField.getText()));
                    //asynchronously write data
                    ApiFuture<WriteResult> result = docRef.set(data);
                    nameField.clear();
                    majorField.clear();
                    ageField.clear();
                    Thread.sleep(20);
                    readFirebase();
                }
            } catch (InterruptedException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(e.getMessage());
                alert.showAndWait();
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Invalid Input");
                alert.showAndWait();
            } catch (ExecutionException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(e.getMessage());
                alert.showAndWait();
            }
        }

    }

        public boolean readFirebase()
         {
             key = false;

        //asynchronously retrieve all documents
        ApiFuture<QuerySnapshot> future =  App.fstore.collection("References").get();
        // future.get() blocks on response
        List<QueryDocumentSnapshot> documents;
        try
        {
            documents = future.get().getDocuments();
            if(documents.size()>0)
            {
                System.out.println("Outing....");
                //clear table of contents before reading data
                outputTable.getItems().clear();
                for (QueryDocumentSnapshot document : documents)
                {
                    //outputField.setText(outputField.getText()+ document.getData().get("Name")+ " , Major: "+
                    //        document.getData().get("Major")+ " , Age: "+
                    //        document.getData().get("Age")+ " \n ");
                    System.out.println(document.getId() + " => " + document.getData().get("Name"));
                    person  = new Person(String.valueOf(document.getData().get("Name")),
                            document.getData().get("Major").toString(),
                            Integer.parseInt(document.getData().get("Age").toString()));
                    listOfUsers.add(person);
                }
                //map columns to data and populate table
                nameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));
                majorColumn.setCellValueFactory(new PropertyValueFactory<>("Major"));
                ageColumn.setCellValueFactory(new PropertyValueFactory<>("Age"));
                outputTable.setItems(getListOfUsers());
            }
            else
            {
               System.out.println("No data");
            }
            key=true;

        }
        catch (InterruptedException | ExecutionException ex)
        {
             ex.printStackTrace();
        }
        return key;
    }

        public void sendVerificationEmail() {
        try {
            UserRecord user = App.fauth.getUser("name");
            //String url = user.getPassword();

        } catch (Exception e) {
        }
    }

    //only invoked if user selects File -> Register (generic user)
    public boolean registerUser() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Generic User Registration Confirmation");
        alert.setHeaderText("Create User user@example.com?");
        Optional<ButtonType> confirm =  alert.showAndWait();
        if (confirm.isPresent() && confirm.get() == ButtonType.OK) {
            UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                    .setEmail("user@example.com")
                    .setEmailVerified(false)
                    .setPassword("secretPassword")
                    .setDisabled(false);

            UserRecord userRecord;
            try {
                userRecord = App.fauth.createUser(request);
                System.out.println("Successfully created new user: " + userRecord.getUid());
                return true;

            } catch (FirebaseAuthException ex) {
                // Logger.getLogger(FirestoreContext.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        }
        return false;
    }

    //creates user from sign up page
    public boolean registerUser(String email, String password) {
        UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                .setEmail(email)
                .setEmailVerified(false)
                .setPassword(password)
                .setDisabled(false);

        UserRecord userRecord;
        try {
            userRecord = App.fauth.createUser(request);
            System.out.println("Successfully created new user: " + userRecord.getUid());
            return true;

        } catch (FirebaseAuthException ex) {
            // Logger.getLogger(FirestoreContext.class.getName()).log(Level.SEVERE, null, ex);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(ex.getMessage());
            alert.showAndWait();
            return false;
        }

    }


    public boolean deleteData() {

        //asynchronously retrieve all documents
        ApiFuture<QuerySnapshot> future =  App.fstore.collection("References").get();
        // future.get() blocks on response
        List<QueryDocumentSnapshot> documents;
        try{
            documents = future.get().getDocuments();
            Person selected = outputTable.getSelectionModel().getSelectedItem();
            //confirm data deletion
            Alert alertConfirm = new Alert(Alert.AlertType.CONFIRMATION);
            alertConfirm.setTitle("Confirm Deletion");
            alertConfirm.setHeaderText("Are you sure you want to delete " +  selected.getName() + "?");
            Optional<ButtonType> confirm = alertConfirm.showAndWait();
            if(confirm.isPresent() && confirm.get() == ButtonType.OK){
                for(QueryDocumentSnapshot document : documents){
                    if(selected.getName().equals(document.getString("Name"))){
                        document.getReference().delete();
                        System.out.println("Successfully deleted document: " + selected.getName());
                        break;
                    }
                }
                Thread.sleep(20);
                readFirebase();
                return true;
            }else{
                return false;
            }
        } catch (ExecutionException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(e.getMessage());
            alert.showAndWait();
            return false;
        } catch (InterruptedException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(e.getMessage());
            alert.showAndWait();
            return false;
        } catch (RuntimeException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Please select a row");
            alert.showAndWait();
            return false;
        }
    }

    public boolean logout() {
        try{
            App.setRoot("/files/LoginView.fxml");
            return true;
        }catch(IOException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(e.getMessage());
            alert.showAndWait();
            return false;
        }
    }

    public void about(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("About This Application");
        alert.setContentText("Version 1.0\nDeveloped by Dan Scholem\n" +
                "Read - Reads data from Firestore Database\nWrite - Writes data to Firestore Database\n" +
                "Switch - Switch to HTML window\nEdit -> Delete: Deletes data from Firestore Database\n" +
                "File -> Register: Registers user user@example.com");
        alert.showAndWait();
    }
}
