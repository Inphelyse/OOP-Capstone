import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.util.ArrayList;



public class Admin extends JFrame {
    private JPanel AdminPnl;
    private JPanel HeaderPnl;
    private JPanel BodyPnl;
    private JPanel NavigationPnl;
    private JButton HomeBtn;
    private JButton SellersButton;
    private JButton CustomersBtn;
    private JButton CouriersBtn;
    private JButton FeedbackBtn;
    private JButton BansBtn;
    private JButton HistoryBtn;
    private JPanel DataPnl;
    private JPanel Summary_Report_Pnl;
    private JPanel Report_Details_Admin;
    private JPanel SellersPnl;
    private JPanel CustomersPnl;
    private JPanel CouriersPnl;
    private JPanel FeedbackPnl;
    private JPanel FeedBacks_and_Report;
    private JTable SubmissionsTbl;
    private JPanel Info_Details;
    private JPanel BansPnl;
    private JPanel BannedAccountsPnl;
    private JTable BannedTbl;
    private JPanel Band_DetailsPnl;
    private JPanel HistoryPnl;
    private JPanel ActiveLog_or_HistoryPnl;
    private JTable activityLogHistoryTbl;
    private JPanel ActionDetailsPnl;
    private JPanel pnlMain;
    private JTable customerAccountsTable;
    private JPanel CustomerDetailsPnl;
    private JTable courierAccountsTable;
    private JLabel CourierCountLbl;
    private JLabel CourierRangeLbl;
    private JLabel CourierStatusLbl;
    private JLabel CourierNameLbl;
    private JLabel CourierPhoneLbl;
    private JLabel CourierPhoneValueLbl;
    private JLabel CourierAddressLbl;
    private JScrollPane CouriersScroll;
    private JButton logOutAdmin;
    private JLabel showTotalPeople;
    private JLabel SellerNameLbl;
    private JLabel SellerPhoneLbl;
    private JLabel customerTotal;
    private JLabel sellerTotal;
    private JLabel courierTotal;
    private JLabel accountType;
    private JLabel greetUsername;
    private JTextField searchSellerAccount;
    private JTextField searchCustomerAccount;
    private JTextField searchCourierAccount;
    private JTable sellerAccountsTable;
    private JButton searchSellerAccountButton;
    private JButton searchCourierAccountButton;
    private JLabel CustomerNameLbl;
    private JLabel CustomerPhoneLbl;
    private JLabel CustomerStatusLbl;
    private JLabel CustomerPhoneValueLbl;
    private JLabel CustomerAddressLbl;
    private JLabel SellerPhoneValueLbl;
    private JLabel SellerAddressLbl;
    private JPanel SellerDetailsPnl;
    private JLabel SellerStatusLbl;
    private JPanel CourierDetailsPnl;
    private JButton banCourierButton;
    private JTextField allSearchSubmissionsReport;
    private JButton allSearchSubmissionsReportButton;
    private JTextField bannedSearchAccounts;
    private JButton bannedSearchAccountsButton;
    private JComboBox sortAccountTypes;
    private JComboBox sortSubmittedBy;
    private JComboBox sortStatusReport;
    private JComboBox sortStatusCourierReports;
    private JComboBox sortStatusCustomerReports;
    private JComboBox sortStatusSellerrReports;
    private JButton sellerBanButton;
    private JTextArea banReasonDisplayInLogIn;
    private JTextArea reviewNoteBan;
    private JLabel accountName;
    private JLabel accountRole;
    private JLabel dateBanned;
    private JTable currentCartCustomerTable;
    private JLabel cartTotalCustomer;
    private JButton unbanningAccount;
    private JLabel accountUsernameRemovedAccount;
    private JLabel dateRemovedAccount;
    private JLabel accountTypeRemoved;
    private JTextArea reasonForRemoving;
    private JTable ReportsTbl;
    private JTextField activityLogAccountSearch;
    private JButton activityLogAccountSearchButton;
    private JButton allSubmissionsButton;
    private JButton reportSubmissionsButton;
    private JButton feedSubmissionsButton;
    private JButton customerBanButton;
    private JTextArea internalNotes;
    private JTextArea reportMessage_feedbackMessage;
    private JButton removeAccountButton;
    private static final int COURIERS_PER_PAGE = 20;
    private static final int CUSTOMER_PER_PAGE = 20;
    private static final int SELLER_PER_PAGE = 20;

