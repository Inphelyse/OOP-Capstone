import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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
    private JTable ReportsTbl;
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
    private JTable HistoryTbl;
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

    public Admin(String username, String _accountType){
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
        BansBtn.addActionListener(d->showPage("bans"));
        HistoryBtn.addActionListener(d->showPage("history"));

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

        if (row < 0 || index >= customer.size()) {
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

    private void generateTable(){
        try(BufferedReader br = new BufferedReader(new FileReader("Persons/customer.txt"))){
            String[] columns = {"Role", "Name", "Username", "Number", "Location"};
            DefaultTableModel model = new DefaultTableModel(columns, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            customerAccountsTable.setModel(model);
            String line;
            while((line = br.readLine()) != null){
                String[] customerInfo = line.split("\\|");
                if (customerInfo.length < 5) {
                    continue;
                }
                model.addRow(new String[]{customerInfo[0], customerInfo[1], customerInfo[2], customerInfo[3], customerInfo[4]});
            }
        }catch(IOException e){
            System.out.println("No customers Info!");
        }

        try(BufferedReader br = new BufferedReader(new FileReader("Persons/seller.txt"))){
            String[] columns = {"Role", "Name", "Username", "Number", "Location"};
            DefaultTableModel model = new DefaultTableModel(columns, 0);
            sellerAccountsTable.setModel(model);
            String line;
            while((line = br.readLine()) != null){
                String[] sellerInfo = line.split("\\|");
                if (sellerInfo.length < 5) {
                    continue;
                }
                model.addRow(new String[]{sellerInfo[0], sellerInfo[1], sellerInfo[2], sellerInfo[3], sellerInfo[4]});
            }
        }catch(IOException e){
            System.out.println("No customers Info!");
        }

        try(BufferedReader br = new BufferedReader(new FileReader("Persons/courier.txt"))){
            String[] columns = {"Role", "Name", "Username", "Number", "Location"};
            DefaultTableModel model = new DefaultTableModel(columns, 0);
            courierAccountsTable.setModel(model);
            String line;
            while((line = br.readLine()) != null){
                String[] courierInfo = line.split("\\|");

                if (courierInfo.length < 5) {
                    continue;
                }

                model.addRow(new String[]{courierInfo[0], courierInfo[1], courierInfo[2], courierInfo[3], courierInfo[4]});
            }
        }catch(IOException e){
            System.out.println("No customers Info!");
        }

    }





}
