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
import ca.gbc.comp3074groupt22.Dialogs.AddEmployeeDialog;
import ca.gbc.comp3074groupt22.Dialogs.EditEmployeeDialog;
import ca.gbc.comp3074groupt22.Dialogs.ViewEmployeeDialog;
import ca.gbc.comp3074groupt22.Employee.Employee;

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

        // Initialize RecyclerView and FloatingActionButton
        recyclerView = findViewById(R.id.empRecyclerView);
        fab = findViewById(R.id.addEmpFab);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the employee list and adapter
        employeeList = new ArrayList<>();
        adapter = new EmployeeCardAdapter(employeeList, new EmployeeCardAdapter.EmployeeActionListener() {
            @Override
            public void onViewEmployee(Employee employee) {
                ViewEmployeeDialog.showDialog(ManageEmployeeActivity.this, employee);
            }

            @Override
            public void onEditEmployee(Employee employee, int position) {
                EditEmployeeDialog.showDialog(ManageEmployeeActivity.this, employee, updatedEmployee -> {
                    if (updatedEmployee != null) {
                        // Update the employee at the given position in the list
                        employeeList.set(position, updatedEmployee);
                        adapter.notifyItemChanged(position);

                        // Update the employee in Firestore as well
                        firestore.collection("employees")
                                .document(employee.getName())  // Assuming name is unique and used as document ID
                                .set(updatedEmployee)
                                .addOnSuccessListener(aVoid ->
                                        Toast.makeText(ManageEmployeeActivity.this, "Employee updated in Firestore", Toast.LENGTH_SHORT).show())
                                .addOnFailureListener(e ->
                                        Toast.makeText(ManageEmployeeActivity.this, "Error updating employee in Firestore: " + e.getMessage(), Toast.LENGTH_SHORT).show());

                        Toast.makeText(ManageEmployeeActivity.this, "Employee updated successfully", Toast.LENGTH_SHORT).show();
                    }
                });
            }


            @Override
            public void onDeleteEmployee(Employee employee, int position) {
                // Delete employee from Firestore
                firestore.collection("employees").document(employee.getName())
                        .delete()
                        .addOnSuccessListener(aVoid -> {
                            employeeList.remove(position);
                            adapter.notifyItemRemoved(position);
                            Toast.makeText(ManageEmployeeActivity.this, "Deleted: " + employee.getName(), Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e ->
                                Toast.makeText(ManageEmployeeActivity.this, "Error deleting: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        });
        recyclerView.setAdapter(adapter);

        // Fetch employees from Firestore
        fetchEmployeesFromFirestore();

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