# Printing Shop POS System

A simple computer program that helps cashiers and printing shop owners handle daily business operations without doing manual calculations and paperwork.

# How to run?
Launch the PrintPOSApplication.java after the project was imported to any IDE.

## What This Program Does

This program helps printing shop staff by making their work easier. Instead of calculating prices by hand and writing receipts manually, cashiers can:
- Quickly process customer orders for printing, photocopying, and scanning
- Automatically calculate total costs based on pages, color choice, and special options
- Create professional receipts that save as text files
- Change prices for different services when needed

**Important**: This program is designed for shop staff only - customers don't need accounts or login information.

## How It Makes Work Easier

### Simple Order Processing
- **Print Transactions**: Handle document printing requests with automatic price calculation
- **Photocopy Transactions**: Process copy requests with different pricing options  
- **Scan Transactions**: Manage document scanning services with flexible pricing

The cashier only needs to:
1. Choose the service type
2. Enter number of pages
3. Select color preference (black & white or colored)
4. Check if documents include images
5. Add the transaction and generate a receipt

### Easy Price Management
- Set different prices for each service type
- Separate costs for black & white vs colored printing
- Extra charges for documents with images
- Price changes save automatically in a settings file

### Automatic Receipt Creation
- Creates detailed summary of all services provided
- Calculates total cost automatically
- Saves receipts as text files with date and time
- Allows multiple services on one receipt

## Key Features and How They Work

### 1. Main Menu System
**What it does**: Shows three service buttons and a price settings link

**Code that makes it work**:
```java
@FXML
private void handlePrintAction() throws IOException {
    navigateToTransactionScreen("Print");
}

@FXML
private void handlePhotocopyAction() throws IOException {
    navigateToTransactionScreen("Photocopy");
}

@FXML
private void handleScanAction() throws IOException {
    navigateToTransactionScreen("Scan");
}
```
**How it works**: When someone clicks a service button, the program opens the correct input window for that service type.

### 2. Transaction Input System
**What it does**: Collects information about the customer's transaction

**Code that makes it work**:
```java
@FXML
private void handleAddTransaction() throws IOException {
    int pages = Integer.parseInt(numberOfPagesField.getText());
    String colorOption = coloredRadio.isSelected() ? "Colored" : "Monochrome";
    
    Transaction transaction = new Transaction(
        serviceType,
        pages,
        colorOption,
        imagesCheckbox.isSelected()
    );
    
    PrintPOSApplication.addTransaction(transaction);
    navigateToConfirmationScreen();
}
```
**How it works**: Takes the information entered by the cashier, creates a record of the transaction, and moves to the confirmation window.

### 3. Price Calculation System
**What it does**: Automatically calculates costs based on current prices

**Code that makes it work**:
```java
private double calculatePrice() {
    double baseRate = ConfigManager.getPriceForService(serviceType, 
        colorOption.equals("Colored") ? "colored" : "monochrome");
    
    double price = numberOfPages * baseRate;
    
    if (includesImages) {
        price += ConfigManager.getImagesSurcharge();
    }
    
    return price;
}
```
**How it works**: Looks up the current price for the service type and color option, multiplies by number of pages, and adds image charges if needed.

### 4. Price Settings Manager
**What it does**: Lets shop owners change prices for different services

**Code that makes it work**:
```java
public static double getPriceForService(String serviceType, String colorOption) {
    String key = serviceType.toLowerCase() + "." + colorOption.toLowerCase();
    String value = properties.getProperty(key);
    return value != null ? Double.parseDouble(value) : 0.0;
}
```
**How it works**: Reads prices from a settings file and provides them when calculating costs. Can be updated through the price settings window.

### 5. Receipt Creation System
**What it does**: Creates professional receipts and saves them as files

**Code that makes it work**:
```java
@FXML
private void handleConfirmReceipt() {
    LocalDateTime now = LocalDateTime.now();
    String filename = String.format("Receipt %s Time %s.txt",
        now.format(DateTimeFormatter.ofPattern("MM-dd-yyyy")),
        now.format(DateTimeFormatter.ofPattern("hh-mm-ss a")));
    
    File receiptFile = new File(filename);
    try (FileWriter writer = new FileWriter(receiptFile)) {
        writer.write(receiptContent);
    }
}
```
**How it works**: Creates a text file with all transaction details, current date/time, and saves it with a specific naming format for easy organization.

### 6. Transaction Storage System
**What it does**: Keeps track of multiple services for one customer

