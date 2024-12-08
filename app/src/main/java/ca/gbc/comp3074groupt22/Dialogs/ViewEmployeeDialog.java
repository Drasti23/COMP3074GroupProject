package ca.gbc.comp3074groupt22.Dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.TextView;

import ca.gbc.comp3074groupt22.Employee.Employee;
import ca.gbc.comp3074groupt22.R;

public class ViewEmployeeDialog {
    public static void showDialog(Context context, Employee employee) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        builder.setView(inflater.inflate(R.layout.activity_view_employee_dialog, null));
        AlertDialog dialog = builder.create();

        dialog.setOnShowListener(d -> {
            TextView name = dialog.findViewById(R.id.viewEmployeeName);
            TextView email = dialog.findViewById(R.id.viewEmployeeEmail);
            TextView phone = dialog.findViewById(R.id.viewEmployeeContact);
            TextView position = dialog.findViewById(R.id.viewEmployeePosition);
            TextView address = dialog.findViewById(R.id.viewEmployeeAddress);

            name.setText(employee.getName());
            email.setText(employee.getEmail());
            phone.setText(employee.getPhone());
            position.setText(employee.getPosition());
            address.setText(employee.getAddress());

            dialog.findViewById(R.id.closeButton).setOnClickListener(v -> dialog.dismiss());
        });

        dialog.show();
    }
}