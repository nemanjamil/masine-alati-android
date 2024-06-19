package rs.tridanwebshop.tridan.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;


import androidx.appcompat.app.AlertDialog;

import rs.tridanwebshop.tridan.R;


/**
 * Created by 1 on 1/28/2016.
 */
public class CartDeleteAllConfirmationDialog {
    AlertDialog.Builder builder;

    public CartDeleteAllConfirmationDialog(Context context) {
        builder = new AlertDialog.Builder(context);

        // Use the Builder class for convenient dialog construction
        builder.setTitle(R.string.delete_confirmation_title);
        builder.setMessage(R.string.delete_all_confirmation_question);
    }

    public void setPositiveButtonListener(DialogInterface.OnClickListener listener) {
        builder.setPositiveButton(R.string.yes, listener);
    }

    public void setNegativeButtonListener(DialogInterface.OnClickListener listener) {
        builder.setNegativeButton(R.string.cancel, listener);
    }

    public Dialog create() {
        // Create the AlertDialog object and return
        return builder.create();
    }
}
