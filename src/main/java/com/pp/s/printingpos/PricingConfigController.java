package com.pp.s.printingpos;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

public class PricingConfigController {
    @FXML private TextField printMonochromeField;
    @FXML private TextField printColoredField;
    @FXML private TextField photocopyMonochromeField;
    @FXML private TextField photocopyColoredField;
    @FXML private TextField scanMonochromeField;
    @FXML private TextField scanColoredField;
    @FXML private TextField imagesSurchargeField;

    private static final String CONFIG_PATH = "src/main/resources/com/pp/s/printingpos/config.properties";

    @FXML
    private void initialize() {
        // Load current values from config
        printMonochromeField.setText(String.valueOf(ConfigManager.getPriceForService("print", "monochrome")));
        printColoredField.setText(String.valueOf(ConfigManager.getPriceForService("print", "colored")));
        photocopyMonochromeField.setText(String.valueOf(ConfigManager.getPriceForService("photocopy", "monochrome")));
        photocopyColoredField.setText(String.valueOf(ConfigManager.getPriceForService("photocopy", "colored")));
        scanMonochromeField.setText(String.valueOf(ConfigManager.getPriceForService("scan", "monochrome")));
        scanColoredField.setText(String.valueOf(ConfigManager.getPriceForService("scan", "colored")));
        imagesSurchargeField.setText(String.valueOf(ConfigManager.getImagesSurcharge()));
    }

    @FXML
    private void handleSaveChanges() {
        // First, show confirmation dialog
        boolean confirmed = showConfirmationDialog(
                "Confirm Changes",
                "Saving these changes will require restarting the application.\n\nDo you want to continue?"
        );

        if (!confirmed) {
            return; // User canceled
        }

        try {
            // Create and populate properties
            Properties properties = new Properties();

            // Print pricing
            properties.setProperty("print.monochrome", printMonochromeField.getText());
            properties.setProperty("print.colored", printColoredField.getText());

            // Photocopy pricing
            properties.setProperty("photocopy.monochrome", photocopyMonochromeField.getText());
            properties.setProperty("photocopy.colored", photocopyColoredField.getText());

            // Scan pricing
            properties.setProperty("scan.monochrome", scanMonochromeField.getText());
            properties.setProperty("scan.colored", scanColoredField.getText());

            // Additional charges
            properties.setProperty("images.additional", imagesSurchargeField.getText());

            // Save to file
            try (OutputStream output = new FileOutputStream(CONFIG_PATH)) {
                properties.store(output, "Updated pricing configuration");
            }

            // Reload configuration
            ConfigManager.reloadConfig();

            // Show restart required dialog
            showRestartRequiredDialog();

            // Close the dialog
            closeStage();
        } catch (IOException e) {
            showAlert("Error", "Failed to save configuration: " + e.getMessage());
        } catch (NumberFormatException e) {
            showAlert("Invalid Input", "Please enter valid numbers for all fields.");
        }
    }

    @FXML
    private void handleCancel() {
        closeStage();
    }

    private void closeStage() {
        Stage stage = (Stage) printMonochromeField.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String content) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private boolean showConfirmationDialog(String title, String content) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                javafx.scene.control.Alert.AlertType.CONFIRMATION
        );
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);

        return alert.showAndWait()
                .filter(response -> response == javafx.scene.control.ButtonType.OK)
                .isPresent();
    }

    private void showRestartRequiredDialog() {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                javafx.scene.control.Alert.AlertType.INFORMATION
        );
        alert.setTitle("Restart Required");
        alert.setHeaderText("Application Restart Required");
        alert.setContentText("The pricing changes have been saved.\n\n" +
                "You must restart the application for changes to take full effect.");

        alert.showAndWait()
                .filter(response -> response == javafx.scene.control.ButtonType.OK)
                .ifPresent(response -> {
                    // Close the application
                    Stage stage = (Stage) printMonochromeField.getScene().getWindow();
                    stage.close();

                    // Get the primary stage and close it
                    if (stage.getOwner() instanceof Stage) {
                        ((Stage) stage.getOwner()).close();
                    }

                    // Force application exit
                    System.exit(0);
                });
    }
}