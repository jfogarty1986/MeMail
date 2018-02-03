package memail.data;

import java.sql.*;
import java.util.ArrayList;

public class DatabaseDriver {
    private static String URL = "jdbc:mysql://localhost:3306/";
    private static String DB = "memaildb";
    private static String USERNAME = "root";
    private static String PASSWORD = "Meghan090909";
    private static DatabaseDriver INSTANCE;

    private Connection connection;

    private DatabaseDriver(){
        connect();
    }

    public static DatabaseDriver getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new DatabaseDriver();
        }
        return INSTANCE;
    }


    public boolean isAccountValid(Account account) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM user_account WHERE account_username=? AND account_password=?");
        statement.setString(1, account.getUsername());
        statement.setString(2, account.getPassword());
        ResultSet resultSet = statement.executeQuery();
        return resultSet.next();
    }


    private void connect() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = (Connection) DriverManager.getConnection(URL + DB, USERNAME, PASSWORD);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public Message[] getInboxMessages(String username) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM message WHERE recipient_username=?");
        statement.setString(1, username);
        ResultSet resultSet = statement.executeQuery();
        return parseMessages(username, resultSet);
    }

    private Message[] parseMessages(String username, ResultSet resultSet) throws SQLException {
        ArrayList<Message> inboxMessages = new ArrayList<>();

        while(resultSet.next()) {
            String senderUsername = resultSet.getString(2);
            String messageSubject = resultSet.getString(4);
            String messageBody = resultSet.getString(5);
            Timestamp messageDateSent = resultSet.getTimestamp(6);
            java.util.Date formattedDate = Date.from(messageDateSent.toInstant());

            Message currentMessage = new Message(senderUsername, username);
            currentMessage.setDateSent(formattedDate);
            currentMessage.setBody(messageBody);
            currentMessage.setSubject(messageSubject);
            currentMessage.setRecipientUsername(username);

            inboxMessages.add(currentMessage);
        }
        return convertToArray(inboxMessages);
    }

    private Message[] convertToArray(ArrayList<Message> inboxMessages) {
        return inboxMessages.toArray(new Message[inboxMessages.size()]);
    }


    public void deleteMessage(int selectedMessage,Account account) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "DELETE * FROM message WHERE recipient_username=? && ");
        statement.setString(1, account.getUsername());
    }
}
