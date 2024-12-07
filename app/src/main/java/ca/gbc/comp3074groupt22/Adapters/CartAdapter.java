package ca.gbc.comp3074groupt22.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ca.gbc.comp3074groupt22.Item.Item;
import ca.gbc.comp3074groupt22.R;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<Item> cartItems;
    private Runnable updateTotals;

    public CartAdapter(List<Item> cartItems, Runnable updateTotals) {
        this.cartItems = cartItems;
        this.updateTotals = updateTotals;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_item_layout, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Item item = cartItems.get(position);
        holder.itemNameTextView.setText(item.getName());
        holder.price.setText(item.getPrice()+"");
        holder.quantityTextView.setText(String.valueOf(item.getQuantity()));

        holder.increaseQuantityButton.setOnClickListener(v -> {
            item.setQuantity(item.getQuantity() + 1);
            notifyItemChanged(position);
            updateTotals.run();
        });

        holder.decreaseQuantityButton.setOnClickListener(v -> {
            if (item.getQuantity() > 1) {
                item.setQuantity(item.getQuantity() - 1);
                notifyItemChanged(position);
                updateTotals.run();
            }
        });

        holder.removeItemButton.setOnClickListener(v -> {
            cartItems.remove(position);
            notifyItemRemoved(position);
            updateTotals.run();
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView itemNameTextView, quantityTextView,price;
        ImageView increaseQuantityButton, decreaseQuantityButton, removeItemButton;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            itemNameTextView = itemView.findViewById(R.id.itemNameCart);
            price = itemView.findViewById(R.id.itemPriceCart);
            quantityTextView = itemView.findViewById(R.id.quantityTextView);
            increaseQuantityButton = itemView.findViewById(R.id.increaseQuantityButton);
            decreaseQuantityButton = itemView.findViewById(R.id.decreaseQuantityButton);
            removeItemButton = itemView.findViewById(R.id.removeItemButton);
        }
    }
}