**Code that makes it work**:
```java
private static ArrayList<Transaction> transactions = new ArrayList<>();

public static void addTransaction(Transaction transaction) {
    transactions.add(transaction);
}

public static void clearTransactions() {
    transactions.clear();
}
```
**How it works**: Stores all services in a list until the receipt is created, then clears the list for the next customer.

### 7. Transaction Confirmation System
**What it does**: Shows a summary of all transactions before creating the receipt

**Code that makes it work**:
```java
private void displayTransactions() {
    ArrayList<Transaction> transactions = PrintPOSApplication.getTransactions();
    StringBuilder sb = new StringBuilder();
    double total = 0;

    for (Transaction transaction : transactions) {
        sb.append(transaction.toString()).append("\n");
        total += transaction.getPrice();
    }

    sb.append("\nTotal Price: ₱").append(String.format("%.2f", total));
    transactionsTextArea.setText(sb.toString());
}
```
**How it works**: Creates a detailed summary showing all services, their costs, and the total amount before generating the final receipt.

### 8. Receipt Preview System
**What it does**: Shows what the receipt will look like before saving it

**Code that makes it work**:
```java
public void setReceiptContent(String content) {
    LocalDateTime now = LocalDateTime.now();
    String receiptDateTime = now.format(DateTimeFormatter.ofPattern("MM-dd-yyyy hh:mm:ss a"));
    
    String updatedContent = content.replaceFirst(
        "Date: \\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}",
        "Date: " + receiptDateTime
    );
    
    this.receiptContent = updatedContent;
    receiptTextArea.setText(updatedContent);
}
```
**How it works**: Shows the receipt in a popup window where staff can review it before saving to make sure everything looks correct.

### 9. Input Validation and Error Handling
**What it does**: Checks for problems with entered information and shows helpful popup messages

**Code that makes it work**:
```java
private void showAlert(String title, String content) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(content);
    alert.showAndWait();
}

// Validate input
int pages;
try {
    pages = Integer.parseInt(numberOfPagesField.getText());
    if (pages <= 0) {
        showAlert("Invalid Input", "Please enter a positive number of pages.");
        return;
    }
} catch (NumberFormatException e) {
    showAlert("Invalid Input", "Please enter a valid number of pages.");
    return;
}
```
**How it works**: Checks if the page number is valid (positive number) and shows popup windows with clear error messages if something is wrong.

### 10. Modal Dialog Systems
**What it does**: Opens special popup windows for important tasks like changing prices or previewing receipts

**Code that makes it work**:
```java
@FXML
private void handleCustomizePricing() throws IOException {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pp/s/printingpos/pricing_config.fxml"));
    Parent root = loader.load();

    Stage stage = new Stage();
    stage.setTitle("Customize Pricing");
    stage.initModality(Modality.APPLICATION_MODAL); // Make it modal
    stage.setScene(new Scene(root));
    stage.showAndWait(); // This will block until the dialog is closed
}
```
**How it works**: Creates popup windows that must be completed before returning to the main program, ensuring important tasks like price changes are handled properly.

## Simple User Guide

### For Daily Operations:
1. Open the program
2. Click the service button (Print, Photocopy, or Scan)
3. Fill in the details:
   - Number of pages
   - Choose black & white or colored
   - Check if documents have images
4. Click "Add Transaction"
5. Review the details on the summary window
6. Either add more services or click "Generate Receipt"
7. Check the receipt preview and click "Confirm" to save

### For Changing Prices:
1. From the main menu, click "Customize Pricing"
2. Enter new prices for each service type
3. Update the image surcharge if needed
4. Click "Save Changes"
5. Restart the program for changes to take effect

## Technical Information

- **Programming Language**: Java
- **User Interface**: JavaFX (creates windows and buttons)
- **Project Management**: Maven (handles program dependencies)
- **File Organization**: MVC pattern (separates display, logic, and data)
- **Settings Storage**: Properties file (keeps price information)

## Files Created by the Program

- **Receipt Files**: Saved in the main program folder
- **File Names**: `Receipt MM-DD-YYYY Time HH-MM-SS AM/PM.txt`
- **Settings File**: `config.properties` (stores current prices)

## Why This Program Helps

- **Saves Time**: No manual calculations or handwritten receipts
- **Reduces Errors**: Automatic calculations prevent math mistakes  
- **Professional Appearance**: Clean, consistent receipts for customers
- **Easy Updates**: Change prices quickly when needed
- **Good Records**: All receipts saved with date/time stamps
- **Simple to Use**: Designed for busy shop environments
