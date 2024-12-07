package ca.gbc.comp3074groupt22;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ca.gbc.comp3074groupt22.Adapters.SectionPagerAdapter;
import ca.gbc.comp3074groupt22.Cart.CartActivity;
import ca.gbc.comp3074groupt22.Item.Item;

public class POSActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private ImageView backButton, cartIcon;
    private FirebaseFirestore firestore;
    private List<String> sectionNames;
    public static List<Item> cartItems; // Cart items list
    private int orderNumber; // To track the current order number

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posactivity);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        backButton = findViewById(R.id.backButton);
        cartIcon = findViewById(R.id.cartIcon);
        firestore = FirebaseFirestore.getInstance();
        sectionNames = new ArrayList<>();
        cartItems = new ArrayList<>();

        // Back button click listener
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(POSActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

// Cart icon click listener
        cartIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(POSActivity.this, CartActivity.class);
                i.putExtra("cartItems", (ArrayList<Item>) cartItems);
                startActivity(i);
            }
        });

        fetchSectionsFromFirestore();
    }

    private void fetchSectionsFromFirestore() {
        firestore.collection("sections")
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Toast.makeText(this, "Error fetching data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (value != null) {
                        for (QueryDocumentSnapshot doc : value) {
                            String sectionName = doc.getString("name");
                            if (sectionName != null) {
                                sectionNames.add(sectionName);
                            }
                        }
                        setupViewPagerAndTabs();
                    }
                });
    }

    private void setupViewPagerAndTabs() {
        viewPager.setAdapter(new SectionPagerAdapter(this, sectionNames));

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) ->
                tab.setText(sectionNames.get(position))).attach();
    }

    private void saveOrderToFirestore() {
        // Fetch the current order number from Firestore
        firestore.collection("orders")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    orderNumber = queryDocumentSnapshots.size() + 1;

                    // Create an order map
                    HashMap<String, Object> orderData = new HashMap<>();
                    orderData.put("orderNumber", orderNumber);

                    // Create a list of cart item names
                    List<Item> items = new ArrayList<>();
                    for (Item item : cartItems) {
                        items.add(item);
                    }
                    orderData.put("items", items);

                    // Save the order to Firestore
                    firestore.collection("orders").document(String.valueOf(orderNumber))
                            .set(orderData)
                            .addOnSuccessListener(unused -> {
                                Toast.makeText(this, "Order #" + orderNumber + " saved!", Toast.LENGTH_SHORT).show();
                                cartItems.clear(); // Clear the cart after saving
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(this, "Failed to save order: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                });
    }

    // Method to add items to the cart (called from adapter)
    public void addItemToCart(Item item) {
        if (!cartItems.contains(item)) {
            cartItems.add(item);
            Toast.makeText(this, item.getName() + " added to cart!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, item.getName() + " is already in the cart!", Toast.LENGTH_SHORT).show();
        }
    }
}
