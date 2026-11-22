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

    public BossStore(){

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

        logInButton.addActionListener(e->{
            boolean flag = false;

            for(int i = 0; i < size; i++) {
                System.out.println("hello");
            }
            logInPanel.setVisible(false);
            AdminPnl.setVisible(true);
            setExtendedState(JFrame.MAXIMIZED_BOTH);
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
        new File("Persons").mkdir();   // creates the folder if it doesn't exist yet

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
