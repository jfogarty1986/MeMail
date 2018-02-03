package memail.inbox;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import memail.data.Account;
import memail.data.DatabaseDriver;
import memail.data.Message;

import java.io.IOException;
import java.sql.SQLException;

public class InboxController {

    @FXML private Tab inboxTab;
    @FXML private VBox messageListVBox;

    @FXML private TextField senderTextField;
    @FXML private TextField subjectTextField;
    @FXML private TextArea messageBodyTextArea;
    @FXML private TextArea messageTextArea;

    private Account currentAccount;
    private Message[] inboxMessages;
    private int selectedMessageIndex = -1;
    private int selectedMessage = 0;

    public void initAccount(Account account) {
        currentAccount = account;

        if(currentAccount != null) {
            inboxMessages = getMessagesFor(account);
            populateInboxView(inboxMessages);
        }
    }

    private void populateInboxView(Message[] inboxMessages) {
        int i = 0;
        for(Message message: inboxMessages) {
            addMessageToInboxView(message, i);
            i++;
        }
    }

    private void addMessageToInboxView(Message message, int messageIndex) {
        try {
            Parent messagePreviewLayout = FXMLLoader.load(getClass()
                    .getResource("message_preview.fxml"));

            // Set every second message preview to have a light blue background
            if(messageIndex % 2 == 0) {
                messagePreviewLayout.setStyle("-fx-background-color: lightblue");
            }

            messagePreviewLayout.setCursor(Cursor.HAND);

            Label messageSender = (Label) messagePreviewLayout.lookup("#messageSenderLabel");
            messageSender.setText(message.getSenderUsername());
            Label messageDate = (Label) messagePreviewLayout.lookup("#messageDateLabel");
            messageDate.setText(message.getDateSent().toString());
            Label messageSubject = (Label) messagePreviewLayout.lookup("#messageSubjectLabel");
            messageSubject.setText("Re: " + message.getSubject());

            messagePreviewLayout.setOnMouseClicked(event -> {
                displayFullMessage(messageIndex);
                selectedMessage = messageIndex;
            });

            messageListVBox.getChildren().add(messagePreviewLayout);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void displayFullMessage(int messageIndex) {
        Message messageToDisplay = inboxMessages[messageIndex];
        senderTextField.setText(messageToDisplay.getSenderUsername());
        subjectTextField.setText(messageToDisplay.getSubject());
        messageBodyTextArea.setText(messageToDisplay.getBody());
    }

    private Message[] getMessagesFor(Account account) {
        // TODO: get Messages from DB
        DatabaseDriver db = DatabaseDriver.getInstance();
        try {
            return db.getInboxMessages(account.getUsername());
        } catch (SQLException e) {
            return new Message[] {};
        }
    }

    public void replyToMessage(ActionEvent actionEvent) {

    }

    public void deleteMessage(ActionEvent actionEvent,Account account) throws SQLException {
        DatabaseDriver db = DatabaseDriver.getInstance();
        db.deleteMessage(selectedMessage,account);
    }

    public void sendMessage(ActionEvent actionEvent) {
    }

    public void deleteMessage(ActionEvent actionEvent) {
    }
}
