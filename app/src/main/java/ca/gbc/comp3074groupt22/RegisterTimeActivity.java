package ca.gbc.comp3074groupt22;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class RegisterTimeActivity extends AppCompatActivity {

    TextView clockInButton;
    TextView clockOutButton;
    Button back;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register_time);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        firestore = FirebaseFirestore.getInstance(); // Initialize Firestore

        clockInButton = findViewById(R.id.clockInButton);
        clockOutButton = findViewById(R.id.clockOutButton);
        back = findViewById(R.id.buttonBack1);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegisterTimeActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        clockInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showClockInDialog();
            }
        });

        clockOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showClockOutDialog();
            }
        });
    }

    // Method to get the current time in HH:mm format
    private String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return sdf.format(new Date());
    }

    private void showClockInDialog() {
        String currentTime = getCurrentTime();
        saveTimeToFirestore("Clock In", currentTime);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Success");
        builder.setMessage("Successfully Clocked In at " + currentTime);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();
    }

    private void showClockOutDialog() {
        String currentTime = getCurrentTime();
        saveTimeToFirestore("Clock Out", currentTime);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Success");
        builder.setMessage("Successfully Clocked Out at " + currentTime);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.create().show();
    }

    // Method to save the time to Firestore
    private void saveTimeToFirestore(String type, String time) {
        // Create a data map to store the information
        Map<String, Object> timeData = new HashMap<>();
        timeData.put("type", type);
        timeData.put("time", time);
        timeData.put("date", new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()));

        // Save to Firestore in a "time_records" collection
        firestore.collection("time_records")
                .add(timeData)
                .addOnSuccessListener(documentReference ->
                        Toast.makeText(RegisterTimeActivity.this, type + " time saved to Firestore!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(RegisterTimeActivity.this, "Error saving " + type + ": " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}