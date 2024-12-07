package ca.gbc.comp3074groupt22.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;

import ca.gbc.comp3074groupt22.Item.Item;
import ca.gbc.comp3074groupt22.R;

public class ViewItemDialog extends Dialog {

    private final Item item;

    public ViewItemDialog(@NonNull Context context, Item item) {
        super(context);
        this.item = item;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_view_item);

        // Initialize views
        TextView nameTextView = findViewById(R.id.itemName);
        TextView descriptionTextView = findViewById(R.id.itemDescription);
        TextView priceTextView = findViewById(R.id.itemPrice);

        // Set item details
        nameTextView.setText(item.getName());
        descriptionTextView.setText(item.getDescription());
        priceTextView.setText(String.format("$%.2f", item.getPrice()));
    }
}
