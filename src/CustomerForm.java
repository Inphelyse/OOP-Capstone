import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CustomerForm extends JFrame {
    // Root panel (bound in CustomerForm.form)
    private JPanel pnlMain;

    // Header
    private JPanel CustomerHeaderPnl;
    private JPanel CustomerNavigationPnl;
    private JPanel MyCartPnl;
    private JPanel TrackOrderPnl;
    private JButton btnTrackBackToShop;
    private JButton btnContactSupport;
    private JPanel MyAccountPnl;
    private JButton btnEditProfile;
    private JButton btnEditAddress;
    private JButton btnAddAddress;
    private JButton btnChangePassword;
    private JButton btnManagePayment;
    private JButton btnEditPreferences;
    private JPanel ReportFeedbackPnl;
    private JRadioButton rbReportIssue;
    private JRadioButton rbShareFeedback;
    private JComboBox cmbCategory;
    private JTextField txtRelatedOrder;
    private JTextField txtSubject;
    private JTextArea txtDetails;
    private JLabel lblCharCount;
    private JButton btnAttach;
    private JButton btnSubmitReport;
    private JButton btnCancelReport;
    private JTextField txtSearch;
    private JButton btnSearch;
    private JLabel lblHello;
    private JButton btnCart;
    private JButton logOutCustomer;

    // Sidebar
    private JButton btnShop;
    private JButton btnMyCart;
    private JButton btnMyOrders;
    private JButton btnMyAccount;
    private JButton btnReport;
    private JPanel showCaseItemPnl;
    private JPanel AllproductsPnl;
    private JButton btnAdd1;
    private JPanel RightShowCasePnl;

    // Pages: row 0 and row 1 of the center area
    private JPanel pnlShowcase;   // row 0 - shop showcase (visible at start)
    private JPanel pnlCartPage;   // row 1 - shopping cart page (hidden at start)

    // Shopping cart page
    private JButton btnProceedCheckout;
    private JButton btnContinueShopping;
    private JButton btnBackToShop;
    private JLabel UserNameJLbl;
    private JPanel InsertItemsPnl;
    private JScrollPane shopScrollPane;

    public CustomerForm(CustomerClass customer) {
        setContentPane(pnlMain);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        showPage("Shop");
        UserNameJLbl.setText("Hello! " + customer.getName() + " What's on your mind?");

        loadProducts(customer);


        btnMyCart.addActionListener(e -> {
                setActiveButton(btnMyCart);
                showPage("Cart");
        });
        btnShop.addActionListener(e -> {
            setActiveButton(btnShop);
            showPage("Shop");
        });
        btnMyOrders.addActionListener(e -> {
            setActiveButton(btnMyOrders);
            showPage("TrackOrder");
        });
        btnMyAccount.addActionListener(e -> {
            setActiveButton(btnMyAccount);
            showPage("MyAccount");
        });
        btnReport.addActionListener(e -> showPage("ReportFeedback"));

        logOutCustomer.addActionListener(d->{
            BossStore.showLogInPanel();
            this.dispose();
        });


        shopScrollPane.setBorder(null);
        shopScrollPane.getViewport().setBackground(Color.decode("#232D4D"));
        shopScrollPane.getVerticalScrollBar().setUnitIncrement(16);
    }

    private void showPage(String panel){
        MyCartPnl.setVisible(panel.equals("Cart"));
        TrackOrderPnl.setVisible(panel.equals("TrackOrder"));
        MyAccountPnl.setVisible(panel.equals("MyAccount"));
        ReportFeedbackPnl.setVisible(panel.equals("ReportFeedback"));
        showCaseItemPnl.setVisible(panel.equals("Shop"));
    }

    private void setActiveButton(JButton activeButton) {
        JButton[] buttons = {btnShop, btnMyCart, btnMyOrders, btnMyAccount};

        for (JButton btn : buttons) {
            if (btn == activeButton) {

                btn.setBackground(Color.decode("#F5A624"));
                btn.setForeground(Color.decode("#1B2341"));
                btn.setOpaque(true);
                btn.setContentAreaFilled(true);
            } else {
                // "OFF" state (Transparent/dark background, muted text)
                btn.setForeground(Color.decode("#B8C0D6"));
                btn.setOpaque(false);
                btn.setContentAreaFilled(false);
            }
        }
    }


    private void loadProducts(CustomerClass customer) {
        InsertItemsPnl.setLayout(new GridLayout(0, 3, 20, 20));
        InsertItemsPnl.removeAll();
        try (BufferedReader br = new BufferedReader(new FileReader("Items/Items.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] itemArray = line.split("\\|");
                if (itemArray.length < 4) {
                    continue;
                }

                String imgPath = itemArray[0].trim();
                String name = itemArray[1].trim();
                String seller = itemArray[2].trim();
                String price = itemArray[3].trim();
                Item item = new Item(imgPath,name,seller, Double.parseDouble(price));
                JPanel card = createProductCard(item, customer);
                InsertItemsPnl.add(card);
            }
        } catch (IOException e) {
            System.out.println("Error reading Items/Items.txt: " + e.getMessage());
        }

        InsertItemsPnl.revalidate();
        InsertItemsPnl.repaint();
    }

    private JPanel createProductCard(Item item, CustomerClass customer) {
        // 1. Card Container
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(new Color(0x1E2942));
        card.setBorder(BorderFactory.createLineBorder(new Color(0x33415C), 1));

        // 2. Simple Top Image (centered inside a full-width light background box)
        ImageIcon raw = new ImageIcon(item.getImagePath());
        Image scaled = raw.getImage().getScaledInstance(360, 175, Image.SCALE_SMOOTH);
        JLabel lblImage = new JLabel(new ImageIcon(scaled));
        lblImage.setHorizontalAlignment(SwingConstants.CENTER);
        lblImage.setOpaque(true);
        lblImage.setBackground(new Color(0xE8E5DF));
        card.add(lblImage, BorderLayout.NORTH);

        // 3. Body Section
        JPanel body = new JPanel(new GridLayout(4, 1, 0, 6));
        body.setBackground(new Color(0x1E2942));
        body.setBorder(BorderFactory.createEmptyBorder(12, 16, 16, 16));

        // Row 1: Title
        JLabel lblName = new JLabel(item.getName());
        lblName.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblName.setForeground(Color.WHITE);

        // Row 2: Seller
        JLabel lblSeller = new JLabel("Sold by " + item.getSeller());
        lblSeller.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblSeller.setForeground(new Color(0x94A3B8));

        // Row 3: Price (Left) & In stock (Right)
        JPanel priceRow = new JPanel(new BorderLayout());
        priceRow.setOpaque(false);

        JLabel lblPrice = new JLabel("$" + item.getPrice());
        lblPrice.setFont(new Font("SansSerif", Font.BOLD, 17));
        lblPrice.setForeground(Color.WHITE);

        JLabel lblStock = new JLabel("In stock");
        lblStock.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblStock.setForeground(new Color(0x94A3B8));

        priceRow.add(lblPrice, BorderLayout.WEST);
        priceRow.add(lblStock, BorderLayout.EAST);

        // Row 4: Add to cart button
        JButton btnAdd = new JButton("Add to cart");
        btnAdd.setBackground(new Color(0xF5A623));
        btnAdd.setForeground(new Color(0x1A202C));
        btnAdd.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnAdd.setFocusPainted(false);
        btnAdd.setBorderPainted(false);

        btnAdd.addActionListener(e -> {
            customer.cart.add(item);
            System.out.println(item);
        });

        body.add(lblName);
        body.add(lblSeller);
        body.add(priceRow);
        body.add(btnAdd);

        card.add(body, BorderLayout.CENTER);
        return card;
    }
}