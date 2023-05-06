package project;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The MenuWindow class demonstrates a menu system.
 */

public class MainWindow extends JFrame {
    private static JTextField usernameField;
    private static JTextField passwordField;
    private static JButton loginButton;
    // create object of menu panel
    private MenuPanel menuPanel;
    private static JMenuBar menuBar;
    private static JLabel usernameLabel;
    private static JLabel passwordLabel,Welcome;
    private static String approvedName= "Java Project";
    private static String approvedPassword = "Password";

    /**
     * Constructor
     */

    public MainWindow() {
        // Set the title.
        setTitle("Main Window");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        menuPanel = new MenuPanel(); // initialize menu panel object
        setJMenuBar(menuPanel.getMenuBar()); // get Window menu from the menu panel
        menuPanel.setCurrentWindow(this); // set current window

        Welcome = new JLabel("Welcome");
        Font font = Welcome.getFont();
        float size = font.getSize() + 100.0f; // increase the font size by 100
        font = font.deriveFont(size); // create a new font object with the new size
        Welcome.setFont(font); // set the new font for the label

        JPanel centerPanel = new JPanel();
        centerPanel.add(Welcome);
        centerPanel.setLayout(new GridBagLayout());

        add(centerPanel);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        // create login screen
        JFrame loginFrame = new JFrame();
        JPanel loginPanel = new JPanel(new GridBagLayout());
        GridBagConstraints loginConstraints = new GridBagConstraints();
        loginConstraints.insets = new Insets(10, 10, 10, 10);
        loginConstraints.gridx = 0;
        loginConstraints.gridy = 0;
        loginConstraints.anchor = GridBagConstraints.CENTER;

        usernameLabel = new JLabel("Username:");
        usernameField = new JTextField(15);
        passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(15);
        loginButton = new JButton("Login");
        loginButton.addActionListener(new loginListener());

        loginPanel.add(usernameLabel, loginConstraints);
        loginConstraints.gridx = 1;
        loginPanel.add(usernameField, loginConstraints);
        loginConstraints.gridx = 0;
        loginConstraints.gridy = 1;
        loginPanel.add(passwordLabel, loginConstraints);
        loginConstraints.gridx = 1;
        loginPanel.add(passwordField, loginConstraints);
        loginConstraints.gridx = 0;
        loginConstraints.gridy = 2;
        loginConstraints.gridwidth = 2;
        loginPanel.add(loginButton, loginConstraints);

        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.add(loginPanel);
        loginFrame.pack();
        loginFrame.setLocationRelativeTo(null);
        loginFrame.setVisible(true);

        // hide menu bar until login is successful
        menuBar = new JMenuBar();
        menuBar.setVisible(false);

        // create MainWindow object after successful login
        while(!menuBar.isVisible()) {
            // wait for user to login
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        new MainWindow();
    }

    private static class loginListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String password = passwordField.getText();
            String userName =  usernameField.getText();
            if(userName.equals(approvedName) && password.equals(approvedPassword)) {
                // User has entered the correct credentials
                menuBar.setVisible(true); // show menu bar
                ((JFrame)((JButton) e.getSource()).getTopLevelAncestor()).dispose(); // dispose of login window
            } else {
                JOptionPane.showMessageDialog(null, "Invalid username or password. Please try again.");
            }
        }
    }
}

