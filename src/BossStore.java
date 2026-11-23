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
    //
    // Creates an already made account for admin
    private void AdminAccount() {
        new File("Persons").mkdir();
        File file4 = new File("Persons/admin.txt");


        if (file4.exists() && file4.length() > 0) return;

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file4, true))) {
            bw.write("Admin|Boss Tamaki|12345678|099912312|Secret");
            bw.newLine();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(logInPanel, "Error: " + ex.getMessage());
        }

        System.out.println("test file");
    }

    // Creates example courier accounts, same way the admin account is created
    private void CourierAccounts() {
        new File("Persons").mkdir();
        File file = new File("Persons/courier.txt");

        if (file.exists() && file.length() > 0) return;

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
            bw.write("Courier|Nico Carato|courier123|09991112233|Cebu City");
            bw.newLine();
            bw.write("Courier|Ana Reyes|delivery88|09171234567|Mandaue City");
            bw.newLine();
            bw.write("Courier|Luis Gomez|riderpass1|09228887766|Lapu-Lapu City");
            bw.newLine();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(logInPanel, "Error: " + ex.getMessage());
        }
    }

    public BossStore(){
        // Either seller or a customer (only choose one)
        ButtonGroup bg = new ButtonGroup();
        bg.add(customerRadioButton);
        bg.add(sellerRadioButton);

        customerRadioButton.setSelected(true);

        AdminAccount();
        CourierAccounts();

        signUpPanel.setVisible(false);


        showSignUpButton.addActionListener(e->{
            logInPanel.setVisible(false);   //SignUp Panel
            signUpPanel.setVisible(true);
        });

        backToLogInButton.addActionListener(e->{
            logInPanel.setVisible(true);    //LogIn Panel
            signUpPanel.setVisible(false);
        });

        signUpSubmitButton.addActionListener(e->{

            // Getting the inputted text in the panel and storing it
            String username = signUpUsernameField.getText();
            String password = signUpPasswordField.getText();
            String confirmPass = signUpConfirmPasswordField.getText();
            String phoneNumString = signUpPhoneField.getText();
            int phoneNum = 0;
            String address = signUpAddressField.getText();
            boolean flagUsername = false;
            File file = new File("Persons/customer.txt");

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
        });

        logInButton.addActionListener(e -> {
            String username = logInUsernameField.getText().trim();
            String password = new String(logInPasswordField.getPassword());

            boolean flagUsername = false;
            String account = null;

            // Checks if password or username is empty
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(logInPanel, "Please enter username || password");
                return;
            }

            File file = new File("Persons/admin.txt");
            File file2 = new File("Persons/seller.txt");
            File file3 = new File("Persons/customer.txt");
            File file4 = new File("Persons/courier.txt");

            // Checks if the account exist (length() is 0 if the file is missing or empty)
            if (file.length() == 0 && file2.length() == 0 && file3.length() == 0 && file4.length() == 0) {
                JOptionPane.showMessageDialog(logInPanel, "No accounts yet");
                return;
            }

            // Reads admin file
            if (file.exists()) {
                try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                    // This is for checking/reading the file, it splits each line
                    // 0=type(Admin, Seller, Customer, Courier), 1=username, 2=password, 3=phone, 4=address
                    //Ex. {Admin|Boss Tamaki|12345678|099912312|Secret}
                    String line;
                    while ((line = br.readLine()) != null) {
                        String[] parts = line.split("\\|");
                        if (parts.length < 5) {
                            continue;
                        }
                        if (parts[1].equals(username)) {
                            flagUsername = true;
                            if (parts[2].equals(password)) {
                                account = parts[0];
                            }
                            break;
                        }
                    }
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(logInPanel, "Error: " + ex.getMessage());
                    return;
                }
            }

            // Reads the seller file - only if the username was not found yet
            if (!flagUsername && file2.exists()) {
                try (BufferedReader br = new BufferedReader(new FileReader(file2))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        String[] parts = line.split("\\|");
                        if (parts.length < 5) {
                            continue;
                        }
                        if (parts[1].equals(username)) {
                            flagUsername = true;
                            if (parts[2].equals(password)) {
                                account = parts[0];
                            }
                            break;
                        }
                    }
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(logInPanel, "Error: " + ex.getMessage());
                    return;
                }
            }

            // Reads the customer file
            if (!flagUsername && file3.exists()) {
                try (BufferedReader br = new BufferedReader(new FileReader(file3))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        String[] parts = line.split("\\|");
                        if (parts.length < 5) {
                            continue;
                        }
                        if (parts[1].equals(username)) {
                            flagUsername = true;
                            if (parts[2].equals(password)) {
                                account = parts[0];
                            }
                            break;
                        }
                    }
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(logInPanel, "Error: " + ex.getMessage());
                    return;
                }
            }

            // Reads the courier file
            if (!flagUsername && file4.exists()) {
                try (BufferedReader br = new BufferedReader(new FileReader(file4))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        String[] parts = line.split("\\|");
                        if (parts.length < 5) {
                            continue;
                        }
                        if (parts[1].equals(username)) {
                            flagUsername = true;
                            if (parts[2].equals(password)) {
                                account = parts[0];
                            }
                            break;
                        }
                    }
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(logInPanel, "Error: " + ex.getMessage());
                    return;
                }


            }//

            // Checks if the acc username was found in any of the files, if !found then it will display error
            if (!flagUsername) {
                System.out.println("Not exist?");
                JOptionPane.showMessageDialog(logInPanel, "Username does not exist");
                logInUsernameField.setText("");
                logInPasswordField.setText("");
            } else if (account == null) {
                System.out.println("Hello testw");
                // Username exists but the password did not match
                JOptionPane.showMessageDialog(logInPanel, "Incorrect password");
                logInPasswordField.setText("");
            } else {
                System.out.println("Hello test3 Clearing");
                logInUsernameField.setText("");
                logInPasswordField.setText("");
                logInPanel.setVisible(false);

                // If the account type is Admin, show admin panel
                if (account.equals("Admin")) {
                    Admin adminForm = new Admin();
                    adminForm.setVisible(true);
                    this.dispose();
                } else {
                    //
                    JOptionPane.showMessageDialog(this, "Welcome, " + username + "! (" + account + ") Wala pay panel for Customer ug Seller");
                    System.out.println("Hello ");
                    logInPanel.setVisible(true);
                }
            }
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