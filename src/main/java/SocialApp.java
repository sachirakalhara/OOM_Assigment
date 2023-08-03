import javax.swing.*;

public class SocialApp {
    private static final MainChannel mainChannel = new MainChannel();

    public static void main(String[] args) {
        String username = getUsernameFromDialog();

        while (username != null && !isValidUsername(username)) {
            JOptionPane.showMessageDialog(null, "Invalid username");
            username = getUsernameFromDialog();
        }

        if (username != null) {
            showInterface(username);
        } else {
            System.exit(0);       // The user pressed cancel, terminate the application
        }
    }

    public static String getUsernameFromDialog() {
        return JOptionPane.showInputDialog(null, "Enter your username:");
    }

    public static boolean isValidUsername(String username) {
        // Add any validation logic here based on your requirements
        return username.equalsIgnoreCase("Main") || username.equalsIgnoreCase("User1")
                || username.equalsIgnoreCase("User2");
    }

    public static void showInterface(String username) {
        User1 user1 = new User1(mainChannel);
        User2 user2 = new User2(mainChannel);

        if ("Main".equalsIgnoreCase(username)) {
            JFrame mainFrame = new MainChannelUI(mainChannel, user1, user2);
            mainFrame.setVisible(true);
        } else if ("User1".equalsIgnoreCase(username)) {
            JFrame user1Frame = new User1(mainChannel);
            user1Frame.setVisible(true);
        } else if ("User2".equalsIgnoreCase(username)) {
            JFrame user2Frame = new User2(mainChannel);
            user2Frame.setVisible(true);
        }
    }

}
