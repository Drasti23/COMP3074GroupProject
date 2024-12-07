package ca.gbc.comp3074groupt22.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ca.gbc.comp3074groupt22.R;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {
    private ArrayList<String> sectionList;
    private OnSectionClickListener listener;

    public interface OnSectionClickListener {
        void onSectionClick(String sectionName);
    }

    public CardAdapter(ArrayList<String> sections, OnSectionClickListener listener) {
        sectionList = sections;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_design, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        String sectionName = sectionList.get(position);
        holder.sectionName.setText(sectionName);
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onSectionClick(sectionName);
            }
        });
    }

    @Override
    public int getItemCount() {
        return sectionList.size();
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {
        TextView sectionName;
        ImageView imageView;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            sectionName = itemView.findViewById(R.id.textViewSectionTitle);
            imageView = itemView.findViewById(R.id.imageViewImage);
            imageView.setImageResource(R.drawable.ic_android_black_24dp);
        }
    }
}