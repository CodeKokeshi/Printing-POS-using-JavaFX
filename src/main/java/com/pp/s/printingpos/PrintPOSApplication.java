package com.pp.s.printingpos;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Objects;

public class PrintPOSApplication extends Application {
    // Singleton-like approach to store transactions across scenes
    private static ArrayList<Transaction> transactions = new ArrayList<>();

    public static ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    public static void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public static void clearTransactions() {
        transactions.clear();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Ensure configuration is loaded
        ConfigManager.getPriceForService("print", "monochrome");

        // Load the landing page
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/pp/s/printingpos/landingpage.fxml")));
        primaryStage.setTitle("Printing Business POS");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}