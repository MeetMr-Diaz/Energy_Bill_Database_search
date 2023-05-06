package project;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

import static project.CustomerSearch.*;
public class CustomerAddWindow extends JFrame {

    // create object of menu panel
    private MenuPanel menuPanel;
    private JLabel acctNoLabel,firstNameLabel,lastNameLabel,currentBillLabel;
    private JPanel southPanel, panel;
    private JButton addBtn,calculateBtn,upDateBtn;
    private JTextField firstNameTextField;
    private JTextField acctNoTextField;
    private JLabel addressLabel,phoneLabel,energyTariffLabel,meterTypeLabel,kWhPriceLabel,calculationsLabel,spacer;
    private JComboBox comboBox,comboBox2;
    public CustomerAddWindow() {

        menuPanel = new MenuPanel(); // initialize menu panel object
        setJMenuBar(menuPanel.getMenuBar()); // get Window menu from the menu panel
        menuPanel.setCurrentWindow(this); // set current window
        setTitle("Add Customer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        //account information
        acctNoLabel = new JLabel("Account Number: ");
        acctNoTextField = new JTextField(15);

        //first name information
        firstNameLabel = new JLabel("First Name: ");
        firstNameTextField = new JTextField(15);

        //last name information
        lastNameLabel = new JLabel("Last Name: ");
        lastNameTextField = new JTextField();

        //address information
        addressLabel = new JLabel("Address");
        addressTexField = new JTextField(10);

        //phone information
        phoneLabel = new JLabel("Telephone Number");
        phoneDisplayTextField = new JTextField();

        //energy tariff information
        energyTariffLabel = new JLabel("Tariff Type");
        String[] tariffUsage = {"Monthly Flat Rate","Daily Usage"};
        comboBox2 = new JComboBox(tariffUsage);
        comboBox2.addActionListener(new setTariffListener());

        //meter type
        meterTypeLabel = new JLabel("Meter Reading");
        meterTypeText = new JTextField();

        //charges paid or unpaid
        kWhPriceLabel = new JLabel("Charge Rate (per KiloWatts)");
        kWhPriceTextField = new JTextField();

        //calculations for the charges
        calculationsLabel = new JLabel("Total monthly charges $");
        calculationsTextField = new JTextField();
        calculationsTextField.setEditable(false);
        calculateBtn = new JButton("Calculate total charges");
        calculateBtn.addActionListener(new calActionListener());
        spacer = new JLabel();
        calculateBtn.setVisible(false);

        currentBillLabel = new JLabel("Bill status");
        currentBillTextField = new JTextField();

        String[] status = {"Paid","Unpaid"};
        comboBox = new JComboBox(status);

        panel = new JPanel(new GridLayout(13,2));
        panel.add(acctNoLabel);
        panel.add(acctNoTextField);
        panel.add(firstNameLabel);
        panel.add(firstNameTextField);
        panel.add(lastNameLabel);
        panel.add(lastNameTextField);
        panel.add(addressLabel);
        panel.add(addressTexField);
        panel.add(phoneLabel);
        panel.add(phoneDisplayTextField);
        panel.add(energyTariffLabel);
        panel.add(comboBox2);
        panel.add(meterTypeLabel);
        panel.add(meterTypeText);
        panel.add(kWhPriceLabel);
        panel.add(kWhPriceTextField);
        panel.add(calculationsLabel);
        panel.add(calculationsTextField);
        panel.add(currentBillLabel);
        panel.add(comboBox);

        panel.add(spacer);
        panel.add(calculateBtn);

        addBtn = new JButton("Add Customer");
        addBtn.addActionListener(new addNewCustomerListener());
        upDateBtn = new JButton("Update");
        upDateBtn.addActionListener(new updateListener(searchTextField));

        southPanel = new JPanel();
        southPanel.add(addBtn);
        southPanel.add(upDateBtn);

        add(panel,BorderLayout.CENTER);
        add(southPanel,BorderLayout.SOUTH);

        setLocationRelativeTo(null);
        setVisible(true);
    }
    public class setTariffListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource()==comboBox2){

                String item = (String) comboBox2.getSelectedItem();
                if(item.equals("Monthly Flat Rate")) {
                    calculateBtn.setVisible(false);
                    kWhPriceTextField.setText("1.9");
                    calculationsTextField.setText("110");
                }else if(item.equals("Daily Usage")){
                    calculateBtn.setVisible(true);
                    kWhPriceTextField.setText("1.1");
                }
            }
        }
    }

    private class calActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            double meters = Double.parseDouble(meterTypeText.getText());
            double prices = Double.parseDouble(kWhPriceTextField.getText());
            double totalCharges = meters*prices;
            calculationsTextField.setText(String.valueOf(totalCharges));
        }
    }

    private class addNewCustomerListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

            int result = JOptionPane.showConfirmDialog(null, "Do you want to add a new customer?", "Confirmation", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {

                String accountNo = acctNoTextField.getText();
                String firstName = firstNameTextField.getText();
                String lastName = lastNameTextField.getText();
                String address = addressTexField.getText();
                String phone = phoneDisplayTextField.getText();
                String tariff = (String) comboBox2.getSelectedItem();
                String meter = meterTypeText.getText();
                String kWhPrice = kWhPriceTextField.getText();
                String payment = calculationsTextField.getText();
                String currentBillStatus = (String) comboBox.getSelectedItem();

                try {
                    // establish database connection
                    Connection conn = DriverManager.getConnection(dbUrl, USERNAME, PASSWORD);

                    // check if record with the same primary key value already exists
                    String checkSql = "SELECT * FROM customer WHERE accountNo = ?";
                    PreparedStatement checkStmt = conn.prepareStatement(checkSql);
                    checkStmt.setString(1, accountNo);
                    ResultSet rs = checkStmt.executeQuery();

                    if (rs.next()) {
                        JOptionPane.showMessageDialog(null, "A customer with the same account number already exists.");
                    } else {
                        // insert new record into the database
                        String insertSql = "INSERT INTO customer (accountNo, FirstName, LastName, address, Telephone, energyTariff) " +
                                "VALUES (?, ?, ?, ?, ?, ?)";
                        PreparedStatement insertStmt = conn.prepareStatement(insertSql);
                        insertStmt.setString(1, accountNo);
                        insertStmt.setString(2, firstName);
                        insertStmt.setString(3, lastName);
                        insertStmt.setString(4, address);
                        insertStmt.setString(5, phone);
                        insertStmt.setString(6, tariff);
                        int rowsUpdated = insertStmt.executeUpdate();

                        String insertDashboardSql = "INSERT INTO dash (accountNo, address, meter, kWhPrice, payment, currentBill) " +
                                "VALUES (?, ?, ?, ?,?,?)";
                        PreparedStatement insertDashboardStmt = conn.prepareStatement(insertDashboardSql);
                        insertDashboardStmt.setString(1, accountNo);
                        insertDashboardStmt.setString(2, address);
                        insertDashboardStmt.setString(3, meter);
                        insertDashboardStmt.setString(4, kWhPrice);
                        insertDashboardStmt.setString(5, payment);
                        insertDashboardStmt.setString(6,currentBillStatus);
                        int rowsUpdated2 = insertDashboardStmt.executeUpdate();

                        if (rowsUpdated > 0 && rowsUpdated2 > 0) {
                            JOptionPane.showMessageDialog(null, "New customer added successfully!");
                        }
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Error adding new customer: " + ex.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(null, "You selected No");
            }
        }
    }
}



