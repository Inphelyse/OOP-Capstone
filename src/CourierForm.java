import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class CourierForm extends JFrame {
    private JPanel pnlMain;
    private JPanel CourierPnl;
    private JPanel HeaderPnl;
    private JPanel BodyPnl;
    private JPanel NavigationPnl;
    private JButton DeliveriesBtn;
    private JButton HistoryBtn;
    private JButton logOutCourier;
    private JPanel DataPnl;
    private JPanel DeliveriesPnl;
    private JPanel HistoryPnl;
    private JTable DeliveriesTbl;
    private JTable HistoryTbl;
    private JLabel DeliveryOrderLbl;
    private JLabel DeliveryDropOffLbl;
    private JLabel DeliveryStatusLbl;
    private JLabel accountType;

    public CourierForm(String username, String _accountType) {
        accountType.setText(username + " (" + _accountType + ")");

        DeliveriesBtn.addActionListener(e -> showPage("deliveries"));
        HistoryBtn.addActionListener(e -> showPage("history"));
        logOutCourier.addActionListener(e -> {
            BossStore.showLogInPanel();
            this.dispose();
        });

        fillDeliveries();
        fillHistory();
        showPage("deliveries");

        setContentPane(pnlMain);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void showPage(String page) {
        DeliveriesPnl.setVisible(page.equals("deliveries"));
        HistoryPnl.setVisible(page.equals("history"));
    }

    private void fillDeliveries() {
        DefaultTableModel model = new DefaultTableModel(new String[]{"Order", "Drop-off", "Status"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        model.addRow(new Object[]{"ORD-1001", "Cebu City", "Out for delivery"});
        model.addRow(new Object[]{"ORD-1002", "Mandaue City", "Picked up"});
        DeliveriesTbl.setModel(model);
        DeliveriesTbl.setRowHeight(44);
        DeliveriesTbl.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        DeliveriesTbl.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                showSelectedDelivery();
            }
        });
        if (model.getRowCount() > 0) {
            DeliveriesTbl.setRowSelectionInterval(0, 0);
        }
    }

    private void showSelectedDelivery() {
        int row = DeliveriesTbl.getSelectedRow();
        if (row < 0) {
            DeliveryOrderLbl.setText("");
            DeliveryDropOffLbl.setText("");
            DeliveryStatusLbl.setText("");
            return;
        }
        DeliveryOrderLbl.setText(DeliveriesTbl.getValueAt(row, 0).toString());
        DeliveryDropOffLbl.setText(DeliveriesTbl.getValueAt(row, 1).toString());
        DeliveryStatusLbl.setText(DeliveriesTbl.getValueAt(row, 2).toString());
    }

    private void fillHistory() {
        DefaultTableModel model = new DefaultTableModel(new String[]{"Order", "Drop-off", "Status"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        model.addRow(new Object[]{"ORD-0988", "Lapu-Lapu City", "Delivered"});
        HistoryTbl.setModel(model);
        HistoryTbl.setRowHeight(44);
        HistoryTbl.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }
}
