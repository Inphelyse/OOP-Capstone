public class Item {
    private String imagePath;
    private String name;
    private String seller;
    private double price;
    private int quantity;

    public Item(String imagePath, String name, String seller, double price) {
        this.imagePath = imagePath;
        this.name = name;
        this.seller = seller;
        this.price = price;
        this.quantity = 1; // Default to 1 when added to cart
    }

    // Getters
    public String getImagePath() { return imagePath; }
    public String getName() { return name; }
    public String getSeller() { return seller; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }

    // Setters / Helpers for Cart
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void incrementQuantity() { this.quantity++; }
    public double getTotalPrice() { return price * quantity; }
}