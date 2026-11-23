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
    private JTable customerInfoTbl;
    private JPanel CustomerDetailsPnl;
    private JTable CouriersTbl;
    private JLabel CourierCountLbl;
    private JLabel CourierRangeLbl;
    private JLabel CourierStatusLbl;
    private JLabel CourierNameLbl;
    private JLabel CourierPhoneLbl;
    private JLabel CourierPhoneValueLbl;
    private JLabel CourierAddressLbl;
    private JScrollPane CouriersScroll;
    private JButton CourierPrevBtn;
    private JButton CourierPageBtn;
    private JButton CourierNextBtn;
    private JButton logOutAdmin;
    private JLabel showTotalPeople;

    private static final int COURIERS_PER_PAGE = 20;

    // Each row is type|username|password|phone|address, same as Persons/courier.txt
    private final ArrayList<String[]> couriers = new ArrayList<>();
    private int courierPage = 0;

    public Admin(){
        CustomerDetailsPnl.setVisible(false);

        BossStore b = new BossStore();
        HomeBtn.addActionListener(d -> showPage("home"));
        SellersButton.addActionListener(d -> showPage("sellers"));
        CustomersBtn.addActionListener(d -> showPage("customers"));
        CouriersBtn.addActionListener(d -> showPage("couriers"));
        FeedbackBtn.addActionListener(d -> showPage("feedbacks"));
        BansBtn.addActionListener(d->showPage("bans"));
        HistoryBtn.addActionListener(d->showPage("history"));

        CouriersScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        CouriersScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        CouriersScroll.getVerticalScrollBar().setUnitIncrement(CouriersTbl.getRowHeight());
        CouriersTbl.setFillsViewportHeight(true);
        CouriersTbl.setPreferredScrollableViewportSize(new Dimension(480, CouriersTbl.getRowHeight() * 8));

        CouriersTbl.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                showSelectedCourier();
            }
        });
        CourierPrevBtn.addActionListener(e -> {
            if (courierPage > 0) {
                courierPage--;
                showCourierPage();
            }
        });
        CourierNextBtn.addActionListener(e -> {
            if (courierPage < pageCount() - 1) {
                courierPage++;
                showCourierPage();
            }
        });
        loadCouriers();

        int totalPeople = b.totalPeople();
        showTotalPeople.setText(String.valueOf(totalPeople));

        setContentPane(pnlMain);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        generateTableCustomer();


        customerInfoTbl.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int row = customerInfoTbl.getSelectedRow();
                String name = customerInfoTbl.getValueAt(row,1).toString();
                CustomerDetailsPnl.setVisible(true);
            }
        });
        logOutAdmin.addActionListener(d->{
            BossStore.showLogInPanel();
            this.dispose();
        });
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

        courierPage = 0;
        showCourierPage();
    }

    private int pageCount() {
        if (couriers.isEmpty()) {
            return 1;
        }
        return (couriers.size() + COURIERS_PER_PAGE - 1) / COURIERS_PER_PAGE;
    }

    private void showCourierPage() {
        int count = couriers.size();
        int pages = pageCount();
        if (courierPage >= pages) {
            courierPage = pages - 1;
        }
        if (courierPage < 0) {
            courierPage = 0;
        }

        int start = courierPage * COURIERS_PER_PAGE;
        int end = Math.min(start + COURIERS_PER_PAGE, count);

        DefaultTableModel model = new DefaultTableModel(new String[]{"Username", "Phone", "Address"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        for (int i = start; i < end; i++) {
            String[] parts = couriers.get(i);
            model.addRow(new Object[]{parts[1], parts[3], parts[4]});
        }

        CouriersTbl.setModel(model);
        CouriersTbl.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        CouriersTbl.setRowHeight(44);

        CourierCountLbl.setText(count + (count == 1 ? " account" : " accounts"));
        CourierPageBtn.setText(String.valueOf(courierPage + 1));
        CourierPrevBtn.setEnabled(courierPage > 0);
        CourierNextBtn.setEnabled(courierPage < pages - 1);

        if (count == 0) {
            CourierRangeLbl.setText("Showing 0 accounts");
            showSelectedCourier();
        } else {
            CourierRangeLbl.setText("Showing " + (start + 1) + "-" + end + " of " + count + " accounts");
            CouriersTbl.setRowSelectionInterval(0, 0);
            CouriersTbl.scrollRectToVisible(CouriersTbl.getCellRect(0, 0, true));
        }
    }

    private void showSelectedCourier() {
        int row = CouriersTbl.getSelectedRow();
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
        CourierNameLbl.setText(parts[1]);
        CourierPhoneLbl.setText(parts[3]);
        CourierPhoneValueLbl.setText(parts[3]);
        CourierAddressLbl.setText(parts[4]);
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

    private void generateTableCustomer(){

        try(BufferedReader br = new BufferedReader(new FileReader("Persons/customer.txt"))){
            String[] columns = {"Role", "Name", "Username", "Number", "Location"};
            DefaultTableModel model = new DefaultTableModel(columns, 0);
            customerInfoTbl.setModel(model);
            String line = null;
            while((line = br.readLine()) != null){
                String[] customerInfo = line.split(",");
                model.addRow(new String[]{customerInfo[0], customerInfo[1], customerInfo[2], customerInfo[3], customerInfo[4]});

            }

        }catch(IOException e){
            System.out.println("No customers Info!");
        }
    }


}
