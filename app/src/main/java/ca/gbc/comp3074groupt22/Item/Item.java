package ca.gbc.comp3074groupt22.Item;

import android.os.Parcel;
import android.os.Parcelable;

public class Item implements Parcelable {

    private String name;
    private String description;
    private double price;
    private int quantity;

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }



    public Item(String name, String description, double price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }
    public Item(String name, String description, double price, int quantity) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity= quantity;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    // Parcelable implementation
    protected Item(Parcel in) {
        name = in.readString();
        quantity = in.readInt();
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(quantity);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };
}