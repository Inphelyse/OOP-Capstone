import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

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
    private JButton logOutAdmin;
    private JLabel showTotalPeople;

    public Admin(){
        CustomerDetailsPnl.setVisible(false);

        HomeBtn.addActionListener(d -> showPage("home"));
        SellersButton.addActionListener(d -> showPage("sellers"));
        CustomersBtn.addActionListener(d -> showPage("customers"));
        CouriersBtn.addActionListener(d -> showPage("couriers"));
        FeedbackBtn.addActionListener(d -> showPage("feedbacks"));
        BansBtn.addActionListener(d->showPage("bans"));
        HistoryBtn.addActionListener(d->showPage("history"));

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
