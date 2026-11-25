import javax.swing.*;

public class CustomerForm extends JFrame{
    // Root panel (bound in CustomerForm.form)
    private JPanel pnlMain;

    // Header
    private JTextField txtSearch;
    private JButton btnSearch;
    private JLabel lblHello;
    private JButton btnCart;
    private JButton btnLogout;

    // Sidebar
    private JButton btnShop;
    private JButton btnMyCart;
    private JButton btnMyOrders;
    private JButton btnMyAccount;
    private JButton btnAllProducts;
    private JButton btnReport;
    private JPanel showCaseItemPnl;
    private JPanel AllproductsPnl;
    private JComboBox cmbSort;
    private JButton btnAdd1;
    private JButton btnAdd2;
    private JButton btnAdd3;
    private JButton btnAdd4;
    private JButton btnAdd5;
    private JButton btnAdd6;
    private JPanel RightShowCasePnl;

    // Pages: row 0 and row 1 of the center area
    private JPanel pnlShowcase;   // row 0 - shop showcase (visible at start)
    private JPanel pnlCartPage;   // row 1 - shopping cart page (hidden at start)

    // Shopping cart page
    private JButton btnProceedCheckout;
    private JButton btnContinueShopping;
    private JButton btnBackToShop;

    public CustomerForm(){
       setContentPane(pnlMain);
       setExtendedState(JFrame.MAXIMIZED_BOTH);
       setLocationRelativeTo(null);
       setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
   }
}