package ca.gbc.comp3074groupt22;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import ca.gbc.comp3074groupt22.Adapters.EmployeeCardAdapter;
import ca.gbc.comp3074groupt22.AdminPortalActivity;
import ca.gbc.comp3074groupt22.Dialogs.AddEmployeeDialog;
import ca.gbc.comp3074groupt22.Employee.Employee;
import ca.gbc.comp3074groupt22.R;

import java.util.ArrayList;

public class ManageEmployeeActivity extends AppCompatActivity implements AddEmployeeDialog.EmployeeAddedListener {

    RecyclerView recyclerView;
    private ArrayList<Employee> employeeList;
    private EmployeeCardAdapter adapter;
    private FloatingActionButton fab;
    ImageView back;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_employee);
        firestore = FirebaseFirestore.getInstance();
        // Fetch employees from Firestore
        fetchEmployeesFromFirestore();
        // Initialize RecyclerView and FloatingActionButton
        recyclerView = findViewById(R.id.empRecyclerView);
        fab = findViewById(R.id.addEmpFab);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the employee list and adapter
        employeeList = new ArrayList<>();
        adapter = new EmployeeCardAdapter(employeeList);
        recyclerView.setAdapter(adapter);

        // Handle FloatingActionButton click to show the dialog
        fab.setOnClickListener(v -> {
            AddEmployeeDialog dialog = new AddEmployeeDialog();
            dialog.setEmployeeAddedListener(this); // Set the callback for employee added
            dialog.showDialog(ManageEmployeeActivity.this);
        });

        // Set up back button to navigate to the Admin portal
        back = findViewById(R.id.backButtonEmp);
        back.setOnClickListener(v -> {
            Intent i = new Intent(ManageEmployeeActivity.this, AdminPortalActivity.class);
            startActivity(i);
            finish();
        });
    }

    @Override
    public void onEmployeeAdded(String employeeName, String email, String contactNumber, String position, String address, int code) {
        Employee newEmployee = new Employee(employeeName, email, contactNumber, position, address, code);
        employeeList.add(newEmployee);
        adapter.notifyItemInserted(employeeList.size() - 1);
        Toast.makeText(this, "Employee added: " + employeeName, Toast.LENGTH_SHORT).show();
        // Store employee in Firestore
        firestore.collection("employees")
                .add(newEmployee)
                .addOnSuccessListener(documentReference ->
                        Toast.makeText(this, "Employee added to Firestore!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to add employee: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void fetchEmployeesFromFirestore() {
        firestore.collection("employees")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    employeeList.clear();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Employee employee = document.toObject(Employee.class);
                        employeeList.add(employee);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to fetch employees: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}