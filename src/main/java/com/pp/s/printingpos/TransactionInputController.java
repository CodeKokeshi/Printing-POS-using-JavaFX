package com.pp.s.printingpos;

import com.pp.s.printingpos.PrintPOSApplication;
import com.pp.s.printingpos.Transaction;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class TransactionInputController {
    @FXML
    private Label serviceTypeLabel;
    @FXML
    private TextField numberOfPagesField;
    @FXML
    private RadioButton coloredRadio;
    @FXML
    private RadioButton monochromeRadio;
    @FXML
    private CheckBox imagesCheckbox;

    private String serviceType;

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
        serviceTypeLabel.setText(serviceType + " Service");
    }

    @FXML
    private void initialize() {
        // Ensure only one color option can be selected
        ToggleGroup colorToggleGroup = new ToggleGroup();
        coloredRadio.setToggleGroup(colorToggleGroup);
        monochromeRadio.setToggleGroup(colorToggleGroup);
        monochromeRadio.setSelected(true);
    }

    @FXML
    private void handleAddTransaction() throws IOException {
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

        // Determine color option
        String colorOption = coloredRadio.isSelected() ? "Colored" : "Monochrome";

        // Create transaction
        Transaction transaction = new Transaction(
                serviceType,
                pages,
                colorOption,
                imagesCheckbox.isSelected()
        );

        // Add to transactions
        PrintPOSApplication.addTransaction(transaction);

        // Navigate to transaction confirmation
        navigateToConfirmationScreen();
    }

    private void navigateToConfirmationScreen() throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/pp/s/printingpos/transaction_confirmation.fxml")));
        Stage stage = (Stage) serviceTypeLabel.getScene().getWindow();
        stage.setScene(new Scene(root, 600, 400));
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}