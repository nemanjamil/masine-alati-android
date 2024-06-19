package rs.tridanwebshop.tridan.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;


import androidx.appcompat.app.AlertDialog;

import rs.tridanwebshop.tridan.R;


/**
 * Created by 1 on 1/28/2016.
 */
public class CartItemAddConfirmationDialog {
    AlertDialog.Builder builder;

    private String dialogMessage;

    public CartItemAddConfirmationDialog(Context context) {
        builder = new AlertDialog.Builder(context);

        // Use the Builder class for convenient dialog construction
        builder.setTitle(R.string.add_confirmation_title);
        builder.setMessage(dialogMessage);
    }

    public void setDialogMessage(String msg){
        dialogMessage = msg;
        builder.setMessage(dialogMessage);
    }

    public void setPositiveButtonListener(DialogInterface.OnClickListener listener) {
        builder.setPositiveButton(R.string.go_to_cart, listener);
    }

    public void setNegativeButtonListener(DialogInterface.OnClickListener listener) {
        builder.setNegativeButton(R.string.continue_shopping, listener);
    }

    public Dialog create() {
        // Create the AlertDialog object and return
        return builder.create();
    }
}
