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
    private JPanel HeaderPnl;
    private JPanel BodyPnl;
    private JPanel NavigationPnl;
    private JPanel DataPnl;
    private JPanel Summary_Report_Pnl;
    private JPanel Report_Details_Admin;
    private JTable ReportsTbl;
    private JButton HomeBtn;
    private JButton SellersButton;
    private JPanel SellersPnl;
    private JPanel CustomersPnl;
    private JPanel CouriersPnl;
    private JButton CustomersBtn;
    private JButton CouriersBtn;

    private ArrayList<Person> persons = new ArrayList<>();

    // Creates an already made account for admin
    private void AdminAccount() {
        File folder = new File("Persons");
        folder.mkdir();
        File file = new File(folder, "persons.txt");

        if (file.exists() && file.length() > 0) return;

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
            bw.write("Admin|Boss Tamaki|12345678|099912312|Secret");
            bw.newLine();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(logInPanel, "Error: " + ex.getMessage());
        }

        System.out.println("test file");
    }

    public BossStore(){
        AdminAccount();

        signUpPanel.setVisible(false);
        SellersPnl.setVisible(false);
        AdminPnl.setVisible(false);

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

            new File("Persons").mkdir();
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


//            try {
//                BufferedWriter bw1 = new BufferedWriter(new FileWriter("Persons/file.txt"), 1024);
//                bw1.write(String.valueOf(persons));
//                bw1.close();
//            } catch(IOException ex) {
//                System.out.println("An error occured: " + ex.getMessage());
//            }
        });

        logInButton.addActionListener(e -> {
            String username = logInUsernameField.getText().trim();
            String password = new String(logInPasswordField.getPassword());

            boolean flagUsername = false;
            String account = null;

            // Checks if password or username is empty
            if (username.isEmpty() && password.isEmpty()) {
                JOptionPane.showMessageDialog(logInPanel, "Please enter username and password");
                System.out.println("test pass");
                return;
            }


            File file = new File("Persons/persons.txt");

//            if (file.exists() ) {
//                System.out.println("test file12");
//                JOptionPane.showMessageDialog(logInPanel, "No accounts yet");
//            }

            // Checks if the account exist
            if (!file.exists() || file.length() == 0) {
                JOptionPane.showMessageDialog(logInPanel, "No accounts yet");
                System.out.println("No account test");
                return;
            }

//            try(BufferedReader br = new BufferedReader(new FileReader(file))) {
//                String line;
//                while(!(line = br.readLine())) {
//
//                }
//            } catch(NullPointerException ex) {
//                JOptionPane.showMessageDialog(logInPanel, "Error: " + ex.getMessage());
//
//                return;
//            } catch (IOException ex) {
//                throw new RuntimeException(ex);
//            }

            // This code is used for reading the Person folder file (persons.txt)
            try(BufferedReader br = new BufferedReader(new FileReader(file))) {
                System.out.println("Reading a file");
                String line;
                    // This is for checking/reading the file, it splits each line
                    // 0=type(ADmin, Seller, Customer, Currier), 1=username, 2=password, 3=phone, 4=address
                //Ex. {Admin|Boss Tamaki|12345678|099912312|Secret}
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split("\\|");
                    if (parts[1].equals(username)) {
                        flagUsername = true;
                        if (parts[2].equals(password)) {
                            account = parts[0];
                        }
                        break;
                    }
                }
            } catch (IOException ex) {
                System.out.println("Error 12");
                JOptionPane.showMessageDialog(logInPanel, "Error: " + ex.getMessage());

                return;
            }

            // Checks if username was not found in the file
            if (!flagUsername) {
                JOptionPane.showMessageDialog(logInPanel, "Username does not exist");
                clearFields();
            } else if (account == null) {
                JOptionPane.showMessageDialog(logInPanel, "Incorrect password");

                logInPasswordField.setText("");
            } else {
                logInUsernameField.setText("");
                logInPasswordField.setText("");
                logInPanel.setVisible(false);

                // If the username that were inputted is the admin, the adminPanel will be set visible
                if (account.equals("Admin")) {
                    System.out.println("Admin account added to the file");
                    AdminPnl.setVisible(true);
                    setExtendedState(JFrame.MAXIMIZED_BOTH);
                } else {
                    //
                    JOptionPane.showMessageDialog(this, "Welcome, " + username + "! (" + account + ") Wala pay panel for Customer ug Seller");
                    System.out.println("Hello ");
                    logInPanel.setVisible(true);
                }
            }
        });

