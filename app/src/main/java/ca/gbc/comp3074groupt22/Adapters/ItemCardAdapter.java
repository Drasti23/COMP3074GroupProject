package ca.gbc.comp3074groupt22.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ca.gbc.comp3074groupt22.Item.Item;
import ca.gbc.comp3074groupt22.R;

public class ItemCardAdapter extends RecyclerView.Adapter<ItemCardAdapter.ItemViewHolder> {

    private ArrayList<Item> itemList;
    public interface ItemActionListener {
        void onViewItem(Item item);
        void onEditItem(Item item, int position);
        void onDeleteItem(Item item, int position);
    }

    private ItemActionListener actionListener;

    public ItemCardAdapter(ArrayList<Item> itemList, ItemActionListener actionListener) {
        this.itemList = itemList;
        this.actionListener = actionListener;
    }


    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_item_design, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Item item = itemList.get(position);
        holder.textViewItemName.setText(item.getName());
        holder.textViewDetails.setText(String.valueOf(item.getPrice()));

        holder.itemAction.setOnClickListener(v -> {
            // Display a dialog with options for View, Edit, and Delete
            PopupMenu popup = new PopupMenu(holder.itemView.getContext(), holder.itemAction);
            popup.inflate(R.menu.item_action_menu); // Create this menu resource
            popup.setOnMenuItemClickListener(menuItem -> {
                int id = menuItem.getItemId();
                if(id==R.id.viewItem){
                    actionListener.onViewItem(item);
                    return true;
                } else if (id==R.id.editItem) {
                    actionListener.onEditItem(item, position);
                    return true;
                } else if (id==R.id.deleteItem) {
                    actionListener.onDeleteItem(item, position);
                    return true;
                }
                return false;
            });
            popup.show();
        });
    }


    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView textViewItemName, textViewDetails;
        ImageView itemAction;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewItemName = itemView.findViewById(R.id.itemName);
            textViewDetails = itemView.findViewById(R.id.itemDetails);
            itemAction = itemView.findViewById(R.id.itemAction);
        }
    }

}
