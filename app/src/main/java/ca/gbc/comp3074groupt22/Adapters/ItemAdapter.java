package ca.gbc.comp3074groupt22.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ca.gbc.comp3074groupt22.Item.Item;
import ca.gbc.comp3074groupt22.POSActivity;
import ca.gbc.comp3074groupt22.R;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private final List<Item> items; // List of items displayed in RecyclerView
    private final List<Item> cartItems; // List to store items added to the cart
    private final Context context; // Context for displaying Toast messages

    private final HashMap<String, String> itemNotes = new HashMap<>();


    public ItemAdapter(Context context, List<Item> items) {
        this.items = items;
        this.cartItems = new ArrayList<>();
        this.context = context;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Item item = items.get(position);
        String name = items.get(position).getName();
        String price = items.get(position).getPrice()+"";
        holder.itemName.setText(name);
        holder.itemPrice.setText(price);


        holder.addButton.setOnClickListener(v -> {
            POSActivity activity = (POSActivity) context;
            activity.addItemToCart(item);
        });


        holder.noteButton.setOnClickListener(v -> {
            // Open dialog to add a note
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Add a Note");

            // Create an EditText input field
            final EditText input = new EditText(context);
            input.setHint("Enter your note here");
            builder.setView(input);

            // Add positive button to save the note
            builder.setPositiveButton("Save", (dialog, which) -> {
                String note = input.getText().toString().trim();
                if (!note.isEmpty()) {
                    itemNotes.put(item.getName(), note); // Save the note for the item
                    Toast.makeText(context, "Note saved for " + item, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Note cannot be empty!", Toast.LENGTH_SHORT).show();
                }
            });


            // Add negative button to cancel the dialog
            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

            // Show the dialog
            builder.create().show();
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView itemName,itemPrice;
        ImageView addButton, noteButton;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.itemName);
            addButton = itemView.findViewById(R.id.addButton);
            noteButton = itemView.findViewById(R.id.noteButton);
            itemPrice = itemView.findViewById(R.id.itemPrice);
        }
    }

    // Public method to retrieve cart items
    public List<Item> getCartItems() {
        return new ArrayList<>(cartItems); // Return a copy to prevent external modification
    }
}

