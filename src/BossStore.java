import javax.swing.*;

public class BossStore extends JFrame{

    private JPanel pnlMain;
    private JPanel SignUp;
    private JPanel LogIn;
    private JButton signInButton;
    private JButton signUpButton1;
    private JTextField UserSignup;
    private JRadioButton customerRadioButton1;
    private JRadioButton sellerRadioButton;
    private JButton signUpButton;
    private JTextField UserLogIn;
    private JTextField passLogin;
    private JTextField PassSignUp;
    private JTextField CpassSignUp;
    private JTextField phnum;
    private JTextField addSignup;
    private JButton signInButton2;
    private JPanel AdminPnl;
    private JPanel HeaderPnl;
    private JPanel BodyPnl;
    private JPanel NavigationPnl;
    private JPanel DataPnl;
    private JPanel Summary_Report_Pnl;
    private JPanel Info_Details_Pnl;
    private JPanel FeedBack_reports_Pnl;
    private JPanel Report_Details_Admin;
    private JTable ReportsTbl;
    private JLabel SubjectReport;
    private JLabel Name_and_Role;



    public BossStore(){

        ButtonGroup bg = new ButtonGroup();
        bg.add(customerRadioButton1);
        bg.add(sellerRadioButton);



        AdminPnl.setVisible(false);
        SignUp.setVisible(false);

        signInButton2.addActionListener(e->{
            LogIn.setVisible(true);
            SignUp.setVisible(false);


        });


        signInButton.addActionListener(e->{

           String LoginUsername = UserLogIn.getText();
           String LoginPassword = passLogin.getText();

            if(LoginPassword.equals("") && LoginUsername.equals("")){
                JOptionPane.showMessageDialog(LogIn, "Please enter username and password!", "ERROR!", JOptionPane.INFORMATION_MESSAGE);
            }else if(LoginUsername.equals("")) {
                JOptionPane.showMessageDialog(LogIn, "Please enter username!", "ERROR!", JOptionPane.INFORMATION_MESSAGE);
            }else if(LoginPassword.equals("")){
                JOptionPane.showMessageDialog(LogIn, "Please enter password!", "ERROR!", JOptionPane.INFORMATION_MESSAGE);
            }else{
                LogIn.setVisible(false);
                AdminPnl.setVisible(true);
                setExtendedState(JFrame.MAXIMIZED_BOTH);
            }

        });


 signUpButton.addActionListener(e->{
     String signupuser = UserSignup.getText();
    String  signuppass = PassSignUp.getText();
    String signupcpass = CpassSignUp.getText();
    String phonenum = phnum.getText();
   String add = addSignup.getText();





     if(signupuser.equals("") && signuppass.equals("")){
         JOptionPane.showMessageDialog(SignUp, "Please enter username and password!", "ERROR!", JOptionPane.INFORMATION_MESSAGE);
     }else if(signupuser.equals("")) {
         JOptionPane.showMessageDialog(SignUp, "Please enter username!", "ERROR!", JOptionPane.INFORMATION_MESSAGE);
     }else if(signuppass.equals("")){
         JOptionPane.showMessageDialog(SignUp, "Please enter password!", "ERROR!", JOptionPane.INFORMATION_MESSAGE);
     }else if(phonenum.isEmpty() || !phonenum.chars().allMatch(Character::isDigit)){
         JOptionPane.showMessageDialog(SignUp, "Please enter valid number", "ERROR!", JOptionPane.INFORMATION_MESSAGE);
     }else if(!signupcpass.equals(signuppass)) {
        JOptionPane.showMessageDialog(SignUp, "Password does not match", "ERROR!", JOptionPane.INFORMATION_MESSAGE);
    }else if(add.equals("")) {
         JOptionPane.showMessageDialog(SignUp, "Please enter address", "ERROR!", JOptionPane.INFORMATION_MESSAGE);

     }



 });





        signUpButton1.addActionListener(e -> {
            LogIn.setVisible(false);
            SignUp.setVisible(true);

           UserSignup.setText("");
            PassSignUp.setText("");
            CpassSignUp.setText("");
             phnum.setText("");
             addSignup.setText("");



        });

    }


    public static void main(String[] args) {
        BossStore app = new BossStore();
        app.setContentPane(app.pnlMain);
        app.pack();
        app.setLocationRelativeTo(null);
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        app.setVisible(true);


    }
}
