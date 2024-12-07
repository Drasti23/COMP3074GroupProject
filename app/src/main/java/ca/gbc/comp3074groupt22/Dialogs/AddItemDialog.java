package ca.gbc.comp3074groupt22.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ca.gbc.comp3074groupt22.R;


public class AddItemDialog {

    public void setInitialData(String name, String description, double price) {
        this.initialName = name;
        this.initialDescription = description;
        this.initialPrice = price;
    }
    private String initialName = "";
    private String initialDescription = "";
    private double initialPrice = 0.0;


    public interface ItemAddedListener {
        void onItemAdded(String name, String description, double price);
    }

    private ItemAddedListener listener;

    public void setItemAddedListener(ItemAddedListener listener) {
        this.listener = listener;
    }

    public void showDialog(Activity activity) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.add_item_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        EditText inputName = dialog.findViewById(R.id.item_name_input);
        EditText inputDescription = dialog.findViewById(R.id.item_disc_input);
        EditText inputPrice = dialog.findViewById(R.id.item_price_input);

        Button dialogBtnEnter = dialog.findViewById(R.id.item_button_add);
        Button dialogBtnCancel = dialog.findViewById(R.id.item_button_cancel);

        dialogBtnEnter.setOnClickListener(v -> {
            String name = inputName.getText().toString();
            String description = inputDescription.getText().toString();
            String priceText = inputPrice.getText().toString();

            if (name.isEmpty() || description.isEmpty() || priceText.isEmpty()) {
                Toast.makeText(activity, "Fill all fields.", Toast.LENGTH_SHORT).show();
            } else {
                double price = Double.parseDouble(priceText);
                if (listener != null) {
                    listener.onItemAdded(name, description, price);
                }
                dialog.dismiss();
            }
        });

        dialogBtnCancel.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }
}
