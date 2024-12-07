package ca.gbc.comp3074groupt22;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import ca.gbc.comp3074groupt22.Section.ManagePOSActivity;

public class AdminPortalActivity extends AppCompatActivity {
    TextView managePOS;
    TextView manageEmp;
    Button goBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_portal);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        managePOS = findViewById(R.id.textViewManagePOS);
        manageEmp = findViewById(R.id.textViewManageEmp);
        goBack = findViewById(R.id.buttonAdminBack);


        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AdminPortalActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        managePOS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AdminPortalActivity.this, ManagePOSActivity.class);
                startActivity(i);
                finish();
            }
        });

        manageEmp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AdminPortalActivity.this, ManageEmployeeActivity.class);
                startActivity(i);
                finish();
            }
        });


    }
}