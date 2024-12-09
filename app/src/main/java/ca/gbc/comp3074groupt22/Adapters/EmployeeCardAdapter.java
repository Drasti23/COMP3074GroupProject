package ca.gbc.comp3074groupt22.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ca.gbc.comp3074groupt22.Employee.Employee;
import ca.gbc.comp3074groupt22.R;

public class EmployeeCardAdapter extends RecyclerView.Adapter<EmployeeCardAdapter.ItemViewHolder> {

    private ArrayList<Employee> employeeList;
    private EmployeeActionListener employeeActionListener;

    // Interface to handle CRUD actions
    public interface EmployeeActionListener {
        void onViewEmployee(Employee employee);
        void onEditEmployee(Employee employee, int position);
        void onDeleteEmployee(Employee employee, int position);
    }

    public EmployeeCardAdapter(ArrayList<Employee> employeeList, EmployeeActionListener listener) {
        this.employeeList = employeeList;
        this.employeeActionListener = listener;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.employee_card_desing, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Employee employee = employeeList.get(position);

        holder.textViewItemName.setText(employee.getName());
        holder.textViewDetails.setText("Code: " + employee.getCode());

        // Handle "View" action
        holder.itemView.setOnClickListener(v -> {
            if (employeeActionListener != null) {
                employeeActionListener.onViewEmployee(employee);
            }
        });

        // Handle "Edit" action
        holder.editIcon.setOnClickListener(v -> {
            if (employeeActionListener != null) {
                employeeActionListener.onEditEmployee(employee, position);
            }
        });

        // Handle "Delete" action
        holder.deleteIcon.setOnClickListener(v -> {
            if (employeeActionListener != null) {
                employeeActionListener.onDeleteEmployee(employee, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return employeeList.size();
    }

    // Method to update employee list after CRUD operations
    public void updateEmployeeList(ArrayList<Employee> newEmployeeList) {
        this.employeeList = newEmployeeList;
        notifyDataSetChanged();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView textViewItemName, textViewDetails;
        ImageView editIcon, deleteIcon;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewItemName = itemView.findViewById(R.id.employeeName);
            textViewDetails = itemView.findViewById(R.id.employeePosition);
            editIcon = itemView.findViewById(R.id.employeeAction);
           deleteIcon = itemView.findViewById(R.id.deleteEmployeeIcon);
        }
    }
}