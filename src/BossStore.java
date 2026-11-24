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

        if (type.equals("Admin")) {
            new Admin().setVisible(true);
        } else if (type.equals("Seller")) {
            new SellerForm().setVisible(true);
        } else if (type.equals("Courier")) {
            new CourierForm().setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Welcome, " + username + "! (Courier) Wala pay panel for Courier");
            return;
//            new CustomerForm().setVisible(true);
        }
        this.dispose();

        this.dispose();
    }

    private void signUp() {
        // Getting the inputted text in the panel and storing it
        String username = signUpUsernameField.getText();
        String password = signUpPasswordField.getText();
        String confirmPass = signUpConfirmPasswordField.getText();
        String phoneNumString = signUpPhoneField.getText();
        int phoneNum = 0;
        String address = signUpAddressField.getText();
        boolean flagUsername = false;
        File file = new File("Persons/customer.txt");

        if(file.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                // This checks if username already exist in the file
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split("\\|");
                    if (parts.length < 5) {
                        continue;
                    }
                    if (parts[1].equals(username)) {
                        flagUsername = true;
                        break;
                    }
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(signUpPanel, "Error: " + ex.getMessage());
                return;
            }
        }

        if(flagUsername) {
            JOptionPane.showMessageDialog(signUpPanel, "Username already exist!");
            return;
        }
        
        // Checks if username is Empty
        if(username.equals("")) {
            JOptionPane.showMessageDialog(signUpPanel, "Please enter a username");
            return;
        }

        // Checks if phone number is empty
        try {
            if(phoneNumString.isEmpty()) {
                JOptionPane.showMessageDialog(signUpPanel, "Please enter phone number");
                System.out.println("test phone");
                return;
            }
            phoneNum = Integer.parseInt(signUpPhoneField.getText());
        } catch(Exception ex) { // This checks if the inputted character is not a number, if not then error occurs
            JOptionPane.showMessageDialog(signUpPanel, "Enter valid phone number");
            System.out.println("test phone 2");
            signUpPhoneField.setText("");
            return;
        }

        // Checks if password is empty
        if(password.isEmpty()) {
            JOptionPane.showMessageDialog(signUpPanel, "Please a password");
            System.out.println("test pass");
            return;
        } else if(password.length() < 8) {  // This checks if the password is not above 7 characters or less than 8
            JOptionPane.showMessageDialog(signUpPanel, "Password must be at least 8 characters");
            System.out.println("test pass 2");
            return;
        }

        // Checks if confirmation password is empty
        if(confirmPass.isEmpty()) {
            JOptionPane.showMessageDialog(signUpPanel, "Confirm password");
            System.out.println("test confirm pass");
            return;
        } else if(!(confirmPass.equals(password))) {  // This checks if the confirmation password does not match the password
            JOptionPane.showMessageDialog(signUpPanel, "Password do not match");
            System.out.println("test cpnfirm pass 2");
            signUpConfirmPasswordField.setText("");
            return;
        }

        // Checks if address is empty
        if(address.isEmpty()) {
            JOptionPane.showMessageDialog(signUpPanel, "Enter your address");
            System.out.println("test address");
            return;
        }

        // This checks in sign up forum if the user chose either customer or seller. If the user chooses customer,
        // the filled up form will go directly to the Customer class and saves it to the file
        if(customerRadioButton.isSelected()) {
            persons.add(new Customer(username, password, phoneNum, address));
            savePerson("Customer", username, password, phoneNumString, address);
            System.out.println("Saved to the file. Custormer");
        } else if(sellerRadioButton.isSelected()) {
            persons.add(new Seller(username, password, phoneNum, address));
            savePerson("Seller", username, password, phoneNumString, address);
            System.out.println("Saved to the file. Seller");
        } else {
            JOptionPane.showMessageDialog(signUpPanel, "Please choose Customer or Seller");
            return;
        }

        clearFields();
        signUpPanel.setVisible(false);
        logInPanel.setVisible(true);
    }

    public int customerTotal() {
        File file1 = new File("Persons/customer.txt");
        int count = 0;

        if(file1.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(file1))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split("\\|");
                    if (parts.length < 5) {
                        continue;
                    }
                    count++;
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(logInPanel, "Error5: " + ex.getMessage());
            }
        }

        return count;

    }

    public int sellerTotal() {
        File file1 = new File("Persons/seller.txt");
        int count = 0;

        if(file1.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(file1))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split("\\|");
                    if (parts.length < 5) {
                        continue;
                    }
                    count++;
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(logInPanel, "Error5: " + ex.getMessage());
            }
        }

        return count;

    }

    public int courierTotal() {
        File file1 = new File("Persons/courier.txt");
        int count = 0;

        if(file1.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(file1))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split("\\|");
                    if (parts.length < 5) {
                        continue;
                    }
                    count++;
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(logInPanel, "Error5: " + ex.getMessage());
            }
        }

        return count;

    }

    public int totalPeople() {
        File file1 = new File("Persons/customer.txt");
        File file2 = new File("Persons/seller.txt");
        File file3 = new File("Persons/courier.txt");
        int count = 0;

        if(file1.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(file1))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split("\\|");
                    if (parts.length < 5) {
                        continue;
                    }
                    count++;
                }
                System.out.println("asda");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(logInPanel, "Error5: " + ex.getMessage());
            }
        }

        if(file2.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(file2))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length < 5) {
                        continue;
                    }
                    count++;
                }
                System.out.println("asdasda");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(logInPanel, "Error6: " + ex.getMessage());
            }
        }

        if(file3.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(file3))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length < 5) {
                        continue;
                    }
                    count++;
                }
                System.out.println("asdasda");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(logInPanel, "Error6: " + ex.getMessage());
            }
        }

        return count;
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