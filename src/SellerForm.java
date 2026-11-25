import javax.swing.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.awt.Image;

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
    private JPanel ProductImagePnl;
    private JLabel ProductImageLbl;
    private JLabel accountType;
    private JLabel greetUsername;


    public SellerForm(String username, String _accountType){

        accountType.setText(username + " (" + _accountType + ")");
        greetUsername.setText("Hello, " + username);

        uploadProductImageButton.addActionListener(e -> {

            JFileChooser chooser = new JFileChooser();

            // Only allow image files
            chooser.setFileFilter(
                    new javax.swing.filechooser.FileNameExtensionFilter(
                            "Image Files (JPG, PNG)",
                            "jpg", "jpeg", "png"
                    )
            );

            // Open file selection window
            int result = chooser.showOpenDialog(this);

            // Check if seller selected an image
            if (result == JFileChooser.APPROVE_OPTION) {

                java.io.File selectedFile = chooser.getSelectedFile();

                // Display selected image
                displayImage(selectedFile);
            }

        });



        LogOutSeller.addActionListener(e->{
            BossStore.showLogInPanel();
            this.dispose();
        });


        DashboardPnl.setVisible(true);
        MyProductsPnl.setVisible(false);
        AddProductPnl.setVisible(false);
        FeedbackPnl.setVisible(false);
        OrderHistory.setVisible(false);


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

    private void displayImage(java.io.File file) {

        // Load original image
        ImageIcon originalIcon = new ImageIcon(file.getAbsolutePath());

        Image originalImage = originalIcon.getImage();

        // Get original dimensions
        int originalWidth = originalIcon.getIconWidth();
        int originalHeight = originalIcon.getIconHeight();

        // Get JLabel dimensions
        int labelWidth = ProductImageLbl.getWidth();
        int labelHeight = ProductImageLbl.getHeight();

        // Check dimensions
        if (originalWidth <= 0 || originalHeight <= 0 ||
                labelWidth <= 0 || labelHeight <= 0) {
            return;
        }

        // Calculate resizing ratio
        double scale = Math.min(
                (double) labelWidth / originalWidth,
                (double) labelHeight / originalHeight
        );

        // Calculate new dimensions
        int newWidth = (int) (originalWidth * scale);
        int newHeight = (int) (originalHeight * scale);

        // Resize image
        Image resizedImage = originalImage.getScaledInstance(
                newWidth,
                newHeight,
                Image.SCALE_SMOOTH
        );

        // Display image
        ProductImageLbl.setIcon(new ImageIcon(resizedImage));
        ProductImageLbl.setText("");

        // Center image
        ProductImageLbl.setHorizontalAlignment(SwingConstants.CENTER);
        ProductImageLbl.setVerticalAlignment(SwingConstants.CENTER);
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




