import javax.swing.*;
import javax.swing.table.DefaultTableModel;
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
    private JTable CouriersTbl;
    private JLabel CourierCountLbl;
    private JLabel CourierRangeLbl;
    private JLabel CourierStatusLbl;
    private JLabel CourierNameLbl;
    private JLabel CourierPhoneLbl;
    private JLabel CourierPhoneValueLbl;
    private JLabel CourierAddressLbl;

    // Each row is type|username|password|phone|address, same as Persons/courier.txt
    private final ArrayList<String[]> couriers = new ArrayList<>();

    public Admin(){
        HomeBtn.addActionListener(d -> showPage("home"));
        SellersButton.addActionListener(d -> showPage("sellers"));
        CustomersBtn.addActionListener(d -> showPage("customers"));
        CouriersBtn.addActionListener(d -> showPage("couriers"));
        FeedbackBtn.addActionListener(d -> showPage("feedbacks"));
        BansBtn.addActionListener(d->showPage("bans"));
        HistoryBtn.addActionListener(d->showPage("history"));

        CouriersTbl.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                showSelectedCourier();
            }
        });
        loadCouriers();

        setContentPane(pnlMain);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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

        DefaultTableModel model = new DefaultTableModel(new String[]{"Username", "Phone", "Address"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        for (String[] parts : couriers) {
            model.addRow(new Object[]{parts[1], parts[3], parts[4]});
        }

        CouriersTbl.setModel(model);
        CouriersTbl.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        int count = couriers.size();
        CourierCountLbl.setText(count + (count == 1 ? " account" : " accounts"));
        if (count == 0) {
            CourierRangeLbl.setText("Showing 0 accounts");
            showSelectedCourier();
        } else {
            CourierRangeLbl.setText("Showing 1-" + count + " of " + count + " accounts");
            CouriersTbl.setRowSelectionInterval(0, 0);
        }
    }

    private void showSelectedCourier() {
        int row = CouriersTbl.getSelectedRow();
        if (row < 0 || row >= couriers.size()) {
            CourierStatusLbl.setText("");
            CourierNameLbl.setText("");
            CourierPhoneLbl.setText("");
            CourierPhoneValueLbl.setText("");
            CourierAddressLbl.setText("");
            return;
        }

        String[] parts = couriers.get(row);
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




}
