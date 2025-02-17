package rs.tridanwebshop.tridan.fragments;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import rs.tridanwebshop.tridan.AllCategoriesActivity;
import rs.tridanwebshop.tridan.R;
import rs.tridanwebshop.tridan.common.config.AppConfig;
import rs.tridanwebshop.tridan.common.utils.SharedPreferencesUtils;

public class DeleteHistoryDialog extends DialogFragment {

    private boolean clickedNo = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.history_delete_dialog, null);

        if (getDialog().getWindow() != null) {
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }

        Button no = (Button) view.findViewById(R.id.cancelBtn);
        Button yes = (Button) view.findViewById(R.id.deleteBtn);


        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferencesUtils.clearSharedPreferences(getActivity(), AppConfig.HISTORY_KEY);
                dismiss();
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickedNo = true;
                dismiss();
            }
        });
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (!clickedNo) {
            AllCategoriesActivity activity = (AllCategoriesActivity) getActivity();
            activity.populateRecyclerView();
        }
    }

}
