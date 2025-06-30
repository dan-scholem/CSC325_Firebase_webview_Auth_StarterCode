package com.example.csc325_firebase_webview_auth.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class LoginView {


    @FXML
    private TextField emailField = new TextField();
    @FXML
    private TextField passwordField = new TextField();
    @FXML
    private Button loginButton;
    @FXML
    private Button signUpButton;



    @FXML
    private void handleSignUpButton(ActionEvent event) {
        try{
            App.setRoot("/files/SignUpView.fxml");
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
