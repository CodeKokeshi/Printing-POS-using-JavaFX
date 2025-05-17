package com.pp.s.printingpos;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class LandingPageController {
    @FXML
    private Button printButton;
    @FXML
    private Button photocopyButton;
    @FXML
    private Button scanButton;

    @FXML
    private void initialize() {
        // Any initialization logic can go here
    }

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

    @FXML
    private void handleCustomizePricing() throws IOException {
        // Load the pricing configuration dialog
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pp/s/printingpos/pricing_config.fxml"));
        Parent root = loader.load();

        // Create a new stage for the dialog
        Stage stage = new Stage();
        stage.setTitle("Customize Pricing");
        stage.initModality(Modality.APPLICATION_MODAL); // Make it modal
        stage.setScene(new Scene(root));
        stage.showAndWait(); // This will block until the dialog is closed
    }

    private void navigateToTransactionScreen(String serviceType) throws IOException {
        // Load the transaction input screen
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pp/s/printingpos/transaction_input.fxml"));
        Parent root = loader.load();

        // Pass the service type to the transaction input controller
        TransactionInputController controller = loader.getController();
        controller.setServiceType(serviceType);

        // Get the current stage
        Stage stage = (Stage) printButton.getScene().getWindow();
        stage.setScene(new Scene(root, 600, 400));
    }
}