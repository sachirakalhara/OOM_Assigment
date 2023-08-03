import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;

import static junit.framework.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class User2Test {

    private User2 user2Frame;

    @BeforeEach
    public void setup() {
        MainChannel mainChannel = new MainChannel();
        user2Frame = new User2(mainChannel);
        user2Frame.setVisible(true);
    }

    @AfterEach
    public void tearDown() {
        user2Frame.dispose();
    }

    @Test
    public void testComponents() {
        Assertions.assertTrue(user2Frame.isVisible(), "User2 should be visible");
        printComponentNames(user2Frame.getContentPane());

        JLabel welcomeLabel = MainChannelUITest.findComponent(JLabel.class, user2Frame, "welcomeLabel");
        assertEquals("Welcome to User2",welcomeLabel.getText());
    }

    @Test
    public void testPostAppears() {
        JTextArea postTextArea = findComponent(JTextArea.class, user2Frame, "postTextArea");
        JButton postButton = findComponent(JButton.class, user2Frame, "postButton");
        JTextArea contentViewArea1 = findComponent(JTextArea.class, user2Frame, "contentViewArea1");

        // Type text into the post text area
        postTextArea.setText("Test post text");

        // Click the post button
        postButton.doClick();

        // Check if the content view area has the new post text
        String expectedText = "--------------------------------\nTest post text\n\n";
        Assertions.assertTrue(contentViewArea1.getText().startsWith(expectedText));
    }

    @Test
    public void testEmptyPost() {
        JTextArea postTextArea = findComponent(JTextArea.class, user2Frame, "postTextArea");
        JButton postButton = findComponent(JButton.class, user2Frame, "postButton");

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
    public void testSubscribeButton() {
        JButton subscribeButton = findComponent(JButton.class, user2Frame, "subscribeButton");

        // Click the subscribe button
        subscribeButton.doClick();

        // Check if the subscribe button text changes
        Assertions.assertEquals("Unsubscribe", subscribeButton.getText());

        // Click the subscribe button again
        subscribeButton.doClick();

        // Check if the subscribe button text changes back
        Assertions.assertEquals("Subscribe", subscribeButton.getText());
    }

    @Test
    public void testLogoutButton() {
        JButton logoutButton = findComponent(JButton.class, user2Frame, "logoutButton");

        // Click the logout button
        logoutButton.doClick();

        // Check if the user2 frame is disposed
        Assertions.assertFalse(user2Frame.isVisible(), "User2 frame should be disposed");
    }

    private void printComponentNames(Container container) {
        for (Component component : container.getComponents()) {
            System.out.println("Component name: " + component.getName());
            if (component instanceof Container) {
                printComponentNames((Container) component);
            }
        }
    }

    private <T> T findComponent(Class<T> type, Container container, String name) {
        for (Component component : container.getComponents()) {
            if (type.isInstance(component) && name.equals(component.getName())) {
                return type.cast(component);
            }
            if (component instanceof Container) {
                T nestedComponent = findComponent(type, (Container) component, name);
                if (nestedComponent != null) {
                    return nestedComponent;
                }
            }
        }
        return null;
    }
}
