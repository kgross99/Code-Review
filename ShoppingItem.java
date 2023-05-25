public class ShoppingItem {
    //Variables to store this ShoppingItem's name and price.
    private String itemName;
    private float price;

    //Constructor for creating a ShoppingItem with the provided name and price.
    public ShoppingItem(String itemName, float price) {
        this.itemName = itemName;
        this.price = price;
    }

    //Get this ShoppingItem's name.
    public String getName() {
        return itemName;
    }

    //Set this ShoppingItem's name.
    public void setName(String name) {
        this.itemName = name;
    }

    //Get this ShoppingItem's price.
    public float getPrice() {
        return price;
    }

    //Set this ShoppingItem's price.
    public void setPrice(float price) {
        this.price = price;
    }
}
