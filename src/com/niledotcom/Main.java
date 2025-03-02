/* Name: Rafael Zuniga
Course: CNT 4714 An Event-driven Enterprise Simulation
Date: Friday January 17, 2025
*/
package com.niledotcom;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.ArrayList;
public class Main implements ActionListener {
private JFrame window = new JFrame("Nile.com - Shopping Cart");
private JTextField itemIdField = new JTextField(15);
private JTextField quantityField = new JTextField(5);
private JTextArea itemDetailsArea = new JTextArea(3, 30);
private JTextField subtotalField = new JTextField(10);
private JTextArea cartContentsArea = new JTextArea(5, 30);
private JButton searchButton = new JButton("Search Item");
private JButton addButton = new JButton("Add to Cart");
private JButton deleteLastButton = new JButton("Delete Last Item");
private JButton clearCartButton = new JButton("Clear Cart");
private JButton checkoutButton = new JButton("Checkout");
private Map<String, String[]> inventory = new HashMap<>();
private List<String[]> cart = new ArrayList<>();
private double subtotal = 0.0;
public Main() {
loadInventory();
JPanel panel = new JPanel();
panel.setLayout(new GridLayout(7, 2));
panel.add(new JLabel("Item ID:"));
panel.add(itemIdField);
panel.add(new JLabel("Quantity:"));
panel.add(quantityField);
panel.add(new JLabel("Item Details:"));
itemDetailsArea.setEditable(false);
panel.add(new JScrollPane(itemDetailsArea));
panel.add(new JLabel("Cart Contents:"));
cartContentsArea.setEditable(false);
panel.add(new JScrollPane(cartContentsArea));
panel.add(new JLabel("Subtotal:"));
subtotalField.setEditable(false);
panel.add(subtotalField);
searchButton.addActionListener(this);
addButton.addActionListener(this);
deleteLastButton.addActionListener(this);
clearCartButton.addActionListener(this);
checkoutButton.addActionListener(this);
addButton.setEnabled(false);
JPanel buttonPanel = new JPanel();
buttonPanel.add(searchButton);
buttonPanel.add(addButton);
buttonPanel.add(deleteLastButton);
buttonPanel.add(clearCartButton);
buttonPanel.add(checkoutButton);
window.setLayout(new BorderLayout());
window.add(panel, BorderLayout.CENTER);
window.add(buttonPanel, BorderLayout.SOUTH);
window.setSize(600, 550);
window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
window.setVisible(true);
}
private void loadInventory() {
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(
        getClass().getClassLoader().getResourceAsStream("resources/inventory.csv")))) {
        
        String line;
        while ((line = reader.readLine()) != null) {
            String[] data = line.split(",");
            inventory.put(data[0].trim(), data);
        }
    } catch (IOException e) {
        JOptionPane.showMessageDialog(window, "Error loading inventory file.");
    }
}


public void actionPerformed(ActionEvent e) {
	if (e.getSource() == searchButton) {
searchItem();
} else if (e.getSource() == addButton) {
addItemToCart();
} else if (e.getSource() == deleteLastButton) {
deleteLastItem();
} else if (e.getSource() == clearCartButton) {
clearCart();
} else if (e.getSource() == checkoutButton) {
checkout();
}
}
private void searchItem() {
String itemId = itemIdField.getText().trim();
if (inventory.containsKey(itemId)) {
String[] itemData = inventory.get(itemId);
itemDetailsArea.setText("Name: " + itemData[1] + "\nPrice: $" + itemData[4]);
addButton.setEnabled(true);
} else {
itemDetailsArea.setText("Item not found.");
addButton.setEnabled(false);
}
}
private void addItemToCart() {
String itemId = itemIdField.getText().trim();
String quantityText = quantityField.getText().trim();
if (!inventory.containsKey(itemId) || !
quantityText.matches("\\d+")) {
JOptionPane.showMessageDialog(window, "Invalid item or quantity.");
return;
}
int quantity = Integer.parseInt(quantityText);
String[] itemData = inventory.get(itemId);
boolean inStock =
Boolean.parseBoolean(itemData[2].trim());
int stock = Integer.parseInt(itemData[3].trim());
double price = Double.parseDouble(itemData[4].trim());
if (!inStock || stock == 0) {
JOptionPane.showMessageDialog(window, "Item is out of stock!");
return;
}
if (quantity > stock) {
JOptionPane.showMessageDialog(window, "Not enough stock available!");
return;
}
cart.add(new String[]{itemId, itemData[1], quantityText,
itemData[4]});
subtotal += quantity * price;
subtotalField.setText("$" + String.format("%.2f",
subtotal));
updateCartDisplay();
}
private void deleteLastItem() {
if (!cart.isEmpty()) {
String[] lastItem = cart.remove(cart.size() - 1);
double lastItemPrice = Double.parseDouble(lastItem[3])
* Integer.parseInt(lastItem[2]);
subtotal -= lastItemPrice;
subtotalField.setText("$" + String.format("%.2f",
subtotal));
updateCartDisplay();
}
}
private void clearCart() {
cart.clear();
subtotal = 0.0;
subtotalField.setText("$0.00");
updateCartDisplay();
}
private void updateCartDisplay() {
cartContentsArea.setText("");
for (String[] item : cart) {
cartContentsArea.append(item[1] + " | Qty: " + item[2]
+ " | $" + item[3] + "\n");
}
}
private void logTransaction(String transactionId) {
try (BufferedWriter writer = new BufferedWriter(new
FileWriter("transactions.csv", true))) {
for (String[] item : cart) {
String logEntry = transactionId + "," + item[0] +
"," + item[1] + "," + item[3] + "," +
(subtotal * 0.06) + "," +
ZonedDateTime.now(ZoneId.of("America/New_York"));
writer.write(logEntry);
writer.newLine();
}
} catch (IOException e) {
JOptionPane.showMessageDialog(window, "Error logging transaction.");
}
}
private void checkout() {
double taxRate = 0.06;
int totalItems = cart.stream().mapToInt(item ->
Integer.parseInt(item[2])).sum();
double discount = totalItems >= 15 ? 0.20 : totalItems >=
10 ? 0.15 : totalItems >= 5 ? 0.10 : 0.0;
double discountedSubtotal = subtotal * (1 - discount);
double taxAmount = discountedSubtotal * taxRate;
double totalWithTax = discountedSubtotal + taxAmount;
LocalDateTime now = LocalDateTime.now();
String transactionId =
now.format(DateTimeFormatter.ofPattern("yyyyHHmmss")); // Unique Transaction ID
String receipt = "Transaction ID: " + transactionId + "\nDate: " + ZonedDateTime.now(ZoneId.of("America/New_York")) +
"\nItems: " + totalItems + "\n";
for (String[] item : cart) {
receipt += item[0] + " - " + item[1] + " - $" +
item[3] + " x " + item[2] + "\n";
}
receipt += "Subtotal: $" + String.format("%.2f", subtotal)
+
"\nDiscount: " + (discount * 100) + "%\nTax: $"
+ String.format("%.2f", taxAmount) +
"\nTotal: $" + String.format("%.2f",
totalWithTax);
JOptionPane.showMessageDialog(window, receipt);
logTransaction(transactionId);
}
public static void main(String[] args) {
new Main();
}
}