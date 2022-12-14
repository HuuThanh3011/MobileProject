package edu.hanu.a2_1901040203.models;

public class Product {
    private int productID;
    private String name;
    private String thumbnail;
    private int unitPrice;
    private int quantity;

    public Product(int productID, String name, String thumbnail, int unitPrice, int quantity) {
        this.productID = productID;
        this.name = name;
        this.thumbnail = thumbnail;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }
    public Product(int productID, String name, String thumbnail, int unitPrice) {
        this.productID = productID;
        this.name = name;
        this.thumbnail = thumbnail;
        this.unitPrice = unitPrice;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public int getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(int unitPrice) {
        this.unitPrice = unitPrice;
    }
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productID=" + productID +
                ", name='" + name + '\'' +
                ", thumbnail=" + thumbnail +
                ", unitPrice=" + unitPrice +
                '}';
    }
}
