import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

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
    private JLabel ProductNameLbl;
    private JLabel ProductPriceLbl;
    private JLabel ProductQuantityLbl;
    private JComboBox<String> DeliveryStatusCmb;
    private JLabel accountType;
    private JLabel greetUsername;

    private final List<Delivery> active = new ArrayList<>();
    private final List<Delivery> history = new ArrayList<>();
    private boolean syncingStatus;

    public CourierForm(String username, String _accountType) {
        accountType.setText(username + " (" + _accountType + ")");
        greetUsername.setText("Hello, " + username);

        DeliveriesBtn.addActionListener(e -> showPage("deliveries"));
        HistoryBtn.addActionListener(e -> showPage("history"));
        logOutCourier.addActionListener(e -> {
            BossStore.showLogInPanel();
            this.dispose();
        });

        DeliveryStatusCmb.addActionListener(e -> applySelectedStatus());
        DeliveriesTbl.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                showSelectedDelivery();
            }
        });

        loadDeliveries();
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

    private void loadDeliveries() {
        active.clear();
        history.clear();
        active.add(new Delivery("ORD-1001", "Cebu City", "To be delivered", "Shoes", 67, 1));
        active.add(new Delivery("ORD-1002", "Mandaue City", "Shipping", "Plate", 68, 2));
        history.add(new Delivery("ORD-0988", "Lapu-Lapu City", "Delivered", "Pencil", 69, 1));
        refreshDeliveriesTable(0);
        refreshHistoryTable();
    }

    private void refreshDeliveriesTable(int selectRow) {
        DefaultTableModel model = new DefaultTableModel(new String[]{"Order", "Drop-off", "Status"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        for (Delivery delivery : active) {
            model.addRow(new Object[]{delivery.order, delivery.dropOff, delivery.status});
        }
        DeliveriesTbl.setModel(model);
        DeliveriesTbl.setRowHeight(44);
        DeliveriesTbl.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        if (selectRow >= 0 && selectRow < active.size()) {
            DeliveriesTbl.setRowSelectionInterval(selectRow, selectRow);
        } else {
            clearProductDetails();
        }
    }

    private void refreshHistoryTable() {
        DefaultTableModel model = new DefaultTableModel(new String[]{"Order", "Drop-off", "Status"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        for (Delivery delivery : history) {
            model.addRow(new Object[]{delivery.order, delivery.dropOff, delivery.status});
        }
        HistoryTbl.setModel(model);
        HistoryTbl.setRowHeight(44);
        HistoryTbl.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private void showSelectedDelivery() {
        int row = DeliveriesTbl.getSelectedRow();
        if (row < 0 || row >= active.size()) {
            clearProductDetails();
            return;
        }
        Delivery delivery = active.get(row);
        ProductNameLbl.setText(delivery.name);
        ProductPriceLbl.setText(String.format("$%.2f", delivery.price));
        ProductQuantityLbl.setText(String.valueOf(delivery.quantity));
        DeliveryStatusCmb.setEnabled(true);
        syncingStatus = true;
        DeliveryStatusCmb.setSelectedItem(delivery.status);
        syncingStatus = false;
    }

    private void clearProductDetails() {
        ProductNameLbl.setText("Select a delivery");
        ProductPriceLbl.setText("—");
        ProductQuantityLbl.setText("—");
        syncingStatus = true;
        DeliveryStatusCmb.setSelectedIndex(-1);
        DeliveryStatusCmb.setEnabled(false);
        syncingStatus = false;
    }

    private void applySelectedStatus() {
        if (syncingStatus) {
            return;
        }
        int row = DeliveriesTbl.getSelectedRow();
        if (row < 0 || row >= active.size() || DeliveryStatusCmb.getSelectedItem() == null) {
            return;
        }
        String status = DeliveryStatusCmb.getSelectedItem().toString();
        Delivery delivery = active.get(row);
        delivery.status = status;
        if ("Delivered".equals(status)) {
            active.remove(row);
            history.add(0, delivery);
            refreshHistoryTable();
            int next = active.isEmpty() ? -1 : Math.min(row, active.size() - 1);
            refreshDeliveriesTable(next);
            return;
        }
        DeliveriesTbl.setValueAt(status, row, 2);
    }

    private static class Delivery {
        private final String order;
        private final String dropOff;
        private String status;
        private final String name;
        private final double price;
        private final int quantity;

        private Delivery(String order, String dropOff, String status, String name, double price, int quantity) {
            this.order = order;
            this.dropOff = dropOff;
            this.status = status;
            this.name = name;
            this.price = price;
            this.quantity = quantity;
        }
    }
}
