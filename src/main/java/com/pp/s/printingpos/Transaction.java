package com.pp.s.printingpos;

public class Transaction {
    private String serviceType;
    private int numberOfPages;
    private String colorOption;
    private boolean includesImages;
    private double price;

    public Transaction(String serviceType, int numberOfPages, String colorOption, boolean includesImages) {
        this.serviceType = serviceType;
        this.numberOfPages = numberOfPages;
        this.colorOption = colorOption;
        this.includesImages = includesImages;
        this.price = calculatePrice();
    }

    private double calculatePrice() {
        // Get base rate from configuration
        double baseRate = ConfigManager.getPriceForService(serviceType,
                colorOption.equals("Colored") ? "colored" : "monochrome");

        double price = numberOfPages * baseRate;

        // Additional charge for images from configuration
        if (includesImages) {
            price += ConfigManager.getImagesSurcharge();
        }

        return price;
    }

    // Getters remain the same
    public String getServiceType() { return serviceType; }
    public int getNumberOfPages() { return numberOfPages; }
    public String getColorOption() { return colorOption; }
    public boolean isIncludesImages() { return includesImages; }
    public double getPrice() { return price; }

    @Override
    public String toString() {
        return String.format("%s: %d pages, %s, Images: %s, Price: ₱%.2f",
                serviceType, numberOfPages, colorOption, includesImages ? "Yes" : "No", price);
    }
}