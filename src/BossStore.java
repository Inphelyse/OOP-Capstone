import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class BossStore extends JFrame{
    public static int size = 0;

    private JPanel panelMain;
    private JPanel signUpPanel;
    private JPanel logInPanel;
    private JButton logInButton;
    private JButton showSignUpButton;
    private JTextField signUpUsernameField;
    private JRadioButton customerRadioButton;
    private JRadioButton sellerRadioButton;
    private JButton signUpSubmitButton;
    private JTextField logInUsernameField;
    private JPasswordField logInPasswordField;
    private JTextField signUpPasswordField;
    private JTextField signUpConfirmPasswordField;
    private JTextField signUpPhoneField;
    private JTextField signUpAddressField;
    private JButton backToLogInButton;
    private JRadioButton courierRadioButton;

    public BossStore(){
        // Either seller or a customer (only choose one)
        ButtonGroup bg = new ButtonGroup();
        bg.add(customerRadioButton);
        bg.add(sellerRadioButton);
        bg.add(courierRadioButton);

        customerRadioButton.setSelected(true);

        Files.defaultAccount();

        signUpPanel.setVisible(false);

        showSignUpButton.addActionListener(e->{
            logInPanel.setVisible(false);   //SignUp Panel
            signUpPanel.setVisible(true);
        });

        backToLogInButton.addActionListener(e->{
            logInPanel.setVisible(true);    //LogIn Panel
            signUpPanel.setVisible(false);
        });

        showSignUpButton.addActionListener(e -> {
            logInPanel.setVisible(false);
            signUpPanel.setVisible(true);

            signUpUsernameField.setText("");
            signUpPasswordField.setText("");
            signUpConfirmPasswordField.setText("");
            signUpPhoneField.setText("");
            signUpAddressField.setText("");

        });

        signUpSubmitButton.addActionListener(e-> signUp());
        logInButton.addActionListener(e -> logIn());
    }

    private void logIn() {
        String username = logInUsernameField.getText().trim();
        String password = new String(logInPasswordField.getPassword());
        String[] account = Files.findAccount(username);
        //       boolean flagUsername = false;
        //       String account = null;

        // Checks if password or username is empty
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(logInPanel, "Please enter username and password");
            return;
        }

        // It checks if the username do exists
        if (account == null) {
            JOptionPane.showMessageDialog(logInPanel, "Username does not exist");
            logInUsernameField.setText("");
            logInPasswordField.setText("");
            return;
        }

        String type = account[0];
        // It checks if password is correct
        if (!account[2].equals(password)) {
            JOptionPane.showMessageDialog(logInPanel, "Incorrect password");
            logInPasswordField.setText("");
            return;
        }


        switch (type) {
            case "Admin" -> new Admin(account[1], type).setVisible(true);
            case "Seller" -> new SellerForm(account[1], type).setVisible(true);
            case "Courier" -> new CourierForm(account[1], type).setVisible(true);
            case "Customer" -> new CustomerForm(new CustomerClass(account[1], account[2],account[3], account[4])).setVisible(true);
            default -> {
                JOptionPane.showMessageDialog(this, "Welcome, " + username + "! (Customer) Wala pay panel for Customer");
                logInUsernameField.setText("");
                logInPasswordField.setText("");
                return;
//            new CustomerForm().setVisible(true);
            }
        }
        this.dispose();
    }

    private void signUp() {
        // Getting the inputted text in the panel and storing it
        // Getting the inputted text in the panel and storing it
        String username = signUpUsernameField.getText();
        String password = signUpPasswordField.getText();
        String confirmPass = signUpConfirmPasswordField.getText();
        String phoneNumString = signUpPhoneField.getText().trim();
        String address = signUpAddressField.getText();
        boolean flagPhone = true;

        // Check if phoneNum is a number
        for (int i = 0; i < phoneNumString.length(); i++) {
            if (phoneNumString.charAt(i) < '0' || phoneNumString.charAt(i) > '9') {
                flagPhone= false;
                break;
            }
        }

        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(logInPanel, "Please enter a username");
        } else if(Files.findAccount(username) != null) {
            JOptionPane.showMessageDialog(logInPanel, "Username already exist");
        } else if(Files.findPhoneNum(Integer.parseInt(phoneNumString))) {
            JOptionPane.showMessageDialog(logInPanel, "Phone number already taken");
        } else if (phoneNumString.isEmpty()) {
            JOptionPane.showMessageDialog(logInPanel, "Please enter phone number");
        } else if (phoneNumString.length() < 7 || phoneNumString.length() > 15 || !flagPhone) {
            JOptionPane.showMessageDialog(logInPanel, "Enter valid phone number");
            signUpPhoneField.setText("");
        } else if (password.isEmpty()) {
            JOptionPane.showMessageDialog(logInPanel, "Please enter a password");
        } else if (password.length() < 8) {
            JOptionPane.showMessageDialog(logInPanel, "Password must be at least 8 characters");
        } else if (confirmPass.isEmpty()) {
            JOptionPane.showMessageDialog(logInPanel, "Confirm password");
        } else if (!confirmPass.equals(password)) {
            JOptionPane.showMessageDialog(logInPanel, "Password do not match");
            signUpConfirmPasswordField.setText("");
        } else if (address.isEmpty()) {
            JOptionPane.showMessageDialog(logInPanel, "Enter your address");
        } else {
            Person p;
            if (customerRadioButton.isSelected()) {
              //  p = new CustomerClass(username, password, phoneNumString, address);
                p = PersonFactory.createPerson("Customer", username, password, phoneNumString, address);

            } else if(sellerRadioButton.isSelected()){
                p = PersonFactory.createPerson("Seller", username, password, phoneNumString, address);
            } else {
                p = PersonFactory.createPerson("Courier", username, password, phoneNumString, address);
            }

            Files.saveAccount(p);
            JOptionPane.showMessageDialog(logInPanel, "Account created!");
            clearFields();
            signUpPanel.setVisible(false);
            logInPanel.setVisible(true);
        }

    }

    public static void showLogInPanel() {
        BossStore app = new BossStore();
        app.setContentPane(app.panelMain);
        app.pack();
        app.setLocationRelativeTo(null);
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        app.setVisible(true);
    }

    public void clearFields() {
        signUpUsernameField.setText("");
        signUpPhoneField.setText("");
        signUpConfirmPasswordField.setText("");
        signUpPasswordField.setText("");
        signUpAddressField.setText("");
    }

    public static void main(String[] args) {
        BossStore app = new BossStore();
        app.setContentPane(app.panelMain);
        app.pack();
        app.setLocationRelativeTo(null);
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        app.setVisible(true);


    }
}