    // Each row is type|username|password|phone|address, same as Persons/courier.txt
    private final ArrayList<String[]> couriers = new ArrayList<>();
    private int courierPage = 0;
    private final ArrayList<String[]> customer = new ArrayList<>();
    private int customerPage = 0;
    private final ArrayList<String[]> seller = new ArrayList<>();
    private int sellerPage = 0;

    // Admin name
    private String adminUsername;

    public Admin(String username, String _accountType){
        adminUsername = username;

        accountType.setText(username + " (" + _accountType + ")");
        greetUsername.setText("Hello, " + username);

        CustomerDetailsPnl.setVisible(false);
        SellersPnl.setVisible(false);
        CustomersPnl.setVisible(false);
        CouriersPnl.setVisible(false);
        BansPnl.setVisible(false);
        HistoryPnl.setVisible(false);
        FeedbackPnl.setVisible(false);

        HomeBtn.addActionListener(d -> showPage("home"));
        SellersButton.addActionListener(d -> showPage("sellers"));
        CustomersBtn.addActionListener(d -> showPage("customers"));
        CouriersBtn.addActionListener(d -> showPage("couriers"));
        FeedbackBtn.addActionListener(d -> showPage("feedbacks"));
        //
        BansBtn.addActionListener(d -> {
            loadBannedAccounts();
            showPage("bans");
        });
        //
        HistoryBtn.addActionListener(e -> {
            loadActivityHistory();
            showPage("history");
        });

        courierAccountsTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                showSelectedCourier();
            }
        });
        customerAccountsTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                showSelectedCustomer();
            }
        });
        sellerAccountsTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                showSelectedSeller();
            }
        });


        loadCouriers();
        loadCustomer();
        loadSeller();

        setContentPane(pnlMain);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        generateTable();

        CourierDetailsPnl.setVisible(false);
        CustomerDetailsPnl.setVisible(false);
        SellerDetailsPnl.setVisible(false);
        
        logOutAdmin.addActionListener(d->{
            BossStore.showLogInPanel();
            this.dispose();
        });


        int _customerTotal = Files.customerTotal();
        int _sellerTotal = Files.sellerTotal();
        int _courierTotal = Files.courierTotal();
        int _totalPeople = Files.totalPeople();

        customerTotal.setText("Customer: " + _customerTotal);
        sellerTotal.setText("Seller: " + _sellerTotal);
        courierTotal.setText("Courier: " + _courierTotal);
        showTotalPeople.setText(String.valueOf(_totalPeople));

        //==== For banning and banning an account=====//
        unbanningAccount.addActionListener(e -> unbanSelectedAccount());

        BannedTbl.getSelectionModel().addListSelectionListener(e -> {
//            if (!e.getValueIsAdjusting()) {
//                showSelectedBannedAccount();
//            }
            showSelectedBannedAccount();
        });

        banCourierButton.addActionListener(e -> {
            int row = courierAccountsTable.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Please select a courier first");
                return;
            }

            System.out.println("Banning courier");

            int index = courierAccountsTable.getSelectedRow();
            // -1 indicates that the user did not selected a row or clicked any row
            if (index != -1) {
                banAccount("Courier", couriers.get(index));
            }
        });


        sellerBanButton.addActionListener(e -> {
            int row = sellerAccountsTable.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Please select a seller first");
                return;
            }
            System.out.println("Banning seller");

            int index = sellerAccountsTable.getSelectedRow();
            if (index != -1) {
                banAccount("Seller", seller.get(index));
            }
        });

        customerBanButton.addActionListener(e -> {
            int row = customerAccountsTable.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Please select a customer first");
                return;
            }
            System.out.println("Banning customer");

            int index = customerAccountsTable.getSelectedRow();
            if (index != -1) {
                banAccount("Customer", customer.get(index));
            }
        });
        //==== ======================================//
    }

    // This is for Moderation History, where the admin can see the history when they banned, unbanned, and removed an account
    // Activity Log Table
    private void loadActivityHistory() {
        String[] columns = {"Action", "Username", "Role", "Admin"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        File file = new File("Persons/history.txt");

        if (file.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split("\\|", -1);
                    if (parts.length == 4) {
                        model.addRow(parts);
                    }
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, e.getMessage());
            }
        }

        activityLogHistoryTbl.setModel(model);
    }

    private void loadCouriers() {
        couriers.clear();
        File file = new File("Persons/courier.txt");

        if (file.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split("\\|");
                    if (parts.length < 5) {
                        continue;
                    }
                    couriers.add(parts);
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        }
    }

    private void loadCustomer() {
        customer.clear();
        File file = new File("Persons/customer.txt");

        if (file.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split("\\|");
                    if (parts.length < 5) {
                        continue;
                    }
                    customer.add(parts);
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        }
    }

    private void loadSeller() {
        seller.clear();
        File file = new File("Persons/seller.txt");

        if (file.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split("\\|");
                    if (parts.length < 5) {
                        continue;
                    }
                    seller.add(parts);
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        }
    }

    private void showSelectedCourier() {
        int row = courierAccountsTable.getSelectedRow();
        int index = courierPage * COURIERS_PER_PAGE + row;
        if (row < 0 || index >= couriers.size()) {
            CourierStatusLbl.setText("");
            CourierNameLbl.setText("");
            CourierPhoneLbl.setText("");
            CourierPhoneValueLbl.setText("");
            CourierAddressLbl.setText("");
            return;
        }
        String[] parts = couriers.get(index);
        CourierDetailsPnl.setVisible(true);
        CourierStatusLbl.setText(parts[0]);
        CourierNameLbl.setText(parts[1]);
        CourierPhoneLbl.setText(parts[3]);
        CourierPhoneValueLbl.setText(parts[3]);
        CourierAddressLbl.setText(parts[4]);
    }

    private void showSelectedCustomer() {
        int row = customerAccountsTable.getSelectedRow();
        int index = customerPage * CUSTOMER_PER_PAGE + row;

        if (row < 0 || index >= customer.size()) {
            CustomerStatusLbl.setText("");
            CustomerNameLbl.setText("");
            CustomerPhoneLbl.setText("");
            CustomerPhoneValueLbl.setText("");
            CustomerAddressLbl.setText("");
            CustomerDetailsPnl.setVisible(false);
            return;
        }
        String[] parts = customer.get(index);
        CustomerDetailsPnl.setVisible(true);
        CustomerStatusLbl.setText(parts[0]);
        CustomerNameLbl.setText(parts[1]);
        CustomerPhoneLbl.setText(parts[3]);
        CustomerPhoneValueLbl.setText(parts[3]);
        CustomerAddressLbl.setText(parts[4]);
    }

    private void showSelectedSeller() {
        int row = sellerAccountsTable.getSelectedRow();
        int index = sellerPage * SELLER_PER_PAGE + row;

        if (row < 0 || index >= seller.size()) {
            SellerStatusLbl.setText("");
            SellerNameLbl.setText("");
            SellerPhoneLbl.setText("");
            SellerPhoneValueLbl.setText("");
            SellerAddressLbl.setText("");
            SellerDetailsPnl.setVisible(false);
            return;
        }

        String[] parts = seller.get(index);
        SellerDetailsPnl.setVisible(true);
        SellerStatusLbl.setText(parts[0]);
        SellerNameLbl.setText(parts[1]);
        SellerPhoneLbl.setText(parts[3]);
        SellerPhoneValueLbl.setText(parts[3]);
        SellerAddressLbl.setText(parts[4]);
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
        FeedbackPnl.setVisible(page.equals("feedbacks"));
        BansPnl.setVisible(page.equals("bans"));
        HistoryPnl.setVisible(page.equals("history"));
    }

    // This refresh accounts. Very helpful function when you ban and unban an account
    // because it will refresh your table every time you ban and unban an account
    private void refreshAccounts() {
        try {
            System.out.println("refreshed");
            couriers.clear();
            couriers.addAll(Files.readAccounts("courier"));

            customer.clear();
            customer.addAll(Files.readAccounts("customer"));

            seller.clear();
            seller.addAll(Files.readAccounts("seller"));

            //
            generateTable();
            loadBannedAccounts();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }


    //==========Banning and unbanning funxtions============//

    // Generating Tables for Customer, Seller, and Courier ()
    private void generateTable() {
        String[] columns = {"Role", "Name", "Password", "Number", "Location"};
        System.out.println("generate");

        DefaultTableModel customerModel = new DefaultTableModel(columns, 0);
        DefaultTableModel sellerModel = new DefaultTableModel(columns, 0);
        DefaultTableModel courierModel = new DefaultTableModel(columns, 0);

        // This
        for (int i = 0; i < customer.size(); i++) {
            customerModel.addRow(customer.get(i));
        }
        for (int i = 0; i < seller.size(); i++) {
            sellerModel.addRow(seller.get(i));
        }
        for (int i = 0; i < couriers.size(); i++) {
            courierModel.addRow(couriers.get(i));
        }

        customerAccountsTable.setModel(customerModel);
        sellerAccountsTable.setModel(sellerModel);
        courierAccountsTable.setModel(courierModel);
    }


    private ArrayList<String[]> bannedAccounts = new ArrayList<>();
    // For loading the banned accounts in the Table
    private void loadBannedAccounts() {
        try {
            bannedAccounts = Files.readAccounts("banned");

            DefaultTableModel model = new DefaultTableModel(
                    new String[]{"Name", "Role"}, 0);

            for (int i = 0; i < bannedAccounts.size(); i++) {
                String[] parts = bannedAccounts.get(i);
                model.addRow(new Object[]{parts[1], parts[0]});
            }

            BannedTbl.setModel(model);

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }


    // Feature in Ban Table when clicking a banned account
    private void showSelectedBannedAccount() {
        int row = BannedTbl.getSelectedRow();

        if (row < 0 || row >= bannedAccounts.size()) {
            return;
        }

        String[] account = bannedAccounts.get(row);

        accountName.setText(account[1]);
        accountRole.setText(account[0]);

        if (account.length >= 6) {
            banReasonDisplayInLogIn.setText(account[5]);
        }
    }


    // Unbanning an account. Removing the account from the banned.txt and updating the table at the same time
    private void unbanSelectedAccount() {
        int row = BannedTbl.getSelectedRow();

        if (row < 0 || row >= bannedAccounts.size()) {
            JOptionPane.showMessageDialog(this, "Please select an account first");
            return;
        }

        String[] account = bannedAccounts.get(row);

        try {
            System.out.println("unbanning acc");

            Files.unbanAccount(account[0], account[1]);

            // This is for saving the action (unbanning) to the history
            Files.saveHistory("Unbanned", account[1], account[0], adminUsername);

            refreshAccounts();

            accountName.setText("");
            accountRole.setText("");
            banReasonDisplayInLogIn.setText("");

            JOptionPane.showMessageDialog(this, "Account successfully unbanned!");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }


    // Banning an account and giving the reason why it was banned
    private void banAccount(String role, String[] account) {
        String reason = JOptionPane.showInputDialog(this, "Reason for ban:");

        if (reason == null || reason.trim().isEmpty()) {
            return;
        }

        try {
            System.out.println("Banning acc");
            Files.banAccount(role, account, reason);

            // This is for saving the action (banning) to the history
            Files.saveHistory("Banned", account[1], account[0], adminUsername);

            refreshAccounts();

            CustomerDetailsPnl.setVisible(false);
            SellerDetailsPnl.setVisible(false);
            CourierDetailsPnl.setVisible(false);

            JOptionPane.showMessageDialog(this, "Account successfully banned!");

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }


}
