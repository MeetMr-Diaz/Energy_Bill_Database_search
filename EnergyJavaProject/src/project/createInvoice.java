package project;

import javax.swing.*;
import java.awt.*;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

public class createInvoice extends JFrame implements ActionListener {
    private final JPanel northPanel;
    private final JPanel centerPanel;
    private final JPanel southPanel;
    private JLabel label1, acctNoLabel,acctNoSet, nameLabel, nameSet, addrLabel, addrSet, phoneLabel, phoneSet, energyLabel, energySet,
            meterlabel, meterSet, priceLabel, priceSet, chargeLabel, chargeSet, billStatus, billStatusSet;
    private String acctNo,firstName,lastName,address,phone,energyPlan,meterType,kWhPrice,status,charges;
    private JButton print;

    public createInvoice(String acctNo, String firstName, String lastName, String address, String phone,String energyPlan,
                         String meterType, String kWhPrice, String status,String charges) {
        this.acctNo=acctNo;
        this.firstName=firstName;
        this.lastName=lastName;
        this.address=address;
        this.phone=phone;
        this.energyPlan=energyPlan;
        this.meterType=meterType;
        this.kWhPrice=kWhPrice;
        this.status=status;
        this.charges = charges;

        setTitle("Invoice");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(400, 500);

        northPanel = new JPanel();
        southPanel = new JPanel();
        centerPanel = new JPanel(new GridLayout( 10,2));
        label1 = new JLabel("This month Charges");

        Font font = label1.getFont();
        float size = font.getSize() + 10.0f; // increase the font size by 100
        font = font.deriveFont(size); // create a new font object with the new size
        label1.setFont(font); // set the new font for the label
        acctNoSet = new JLabel();
        nameSet = new JLabel();
        addrSet = new JLabel();
        phoneSet = new JLabel();
        energySet = new JLabel();
        meterSet = new JLabel();
        priceSet = new JLabel();
        chargeSet = new JLabel();
        billStatusSet = new JLabel();

        acctNoLabel = new JLabel("Account Number:");     acctNoSet.setText(acctNo);
        nameLabel = new JLabel("Name: ");                nameSet.setText(firstName+" " +lastName);
        addrLabel = new JLabel("Address: ");             addrSet.setText(address);
        phoneLabel = new JLabel("Telephone: ");          phoneSet.setText(phone);
        energyLabel = new JLabel("Energy Plan: ");       energySet.setText(energyPlan);
        meterlabel = new JLabel("Meter Type: ");           meterSet.setText(meterType);
        priceLabel =new JLabel("Price Per KhW: ");         priceSet.setText(kWhPrice);
        chargeLabel = new JLabel("This month charges: ");   chargeSet.setText(charges);
        billStatus = new JLabel("Status: ");               billStatusSet.setText(status);

        print  = new JButton("Print invoice");
        print.addActionListener(this);

        centerPanel.add(acctNoLabel);
        centerPanel.add(acctNoSet);
        centerPanel.add(nameLabel);
        centerPanel.add(nameSet);
        centerPanel.add(addrLabel);
        centerPanel.add(addrSet);
        centerPanel.add(phoneLabel);
        centerPanel.add(phoneSet);
        centerPanel.add(energyLabel);
        centerPanel.add(energySet);
        centerPanel.add(meterlabel);
        centerPanel.add(meterSet);
        centerPanel.add(priceLabel);
        centerPanel.add(priceSet);
        centerPanel.add(chargeLabel);
        centerPanel.add(chargeSet);
        centerPanel.add(billStatus);
        centerPanel.add(billStatusSet);

        northPanel.add(label1);
        southPanel.add(print);

        add(northPanel,BorderLayout.NORTH);
        add(centerPanel,BorderLayout.CENTER);
        add(southPanel,BorderLayout.SOUTH);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == print) {
            // call the printInvoice method to print the invoice
            printInvoice();
        }
    }
    private void printInvoice() {
        // create a PrinterJob
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setJobName("Invoice");

        // create a Printable object to print the contents of the centerPanel
        Printable printable = new Printable() {
            @Override
            public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
                if (pageIndex > 0) {
                    // no more pages
                    return Printable.NO_SUCH_PAGE;
                } else {
                    // print the centerPanel
                    centerPanel.print(graphics);
                    return Printable.PAGE_EXISTS;
                }
            }
        };
        // set the printable for the PrinterJob
        job.setPrintable(printable);

        // display the print dialog and print the invoice if the user clicks "Print"
        if (job.printDialog()) {
            try {
                job.print();
            } catch (PrinterException ex) {
                ex.printStackTrace();
            }
        }
    }
}

