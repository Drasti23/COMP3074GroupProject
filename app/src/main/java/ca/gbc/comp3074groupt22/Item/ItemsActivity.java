package ca.gbc.comp3074groupt22.Item;

import ca.gbc.comp3074groupt22.R;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ca.gbc.comp3074groupt22.Adapters.ItemCardAdapter;

import ca.gbc.comp3074groupt22.Dialogs.AddItemDialog;
import ca.gbc.comp3074groupt22.Dialogs.ViewItemDialog;

import ca.gbc.comp3074groupt22.Section.ManagePOSActivity;


import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class ItemsActivity extends AppCompatActivity implements AddItemDialog.ItemAddedListener,ItemCardAdapter.ItemActionListener {

    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private ArrayList<Item> itemList;
    private ItemCardAdapter adapter;
    private String sectionName;
    private TextView title;
    private ImageView back;
    private FirebaseFirestore firestore; // Firestore instance

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance();

        title = findViewById(R.id.manageItemsTitle);
        back = findViewById(R.id.backButtonItem);

        sectionName = getIntent().getStringExtra("sectionName");
        setTitle("Manage Items for " + sectionName);
        title.setText(sectionName);

        recyclerView = findViewById(R.id.itemRecyclerView);
        fab = findViewById(R.id.addItemFab);

        itemList = new ArrayList<>();
        adapter = new ItemCardAdapter(itemList,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Fetch existing items from Firestore
        fetchItemsFromFirestore();

        // Handle back button
        back.setOnClickListener(v -> {
            Intent i = new Intent(ItemsActivity.this, ManagePOSActivity.class);
            startActivity(i);
            finish();
        });

        // Handle Add Item button
        fab.setOnClickListener(v -> {
            AddItemDialog dialog = new AddItemDialog();
            dialog.setItemAddedListener(this);
            dialog.showDialog(this);
        });
    }

    @Override
    public void onItemAdded(String name, String description, double price) {
        Item newItem = new Item(name, description, price);

        // Add item to the RecyclerView
        itemList.add(newItem);
        adapter.notifyItemInserted(itemList.size() - 1);

        // Store the item in Firestore under the section
        storeItemInFirestore(newItem);
    }



    private void fetchItemsFromFirestore() {
        firestore.collection("sections")
                .document(sectionName)
                .collection("items")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        itemList.clear(); // Clear current list
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            String name = doc.getString("name");
                            String description = doc.getString("description");
                            double price = doc.getDouble("price");

                            Item item = new Item(name, description, price);
                            itemList.add(item); // Add each item to the list
                        }
                        adapter.notifyDataSetChanged(); // Notify adapter to update the RecyclerView
                    } else {
                        Toast.makeText(this, "Failed to fetch items: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    @Override
    public void onViewItem(Item item) {
        ViewItemDialog dialog = new ViewItemDialog(this, item);
        dialog.show();
    }


    @Override
    public void onEditItem(Item item, int position) {
        // Show a dialog to edit the item
        AddItemDialog dialog = new AddItemDialog();
        dialog.setItemAddedListener((name, description, price) -> {
            item.setName(name);
            item.setDescription(description);
            item.setPrice(price);

            // Update Firestore
            updateItemInFirestore(item, position);

            // Update RecyclerView
            itemList.set(position, item);
            adapter.notifyItemChanged(position);
        });
        dialog.setInitialData(item.getName(), item.getDescription(), item.getPrice());
        dialog.showDialog(this);
    }

    @Override
    public void onDeleteItem(Item item, int position) {
        // Remove from Firestore
        deleteItemFromFirestore(item);

        // Remove from RecyclerView
        itemList.remove(position);
        adapter.notifyItemRemoved(position);
    }
    private void storeItemInFirestore(Item item) {
        // Create a map for the item's fields
        Map<String, Object> itemData = new HashMap<>();
        itemData.put("name", item.getName());
        itemData.put("description", item.getDescription());
        itemData.put("price", item.getPrice());

        firestore.collection("sections")
                .document(sectionName)
                .collection("items")
                .add(itemData) // Add the item as a new document in the "items" sub-collection
                .addOnSuccessListener(documentReference ->
                        Toast.makeText(this, "Item saved to Firestore!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Error saving item: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void updateItemInFirestore(Item item, int position) {
        Map<String, Object> itemData = new HashMap<>();
        itemData.put("name", item.getName());
        itemData.put("description", item.getDescription());
        itemData.put("price", item.getPrice());
        firestore.collection("sections")
                .document(sectionName)
                .collection("items")
                .whereEqualTo("name", item.getName())
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        String docId = querySnapshot.getDocuments().get(0).getId();
                        firestore.collection("sections")
                                .document(sectionName)
                                .collection("items")
                                .document(docId)
                                .set(item)
                                .addOnSuccessListener(aVoid ->
                                        Toast.makeText(this, "Item updated in Firestore!", Toast.LENGTH_SHORT).show())
                                .addOnFailureListener(e ->
                                        Toast.makeText(this, "Error updating item: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                    }
                });
    }

    private void deleteItemFromFirestore(Item item) {
        firestore.collection("sections")
                .document(sectionName)
                .collection("items")
                .whereEqualTo("name", item.getName())
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        String docId = querySnapshot.getDocuments().get(0).getId();
                        firestore.collection("sections")
                                .document(sectionName)
                                .collection("items")
                                .document(docId)
                                .delete()
                                .addOnSuccessListener(aVoid ->
                                        Toast.makeText(this, "Item deleted from Firestore!", Toast.LENGTH_SHORT).show())
                                .addOnFailureListener(e ->
                                        Toast.makeText(this, "Error deleting item: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                    }
                });
    }
}
