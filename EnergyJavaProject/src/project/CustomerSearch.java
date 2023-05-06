package project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
public class CustomerSearch extends JFrame {
    private final MenuPanel menuPanel;
    private JPanel panel,informationPanel,southPanel;
    static JTextField acctNoTextField,firstNameTextField,lastNameTextField, addressTexField;
    static JTextField searchTextField,currentBillTextField,phoneDisplayTextField;
    static JTextField meterTypeText,kWhPriceTextField,calculationsTextField;
    private JLabel searchLabel,acctNoLabel,firstNameLabel,lastNameLabel,addressLabel,phoneLabel,energyTariffLabel;
    private JLabel meterTypeLabel,kWhPriceLabel,calculationsLabel,currentBillLabel,spacer;
    private JButton searchBtn, calculateBtn,invoiceBtn,upDateBtn,deleteBtn;
    static final String dbUrl = "jdbc:mysql://localhost:3306/projectenergy";
    static final String USERNAME = "root";
    static final String PASSWORD = "Password123";
    static JComboBox billStatusComboBox, energyPlanComboBox;

    public CustomerSearch(Object source){

        setTitle("search Customer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        menuPanel = new MenuPanel(); // initialize menu panel object
        setJMenuBar(menuPanel.getMenuBar()); // get Window menu from the menu panel
        menuPanel.setCurrentWindow(this); // set current window

        searchLabel = new JLabel("Name or Account");
        searchTextField = new JTextField(15);
        searchBtn = new JButton("Find");
        searchBtn.addActionListener(new searchActionListener());

        panel = new JPanel();
        panel.add(searchLabel);
        panel.add(searchTextField);
        panel.add(searchBtn);

        southPanel = new JPanel();
        calculateBtn = new JButton("Calculate Total Charges");
        calculateBtn.setVisible(false);
        calculateBtn.addActionListener(new calBtnListener());
        upDateBtn = new JButton("Update information");
        upDateBtn.addActionListener(new updateListener(searchTextField));
        deleteBtn = new JButton("Delete Customer");
        deleteCustomerListener listener = new deleteCustomerListener(searchTextField);
        deleteBtn.addActionListener(listener);
        invoiceBtn = new JButton("Create Invoice");
        invoiceBtn.addActionListener(new invoiceBill() );
        // createInvoice bill = new createInvoice(searchTextField);

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
        energyTariffLabel = new JLabel("Energy Plan");
        String[] tariffUsage = {"Monthly Flat Rate","Daily Usage"};
        energyPlanComboBox = new JComboBox(tariffUsage);
        energyPlanComboBox.addActionListener(new TariffListener());

        //meter type
        meterTypeLabel = new JLabel("Meter Reading");
        meterTypeText = new JTextField();

        //charges paid or unpaid
        kWhPriceLabel = new JLabel("Charge Rate (per KiloWatts)");
        kWhPriceTextField = new JTextField();

        //calculations for the charges
        calculationsLabel = new JLabel("total charges $");
        calculationsTextField = new JTextField();
        calculationsTextField.setEditable(false);

        currentBillLabel = new JLabel("Bill status");
        //comboBox for bill status
        String[] status = {"Paid","Unpaid"};
        billStatusComboBox = new JComboBox(status);
        spacer = new JLabel();

        informationPanel = new JPanel(new GridLayout(13,2));
        informationPanel.add(acctNoLabel);
        informationPanel.add(acctNoTextField);
        informationPanel.add(firstNameLabel);
        informationPanel.add(firstNameTextField);
        informationPanel.add(lastNameLabel);
        informationPanel.add(lastNameTextField);
        informationPanel.add(addressLabel);
        informationPanel.add(addressTexField);
        informationPanel.add(phoneLabel);
        informationPanel.add(phoneDisplayTextField);
        informationPanel.add(energyTariffLabel);
        informationPanel.add(energyPlanComboBox);
        informationPanel.add(meterTypeLabel);
        informationPanel.add(meterTypeText);
        informationPanel.add(kWhPriceLabel);
        informationPanel.add(kWhPriceTextField);
        informationPanel.add(calculationsLabel);
        informationPanel.add(calculationsTextField);
        informationPanel.add(currentBillLabel);
        informationPanel.add(billStatusComboBox);
        informationPanel.add(spacer);
        informationPanel.add(calculateBtn);
        informationPanel.setVisible(false);

        southPanel.add(upDateBtn);
        southPanel.add(invoiceBtn);
        southPanel.add(deleteBtn);

        add(panel, BorderLayout.NORTH);
        add(informationPanel, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
        setVisible(true);
    }
    private class TariffListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource()== energyPlanComboBox){

                String item = (String) energyPlanComboBox.getSelectedItem();
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
    private class calBtnListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            double meters = Double.parseDouble(meterTypeText.getText());
            double prices = Double.parseDouble(kWhPriceTextField.getText());
            double totalCharges = meters*prices;

            String formattedCharges = String.format("%.2f", totalCharges);
            calculationsTextField.setText(formattedCharges);
        }
    }

