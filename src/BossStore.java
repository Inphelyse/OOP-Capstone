import javax.swing.*;

public class BossStore extends JFrame{

    private JPanel pnlMain;
    private JPanel SignUp;
    private JPanel LogIn;
    private JTextField textField1;
    private JPasswordField passwordField1;
    private JButton signInButton;
    private JButton signUpButton1;
    private JTextField textField2;
    private JRadioButton customerRadioButton1;
    private JRadioButton sellerRadioButton;
    private JButton signUpButton;

    public BossStore(){
        SignUp.setVisible(false);





        signUpButton1.addActionListener(e -> {
            LogIn.setVisible(false);
            SignUp.setVisible(true);
        });

    }

    public static void main(String[] args) {
        BossStore app = new BossStore();
        app.setContentPane(app.pnlMain);
        app.setSize(800,600);
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        app.setVisible(true);
    }
}
