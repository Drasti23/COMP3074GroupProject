package ca.gbc.comp3074groupt22.Cart;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ca.gbc.comp3074groupt22.Adapters.CartAdapter;
import ca.gbc.comp3074groupt22.Item.Item;
import ca.gbc.comp3074groupt22.POSActivity;
import ca.gbc.comp3074groupt22.R;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView totalAmountTextView;
    private List<Item> cartItems;
    private CartAdapter adapter;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView = findViewById(R.id.cartRecyclerView);
        totalAmountTextView = findViewById(R.id.totalPriceTextView);
        back = findViewById(R.id.backButtonAdded);

        // Get cart items from Intent
        cartItems = (List<Item>) getIntent().getSerializableExtra("cartItems");
        if (cartItems == null) {
            cartItems = new ArrayList<>();
            Toast.makeText(this, "No items in cart!", Toast.LENGTH_SHORT).show();
        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CartActivity.this, POSActivity.class);
                startActivity(i);
            }
        });


        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CartAdapter(cartItems, this::updateTotalAmount);
        recyclerView.setAdapter(adapter);

        // Calculate the initial total
        updateTotalAmount();
    }

    // Method to calculate and display total amount
    private void updateTotalAmount() {
        double total = 0;
        for (Item item : cartItems) {
            total += item.getPrice() * item.getQuantity();
        }
        double tax = total * 0.13; // 13% HST
        totalAmountTextView.setText(String.format("Total: $%.2f\nTax: $%.2f\nGrand Total: $%.2f", total, tax, total + tax));
    }
}