    class searchActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String searchText = searchTextField.getText();
            boolean searchByName = true;
            // check if the search text is an account number or a name
            try {
                Integer.parseInt(searchText);
                searchByName = false;
            } catch (NumberFormatException ex) {
                // not an integer, it's a name
            }

            try {
                Connection conn = DriverManager.getConnection(dbUrl, USERNAME, PASSWORD);
                PreparedStatement statement;

                if (searchByName) {
                    // search by name
                    String[] names = searchText.split(" ");
                    String query = "SELECT c.*, d.meter, d.kWhPrice, d.currentBill FROM customer c JOIN dash d ON c.accountNo = d.accountNo WHERE c.FirstName = ? AND c.LastName = ?";
                    statement = conn.prepareStatement(query);
                    statement.setString(1, names[0]);
                    statement.setString(2, names[1]);
                } else {
                    // search by account number
                    String query = "SELECT customer.*, dash.meter, dash.kWhPrice,dash.currentBill FROM customer LEFT JOIN dash ON customer.accountNo = dash.accountNo WHERE customer.accountNo = ?";
                    statement = conn.prepareStatement(query);
                    statement.setString(1, searchText);
                }


                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    int account = Integer.parseInt(resultSet.getString("accountNo"));
                    String firstName = resultSet.getString("FirstName"); // customer table
                    String lastName = resultSet.getString("LastName");  // customer table
                    String address = resultSet.getString("Address");     // customer table
                    String phone = resultSet.getString("Telephone");       // customer table
                    String energyTariff = resultSet.getString("energyTariff");  // customer table
                    String meter = resultSet.getString("meter"); // from dash table
                    String kWhPrice = resultSet.getString("kWhPrice"); // from dash table
                    String currentBill = resultSet.getString("currentBill"); // from dash

                    acctNoTextField.setText(String.valueOf(account));
                    firstNameTextField.setText(firstName);
                    lastNameTextField.setText(lastName);
                    addressTexField.setText(address);
                    phoneDisplayTextField.setText(phone);
                    energyPlanComboBox.setSelectedItem(energyTariff);

                    // energyTariffText.setText(energyTariff);
                    meterTypeText.setText(meter);
                    kWhPriceTextField.setText(kWhPrice);
                    billStatusComboBox.setSelectedItem(currentBill);

                    double meters = Double.parseDouble(meterTypeText.getText());
                    double prices = Double.parseDouble(kWhPriceTextField.getText());
                    double totalCharges = meters * prices;
                    calculationsTextField.setText(String.valueOf(totalCharges));
                    informationPanel.setVisible(true);

                } else {
                    JOptionPane.showMessageDialog(informationPanel, "No results found.");
                }
                resultSet.close();
                statement.close();
                conn.close();

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error adding new customer: " + ex.getMessage());
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            revalidate();
            repaint();
        }
        public String getAcctNo() {
            return acctNoTextField.getText();
        }
        public String getFirstName() {
            return firstNameTextField.getText();
        }
        public String getLastName() {
            return lastNameTextField.getText();
        }
        public String getAddress() {
            return addressTexField.getText();
        }
        public String getPhone() {
            return phoneDisplayTextField.getText();
        }

        public String getEnergyPlan(){
            return (String) energyPlanComboBox.getSelectedItem();
        }
        public String getMeterType() {
            return meterTypeText.getText();
        }

        public String getkWhPrice() {
            return kWhPriceTextField.getText();
        }

        public String getStatus() {
            return (String) billStatusComboBox.getSelectedItem();
        }

        public String calculationsTextField() {
            return calculationsTextField.getText();
        }
    }

    class invoiceBill implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            searchActionListener searchActionListener = new searchActionListener();

            String acctNo = searchActionListener.getAcctNo();
            String firstName = searchActionListener.getFirstName();
            String lastName = searchActionListener.getLastName();
            String address = searchActionListener.getAddress();
            String phone = searchActionListener.getPhone();
            String energyPlan = searchActionListener.getEnergyPlan();
            String meterType = searchActionListener.getMeterType();
            String kWhPrice = searchActionListener.getkWhPrice();
            String status = searchActionListener.getStatus();
            String charges = searchActionListener.calculationsTextField();

            createInvoice createInvoice = new createInvoice(acctNo, firstName, lastName, address, phone,energyPlan, meterType,
                    kWhPrice, status, charges);
        }
    }
}



