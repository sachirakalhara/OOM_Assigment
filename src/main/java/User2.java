import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class User2 extends JFrame implements Observer {

    private final JButton subscribeButton;
    private boolean isSubscribed = false;
    private final JTextArea contentViewArea2;

    public User2(MainChannel mainChannel) {
        setTitle("Social App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        JPanel topPanel = new JPanel(new BorderLayout());

        // Top panel for welcome label and subscription button
        JPanel welcomePanel = new JPanel(new BorderLayout());
        JLabel welcomeLabel = new JLabel("Welcome to User2");
        welcomeLabel.setName("welcomeLabel");
        welcomeLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 25));
        welcomeLabel.setForeground(Color.GREEN);
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        welcomePanel.add(welcomeLabel, BorderLayout.NORTH);

        JLabel mainChannelLabel = new JLabel("Main Channel");
        mainChannelLabel.setName("mainChannelLabel");
        mainChannelLabel.setFont(new Font("Arial", Font.BOLD, 15));
        mainChannelLabel.setForeground(Color.BLUE);
        welcomePanel.add(mainChannelLabel, BorderLayout.WEST);

        subscribeButton = new JButton("Subscribe");
        subscribeButton.setName("subscribeButton");
        subscribeButton.setFont(new Font("Arial", Font.BOLD, 15));
        subscribeButton.setBackground(Color.RED);
        welcomePanel.add(subscribeButton, BorderLayout.EAST);

        topPanel.add(welcomePanel, BorderLayout.NORTH);

        JTextArea postTextArea = new JTextArea(5, 30);
        postTextArea.setName("postTextArea");
        JButton postButton = new JButton("Post");
        postButton.setName("postButton");
        topPanel.add(postTextArea, BorderLayout.CENTER);
        topPanel.add(postButton, BorderLayout.EAST);

        // Center panel for content view areas
        JTextArea contentViewArea1 = new JTextArea();
        contentViewArea2 = new JTextArea();
        contentViewArea1.setEditable(false);
        contentViewArea2.setEditable(false);
        contentViewArea1.setName("contentViewArea1");
        contentViewArea2.setName("contentViewArea2");
        JScrollPane scrollPane1 = new JScrollPane(contentViewArea1);
        JScrollPane scrollPane2 = new JScrollPane(contentViewArea2);
        JLabel mainLabel = new JLabel("Main Channel");
        mainLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Panel to hold the mainLabel and contentViewArea2
        JPanel contentPanel2 = new JPanel(new BorderLayout());
        contentPanel2.add(mainLabel, BorderLayout.NORTH);
        contentPanel2.add(scrollPane2, BorderLayout.CENTER);

        // Logout button aligned to the right side
        JButton logoutButton = new JButton("Logout");
        logoutButton.setName("logoutButton");
        logoutButton.setAlignmentX(Component.RIGHT_ALIGNMENT);

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

        setLayout(new BorderLayout());
        add(topPanel, BorderLayout.NORTH);

        // Create a panel to hold both content view areas side by side
        JPanel contentPanel = new JPanel(new GridLayout(1, 2));
        contentPanel.add(scrollPane1);
        contentPanel.add(contentPanel2);

        // Add the panel with content view areas to the center of the frame
        add(contentPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.setBackground(Color.DARK_GRAY);
        bottomPanel.add(logoutButton, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);

        // Post button action listener
        postButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String postText = postTextArea.getText();
                if (!postText.trim().isEmpty()) {
                    String currentText1 = contentViewArea1.getText();
                    String newPost1 = "--------------------------------\n" + postText + "\n\n" + currentText1;
                    contentViewArea1.setText(newPost1);
                    postTextArea.setText("");
                } else {
                    JOptionPane.showMessageDialog(User2.this, "Empty content", "Warning", JOptionPane.WARNING_MESSAGE);

                }
            }
        });

        // Add action listener for the subscribeButton
        subscribeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toggleSubscription(contentViewArea2, mainChannel);
            }
        });
        mainChannel.addObserver(this);

        // Set custom colors for text areas
        contentViewArea1.setBackground(Color.LIGHT_GRAY);
        contentViewArea2.setBackground(Color.WHITE);
    }

    private void toggleSubscription(JTextArea contentViewArea2, MainChannel mainChannel) {
        isSubscribed = !isSubscribed;
        if (isSubscribed) {
            subscribeButton.setText("Unsubscribe");
            ArrayList<String> channelPosts = mainChannel.getMainChannelPosts();
            updateContentViewArea(channelPosts);
        } else {
            subscribeButton.setText("Subscribe");
            contentViewArea2.setText("");
        }
    }

    private void updateContentViewArea(ArrayList<String> mainChannelPosts) {
        StringBuilder content = new StringBuilder();
        for (String post : mainChannelPosts) {
            content.append("--------------------------------\n");
            content.append(post);
            content.append("\n\n");
        }
        contentViewArea2.setText(content.toString());
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof ArrayList<?>) {
            ArrayList<String> mainChannelPosts = (ArrayList<String>) arg;
            updateContentViewArea(mainChannelPosts);
        }
    }
}
