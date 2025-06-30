package com.example.csc325_firebase_webview_auth.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class SignUpView {

    @FXML
    private Button backToLoginButton;







    @FXML
    private void handleBackToLoginButton(ActionEvent event) {
        try {
            App.setRoot("/files/loginView.fxml");
        }catch(Exception e){
            e.printStackTrace();
        }
    }


}
