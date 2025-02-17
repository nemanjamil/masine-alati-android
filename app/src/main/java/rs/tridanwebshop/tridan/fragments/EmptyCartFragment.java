package rs.tridanwebshop.tridan.fragments;


import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import rs.tridanwebshop.tridan.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class EmptyCartFragment extends Fragment implements View.OnClickListener {


    public EmptyCartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_empty_cart, container, false);
        view.findViewById(R.id.btn_continue_shopping).setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_continue_shopping:
                getActivity().finish();
                break;
            default:
        }
    }
}