//        ButtonGroup bg = new ButtonGroup();
//        bg.add(customerRadioButton);
//        bg.add(sellerRadioButton);
//
//        SellersPnl.setVisible(false);
//        AdminPnl.setVisible(false);
//        signUpPanel.setVisible(false);
//
//        backToLogInButton.addActionListener(e->{
//            logInPanel.setVisible(true);
//            signUpPanel.setVisible(false);
//
//
//        });
//
//
//        logInButton.addActionListener(e->{
//
//           String LoginUsername = logInUsernameField.getText();
//           String LoginPassword = logInPasswordField.getText();
//
//            if(LoginPassword.isEmpty() && LoginUsername.isEmpty()){
//                JOptionPane.showMessageDialog(logInPanel, "Please enter username and password!", "ERROR!", JOptionPane.INFORMATION_MESSAGE);
//            }else if(LoginUsername.isEmpty()) {
//                JOptionPane.showMessageDialog(logInPanel, "Please enter username!", "ERROR!", JOptionPane.INFORMATION_MESSAGE);
//            }else if(LoginPassword.isEmpty()){
//                JOptionPane.showMessageDialog(logInPanel, "Please enter password!", "ERROR!", JOptionPane.INFORMATION_MESSAGE);
//            }else{
//                logInPanel.setVisible(false);
//                AdminPnl.setVisible(true);
//                setExtendedState(JFrame.MAXIMIZED_BOTH);
//            }
//        });
//
//
//        signUpSubmitButton.addActionListener(e->{
//            String signupuser = signUpUsernameField.getText();
//            String  signuppass = signUpPasswordField.getText();
//            String signupcpass = signUpConfirmPasswordField.getText();
//            String phonenum = signUpPhoneField.getText();
//            String add = signUpAddressField.getText();
//
//            if(signupuser.isEmpty() && signuppass.isEmpty()){
//                 JOptionPane.showMessageDialog(signUpPanel, "Please enter username and password!", "ERROR!", JOptionPane.INFORMATION_MESSAGE);
//            }else if(signupuser.isEmpty()) {
//                JOptionPane.showMessageDialog(signUpPanel, "Please enter username!", "ERROR!", JOptionPane.INFORMATION_MESSAGE);
//            }else if(signuppass.isEmpty()){
//                 JOptionPane.showMessageDialog(signUpPanel, "Please enter password!", "ERROR!", JOptionPane.INFORMATION_MESSAGE);
//            }else if(phonenum.isEmpty() || !phonenum.chars().allMatch(Character::isDigit)){
//                 JOptionPane.showMessageDialog(signUpPanel, "Please enter valid number", "ERROR!", JOptionPane.INFORMATION_MESSAGE);
//            }else if(!signupcpass.equals(signuppass)) {
//                JOptionPane.showMessageDialog(signUpPanel, "Password does not match", "ERROR!", JOptionPane.INFORMATION_MESSAGE);
//            }else if(add.isEmpty()) {
//                 JOptionPane.showMessageDialog(signUpPanel, "Please enter address", "ERROR!", JOptionPane.INFORMATION_MESSAGE);
//            }
//        });


        HomeBtn.addActionListener(d -> showPage("home"));
        SellersButton.addActionListener(d -> showPage("sellers"));
        CustomersBtn.addActionListener(d -> showPage("customers"));
        CouriersBtn.addActionListener(d -> showPage("couriers"));


        showSignUpButton.addActionListener(e -> {
            logInPanel.setVisible(false);
            signUpPanel.setVisible(true);

           signUpUsernameField.setText("");
            signUpPasswordField.setText("");
            signUpConfirmPasswordField.setText("");
             signUpPhoneField.setText("");
             signUpAddressField.setText("");



        });

        Summary_Report_Pnl.setVisible(true);
        Report_Details_Admin.setVisible(true);

        // Other pages
        SellersPnl.setVisible(false);
        CustomersPnl.setVisible(false);
        CouriersPnl.setVisible(false);
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
    }

    public void clearFields() {
        signUpUsernameField.setText("");
        signUpPhoneField.setText("");
        signUpConfirmPasswordField.setText("");
        signUpPasswordField.setText("");
        signUpAddressField.setText("");
    }

    public void savePerson(String type, String username, String password, String phone, String address) {
        // Creates the folder if it doesn't exist yet
        new File("Persons").mkdir();

        // "true" = append, so old accounts aren't overwritten
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("Persons/persons.txt", true))) {
            bw.write(type + "|" + username + "|" + password + "|" + phone + "|" + address);
            bw.newLine();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(signUpPanel, "Could not save account: " + ex.getMessage());
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
