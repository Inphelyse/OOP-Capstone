import javax.swing.*;

public class SellerForm extends JFrame {
    private JPanel SellerPnl;
    private JPanel HeaderPnl;
    private JPanel BodyPnl;
    private JPanel NavigationPnl;
    private JButton dashboardButton;
    private JButton addProductsButton;
    private JButton FeedbackBtn;
    private JButton OrderHistoryBtn;
    private JPanel DataPnl;
    private JPanel Summary_Report_Pnl;
    private JPanel PnlMain;
    private JPanel DashboardPnl;
    private JPanel FeedbackPnl;
    private JPanel FeedBacks_and_Report;
    private JTable SubmissionsTbl;
    private JPanel Info_Details;
    private JPanel MyProductsPnl;
    private JTextField textField1;
    private JPanel ProductDetails;
    private JPanel AddProductPnl;
    private JPanel productInfo;
    private JTextField ProductName;
    private JTextField textField2;
    private JButton uploadProductImageButton;
    private JButton saveProductButton;
    private JButton clearButton;
    private JPanel OrderHistory;
    private JTable table1;
    private JButton logOutSeller;

    public SellerForm(){

        dashboardButton.addActionListener(e->showPage("dashboard"));

        logOutSeller.addActionListener(d->{
            BossStore.showLogInPanel();
            this.dispose();
        });

        setContentPane(PnlMain);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void showPage(String page) {
        // Home part

        boolean home = page.equals("dashboard");
        Summary_Report_Pnl.setVisible(home);
    }


}




