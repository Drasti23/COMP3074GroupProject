package ca.gbc.comp3074groupt22.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ca.gbc.comp3074groupt22.R;

public class AddEmployeeDialog {

    public interface EmployeeAddedListener {
        void onEmployeeAdded(String employeeName, String email, String contactNumber, String position, String address,int code);
    }

    private EmployeeAddedListener listener;

    public void setEmployeeAddedListener(EmployeeAddedListener listener) {
        this.listener = listener;
    }

    public void showDialog(Activity activity) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.activity_add_employee_dialog);  // Ensure the layout is updated for additional fields
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        EditText inputName = dialog.findViewById(R.id.employee_name_input);
        EditText inputEmail = dialog.findViewById(R.id.employee_email_input);
        EditText inputContact = dialog.findViewById(R.id.employee_contact_input);
        EditText inputPosition = dialog.findViewById(R.id.employee_position_input);
        EditText inputAddress = dialog.findViewById(R.id.employee_address_input);
        EditText inputCode = dialog.findViewById(R.id.employee_code_input);

        Button dialogBtnAdd = dialog.findViewById(R.id.button_add);
        Button dialogBtnCancel = dialog.findViewById(R.id.button_cancel);

        dialogBtnAdd.setOnClickListener(v -> {
            String name = inputName.getText().toString();
            String email = inputEmail.getText().toString();
            String contact = inputContact.getText().toString();
            String position = inputPosition.getText().toString();
            String address = inputAddress.getText().toString();
            String code = inputCode.getText().toString();

            if (name.isEmpty() || email.isEmpty() || contact.isEmpty() || position.isEmpty() || address.isEmpty()) {
                Toast.makeText(activity.getApplicationContext(), "All fields are required.", Toast.LENGTH_SHORT).show();
            } else {
                if (listener != null) {
                    listener.onEmployeeAdded(name, email, contact, position, address, Integer.parseInt(code));
                }
                dialog.dismiss();
            }
        });

        dialogBtnCancel.setOnClickListener(v -> {
            Toast.makeText(activity.getApplicationContext(), "Canceled.", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        dialog.show();
    }
}