package ca.gbc.comp3074groupt22;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import ca.gbc.comp3074groupt22.Dialogs.AdminDialog;
//import ca.gbc.comp3074groupt22.Dialogs.RegisterTimeDialog;
import ca.gbc.comp3074groupt22.Order.OrderActivity;
import ca.gbc.comp3074groupt22.Order.OrderActivity;

public class MainActivity extends AppCompatActivity {

    TextView adminPortal,POS,registerTime,orderHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        adminPortal = findViewById(R.id.textViewAdminPortal);
        POS = findViewById(R.id.textViewPOS);
        registerTime = findViewById(R.id.textViewRegisterTime);
        orderHistory = findViewById(R.id.textViewOrderHistory);

        adminPortal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AdminDialog alert = new AdminDialog();
                alert.showDialog(MainActivity.this);
            }
        });
        orderHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, OrderActivity.class);
                startActivity(i);

            }
        });
//        registerTime.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                RegisterTimeDialog alert = new RegisterTimeDialog();
//                alert.showDialog(MainActivity.this);
//            }
//        });

        POS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, POSActivity.class);
                startActivity(i);
            }
        });
    }
}
