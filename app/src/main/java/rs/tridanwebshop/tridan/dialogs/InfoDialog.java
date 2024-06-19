package rs.tridanwebshop.tridan.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import rs.tridanwebshop.tridan.R;

/**
 * ******************************
 * Created by milan on 7/8/2016.
 * ******************************
 */
public class InfoDialog extends DialogFragment {
    InfoDialogListener mListener;

    public static InfoDialog newInstance(String title, String message) {
        InfoDialog frag = new InfoDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("message", message);
        frag.setArguments(args);
        return frag;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getArguments().getString("title"))
                .setMessage(getArguments().getString("message"))
                .setCancelable(false)
                .setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                        // call callback method
                       // mListener.onInfoDialogOKClick(InfoDialog.this);
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

//    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        // Verify that the host activity implements the callback interface
//        try {
//            // Instantiate the NoticeDialogListener so we can send events to the host
//            mListener = (InfoDialogListener) context;
//        } catch (ClassCastException e) {
//            // The activity doesn't implement the interface, throw exception
//            throw new ClassCastException(context.toString()
//                    + " must implement NoticeDialogListener");
//        }
//    }

    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface InfoDialogListener {
        void onInfoDialogOKClick(InfoDialog dialog);
    }
}
