package com.example.csc325_firebase_webview_auth.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpView {

    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private Button signUpButton;
    @FXML
    private Button backToLoginButton;


    @FXML
    private void handleSignUpButton(ActionEvent event) throws IOException {
        try{
            String email = emailField.getText().trim();
            String password = passwordField.getText().trim();
            String confirmPassword = confirmPasswordField.getText().trim();
            if(hasAllFields(email, password,confirmPassword)){
                if(isValidEmail(email)){
                    if(passwordMatches(password,confirmPassword)){
                        AccessFBView fbView = new AccessFBView();
                        fbView.registerUser(email,password);
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }




    @FXML
    private void handleBackToLoginButton(ActionEvent event) {
        try {
            App.setRoot("/files/loginView.fxml");
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private static boolean hasAllFields(String email, String password, String confirmPassword) {
        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Enter All Fields");
            alert.showAndWait();
            return false;
        } else {
            return true;
        }
    }

    private static boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        if(matcher.matches()){
            return true;
        }else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid Email");
            alert.showAndWait();
            return false;
        }
    }

    private static boolean isExistingAccount(String email) {
        return true;
    }

    private static boolean passwordMatches(String password, String confirmPassword) {
        if(password.equals(confirmPassword)){
            return true;
        }else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Passwords do not match");
            alert.showAndWait();
            return false;
        }
    }

}
