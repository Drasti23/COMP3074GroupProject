package ca.gbc.comp3074groupt22.Dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.EditText;

import com.google.firebase.firestore.FirebaseFirestore;

import ca.gbc.comp3074groupt22.Employee.Employee;
import ca.gbc.comp3074groupt22.R;

public class EditEmployeeDialog {

    public interface OnEmployeeEditedListener {
        void onEmployeeEdited(Employee updatedEmployee);
    }

    public static void showDialog(Context context, Employee employee, OnEmployeeEditedListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        builder.setView(inflater.inflate(R.layout.activity_edit_employee_dialog, null));
        AlertDialog dialog = builder.create();

        dialog.setOnShowListener(d -> {
            EditText name = dialog.findViewById(R.id.editEmployeeName);
            EditText email = dialog.findViewById(R.id.editEmployeeEmail);
            EditText phone = dialog.findViewById(R.id.editEmployeeContact);
            EditText position = dialog.findViewById(R.id.editEmployeePosition);
            EditText address = dialog.findViewById(R.id.editEmployeeAddress);

            name.setText(employee.getName());
            email.setText(employee.getEmail());
            phone.setText(employee.getPhone());
            position.setText(employee.getPosition());
            address.setText(employee.getAddress());

            dialog.findViewById(R.id.saveButton).setOnClickListener(v -> {
                employee.setName(name.getText().toString());
                employee.setEmail(email.getText().toString());
                employee.setPhone(phone.getText().toString());
                employee.setPosition(position.getText().toString());
                employee.setAddress(address.getText().toString());

                FirebaseFirestore.getInstance().collection("employees")
                        .document(employee.getName())
                        .set(employee)
                        .addOnSuccessListener(aVoid -> {
                            listener.onEmployeeEdited(employee);
                            dialog.dismiss();
                        })
                        .addOnFailureListener(e ->
                                listener.onEmployeeEdited(null));
            });

            dialog.findViewById(R.id.cancelButton).setOnClickListener(v -> dialog.dismiss());
        });

        dialog.show();
    }
}