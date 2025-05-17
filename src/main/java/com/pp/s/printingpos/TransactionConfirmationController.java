package com.pp.s.printingpos;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class TransactionConfirmationController {
    @FXML
    private TextArea transactionsTextArea;

    @FXML
    private void initialize() {
        displayTransactions();
    }

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

    @FXML
    private void handleGenerateReceipt() {
        try {
            // Generate receipt content
            StringBuilder sb = new StringBuilder();
            sb.append("Printing Business - Receipt\n");
            sb.append("Date: ").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).append("\n\n");

            ArrayList<Transaction> transactions = PrintPOSApplication.getTransactions();
            double total = 0;

            for (Transaction transaction : transactions) {
                sb.append(transaction.toString()).append("\n");
                total += transaction.getPrice();
            }

            sb.append("\nTotal Price: ₱").append(String.format("%.2f", total));

            // Show receipt preview modal
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pp/s/printingpos/receipt_preview.fxml"));
            Parent root = loader.load();

            ReceiptPreviewController previewController = loader.getController();
            previewController.setReceiptContent(sb.toString());

            // Set up callback to navigate to landing page
            previewController.setOnConfirmCallback(() -> {
                try {
                    // Clear transactions
                    PrintPOSApplication.clearTransactions();

                    // Navigate back to landing page
                    Parent landingRoot = FXMLLoader.load(getClass().getResource("/com/pp/s/printingpos/landingpage.fxml"));
                    Stage mainStage = (Stage) transactionsTextArea.getScene().getWindow();
                    mainStage.setScene(new Scene(landingRoot, 600, 400));
                } catch (IOException e) {
                    showAlert("Error", "Could not navigate to landing page: " + e.getMessage());
                }
            });

            Stage previewStage = new Stage();
            previewStage.setTitle("Receipt Preview");
            previewStage.initModality(Modality.APPLICATION_MODAL);
            previewStage.setScene(new Scene(root));
            previewStage.showAndWait();

        } catch (IOException e) {
            showAlert("Error", "Could not generate receipt preview: " + e.getMessage());
        }
    }

    @FXML
    private void handleIncludeAnotherTransaction() throws IOException {
        // Navigate back to landing page
        Parent root = FXMLLoader.load(getClass().getResource("/com/pp/s/printingpos/landingpage.fxml"));
        Stage stage = (Stage) transactionsTextArea.getScene().getWindow();
        stage.setScene(new Scene(root, 600, 400));
    }

    private void showAlert(String title, String content) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}