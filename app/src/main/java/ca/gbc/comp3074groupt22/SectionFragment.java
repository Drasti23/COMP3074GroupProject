package ca.gbc.comp3074groupt22;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import ca.gbc.comp3074groupt22.Adapters.ItemAdapter;
import ca.gbc.comp3074groupt22.Item.Item;

public class SectionFragment extends Fragment {

    private static final String ARG_SECTION_NAME = "section_name";
    private String sectionName;
    private RecyclerView recyclerView;
    private FirebaseFirestore firestore;
    private ItemAdapter itemAdapter;
    private List<Item> items;

    public static SectionFragment newInstance(String sectionName) {
        SectionFragment fragment = new SectionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SECTION_NAME, sectionName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            sectionName = getArguments().getString(ARG_SECTION_NAME);
        }
        firestore = FirebaseFirestore.getInstance();
        items = new ArrayList<>();
        itemAdapter = new ItemAdapter(getContext(),items);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_section, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(itemAdapter);
        fetchItemsForSection();
        return view;
    }


    private void fetchItemsForSection() {
        firestore.collection("sections")
                .document(sectionName)
                .collection("items")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        items.clear(); // Clear current list
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            String name = doc.getString("name");
                            String description = doc.getString("description");
                            double price = doc.getDouble("price");

                            Item item = new Item(name, description, price);
                            items.add(item); // Add each item to the list
                        }
                        itemAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getContext(), "Failed to fetch items: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
