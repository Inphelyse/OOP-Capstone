import javax.swing.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
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
    private JButton myProductButton;
    private JPanel FeedbackPnl;
    private JLabel date;
    private JPanel FeedBacks_and_Report;
    private JTable SubmissionsTbl;
    private JPanel Info_Details;
    private JButton LogOutSeller;

    public SellerForm(){

        LogOutSeller.addActionListener(e->{
            BossStore.showLogInPanel();
            this.dispose();
        });


        dashboardButton.addActionListener(e->showPage("dashboard"));
        myProductButton.addActionListener(e->showPage("Myproducts"));
        addProductsButton.addActionListener(e->showPage("addproducts"));
        FeedbackBtn.addActionListener(e->showPage("feedback"));
        OrderHistoryBtn.addActionListener(e->showPage("OrderHistory"));

        setContentPane(PnlMain);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy");
        date.setText(LocalDate.now().format(formatter));


    }

    private void showPage(String page) {
        // Home part

        boolean home = page.equals("dashboard");
        DashboardPnl.setVisible(home);
        MyProductsPnl.setVisible(page.equals("Myproducts"));
        AddProductPnl.setVisible(page.equals("addproducts"));
        FeedbackPnl.setVisible(page.equals("feedback"));
        OrderHistory.setVisible(page.equals("OrderHistory"));

    }





}




