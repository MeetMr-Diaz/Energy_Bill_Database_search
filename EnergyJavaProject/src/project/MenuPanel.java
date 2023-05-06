package project;

import java.awt.event.*;
import javax.swing.*;

public class MenuPanel extends JPanel {

    // The following will reference menu components.
    private JFrame currentWindow; // reference the current window initialized with the menu bar
    private JMenuBar menuBar; // The menu bar
    private JMenu customerMenuModify; // The Customer menu
    private JMenu customerMenuSearch; // The Customer menu
    private JMenuItem addCustomer; // To add customer menu item
    private JMenuItem customerLookup; // To add customer menu item
    private JMenuItem remove; // To remove customer menu item

    public MenuPanel() {
        // initialize the menu bar object
        menuBar = new JMenuBar();

        // call method to create the customer menu.
        buildCustomerMenu();

        // Add the Customer menu to the menu bar.
        menuBar.add( customerMenuSearch);
    }
    private void buildCustomerMenu() {

        // initialize customer 'add' menu item object
        addCustomer = new JMenuItem("Add Customer");
        addCustomer.setMnemonic(KeyEvent.VK_A);
        addCustomer.addActionListener(new MenuItemClick());

        // initialize customer 'remove' menu item object
        remove = new JMenuItem("Remove Customer");
        remove.setMnemonic(KeyEvent.VK_R);
        remove.addActionListener(new MenuItemClick());

        //search customer by name
        customerMenuSearch = new JMenu("Search");
        customerMenuSearch.setMnemonic(KeyEvent.VK_S);

        customerLookup = new JMenuItem("Customer");
        customerLookup.setMnemonic(KeyEvent.VK_C);
        customerLookup.addActionListener(new MenuItemClick());

        customerMenuSearch.add(customerLookup);
        customerMenuSearch.add(addCustomer);
        customerMenuSearch.add(remove);
    }

    /**
     * The MenuItemClick is an action listener to the menu click events
     *
     */

    private class MenuItemClick implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == addCustomer) {
                currentWindow.setVisible(false); // hide current/parent window
                new CustomerAddWindow(); // open customer add window

            } else if (e.getSource() == remove) {
                currentWindow.setVisible(false); // hide current/parent window
                new CustomerSearch(e.getSource());
            } else if ( e.getSource()== customerLookup ){
                currentWindow.setVisible(false);
                new CustomerSearch(e.getSource());  // opens search customer window
            }
        }
    }


    /**
     * Method to return the menu bar containing all menu items
     *
     * @returns JMenuBar object
     */
    public JMenuBar getMenuBar() {
        return menuBar;
    }

    /**
     * Method to set the current window frame
     *
     * @param f is the current window object reference
     */
    public void setCurrentWindow(JFrame f) {
        currentWindow = f;
    }
}

