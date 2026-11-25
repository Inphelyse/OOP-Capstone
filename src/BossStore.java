import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
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
    private JPanel AdminPnl;
    private JPanel Summary_Report_Pnl;
    private JPanel Report_Details_Admin;
    private JButton HomeBtn;
    private JButton SellersButton;
    private JPanel SellersPnl;
    private JPanel CustomersPnl;
    private JPanel CouriersPnl;
    private JButton CustomersBtn;
    private JButton CouriersBtn;
    private JButton FeedbackBtn;
    private JButton BansBtn;
    private JButton HistoryBtn;
    private JPanel FeedbackPnl;
    private JPanel BansPnl;
    private JPanel HistoryPnl;

    private ArrayList<Person> persons = new ArrayList<>();

    public BossStore(){
        // Either seller or a customer (only choose one)
        ButtonGroup bg = new ButtonGroup();
        bg.add(customerRadioButton);
        bg.add(sellerRadioButton);

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
            JOptionPane.showMessageDialog(logInPanel, "Please enter username || password");
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
            case "Admin" -> new Admin().setVisible(true);
            case "Seller" -> new SellerForm().setVisible(true);
            case "Courier" -> new CourierForm().setVisible(true);
            case "Customer" -> new CustomerForm().setVisible(true);
            default -> {
                //    JOptionPane.showMessageDialog(this, "Welcome, " + username + "! (Courier) Wala pay panel for Courier");
                logInUsernameField.setText("");
                logInPasswordField.setText("");
                return;
//            new CustomerForm().setVisible(true);
            }
        }
        this.dispose();
        // this.dispose();
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

        for (int i = 0; i < phoneNumString.length(); i++) {
            if (phoneNumString.charAt(i) < '0' || phoneNumString.charAt(i) > '9') {
                flagPhone= false;
                break;
            }
        }

        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(logInPanel, "Please enter a username");
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
            int phoneNum = Integer.parseInt(phoneNumString);
            Person p;
            if (customerRadioButton.isSelected()) {
                p = new Customer(username, password, phoneNum, address);
            } else {
                p = new Seller(username, password, phoneNum, address);
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

    private void showPage(String page) {
        // Home part

        boolean home = page.equals("home");
        Summary_Report_Pnl.setVisible(home);
        Report_Details_Admin.setVisible(home);

        // Other pages
        SellersPnl.setVisible(page.equals("sellers"));
        CustomersPnl.setVisible(page.equals("customers"));
        CouriersPnl.setVisible(page.equals("couriers"));
        FeedbackPnl.setVisible(page.equals("feedbacks"));
        BansPnl.setVisible(page.equals("bans"));
        HistoryPnl.setVisible(page.equals("history"));
    }

    public void clearFields() {
        signUpUsernameField.setText("");
        signUpPhoneField.setText("");
        signUpConfirmPasswordField.setText("");
        signUpPasswordField.setText("");
        signUpAddressField.setText("");
    }

    public void savePerson(String type, String username, String password, String phone, String address) {
        File file = new File("Persons/customer.txt");
        File file2 = new File("Persons/seller.txt");
        File file3 = new File("Persons/courier.txt");

        // "true" = append, so old accounts aren't overwritten
        if(type.equals("Customer")) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
                bw.write(type + "|" + username + "|" + password + "|" + phone + "|" + address);
                bw.newLine();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(signUpPanel, "Could not save account: " + ex.getMessage());
            }
        } else if(type.equals("Seller")) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file2, true))) {
                bw.write(type + "|" + username + "|" + password + "|" + phone + "|" + address);
                bw.newLine();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(signUpPanel, "Could not save account: " + ex.getMessage());
            }
        } else if(type.equals("Courier")) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file3, true))) {
                bw.write(type + "|" + username + "|" + password + "|" + phone + "|" + address);
                bw.newLine();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(signUpPanel, "Could not save account: " + ex.getMessage());
            }
        }
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