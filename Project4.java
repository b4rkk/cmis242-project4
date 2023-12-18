import java.awt.Graphics;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JComponent;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.Border;
import java.util.TreeMap;

enum Status {
    FOR_SALE,
    UNDER_CONTRACT,
    SOLD;
}

interface StateChangeable<T extends Status>{
    public void changeState(T t);
}

class Property implements StateChangeable {

    // Instance Variables
    String address;
    int bedrooms, sqFeet, price;
    Status status;

    // Constructor
    public Property(String address, int bedrooms, int sqFeet, int price) {
        this.address = address;
        this.bedrooms = bedrooms;
        this.sqFeet = sqFeet;
        this.price = price;
        this.status = Status.FOR_SALE;
    }

    @Override
    // changeState Method
    public void changeState(Status status) {
        switch(status) {
            case FOR_SALE:
                this.status = Status.FOR_SALE;
                break;
            case UNDER_CONTRACT:
                this.status = Status.UNDER_CONTRACT;
                break;
            case SOLD:
                this.status = Status.SOLD;
                break;
        }
    }

    @Override
    // toString Method
    public String toString() {
        String str = "Property Address: " + this.address + "\n" +
                        "Bedrooms: " + this.bedrooms + "\n" +
                        "Square Feet: " + this.sqFeet + "\n" +
                        "Price: " + this.price + "\n" +
                        "Status: " + this.status;
        return str;
    }
}

public class Project4 extends JFrame {

    // Create TreeMap
    private TreeMap<Integer, Property> database = new TreeMap<Integer, Property>();

    // Create Labels
    JLabel lblTransNum = new JLabel("Transaction No:");
    JLabel lblAddress = new JLabel("Address:");
    JLabel lblBedrooms = new JLabel("Bedrooms:");
    JLabel lblSqFeet = new JLabel("Square Footage:");
    JLabel lblPrice = new JLabel("Price:");

    // Create Textfields
    JTextField tfTransNum = new JTextField(12);
    JTextField tfAddress = new JTextField(12);
    JTextField tfBedrooms = new JTextField(12);
    JTextField tfSqFeet = new JTextField(12);
    JTextField tfPrice = new JTextField(12);

    // Create ComboBoxes
    JComboBox<String> cbAction = new JComboBox<>(new String[] {"Insert", "Delete", "Find"});
    JComboBox<String> cbStatus = new JComboBox<>(new String[] {"FOR_SALE", "UNDER_CONTRACT", "SOLD"});

    // Create Buttons
    JButton btnProcess = new JButton("Process");
    JButton btnChStatus = new JButton("Change Status");

    // Create Borders
    Border noMargins = BorderFactory.createEmptyBorder(3, 0, 5, 0);

    // Creat Panels
    JPanel app = new JPanel();

    public Project4 () {

        // Add components to panel
        app.setLayout(new GridLayout(7,2));
        app.setBorder(noMargins);
        app.add(lblTransNum);
        app.add(tfTransNum);
        app.add(lblAddress);
        app.add(tfAddress);
        app.add(lblBedrooms);
        app.add(tfBedrooms);
        app.add(lblSqFeet);
        app.add(tfSqFeet);
        app.add(lblPrice);
        app.add(tfPrice);
        app.add(btnProcess);
        app.add(cbAction);
        app.add(btnChStatus);
        app.add(cbStatus);

        // Add panel to frame
        add(app);
        
        // Create action listener for button
        btnProcess.addActionListener(new BtnProcessL());
        btnChStatus.addActionListener(new BtnChStatusL());
    }

    class BtnProcessL implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // Try to process textfields as integers
            try {
                int transNum = Integer.parseInt(tfTransNum.getText());
                String address = tfAddress.getText();
                int bedrooms = Integer.parseInt(tfBedrooms.getText());
                int area = Integer.parseInt(tfSqFeet.getText());
                int price = Integer.parseInt(tfPrice.getText());
                // check whether TreeMap already has a value for the key
                boolean hasValue = database.containsKey(transNum);
                
                switch (cbAction.getSelectedItem().toString()) {
                    case "Insert":
                        if (hasValue == true) {
                            JOptionPane.showMessageDialog(null, "Transaction ID already exists.  Please choose another.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                        else {
                            // create new Property object and store in TreeMap using transaction number as key
                            Property newProperty = new Property(address, bedrooms, area, price);
                            database.put(transNum,newProperty);
                            JOptionPane.showMessageDialog(null, "Property added successfully.", "Property Added", JOptionPane.INFORMATION_MESSAGE);
                        }
                        break;
                    case "Delete":
                        if (hasValue == false) {
                            JOptionPane.showMessageDialog(null, "No records with that Transaction ID exist.  Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                        else {
                            // remove property at key from TreeMap
                            database.remove(transNum);
                            JOptionPane.showMessageDialog(null, "Property removed successfully.", "Property Removed", JOptionPane.INFORMATION_MESSAGE);
                        }
                        break;
                    case "Find":
                        if (hasValue == false) {
                            JOptionPane.showMessageDialog(null, "No records with that Transaction ID exist.  Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                        else {
                            // Display property information in pane then set values in textfields to match found property
                            JOptionPane.showMessageDialog(null, "Property Found: \n" + database.get(transNum).toString(), "Property Found", JOptionPane.INFORMATION_MESSAGE);
                            tfAddress.setText(database.get(transNum).address);
                            tfBedrooms.setText(database.get(transNum).bedrooms + "");
                            tfSqFeet.setText(database.get(transNum).sqFeet + "");
                            tfPrice.setText(database.get(transNum).price + "");
                            cbStatus.setSelectedItem(database.get(transNum).status.toString());
                        }
                        break;
                    default:
                        break;
                }
            } catch (NumberFormatException e1) {
                JOptionPane.showMessageDialog(null, "Please enter numbers for integer fields", "Exception", JOptionPane.ERROR_MESSAGE);
            } 
        }
    }

    class BtnChStatusL implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // set status of property in TreeMap using transaction number as key
            int transNum = Integer.parseInt(tfTransNum.getText());
            Status newStatus = Status.valueOf(cbStatus.getSelectedItem().toString());
            database.get(transNum).changeState(newStatus);
        }
    }

    public static void main(String[] args) {

        Project4 f = new Project4();
        f.setTitle("Real Estate Database");
        //f.setSize(370,240);
        f.pack();
        f.setLayout(new FlowLayout());
        f.setLocationRelativeTo(null);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }
}


