import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;

import static junit.framework.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class User1Test {

    private User1 user1Frame;

    @BeforeEach
    public void setup() {
        MainChannel mainChannel = new MainChannel();
        user1Frame = new User1(mainChannel);
        user1Frame.setVisible(true);
    }

    @AfterEach
    public void tearDown() {
        user1Frame.dispose();
    }

    @Test
    public void testComponents() {
        Assertions.assertTrue(user1Frame.isVisible(), "User1 should be visible");
        MainChannelUITest.printComponentNames(user1Frame.getContentPane());

        JLabel welcomeLabel = MainChannelUITest.findComponent(JLabel.class, user1Frame, "welcomeLabel");
        assertEquals("Welcome to User1",welcomeLabel.getText());
    }

    @Test
    public void testPostText() {
        JTextArea postTextArea = MainChannelUITest.findComponent(JTextArea.class, user1Frame, "postTextArea");
        JButton postButton = MainChannelUITest.findComponent(JButton.class, user1Frame, "postButton");
        JTextArea contentViewArea1 = MainChannelUITest.findComponent(JTextArea.class, user1Frame, "contentViewArea1");

        // Type text into the post text area
        postTextArea.setText("This is a test post User1.");

        // Click the post button
        postButton.doClick();

        // Check if the content view area has the new post text
        String expectedText = "--------------------------------\nThis is a test post User1.\n\n";
        Assertions.assertTrue(contentViewArea1.getText().startsWith(expectedText));
    }

    @Test
    public void testEmptyPost() {
        JTextArea postTextArea = MainChannelUITest.findComponent(JTextArea.class, user1Frame, "postTextArea");
        JButton postButton = MainChannelUITest.findComponent(JButton.class, user1Frame, "postButton");

        // Empty the post text area
        postTextArea.setText("");

        // Click the post button
        postButton.doClick();

        // Verify that a message dialog window is open
        Window[] windows = Window.getWindows();
        boolean messageDialogFound = false;
        for (Window window : windows) {
            if ("dialog0".equals(window.getName())) {
                JDialog dialog = (JDialog) window;

                // Get dialog title
                String dialogTitle = dialog.getTitle();

                // Get dialog content (assumes JOptionPane is used)
                Component[] components = dialog.getContentPane().getComponents();
                String dialogContent = "";
                for (Component component : components) {
                    if (component instanceof JOptionPane) {
                        JOptionPane optionPane = (JOptionPane) component;
                        dialogContent = optionPane.getMessage().toString();
                        break;
                    }
                }
                assertEquals("Input", dialogTitle);
                assertEquals("", dialogContent);
                messageDialogFound = true;
                break;
            }
        }

        assertTrue(messageDialogFound, "Message dialog should be open");
    }


    @Test
    public void testMainChannelLabel() {
        JLabel mainChannelLabel = MainChannelUITest.findComponent(JLabel.class, user1Frame, "mainChannelLabel");

        // Check if the main channel label is displaying the correct text
        Assertions.assertEquals("Main Channel", mainChannelLabel.getText());
    }

    @Test
    public void testSubscribeButton() {
        JButton subscribeButton = MainChannelUITest.findComponent(JButton.class, user1Frame, "subscribeButton");

        // Click the subscribe button
        subscribeButton.doClick();

        // Check if the button text changes after clicking
        Assertions.assertEquals("Unsubscribe", subscribeButton.getText());
    }

    @Test
    public void testLogoutButton() {
        JButton logoutButton = MainChannelUITest.findComponent(JButton.class, user1Frame, "logoutButton");

        // Click the logout button
        logoutButton.doClick();

        // Check if the user1 frame is disposed
        Assertions.assertFalse(user1Frame.isVisible(), "User1 frame should be disposed");
    }

}
