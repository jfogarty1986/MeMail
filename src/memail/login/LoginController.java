package memail.login;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import memail.data.Account;
import memail.data.DatabaseDriver;
import memail.inbox.InboxController;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;


public class LoginController implements Initializable {

    private static final Account VALID_ACCOUNT = new Account("James", "123");

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private CheckBox rememberMeBox;
    @FXML private Label errorLabel;

    public void login(ActionEvent event) throws IOException {
        Account currentAccount = new Account(usernameField.getText(),passwordField.getText());
        boolean isValidAccount = isAccountValid(currentAccount);

        if(isValidAccount) {
            // Open inbox
            showInbox(event, currentAccount);
            if(rememberMeBox.isSelected()) {
                saveLogin(currentAccount);
            }
        }
        else {
            // show error
            clearFields();
            errorLabel.setVisible(true);
        }
    }

    private void saveLogin(Account currentAccount) {
        // TODO: save login details to file
    }

    private void showInbox(ActionEvent event, Account account) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../inbox/inbox.fxml"));
        Parent inboxParent = loader.load();

        InboxController inboxController = loader.getController();
        inboxController.initAccount(account);

        Scene inboxScene = new Scene(inboxParent);
        Stage window = (Stage)((Node) event.getSource()).getScene().getWindow();
        window.setScene(inboxScene);
        window.show();
    }

    private void clearFields() {
        usernameField.setText("");
        passwordField.setText("");
    }

    private boolean isAccountValid(Account currentAccount) {
        // TODO: validate account using DB
        //return currentAccount.equals(VALID_ACCOUNT);

        DatabaseDriver db = DatabaseDriver.getInstance();
        try {
            return db.isAccountValid(currentAccount);
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Account savedLogin = getSavedLogin();
        if(savedLogin != null) {
            usernameField.setText(savedLogin.getUsername());
            passwordField.setText(savedLogin.getPassword());
        }
    }

    private Account getSavedLogin() {
        // TODO: get saved login from file
//        return new Account("James", "123");
        return null;
    }
}
