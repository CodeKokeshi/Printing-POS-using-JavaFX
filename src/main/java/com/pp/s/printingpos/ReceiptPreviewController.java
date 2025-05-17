package com.pp.s.printingpos;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ReceiptPreviewController {
    @FXML
    private TextArea receiptTextArea;

    private String receiptContent;
    private Runnable onConfirmCallback;

    public void setReceiptContent(String content) {
        LocalDateTime now = LocalDateTime.now();

        // Create formatters for 12-hour clock format with seconds
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh-mm-ss a");
        DateTimeFormatter receiptDateFormatter = DateTimeFormatter.ofPattern("MM-dd-yyyy hh:mm:ss a");

        // Format dates consistently
        String formattedDate = now.format(dateFormatter);
        String formattedTime = now.format(timeFormatter);
        String receiptDateTime = now.format(receiptDateFormatter);

        // Replace the datetime in receipt content with new consistent format
        String updatedContent = content.replaceFirst(
                "Date: \\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}",
                "Date: " + receiptDateTime
        );

        this.receiptContent = updatedContent;
        receiptTextArea.setText(updatedContent);
    }

    public void setOnConfirmCallback(Runnable callback) {
        this.onConfirmCallback = callback;
    }

    @FXML
    private void handleConfirmReceipt() {
        try {
            // Generate receipt file with updated naming format including seconds
            LocalDateTime now = LocalDateTime.now();
            String filename = String.format("Receipt %s Time %s.txt",
                    now.format(DateTimeFormatter.ofPattern("MM-dd-yyyy")),
                    now.format(DateTimeFormatter.ofPattern("hh-mm-ss a")));

            File receiptFile = new File(filename);
            try (FileWriter writer = new FileWriter(receiptFile)) {
                writer.write(receiptContent);
            }

            // Show success message
            showAlert("Receipt Generated", "Receipt saved as " + filename);

            // Close window
            closeWindow();

            // Execute the callback if set
            if (onConfirmCallback != null) {
                onConfirmCallback.run();
            }
        } catch (IOException e) {
            showAlert("Error", "Could not generate receipt: " + e.getMessage());
        }
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) receiptTextArea.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String content) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}