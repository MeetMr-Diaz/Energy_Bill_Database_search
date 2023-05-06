package project;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import static project.CustomerSearch.*;
public class deleteCustomerListener implements ActionListener {
    private JTextField searchTextField; // new field to hold searchTextField

    public deleteCustomerListener(JTextField searchTextField) {
        this.searchTextField = searchTextField;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String searchText = searchTextField.getText(); // use the searchTextField passed in the constructor

        try {
            // establish database connection
            Connection conn = DriverManager.getConnection(dbUrl, USERNAME, PASSWORD);
            Statement statement = conn.createStatement();

            String query;
            int rowsDeleted;

            if (searchText.matches("\\d+")) { // check if input contains only digits
                query = "DELETE FROM customer WHERE accountNo = " + searchText;
                rowsDeleted = statement.executeUpdate(query);
            } else {
                String[] names = searchText.split(" ");
                query = "DELETE FROM customer WHERE FirstName = '" + names[0] + "' AND LastName = '" + names[1] + "'";
                rowsDeleted = statement.executeUpdate(query);
            }

            if (rowsDeleted > 0) {
                JOptionPane.showMessageDialog(null, "Customer record deleted successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "No customer record found for the given input.");
            }

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
}

