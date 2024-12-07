package ca.gbc.comp3074groupt22.Dialogs;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import ca.gbc.comp3074groupt22.R;

public class AddSectionDialog {

    public interface SectionAddedListener {
        void onSectionAdded(String sectionName);
    }

    private SectionAddedListener listener;

    public void setSectionAddedListener(SectionAddedListener listener) {
        this.listener = listener;
    }

    public void showDialog(Activity activity) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.add_section_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        EditText inputSection = dialog.findViewById(R.id.section_name_input);

        Button dialogBtn_Enter = dialog.findViewById(R.id.button_add);
        Button dialogBtn_cancel = dialog.findViewById(R.id.button_cancel);

        dialogBtn_Enter.setOnClickListener(v -> {
            String inputSectionString = inputSection.getText().toString();

            if (inputSectionString.isEmpty()) {
                Toast.makeText(activity.getApplicationContext(), "Fill required fields.", Toast.LENGTH_SHORT).show();
            } else {
                if (listener != null) {
                    listener.onSectionAdded(inputSectionString);
                }
                dialog.dismiss();
            }
        });

        dialogBtn_cancel.setOnClickListener(v -> {
            Toast.makeText(activity.getApplicationContext(), "Canceled.", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        dialog.show();
    }
}
