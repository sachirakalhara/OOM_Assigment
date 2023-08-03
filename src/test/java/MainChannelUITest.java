import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;

import static junit.framework.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class MainChannelUITest {

    private MainChannelUI mainFrame;
    private User1 user1Frame;

    @BeforeEach
    public void setup() {
        MainChannel mainChannel = new MainChannel();
        User2 user2 = new User2(mainChannel);
        user1Frame = new User1(mainChannel);
        mainFrame = new MainChannelUI(mainChannel, user1Frame, user2);
        mainFrame.setVisible(true);
    }

    @AfterEach
    public void tearDown() {
        mainFrame.dispose();
    }

    @Test
    public void testComponents() {
        Assertions.assertTrue(mainFrame.isVisible(), "MainChannel should be visible");
        MainChannelUITest.printComponentNames(mainFrame.getContentPane());

        JLabel welcomeLabel = findComponent(JLabel.class, mainFrame, "welcomeLabel");
        assertEquals("Welcome to Main Channel",welcomeLabel.getText());
    }

    @Test
    public void testPostButtonWithContent() {
        JTextArea postTextArea = findComponent(JTextArea.class, mainFrame, "postTextArea");
        JButton postButton = findComponent(JButton.class, mainFrame, "postButton");
        JTextArea contentViewArea = findComponent(JTextArea.class, mainFrame, "contentViewArea");

        // Type text into the post text area
        postTextArea.setText("This is a test post.");

        // Click the post button
        postButton.doClick();

        // Check if the content view area has the new post text
        String expectedText = "--------------------------------\nThis is a test post.\n\n";
        Assertions.assertTrue(contentViewArea.getText().startsWith(expectedText));
    }

    @Test
    public void testPostButtonWithEmptyContent() {
        JTextArea postTextArea = findComponent(JTextArea.class, mainFrame, "postTextArea");
        JButton postButton = findComponent(JButton.class, mainFrame, "postButton");

        // Empty the post text area
        postTextArea.setText("");

        // Click the post button
        postButton.doClick();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

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
    public void testLogoutButton() {
        JButton logoutButton = MainChannelUITest.findComponent(JButton.class, mainFrame, "logoutButton");

        // Click the logout button
        logoutButton.doClick();

        // Check if the user1 frame is disposed
        Assertions.assertFalse(mainFrame.isVisible(), "Main frame should be disposed");
    }

    @Test
    public void testPostAndViewAfterLogoutAndLogin() {
        // Get the postTextArea, postButton, and subscribeButton components
        JTextArea postTextArea = findComponent(JTextArea.class, mainFrame, "postTextArea");
        JButton postButton = findComponent(JButton.class, mainFrame, "postButton");
        JButton subscribeButton = findComponent(JButton.class, user1Frame, "subscribeButton");

        // Add a post to the postTextArea
        postTextArea.setText("Test post content");

        // Click the post button
        postButton.doClick();

        // Click the logout button
        JButton logoutButton = findComponent(JButton.class, mainFrame, "logoutButton");
        logoutButton.doClick();

        // if logging in as User1:
        user1Frame.setVisible(true);

        // Click the subscribe button
        subscribeButton.doClick();

        // Check if the added post is displayed in contentViewArea2
        JTextArea contentViewArea2 = findComponent(JTextArea.class, user1Frame, "contentViewArea2");
        String contentAfterSubscribe = contentViewArea2.getText();
        assertTrue(contentAfterSubscribe.contains("Test post content"), "Post should be displayed after subscribing");

        // Click the subscribe button again to unsubscribe
        subscribeButton.doClick();

        // Check if the contentViewArea2 is empty after unsubscribing
        String contentAfterUnsubscribe = contentViewArea2.getText();
        assertTrue(contentAfterUnsubscribe.isEmpty(), "Content should be empty after unsubscribing");
    }

    public static void printComponentNames(Container container) {
        for (Component component : container.getComponents()) {
            System.out.println("Component name: " + component.getName());
            if (component instanceof Container) {
                printComponentNames((Container) component);
            }
        }
    }

    public static <T> T findComponent(Class<T> type, Container container, String name) {
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
