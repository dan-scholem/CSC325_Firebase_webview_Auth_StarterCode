package com.example.csc325_firebase_webview_auth.view;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class LoginView {


    @FXML
    private TextField emailField = new TextField();
    @FXML
    private PasswordField passwordField = new PasswordField();
    @FXML
    private Button loginButton;
    @FXML
    private Button signUpButton;


    @FXML
    private void handleLoginButton(ActionEvent event) {
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();
        try{
            if(email.isEmpty() || password.isEmpty()){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText("Please enter your email and password");
                alert.showAndWait();
            } else{
                UserRecord user = App.fauth.getUserByEmail(email);
                if (user == null){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Error");
                    alert.setHeaderText("Invalid Email");
                    alert.showAndWait();
                }else{
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText("Simulated Login Successful");
                    alert.showAndWait();
                    App.setRoot("/files/AccessFBView.fxml");
                }
            }

        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid Email or Password");
            alert.showAndWait();
        }

    }



    @FXML
    private void handleSignUpButton(ActionEvent event) {
        try{
            App.setRoot("/files/SignUpView.fxml");
        }catch(Exception e){
            e.printStackTrace();
        }
    }


}
