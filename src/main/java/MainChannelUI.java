import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainChannelUI extends JFrame {
    private final MainChannel mainChannel;
    private final User1 user1;
    private final User2 user2;

    public MainChannelUI(MainChannel mainChannel, User1 user1, User2 user2) {
        this.mainChannel = mainChannel;
        this.user1 = user1;
        this.user2 = user2;

        setTitle("Social App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Top panel for welcome message, text field, and post button
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(44, 62, 80));
        JLabel welcomeLabel = new JLabel("Welcome to Main Channel");
        welcomeLabel.setName("welcomeLabel");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        welcomeLabel.setForeground(new Color(52, 152, 219));
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JTextArea postTextArea = new JTextArea(5, 30);
        postTextArea.setName("postTextArea");
        JButton postButton = new JButton("Post");
        postButton.setName("postButton");
        topPanel.add(welcomeLabel, BorderLayout.NORTH);
        topPanel.add(postTextArea, BorderLayout.CENTER);
        topPanel.add(postButton, BorderLayout.EAST);

        // Center panel for content view area
        JTextArea contentViewArea = new JTextArea();
        contentViewArea.setName("contentViewArea");
        contentViewArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(contentViewArea);

        // Logout button aligned to the right side
        JButton logoutButton = new JButton("Logout");
        logoutButton.setName("logoutButton");
        logoutButton.setAlignmentX(Component.RIGHT_ALIGNMENT);
        logoutButton.setForeground(new Color(231, 76, 60));
        // Logout button action listener
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                String newUsername = SocialApp.getUsernameFromDialog();
                if (newUsername == null) {
                    // User pressed cancel, terminate the application
                    System.exit(0);
                } else if (SocialApp.isValidUsername(newUsername)) {
                    SocialApp.showInterface(newUsername);
                }
            }
        });

        // Layout customization
        setLayout(new BorderLayout());
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(44, 62, 80));
        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.add(logoutButton, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);

        // Post button action listener
        postButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String postText = postTextArea.getText();
                if (!postText.trim().isEmpty()) {
                    String currentText1 = contentViewArea.getText();
                    String newPost1 = "--------------------------------\n" + postText + "\n\n" + currentText1;
                    contentViewArea.setText(newPost1);
                    postTextArea.setText("");
                    mainChannel.addPost(postText);
                } else {
                    JOptionPane.showMessageDialog(MainChannelUI.this, "Empty content", "Warning", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        // Load previous posts into the content view area when MainChannelUI is initialized
        StringBuilder content = new StringBuilder();
        for (String post : mainChannel.getMainChannelPosts()) {
            content.append("--------------------------------\n");
            content.append(post);
            content.append("\n\n");
        }
        contentViewArea.setText(content.toString());
    }
}
