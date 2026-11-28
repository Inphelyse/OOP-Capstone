import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.*;
import java.util.ArrayList;

public class CustomerForm extends JFrame {
    // Exact bindings from CustomerForm.form
    private JPanel pnlMain;
    private JPanel CustomerHeaderPnl;
    private JTextField txtSearch;
    private JButton btnSearch;
    private JButton logOutCustomer;
    private JPanel CustomerNavigationPnl;
    private JButton btnShop;
    private JButton btnMyCart;
    private JButton btnMyOrders;
    private JButton btnMyAccount;
    private JButton btnReport;
    private JPanel RightShowCasePnl;
    private JPanel MyCartPnl;
    private JPanel OrderedItems;
    private JButton btnProceedCheckout;
    private JPanel showCaseItemPnl;
    private JLabel UserNameJLbl;
    private JPanel AllproductsPnl;
    private JScrollPane shopScrollPane;
    private JPanel InsertItemsPnl;
    private JPanel TrackOrderPnl;
    private JProgressBar orderProgressBar;
    private JButton btnTrackBackToShop;
    private JButton btnContactSupport;
    private JPanel MyAccountPnl;
    private JButton btnEditProfile;
    private JButton btnEditAddress;
    private JButton btnAddAddress;
    private JButton btnChangePassword;
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

    private String attachedFileName = "None";
    private String selectedOrderId = "";

    public CustomerForm(CustomerClass customer) {
        setContentPane(pnlMain);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ensureFoldersExist();
        showPage("Shop");
        UserNameJLbl.setText("Hello! " + customer.getName() + " What's on your mind?");

        // 1. Load saved cart, shop products, account info, and cart badge
        loadCartFromFile(customer);
        loadProducts(customer, "");
        loadAccountUI(customer);
        updateCartBadges(customer);

        // 2. Setup Search Bar, My Account dialogs, and Report/Feedback
        setupSearchBar(customer);
        setupAccountButtons(customer);
        setupReportFeedback(customer);

        // 3. MY CART BUTTON
        btnMyCart.addActionListener(e -> {
            setActiveButton(btnMyCart);
            saveCartToFile(customer);
            loadCartItems(customer);
            showPage("Cart");
        });

        // 4. PROCEED TO CHECKOUT BUTTON
        btnProceedCheckout.addActionListener(e -> {
            if (customer.cart.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Your cart is empty!");
                return;
            }

            String orderId = saveOrderToFile(customer);
            if (orderId.isEmpty()) {
                return;
            }

            selectedOrderId = orderId;
            customer.cart.clear();
            clearCartFile(customer);
            loadCartItems(customer);
            updateCartBadges(customer);

            JOptionPane.showMessageDialog(this, "Order #" + orderId + " placed!");
            setActiveButton(btnMyOrders);
            loadMyOrders(customer);
            showPage("TrackOrder");
        });

        // 5. MY ORDERS BUTTON
        btnMyOrders.addActionListener(e -> {
            setActiveButton(btnMyOrders);
            loadMyOrders(customer);
            showPage("TrackOrder");
        });

        btnTrackBackToShop.addActionListener(e -> {
            setActiveButton(btnShop);
            showPage("Shop");
        });

        btnContactSupport.addActionListener(e -> {
            loadReportReplyInfo(customer);
            if (!selectedOrderId.isEmpty()) {
                txtRelatedOrder.setText("#" + selectedOrderId);
            }
            showPage("ReportFeedback");
        });

        btnShop.addActionListener(e -> {
            setActiveButton(btnShop);
            showPage("Shop");
        });

        btnMyAccount.addActionListener(e -> {
            setActiveButton(btnMyAccount);
            loadAccountUI(customer);
            showPage("MyAccount");
        });

        btnReport.addActionListener(e -> {
            loadReportReplyInfo(customer);
            showPage("ReportFeedback");
        });

        logOutCustomer.addActionListener(d -> {
            BossStore.showLogInPanel();
            this.dispose();
        });

        shopScrollPane.setBorder(null);
        shopScrollPane.getViewport().setBackground(Color.decode("#232D4D"));
        shopScrollPane.getVerticalScrollBar().setUnitIncrement(16);
    }

    private void ensureFoldersExist() {
        try {
            File personsDir = new File("Persons");
            if (!personsDir.exists() && !personsDir.mkdirs()) {
                JOptionPane.showMessageDialog(this, "Could not create Persons folder.");
            }

            File itemsDir = new File("Items");
            if (!itemsDir.exists() && !itemsDir.mkdirs()) {
                JOptionPane.showMessageDialog(this, "Could not create Items folder.");
            }

            File reportDir = new File("FeedBack and Reports");
            if (!reportDir.exists() && !reportDir.mkdirs()) {
                JOptionPane.showMessageDialog(this, "Could not create FeedBack and Reports folder.");
            }
        } catch (SecurityException ex) {
            JOptionPane.showMessageDialog(this, "Folder permission error: " + ex.getMessage());
        }
    }

    private void showPage(String panel) {
        MyCartPnl.setVisible(panel.equals("Cart"));
        TrackOrderPnl.setVisible(panel.equals("TrackOrder"));
        MyAccountPnl.setVisible(panel.equals("MyAccount"));
        ReportFeedbackPnl.setVisible(panel.equals("ReportFeedback"));
        showCaseItemPnl.setVisible(panel.equals("Shop"));
    }

    private void setActiveButton(JButton activeButton) {
        JButton[] buttons = {btnShop, btnMyCart, btnMyOrders, btnMyAccount};
        for (int i = 0; i < buttons.length; i++) {
            if (buttons[i] == activeButton) {
                buttons[i].setBackground(Color.decode("#F5A624"));
                buttons[i].setForeground(Color.decode("#1B2341"));
                buttons[i].setOpaque(true);
                buttons[i].setContentAreaFilled(true);
            } else {
                buttons[i].setForeground(Color.decode("#B8C0D6"));
                buttons[i].setOpaque(false);
                buttons[i].setContentAreaFilled(false);
            }
        }
    }

