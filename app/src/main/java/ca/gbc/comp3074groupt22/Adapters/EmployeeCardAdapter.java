package ca.gbc.comp3074groupt22.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ca.gbc.comp3074groupt22.Employee.Employee;
import ca.gbc.comp3074groupt22.R;

public class EmployeeCardAdapter extends RecyclerView.Adapter<EmployeeCardAdapter.ItemViewHolder> {

    private ArrayList<Employee> employeeList;

    public EmployeeCardAdapter(ArrayList<Employee> employeeList) {
        this.employeeList = employeeList;
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
        holder.textViewItemName.setText(employeeList.get(position).getName());
        holder.textViewDetails.setText(employeeList.get(position).getCode()+"");
    }

    @Override
    public int getItemCount() {
        return employeeList.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView textViewItemName,textViewDetails;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewItemName = itemView.findViewById(R.id.employeeName);
            textViewDetails = itemView.findViewById(R.id.employeePosition);
        }
    }
}