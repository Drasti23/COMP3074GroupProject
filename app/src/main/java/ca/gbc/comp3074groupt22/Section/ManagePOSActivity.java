package ca.gbc.comp3074groupt22.Section;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import ca.gbc.comp3074groupt22.Adapters.CardAdapter;
import ca.gbc.comp3074groupt22.AdminPortalActivity;
import ca.gbc.comp3074groupt22.Dialogs.AddSectionDialog;
import ca.gbc.comp3074groupt22.Item.ItemsActivity;
import ca.gbc.comp3074groupt22.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.firestore.QueryDocumentSnapshot;

public class ManagePOSActivity extends AppCompatActivity implements AddSectionDialog.SectionAddedListener {

    RecyclerView recyclerView;
    private ArrayList<String> sectionList;
    private CardAdapter adapter;
    private FloatingActionButton fab;
    private FirebaseFirestore firestore; // Firestore instance
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_posactivity);

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance();

        // Initialize UI components
        recyclerView = findViewById(R.id.recyclerView);
        fab = findViewById(R.id.fab);
        back = findViewById(R.id.imageViewBackButton);

        // Initialize the section list and adapter
        sectionList = new ArrayList<>();
        adapter = new CardAdapter(sectionList, sectionName -> {
            Intent intent = new Intent(ManagePOSActivity.this, ItemsActivity.class);
            intent.putExtra("sectionName", sectionName);
            startActivity(intent);
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Fetch sections from Firestore
        fetchSectionsFromFirestore();

        // Handle FloatingActionButton click to show the dialog
        fab.setOnClickListener(v -> {
            AddSectionDialog dialog = new AddSectionDialog();
            dialog.setSectionAddedListener(this); // Set the callback
            dialog.showDialog(ManagePOSActivity.this);
        });

        // Handle back button
        back.setOnClickListener(v -> {
            Intent i = new Intent(ManagePOSActivity.this, AdminPortalActivity.class);
            startActivity(i);
            finish();
        });
    }

    @Override
    public void onSectionAdded(String sectionName) {
        // Add section to RecyclerView
        sectionList.add(sectionName);
        adapter.notifyItemInserted(sectionList.size() - 1);

        // Store the section in Firestore
        storeSectionInFirestore(sectionName);
    }

    private void storeSectionInFirestore(String sectionName) {
        Map<String, Object> sectionData = new HashMap<>();
        sectionData.put("name", sectionName);

        firestore.collection("sections")
                .add(sectionData)
                .addOnSuccessListener(documentReference ->
                        Toast.makeText(ManagePOSActivity.this, "Saved to Firestore!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(ManagePOSActivity.this, "Error saving to Firestore: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void fetchSectionsFromFirestore() {
        firestore.collection("sections")
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Toast.makeText(this, "Error fetching data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (value != null) {
                        sectionList.clear(); // Clear the list before adding fetched data
                        for (QueryDocumentSnapshot doc : value) {
                            String sectionName = doc.getString("name");
                            if (sectionName != null) {
                                sectionList.add(sectionName);
                            }
                        }
                        adapter.notifyDataSetChanged(); // Notify the adapter to update the RecyclerView
                    }
                });
    }
}