    // =========================================================================
    // 1. SEARCH BAR (FILTERS BY PRODUCT NAME OR SELLER)
    // =========================================================================
    private void setupSearchBar(CustomerClass customer) {
        String placeholder = "Search products or sellers";

        txtSearch.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (txtSearch.getText().equals(placeholder)) {
                    txtSearch.setText("");
                    txtSearch.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (txtSearch.getText().trim().isEmpty()) {
                    txtSearch.setText(placeholder);
                    txtSearch.setForeground(Color.GRAY);
                }
            }
        });

        btnSearch.addActionListener(e -> runSearch(customer));
        txtSearch.addActionListener(e -> runSearch(customer));

        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { runSearch(customer); }
            public void removeUpdate(DocumentEvent e) { runSearch(customer); }
            public void changedUpdate(DocumentEvent e) { runSearch(customer); }
        });
    }

    private void runSearch(CustomerClass customer) {
        String text = txtSearch.getText().trim();
        if (text.equals("Search products or sellers")) {
            text = "";
        }
        loadProducts(customer, text);
    }

    // =========================================================================
    // 2. MY ACCOUNT: DISPLAY & SINGLE-FORM JOPTIONPANE EDITING
    // File: Persons/customer.txt
    // Format: role|name|password|phoneNum|address
    // =========================================================================
    private File getCustomerFile() {
        return new File("Persons/customer.txt");
    }

    private void loadAccountUI(CustomerClass customer) {
        JPanel leftCol = (JPanel) MyAccountPnl.getComponent(2);
        JPanel personalCard = (JPanel) leftCol.getComponent(0);
        JPanel addressCard = (JPanel) leftCol.getComponent(1);

        JLabel lblFullName = (JLabel) personalCard.getComponent(4);
        JLabel lblEmail = (JLabel) personalCard.getComponent(6);
        JLabel lblPhone = (JLabel) personalCard.getComponent(8);
        JLabel lblType = (JLabel) personalCard.getComponent(10);

        lblFullName.setText(customer.getName());
        lblEmail.setText(customer.getName().toLowerCase().replace(" ", ".") + "@amatobuzz.com");
        lblPhone.setText(customer.getPhoneNum());
        lblType.setText(customer.readType());

        JLabel lblAddrName = (JLabel) addressCard.getComponent(4);
        JLabel lblAddrText = (JLabel) addressCard.getComponent(5);
        lblAddrName.setText(customer.getName());
        lblAddrText.setText("<html>" + customer.getAddress() + "</html>");
    }

    private void setupAccountButtons(CustomerClass customer) {
        // Edit Profile (1 Whole Form: Name + Phone Number + Optional New Password)
        btnEditProfile.addActionListener(e -> {
            JTextField txtName = new JTextField(customer.getName(), 22);
            JTextField txtPhone = new JTextField(customer.getPhoneNum(), 22);
            JPasswordField txtNewPass = new JPasswordField(22);

            JPanel formPanel = new JPanel(new GridLayout(0, 1, 0, 6));
            formPanel.add(new JLabel("Full Name:"));
            formPanel.add(txtName);
            formPanel.add(new JLabel("Phone Number:"));
            formPanel.add(txtPhone);
            formPanel.add(new JLabel("New Password (leave blank to keep current):"));
            formPanel.add(txtNewPass);

            while (true) {
                int result = JOptionPane.showConfirmDialog(
                        this,
                        formPanel,
                        "Edit Profile",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE
                );

                if (result != JOptionPane.OK_OPTION) {
                    return;
                }

                String newName = txtName.getText().trim();
                String newPhone = txtPhone.getText().trim();
                String newPass = new String(txtNewPass.getPassword()).trim();

                if (newName.isEmpty() || newPhone.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Name and phone number cannot be empty!");
                    continue;
                }
                if (newName.contains("|") || newPhone.contains("|") || newPass.contains("|")) {
                    JOptionPane.showMessageDialog(this, "Do not use '|' in your details.");
                    txtNewPass.setText("");
                    continue;
                }
                if (!newPass.isEmpty() && newPass.length() < 4) {
                    JOptionPane.showMessageDialog(this, "New password must be at least 4 characters.");
                    txtNewPass.setText("");
                    continue;
                }

                String oldName = customer.getName();
                customer.setName(newName);
                customer.setPhoneNum(newPhone);
                if (!newPass.isEmpty()) {
                    customer.setPassword(newPass);
                }

                UserNameJLbl.setText("Hello! " + customer.getName() + " What's on your mind?");

                boolean saved = updateCustomerFile(oldName, customer);
                updateOrderedItemsName(oldName, customer.getName());
                saveCartToFile(customer);
                loadAccountUI(customer);
                loadReportReplyInfo(customer);

                if (saved) {
                    JOptionPane.showMessageDialog(this, "Profile updated in Persons/customer.txt!");
                }
                return;
            }
        });

        // Edit / Add Shipping Address (1 Whole Form: Recipient Name + Address)
        btnEditAddress.addActionListener(e -> editAddressDialog(customer));
        btnAddAddress.addActionListener(e -> editAddressDialog(customer));

        // Change Password (1 Whole Form: Current + New + Confirm Password, loops on mistake)
        btnChangePassword.addActionListener(e -> {
            JPasswordField txtCurrent = new JPasswordField(20);
            JPasswordField txtNew = new JPasswordField(20);
            JPasswordField txtConfirm = new JPasswordField(20);

            JPanel formPanel = new JPanel(new GridLayout(0, 1, 0, 6));
            formPanel.add(new JLabel("Current Password:"));
            formPanel.add(txtCurrent);
            formPanel.add(new JLabel("New Password:"));
            formPanel.add(txtNew);
            formPanel.add(new JLabel("Confirm New Password:"));
            formPanel.add(txtConfirm);

            while (true) {
                int result = JOptionPane.showConfirmDialog(
                        this,
                        formPanel,
                        "Change Password",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE
                );

                if (result != JOptionPane.OK_OPTION) {
                    return;
                }

                String currentPass = new String(txtCurrent.getPassword());
                String newPass = new String(txtNew.getPassword()).trim();
                String confirmPass = new String(txtConfirm.getPassword()).trim();

                if (!currentPass.equals(customer.getPassword())) {
                    JOptionPane.showMessageDialog(this, "Incorrect current password!");
                    txtCurrent.setText("");
                    txtNew.setText("");
                    txtConfirm.setText("");
                    continue;
                }
                if (newPass.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "New password cannot be empty!");
                    txtNew.setText("");
                    txtConfirm.setText("");
                    continue;
                }
                if (newPass.length() < 4) {
                    JOptionPane.showMessageDialog(this, "New password must be at least 4 characters.");
                    txtNew.setText("");
                    txtConfirm.setText("");
                    continue;
                }
                if (newPass.contains("|")) {
                    JOptionPane.showMessageDialog(this, "Password cannot contain '|'.");
                    txtNew.setText("");
                    txtConfirm.setText("");
                    continue;
                }
                if (!newPass.equals(confirmPass)) {
                    JOptionPane.showMessageDialog(this, "New passwords do not match!");
                    txtNew.setText("");
                    txtConfirm.setText("");
                    continue;
                }

                customer.setPassword(newPass);
                if (updateCustomerFile(customer.getName(), customer)) {
                    JOptionPane.showMessageDialog(this, "Password updated in Persons/customer.txt!");
                }
                return;
            }
        });
    }

    private void editAddressDialog(CustomerClass customer) {
        JTextField txtRecipient = new JTextField(customer.getName(), 22);
        JTextArea txtFullAddress = new JTextArea(customer.getAddress(), 3, 22);
        txtFullAddress.setLineWrap(true);
        txtFullAddress.setWrapStyleWord(true);
        JScrollPane scrollAddr = new JScrollPane(txtFullAddress);

        JPanel formPanel = new JPanel(new GridLayout(0, 1, 0, 6));
        formPanel.add(new JLabel("Recipient Name:"));
        formPanel.add(txtRecipient);
        formPanel.add(new JLabel("Complete Shipping Address:"));
        formPanel.add(scrollAddr);

        while (true) {
            int result = JOptionPane.showConfirmDialog(
                    this,
                    formPanel,
                    "Shipping Address",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
            );

            if (result != JOptionPane.OK_OPTION) {
                return;
            }

            String newName = txtRecipient.getText().trim();
            String newAddr = txtFullAddress.getText().trim().replace("\n", ", ");

            if (newName.isEmpty() || newAddr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Recipient name and address cannot be empty!");
                continue;
            }
            if (newName.contains("|") || newAddr.contains("|")) {
                JOptionPane.showMessageDialog(this, "Do not use '|' in name or address.");
                continue;
            }

            String oldName = customer.getName();
            customer.setName(newName);
            customer.setAddress(newAddr);
            UserNameJLbl.setText("Hello! " + customer.getName() + " What's on your mind?");

            boolean saved = updateCustomerFile(oldName, customer);
            updateOrderedItemsName(oldName, customer.getName());
            saveCartToFile(customer);
            loadAccountUI(customer);
            if (saved) {
                JOptionPane.showMessageDialog(this, "Shipping address updated in Persons/customer.txt!");
            }
            return;
        }
    }

    private boolean updateCustomerFile(String matchName, CustomerClass customer) {
        File file = getCustomerFile();
        ArrayList<String> lines = new ArrayList<>();
        boolean updated = false;

        try {
            File parent = file.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }

            if (file.exists()) {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.trim().isEmpty()) {
                        continue;
                    }
                    String delimiter = line.contains("|") ? "\\|" : ",";
                    String joiner = line.contains("|") ? "|" : ",";
                    String[] parts = line.split(delimiter);

                    // Format: role|name|password|phoneNum|address
                    if (parts.length >= 5 && parts[1].trim().equalsIgnoreCase(matchName.trim())) {
                        String newLine = parts[0].trim() + joiner
                                + customer.getName() + joiner
                                + customer.getPassword() + joiner
                                + customer.getPhoneNum() + joiner
                                + customer.getAddress();
                        lines.add(newLine);
                        updated = true;
                    } else if (parts.length == 4 && parts[0].trim().equalsIgnoreCase(matchName.trim())) {
                        // Legacy 4-column fallback: name|password|phoneNum|address
                        String newLine = customer.getName() + joiner
                                + customer.getPassword() + joiner
                                + customer.getPhoneNum() + joiner
                                + customer.getAddress();
                        lines.add(newLine);
                        updated = true;
                    } else {
                        lines.add(line);
                    }
                }
                br.close();
            }

            if (!updated) {
                String role = customer.readType() != null && !customer.readType().isEmpty()
                        ? customer.readType()
                        : "Customer";
                lines.add(role + "|" + customer.getName() + "|" + customer.getPassword() + "|" + customer.getPhoneNum() + "|" + customer.getAddress());
            }

            BufferedWriter bw = new BufferedWriter(new FileWriter(file, false));
            for (int i = 0; i < lines.size(); i++) {
                bw.write(lines.get(i));
                bw.newLine();
            }
            bw.close();
            return true;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Failed to update Persons/customer.txt: " + e.getMessage());
            return false;
        }
    }

    private void updateOrderedItemsName(String oldName, String newName) {
        File file = new File("Items/OrderedItems.txt");
        if (!file.exists()) {
            return;
        }

        ArrayList<String> lines = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] parts = line.split("\\|");
                if (parts.length >= 9 && parts[1].trim().equalsIgnoreCase(oldName.trim())) {
                    parts[1] = newName;
                    lines.add(String.join("|", parts));
                } else {
                    lines.add(line);
                }
            }
            br.close();

            BufferedWriter bw = new BufferedWriter(new FileWriter(file, false));
            for (int i = 0; i < lines.size(); i++) {
                bw.write(lines.get(i));
                bw.newLine();
            }
            bw.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Failed to update Items/OrderedItems.txt: " + e.getMessage());
        }
    }

    // =========================================================================
    // 3. REPORT & FEEDBACK: SAVE TO FeedBack and Reports/Feedbacks_Report.txt
    // =========================================================================
    private void loadReportReplyInfo(CustomerClass customer) {
        JPanel rightCol = (JPanel) ReportFeedbackPnl.getComponent(3);
        JPanel replyCard = (JPanel) rightCol.getComponent(1);
        JLabel lblSubmittingAs = (JLabel) replyCard.getComponent(2);
        JLabel lblReplyContact = (JLabel) replyCard.getComponent(3);
        JLabel lblReplyNote = (JLabel) replyCard.getComponent(4);

        lblSubmittingAs.setText("I'm submitting as " + customer.getName());
        lblReplyContact.setText(customer.getPhoneNum());
        lblReplyNote.setText("Updates will be sent to this phone number.");
    }

    private void setupReportFeedback(CustomerClass customer) {
        loadReportReplyInfo(customer);

        rbReportIssue.addActionListener(e -> {
            rbReportIssue.setBackground(Color.decode("#F5A624"));
            rbShareFeedback.setBackground(Color.WHITE);
            btnSubmitReport.setText("Submit report");
        });

        rbShareFeedback.addActionListener(e -> {
            rbShareFeedback.setBackground(Color.decode("#F5A624"));
            rbReportIssue.setBackground(Color.WHITE);
            btnSubmitReport.setText("Submit feedback");
        });

        String subjectPlaceholder = "Briefly describe your issue";
        txtSubject.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (txtSubject.getText().equals(subjectPlaceholder)) {
                    txtSubject.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (txtSubject.getText().trim().isEmpty()) {
                    txtSubject.setText(subjectPlaceholder);
                }
            }
        });

        String detailsPlaceholder = "What happened? Include any details that can help us understand the issue.";
        txtDetails.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (txtDetails.getText().equals(detailsPlaceholder)) {
                    txtDetails.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (txtDetails.getText().trim().isEmpty()) {
                    txtDetails.setText(detailsPlaceholder);
                    lblCharCount.setText("0 / 1,000 characters");
                }
            }
        });

        txtDetails.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { updateCount(); }
            public void removeUpdate(DocumentEvent e) { updateCount(); }
            public void changedUpdate(DocumentEvent e) { updateCount(); }

            private void updateCount() {
                String text = txtDetails.getText();
                if (text.equals(detailsPlaceholder)) {
                    lblCharCount.setText("0 / 1,000 characters");
                } else {
                    lblCharCount.setText(text.length() + " / 1,000 characters");
                    if (text.length() > 1000) {
                        lblCharCount.setForeground(Color.RED);
                    } else {
                        lblCharCount.setForeground(Color.decode("#B8C0D6"));
                    }
                }
            }
        });

        btnAttach.addActionListener(e -> {
            try {
                JFileChooser chooser = new JFileChooser();
                int result = chooser.showOpenDialog(this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selected = chooser.getSelectedFile();
                    if (selected != null && selected.exists()) {
                        attachedFileName = selected.getName();
                        btnAttach.setText(attachedFileName);
                    } else {
                        JOptionPane.showMessageDialog(this, "Selected file does not exist.");
                    }
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Could not open file chooser: " + ex.getMessage());
            }
        });

        btnSubmitReport.addActionListener(e -> {
            String subject = txtSubject.getText().trim();
            String details = txtDetails.getText().trim();

            if (subject.isEmpty() || subject.equals(subjectPlaceholder)) {
                JOptionPane.showMessageDialog(this, "Please enter a Subject.");
                return;
            }
            if (details.isEmpty() || details.equals(detailsPlaceholder)) {
                JOptionPane.showMessageDialog(this, "Please enter the Details.");
                return;
            }
            if (details.length() > 1000) {
                JOptionPane.showMessageDialog(this, "Details cannot exceed 1,000 characters.");
                return;
            }

            String type = rbReportIssue.isSelected() ? "Report Issue" : "Feedback";
            String category = cmbCategory.getSelectedItem().toString();
            String relatedOrder = txtRelatedOrder.getText().trim();
            if (relatedOrder.isEmpty()) {
                relatedOrder = "N/A";
            }

            String cleanDetails = details.replace("\n", " ").replace("|", "/");
            String cleanSubject = subject.replace("|", "/");

            try {
                File folder = new File("FeedBack and Reports");
                if (!folder.exists()) {
                    folder.mkdirs();
                }

                File reportFile = new File(folder, "Feedbacks_Report.txt");
                BufferedWriter bw = new BufferedWriter(new FileWriter(reportFile, true));

                String line = type + "|"
                        + customer.getName() + "|"
                        + customer.getPhoneNum() + "|"
                        + category + "|"
                        + relatedOrder + "|"
                        + cleanSubject + "|"
                        + cleanDetails + "|"
                        + attachedFileName;

                bw.write(line);
                bw.newLine();
                bw.close();

                JOptionPane.showMessageDialog(this, type + " submitted! Saved to FeedBack and Reports/Feedbacks_Report.txt");

                txtSubject.setText(subjectPlaceholder);
                txtDetails.setText(detailsPlaceholder);
                lblCharCount.setText("0 / 1,000 characters");
                attachedFileName = "None";
                btnAttach.setText("Attach screenshot");

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error saving report: " + ex.getMessage());
            }
        });

        btnCancelReport.addActionListener(e -> {
            setActiveButton(btnShop);
            showPage("Shop");
        });
    }

    // =========================================================================
    // 4. CHECKOUT: WRITE CART TO Items/OrderedItems.txt
    // Format: orderId|customerName|address|seller|itemName|imagePath|price|qty|status
    // =========================================================================
    private String saveOrderToFile(CustomerClass customer) {
        String orderId = "AB-" + (int) (Math.random() * 90000 + 10000);

        ArrayList<Item> uniqueItems = new ArrayList<>();
        ArrayList<Integer> quantities = new ArrayList<>();

        for (int i = 0; i < customer.cart.size(); i++) {
            Item current = customer.cart.get(i);
            int foundIndex = -1;

            for (int j = 0; j < uniqueItems.size(); j++) {
                if (uniqueItems.get(j).getName().equals(current.getName())) {
                    foundIndex = j;
                    break;
                }
            }

            if (foundIndex == -1) {
                uniqueItems.add(current);
                quantities.add(1);
            } else {
                quantities.set(foundIndex, quantities.get(foundIndex) + 1);
            }
        }

        try {
            File itemsDir = new File("Items");
            if (!itemsDir.exists()) {
                itemsDir.mkdirs();
            }

            BufferedWriter bw = new BufferedWriter(new FileWriter("Items/OrderedItems.txt", true));
            for (int i = 0; i < uniqueItems.size(); i++) {
                Item item = uniqueItems.get(i);
                int qty = quantities.get(i);

                String line = orderId + "|"
                        + customer.getName() + "|"
                        + customer.getAddress() + "|"
                        + item.getSeller() + "|"
                        + item.getName() + "|"
                        + item.getImagePath() + "|"
                        + item.getPrice() + "|"
                        + qty + "|"
                        + "Pending";

                bw.write(line);
                bw.newLine();
            }
            bw.close();
            return orderId;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving order to Items/OrderedItems.txt: " + e.getMessage());
            return "";
        }
    }

    // =========================================================================
    // 5. MY ORDERS: READ STATUS & SWITCH BETWEEN ALL CUSTOMER ORDERS
    // =========================================================================
    private void loadMyOrders(CustomerClass customer) {
        ArrayList<String[]> myOrders = new ArrayList<>();
        ArrayList<String> orderIds = new ArrayList<>();

        try {
            File file = new File("Items/OrderedItems.txt");
            if (file.exists()) {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.trim().isEmpty()) {
                        continue;
                    }
                    String[] parts = line.split("\\|");
                    if (parts.length >= 9 && parts[1].trim().equalsIgnoreCase(customer.getName().trim())) {
                        myOrders.add(parts);
                        String id = parts[0].trim();
                        if (!orderIds.contains(id)) {
                            orderIds.add(id);
                        }
                    }
                }
                br.close();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading Items/OrderedItems.txt: " + e.getMessage());
        }

        JLabel lblOrderSub = (JLabel) TrackOrderPnl.getComponent(1);
        JPanel leftCol = (JPanel) TrackOrderPnl.getComponent(3);
        JPanel statusCard = (JPanel) leftCol.getComponent(0);
        JPanel updatesCard = (JPanel) leftCol.getComponent(1);
        JPanel shipmentCard = (JPanel) TrackOrderPnl.getComponent(4);

        JLabel lblHeadline = (JLabel) statusCard.getComponent(0);
        JLabel lblStatusText = (JLabel) statusCard.getComponent(1);

        if (myOrders.isEmpty()) {
            selectedOrderId = "";
            lblOrderSub.setText("No orders placed yet.");
            lblHeadline.setText("You have no active orders");
            lblStatusText.setText("Order something from the Shop first.");
            orderProgressBar.setValue(0);
            orderProgressBar.setString("0%");
            return;
        }

        if (selectedOrderId.isEmpty() || !orderIds.contains(selectedOrderId)) {
            selectedOrderId = orderIds.get(orderIds.size() - 1);
        }

        ArrayList<String[]> currentOrder = new ArrayList<>();
        int totalQty = 0;
        double subtotal = 0.0;

        for (int i = 0; i < myOrders.size(); i++) {
            String[] row = myOrders.get(i);
            if (row[0].trim().equals(selectedOrderId)) {
                currentOrder.add(row);
                try {
                    double price = Double.parseDouble(row[6].trim());
                    int qty = Integer.parseInt(row[7].trim());
                    subtotal += price * qty;
                    totalQty += qty;
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Bad number format in OrderedItems.txt for order " + selectedOrderId);
                }
            }
        }

        int progress = 25;
        String currentStatus = "Pending";

        for (int i = 0; i < currentOrder.size(); i++) {
            String st = currentOrder.get(i)[8].trim();
            if (st.equalsIgnoreCase("Delivered")) {
                progress = 100;
                currentStatus = "Delivered";
            } else if (st.equalsIgnoreCase("In transit") || st.equalsIgnoreCase("To be delivered")) {
                if (progress < 75) progress = 75;
                currentStatus = "In transit";
            } else if (st.equalsIgnoreCase("To be grabbed")) {
                if (progress < 75) progress = 75;
                currentStatus = "To be grabbed";
            } else if (st.equalsIgnoreCase("Accepted")) {
                if (progress < 50) progress = 50;
                currentStatus = "Accepted by seller (Packed)";
            }
        }

        lblOrderSub.setText("Order #" + selectedOrderId + " · " + totalQty + " items (Total orders: " + orderIds.size() + ")");
        lblStatusText.setText("Status: " + currentStatus);
        orderProgressBar.setValue(progress);
        orderProgressBar.setString(progress + "% - " + currentStatus);

        if (progress == 25) {
            lblHeadline.setText("Waiting for seller to accept");
        } else if (progress == 50) {
            lblHeadline.setText("Seller accepted — order packed");
        } else if (progress == 75) {
            lblHeadline.setText("Your order is on its way");
        } else {
            lblHeadline.setText("Your order has been delivered!");
        }

        JLabel lblStep1 = (JLabel) statusCard.getComponent(3);
        JLabel lblStep2 = (JLabel) statusCard.getComponent(4);
        JLabel lblStep3 = (JLabel) statusCard.getComponent(5);
        JLabel lblStep4 = (JLabel) statusCard.getComponent(6);

        Color gold = new Color(0xF5A624);
        Color muted = new Color(0xB8C0D6);
        lblStep1.setForeground(progress >= 25 ? gold : muted);
        lblStep2.setForeground(progress >= 50 ? gold : muted);
        lblStep3.setForeground(progress >= 75 ? gold : muted);
        lblStep4.setForeground(progress >= 100 ? gold : muted);

        updatesCard.removeAll();
        updatesCard.setLayout(new BoxLayout(updatesCard, BoxLayout.Y_AXIS));

        if (orderIds.size() > 1) {
            JPanel switchRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
            switchRow.setOpaque(false);
            JLabel lblPick = new JLabel("Select order:");
            lblPick.setForeground(Color.WHITE);
            lblPick.setFont(new Font("SansSerif", Font.BOLD, 12));
            switchRow.add(lblPick);

            for (int i = 0; i < orderIds.size(); i++) {
                String id = orderIds.get(i);
                JButton btnOrd = new JButton("#" + id);
                btnOrd.setFocusPainted(false);
                if (id.equals(selectedOrderId)) {
                    btnOrd.setBackground(gold);
                    btnOrd.setForeground(Color.decode("#1B2341"));
                } else {
                    btnOrd.setBackground(Color.WHITE);
                    btnOrd.setForeground(Color.decode("#1B2341"));
                }
                btnOrd.addActionListener(e -> {
                    selectedOrderId = id;
                    loadMyOrders(customer);
                });
                switchRow.add(btnOrd);
            }
            updatesCard.add(switchRow);
        }

        JLabel title = new JLabel("Order items & status");
        title.setFont(new Font("SansSerif", Font.BOLD, 16));
        title.setForeground(Color.WHITE);
        title.setBorder(BorderFactory.createEmptyBorder(8, 0, 4, 0));
        updatesCard.add(title);

        for (int i = 0; i < currentOrder.size(); i++) {
            String[] row = currentOrder.get(i);
            JLabel itemLbl = new JLabel("• " + row[4].trim() + " (x" + row[7].trim()
                    + ") - Seller: " + row[3].trim() + " [" + row[8].trim() + "]");
            itemLbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
            itemLbl.setForeground(gold);
            itemLbl.setBorder(BorderFactory.createEmptyBorder(4, 0, 2, 0));
            updatesCard.add(itemLbl);
        }

        JLabel lblTracking = (JLabel) shipmentCard.getComponent(3);
        lblTracking.setText("ParcelPost · " + selectedOrderId);

        JLabel lblCustName = (JLabel) shipmentCard.getComponent(6);
        JLabel lblCustAddr = (JLabel) shipmentCard.getComponent(7);
        lblCustName.setText(customer.getName());
        lblCustAddr.setText("<html>" + customer.getAddress() + "</html>");

        double totalPaid = subtotal + 5.00;
        JLabel lblPaid = (JLabel) shipmentCard.getComponent(11);
        lblPaid.setText(String.format("$%.2f", totalPaid));

        TrackOrderPnl.revalidate();
        TrackOrderPnl.repaint();
    }

    // =========================================================================
    // 6. PER-CUSTOMER CART SAVE / LOAD / CLEAR (Items/CustomerCart.txt)
    // Format: customerName|imagePath|itemName|seller|price
    // =========================================================================
    private void loadCartFromFile(CustomerClass customer) {
        File file = new File("Items/CustomerCart.txt");
        if (!file.exists()) {
            return;
        }

        customer.cart.clear();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] parts = line.split("\\|");
                if (parts.length >= 5 && parts[0].trim().equalsIgnoreCase(customer.getName().trim())) {
                    try {
                        Item item = new Item(
                                parts[1].trim(),
                                parts[2].trim(),
                                parts[3].trim(),
                                Double.parseDouble(parts[4].trim())
                        );
                        customer.cart.add(item);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "Skipping invalid price in CustomerCart.txt: " + parts[4].trim());
                    }
                }
            }
            br.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading cart from Items/CustomerCart.txt: " + e.getMessage());
        }
    }

    private void saveCartToFile(CustomerClass customer) {
        File file = new File("Items/CustomerCart.txt");
        ArrayList<String> otherCustomerLines = new ArrayList<>();

        try {
            File parent = file.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }

            if (file.exists()) {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.trim().isEmpty()) continue;
                    String[] parts = line.split("\\|");
                    if (parts.length >= 5 && !parts[0].trim().equalsIgnoreCase(customer.getName().trim())) {
                        otherCustomerLines.add(line);
                    }
                }
                br.close();
            }

            BufferedWriter bw = new BufferedWriter(new FileWriter(file, false));
            for (int i = 0; i < otherCustomerLines.size(); i++) {
                bw.write(otherCustomerLines.get(i));
                bw.newLine();
            }
            for (int i = 0; i < customer.cart.size(); i++) {
                Item item = customer.cart.get(i);
                bw.write(customer.getName() + "|" + item.getImagePath() + "|" + item.getName() + "|" + item.getSeller() + "|" + item.getPrice());
                bw.newLine();
            }
            bw.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving cart to Items/CustomerCart.txt: " + e.getMessage());
        }
    }

    private void clearCartFile(CustomerClass customer) {
        customer.cart.clear();
        saveCartToFile(customer);
    }

    // =========================================================================
    // 7. SHOP PRODUCTS & SEARCH FILTER
    // =========================================================================
    private void loadProducts(CustomerClass customer, String keyword) {
        InsertItemsPnl.setLayout(new GridLayout(0, 3, 20, 20));
        InsertItemsPnl.removeAll();

        String searchLower = keyword.toLowerCase();
        int matchCount = 0;

        try {
            BufferedReader br = new BufferedReader(new FileReader("Items/Items.txt"));
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] itemArray = line.split("\\|");
                if (itemArray.length < 4) {
                    continue;
                }

                String itemName = itemArray[1].trim();
                String sellerName = itemArray[2].trim();

                if (!searchLower.isEmpty()) {
                    boolean matchesName = itemName.toLowerCase().contains(searchLower);
                    boolean matchesSeller = sellerName.toLowerCase().contains(searchLower);
                    if (!matchesName && !matchesSeller) {
                        continue;
                    }
                }

                try {
                    Item item = new Item(
                            itemArray[0].trim(),
                            itemName,
                            sellerName,
                            Double.parseDouble(itemArray[3].trim())
                    );
                    InsertItemsPnl.add(createProductCard(item, customer));
                    matchCount++;
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Skipping product with invalid price: " + itemName);
                }
            }
            br.close();
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Items/Items.txt was not found.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading Items/Items.txt: " + e.getMessage());
        }

        if (matchCount == 0) {
            InsertItemsPnl.setLayout(new FlowLayout(FlowLayout.LEFT));
            JLabel noMatch = new JLabel("No products or sellers found matching \"" + keyword + "\".");
            noMatch.setForeground(Color.WHITE);
            noMatch.setFont(new Font("SansSerif", Font.PLAIN, 14));
            InsertItemsPnl.add(noMatch);
        }

        InsertItemsPnl.revalidate();
        InsertItemsPnl.repaint();
    }

    private JPanel createProductCard(Item item, CustomerClass customer) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(new Color(0x1E2942));
        card.setBorder(BorderFactory.createLineBorder(new Color(0x33415C), 1));

        ImageIcon raw = new ImageIcon(item.getImagePath());
        Image scaled = raw.getImage().getScaledInstance(360, 175, Image.SCALE_SMOOTH);
        JLabel lblImage = new JLabel(new ImageIcon(scaled));
        lblImage.setHorizontalAlignment(SwingConstants.CENTER);
        lblImage.setOpaque(true);
        lblImage.setBackground(new Color(0xE8E5DF));
        card.add(lblImage, BorderLayout.NORTH);

        JPanel body = new JPanel(new GridLayout(4, 1, 0, 6));
        body.setBackground(new Color(0x1E2942));
        body.setBorder(BorderFactory.createEmptyBorder(12, 16, 16, 16));

        JLabel lblName = new JLabel(item.getName());
        lblName.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblName.setForeground(Color.WHITE);

        JLabel lblSeller = new JLabel("Sold by " + item.getSeller());
        lblSeller.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblSeller.setForeground(new Color(0x94A3B8));

        JPanel priceRow = new JPanel(new BorderLayout());
        priceRow.setOpaque(false);

        JLabel lblPrice = new JLabel(String.format("$%.2f", item.getPrice()));
        lblPrice.setFont(new Font("SansSerif", Font.BOLD, 17));
        lblPrice.setForeground(Color.WHITE);

        JLabel lblStock = new JLabel("In stock");
        lblStock.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblStock.setForeground(new Color(0x94A3B8));

        priceRow.add(lblPrice, BorderLayout.WEST);
        priceRow.add(lblStock, BorderLayout.EAST);

        JButton btnAdd = new JButton("Add to cart");
        btnAdd.setBackground(new Color(0xF5A623));
        btnAdd.setForeground(new Color(0x1A202C));
        btnAdd.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnAdd.setFocusPainted(false);
        btnAdd.setBorderPainted(false);

        Timer resetTimer = new Timer(2000, evt -> {
            btnAdd.setBackground(new Color(0xF5A623));
            btnAdd.setForeground(new Color(0x1A202C));
            btnAdd.setText("Add to cart");
        });
        resetTimer.setRepeats(false);

        btnAdd.addActionListener(e -> {
            customer.cart.add(item);
            saveCartToFile(customer);
            updateCartBadges(customer);

            btnAdd.setBackground(new Color(0x2B3559));
            btnAdd.setForeground(new Color(0xE8E4DC));
            btnAdd.setText("Added to Cart!");

            resetTimer.restart();
        });

        body.add(lblName);
        body.add(lblSeller);
        body.add(priceRow);
        body.add(btnAdd);

        card.add(body, BorderLayout.CENTER);
        return card;
    }

    private void updateCartBadges(CustomerClass customer) {
        btnMyCart.setText("My cart (" + customer.cart.size() + ")");
    }

    // =========================================================================
    // 8. CART ITEMS & ORDER SUMMARY
    // =========================================================================
    private void loadCartItems(CustomerClass customer) {
        if (!(OrderedItems.getLayout() instanceof BoxLayout)) {
            JPanel headerBar = new JPanel(new BorderLayout());
            headerBar.setOpaque(false);
            headerBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
            headerBar.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0x64748B)),
                    BorderFactory.createEmptyBorder(0, 0, 8, 0)
            ));

            JLabel lblProd = (JLabel) OrderedItems.getComponent(0);
            JLabel lblPrc = (JLabel) OrderedItems.getComponent(1);
            JLabel lblQtyHdr = (JLabel) OrderedItems.getComponent(2);
            JLabel lblTotHdr = (JLabel) OrderedItems.getComponent(3);

            JPanel rightHdr = new JPanel(new FlowLayout(FlowLayout.RIGHT, 42, 0));
            rightHdr.setOpaque(false);
            rightHdr.add(lblPrc);
            rightHdr.add(lblQtyHdr);
            rightHdr.add(lblTotHdr);

            headerBar.add(lblProd, BorderLayout.WEST);
            headerBar.add(rightHdr, BorderLayout.EAST);

            OrderedItems.removeAll();
            OrderedItems.setLayout(new BoxLayout(OrderedItems, BoxLayout.Y_AXIS));
            OrderedItems.add(headerBar);
        } else {
            while (OrderedItems.getComponentCount() > 1) {
                OrderedItems.remove(1);
            }
        }

        ArrayList<Item> uniqueItems = new ArrayList<>();
        ArrayList<Integer> quantities = new ArrayList<>();
        ArrayList<String> uniqueSellers = new ArrayList<>();
        double subtotal = 0.0;

        for (int i = 0; i < customer.cart.size(); i++) {
            Item cartItem = customer.cart.get(i);
            subtotal += cartItem.getPrice();

            if (!uniqueSellers.contains(cartItem.getSeller())) {
                uniqueSellers.add(cartItem.getSeller());
            }

            int foundIndex = -1;
            for (int j = 0; j < uniqueItems.size(); j++) {
                if (uniqueItems.get(j).getName().equals(cartItem.getName())) {
                    foundIndex = j;
                    break;
                }
            }

            if (foundIndex == -1) {
                uniqueItems.add(cartItem);
                quantities.add(1);
            } else {
                quantities.set(foundIndex, quantities.get(foundIndex) + 1);
            }
        }

        for (int i = 0; i < uniqueItems.size(); i++) {
            Item item = uniqueItems.get(i);
            int qty = quantities.get(i);

            JPanel row = new JPanel(new BorderLayout(15, 0));
            row.setOpaque(false);
            row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 105));
            row.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0x64748B)),
                    BorderFactory.createEmptyBorder(12, 0, 12, 0)
            ));

            JPanel left = new JPanel(new BorderLayout(15, 0));
            left.setOpaque(false);

            Image scaled = new ImageIcon(item.getImagePath()).getImage().getScaledInstance(80, 70, Image.SCALE_SMOOTH);
            JLabel lblImg = new JLabel(new ImageIcon(scaled));
            lblImg.setPreferredSize(new Dimension(80, 70));
            lblImg.setOpaque(true);
            lblImg.setBackground(new Color(0xE8E5DF));

            JPanel info = new JPanel(new GridLayout(4, 1, 0, 2));
            info.setOpaque(false);

            JLabel lblName = new JLabel(item.getName());
            lblName.setFont(new Font("SansSerif", Font.BOLD, 14));
            lblName.setForeground(Color.WHITE);

            JLabel lblSeller = new JLabel("Sold by " + item.getSeller());
            lblSeller.setFont(new Font("SansSerif", Font.PLAIN, 11));
            lblSeller.setForeground(new Color(0xB8C0D6));

            JLabel lblStock = new JLabel("In stock");
            lblStock.setFont(new Font("SansSerif", Font.PLAIN, 10));
            lblStock.setForeground(new Color(0xB8C0D6));

            JButton btnRemove = new JButton("Remove");
            btnRemove.setFont(new Font("SansSerif", Font.PLAIN, 10));
            btnRemove.setForeground(new Color(0xF5A624));
            btnRemove.setContentAreaFilled(false);
            btnRemove.setBorderPainted(false);
            btnRemove.setHorizontalAlignment(SwingConstants.LEFT);
            btnRemove.setBorder(null);
            btnRemove.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            btnRemove.addActionListener(e -> {
                for (int j = customer.cart.size() - 1; j >= 0; j--) {
                    if (customer.cart.get(j).getName().equals(item.getName())) {
                        customer.cart.remove(j);
                    }
                }
                saveCartToFile(customer);
                loadCartItems(customer);
            });

            info.add(lblName);
            info.add(lblSeller);
            info.add(lblStock);
            info.add(btnRemove);

            left.add(lblImg, BorderLayout.WEST);
            left.add(info, BorderLayout.CENTER);

            JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 22));
            right.setOpaque(false);

            JLabel lblPrice = new JLabel(String.format("$%.2f", item.getPrice()));
            lblPrice.setForeground(Color.WHITE);

            JButton btnMinus = new JButton("-");
            btnMinus.setPreferredSize(new Dimension(42, 26));
            btnMinus.setFocusPainted(false);
            btnMinus.addActionListener(e -> {
                for (int j = 0; j < customer.cart.size(); j++) {
                    if (customer.cart.get(j).getName().equals(item.getName())) {
                        customer.cart.remove(j);
                        break;
                    }
                }
                saveCartToFile(customer);
                loadCartItems(customer);
            });

            JLabel lblQty = new JLabel(String.valueOf(qty), SwingConstants.CENTER);
            lblQty.setFont(new Font("SansSerif", Font.BOLD, 12));
            lblQty.setForeground(Color.WHITE);

            JButton btnPlus = new JButton("+");
            btnPlus.setPreferredSize(new Dimension(42, 26));
            btnPlus.setFocusPainted(false);
            btnPlus.addActionListener(e -> {
                customer.cart.add(item);
                saveCartToFile(customer);
                loadCartItems(customer);
            });

            JLabel lblRowTotal = new JLabel(String.format("$%.2f", item.getPrice() * qty));
            lblRowTotal.setFont(new Font("SansSerif", Font.BOLD, 12));
            lblRowTotal.setForeground(Color.WHITE);

            right.add(lblPrice);
            right.add(btnMinus);
            right.add(lblQty);
            right.add(btnPlus);
            right.add(lblRowTotal);

            row.add(left, BorderLayout.WEST);
            row.add(right, BorderLayout.EAST);
            OrderedItems.add(row);
        }

        int totalItems = customer.cart.size();
        double shipping = totalItems > 0 ? 5.00 : 0.00;
        double grandTotal = subtotal + shipping;

        JLabel lblSubtitle = (JLabel) MyCartPnl.getComponent(1);
        lblSubtitle.setText(totalItems + " items from " + uniqueSellers.size() + " sellers. Review your items before checkout.");

        JPanel orderSummaryPnl = (JPanel) MyCartPnl.getComponent(4);
        JLabel lblSubtotalText = (JLabel) orderSummaryPnl.getComponent(2);
        JLabel lblSubtotalVal  = (JLabel) orderSummaryPnl.getComponent(3);
        JLabel lblShippingVal  = (JLabel) orderSummaryPnl.getComponent(5);
        JLabel lblTotalVal     = (JLabel) orderSummaryPnl.getComponent(8);

        lblSubtotalText.setText("Subtotal (" + totalItems + " items)");
        lblSubtotalVal.setText(String.format("$%.2f", subtotal));
        lblShippingVal.setText(String.format("$%.2f", shipping));
        lblTotalVal.setText(String.format("$%.2f", grandTotal));

        updateCartBadges(customer);

        OrderedItems.revalidate();
        OrderedItems.repaint();
    }
}