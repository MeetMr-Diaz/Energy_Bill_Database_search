package project;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static project.CustomerSearch.*;

public class updateListener implements ActionListener {
    private JTextField searchTextField; // new field to hold searchTextField
    public updateListener(JTextField searchTextField) {
        this.searchTextField = searchTextField;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        try {
            // establish database connection
            Connection conn = DriverManager.getConnection(dbUrl, USERNAME, PASSWORD);

            // create prepared statement to update customer record
            PreparedStatement statement = conn.prepareStatement(
                    "UPDATE customer SET FirstName = ?, LastName = ?, Address = ?, Telephone = ?, energyTariff = ? WHERE accountNo = ?");

            // get values from text fields
            String firstName = firstNameTextField.getText();
            String lastName = lastNameTextField.getText();
            String accountNo = acctNoTextField.getText();
            String address = addressTexField.getText();
            String phone = phoneDisplayTextField.getText();
            String tariff = (String) energyPlanComboBox.getSelectedItem();
            //String tariff = energyTariffText.getText();

            // set parameter values in prepared statement
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            statement.setString(3, address);
            statement.setString(4, phone);
            statement.setString(5, tariff);
            statement.setString(6, accountNo);

            PreparedStatement statement2 = conn.prepareStatement("UPDATE dash SET meter =?, kWhPrice = ?, payment =?, currentBill =? WHERE accountNo =?");

            String meter = meterTypeText.getText();
            String kWhPrice = kWhPriceTextField.getText();
            String monthlyCharged = calculationsTextField.getText();
            String currentBillStatus = (String) billStatusComboBox.getSelectedItem();

            statement2.setString(1,meter);
            statement2.setString(2, kWhPrice);
            statement2.setString(3,monthlyCharged);
            statement2.setString(4,currentBillStatus);
            statement2.setString(5, accountNo);

            // execute update statement
            int rowsUpdated = statement.executeUpdate();
            int rowUpdated2 = statement2.executeUpdate();
            if (rowsUpdated > 0 && rowUpdated2 > 0) {
                JOptionPane.showMessageDialog(null, "Customer record updated successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "Failed to update customer record.");
            }

            // close database connection
            conn.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error updating customer record: " + ex.getMessage());
        }

    } }