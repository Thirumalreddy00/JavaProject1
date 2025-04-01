package JavaProject1;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

// UserDatabase to manage registered users
class UserDatabase {
    static Map<String, String> registeredUsers = new HashMap<>();
    private static final String FILE_PATH = "users.txt";

    static {
        loadUsers();
    }

    private static void loadUsers() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    registeredUsers.put(parts[0], parts[1]);
                }
            }
        } catch (IOException e) {
            System.out.println("No existing user data found. Starting fresh.");
        }
    }

    static void saveUsers() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Map.Entry<String, String> entry : registeredUsers.entrySet()) {
                writer.write(entry.getKey() + ":" + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

public class HomePage extends JFrame implements ActionListener {
    private JButton signupButton, signinButton;

    public HomePage() {
        setTitle("Snake Game Home");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(2, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        signupButton = new JButton("Sign Up");
        signinButton = new JButton("Sign In");

        signupButton.addActionListener(this);
        signinButton.addActionListener(this);

        panel.add(signupButton);
        panel.add(signinButton);

        add(panel);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == signupButton) {
            new SignupPage();
        } else if (e.getSource() == signinButton) {
            new SignInPage();
        }
        dispose(); // Close the home page
    }

    public static void main(String[] args) {
        new HomePage();
    }
}

class SignupPage extends JFrame implements ActionListener {
    private JTextField usernameField;
    private JPasswordField passwordField, confirmPasswordField;
    private JButton signupButton;

    public SignupPage() {
        setTitle("Snake Game Signup");
        setSize(300, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField();

        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();

        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        confirmPasswordField = new JPasswordField();

        signupButton = new JButton("Sign Up & Play");
        signupButton.addActionListener(this);

        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(confirmPasswordLabel);
        panel.add(confirmPasswordField);
        panel.add(new JLabel()); // Empty cell for spacing
        panel.add(signupButton);

        add(panel);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
        } else if (!isValidUsername(username)) {
            JOptionPane.showMessageDialog(this, "Username must be at least 4 characters long and contain only letters (a-z, A-Z) and numbers (0-9), without special characters.", "Error", JOptionPane.ERROR_MESSAGE);
        } else if (UserDatabase.registeredUsers.containsKey(username)) {
            JOptionPane.showMessageDialog(this, "Username already exists. Please choose another.", "Error", JOptionPane.ERROR_MESSAGE);
        } else if (!isValidPassword(password)) {
            JOptionPane.showMessageDialog(this, "Password must be at least 8 characters long, include uppercase, lowercase, a digit, and a special character.", "Error", JOptionPane.ERROR_MESSAGE);
        } else if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            UserDatabase.registeredUsers.put(username, password);
            UserDatabase.saveUsers(); // Save user to file
            JOptionPane.showMessageDialog(this, "Signup successful! Welcome, " + username + "!", "Success", JOptionPane.INFORMATION_MESSAGE);
            startGame();
        }
    }

    private boolean isValidUsername(String username) {
        return username.matches("^[a-zA-Z0-9]{4,}$");
    }

    private boolean isValidPassword(String password) {
        return password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$");
    }

    private void startGame() {
        JFrame gameFrame = new JFrame("Snake Game");
        SnakeGame snakeGame = new SnakeGame(500, 500);

        gameFrame.add(snakeGame);
        gameFrame.pack();
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.setLocationRelativeTo(null);
        gameFrame.setVisible(true);

        dispose(); // Close the signup window
    }
}

class SignInPage extends JFrame implements ActionListener {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton signinButton;

    public SignInPage() {
        setTitle("Snake Game Sign In");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField();

        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();

        signinButton = new JButton("Sign In & Play");
        signinButton.addActionListener(this);

        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(new JLabel()); // Empty cell for spacing
        panel.add(signinButton);

        add(panel);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
        } else if (!UserDatabase.registeredUsers.containsKey(username)) {
            JOptionPane.showMessageDialog(this, "Username does not exist.", "Error", JOptionPane.ERROR_MESSAGE);
        } else if (!UserDatabase.registeredUsers.get(username).equals(password)) {
            JOptionPane.showMessageDialog(this, "Incorrect password for the given username.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Sign in successful! Welcome back, " + username + "!", "Success", JOptionPane.INFORMATION_MESSAGE);
            startGame();
        }
    }

    private void startGame() {
        JFrame gameFrame = new JFrame("Snake Game");
        SnakeGame snakeGame = new SnakeGame(500, 500);

        gameFrame.add(snakeGame);
        gameFrame.pack();
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.setLocationRelativeTo(null);
        gameFrame.setVisible(true);

        dispose(); // Close the sign-in window
    }
}
