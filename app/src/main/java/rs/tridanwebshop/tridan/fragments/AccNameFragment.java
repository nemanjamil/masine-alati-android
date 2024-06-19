package rs.tridanwebshop.tridan.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import net.steamcrafted.materialiconlib.MaterialDrawableBuilder;

import rs.tridanwebshop.tridan.R;
import rs.tridanwebshop.tridan.common.application.MyApplication;
import rs.tridanwebshop.tridan.common.utils.Utils;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccNameFragment extends Fragment {

    private FloatingActionButton fab;
    private Toolbar toolbar;
    private String name;
    private String last_name;
    private EditText ed_name;
    private EditText ed_last_name;
    private Bundle args;

    public AccNameFragment() {
        // Required empty public constructor
    }

    public static AccNameFragment newInstance(Toolbar toolbar, FloatingActionButton fab) {
        AccNameFragment f = new AccNameFragment();

        f.fab = fab;
        f.toolbar = toolbar;
        //Bundle args = new Bundle();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_acc_name, container, false);
        ed_name = (EditText) view.findViewById(R.id.et_name);
        ed_last_name = (EditText) view.findViewById(R.id.et_last_name);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO validate fields
                name = ed_name.getText().toString();
                last_name = ed_last_name.getText().toString();
                if (name.trim().length() > 0 && last_name.trim().length() > 0) {
                    args = new Bundle();
                    args.putString("name", name);
                    args.putString("last_name", last_name);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                    // Show signIn fragment
                    AccEmailFragment accEmailFragment = AccEmailFragment.newInstance(toolbar, fab, args);
                    fragmentTransaction.replace(R.id.fragment_container, accEmailFragment);
                    fragmentTransaction.commit();
                } else {
                    // show snackbar to enter credentials
                    Snackbar.make(fab, "Morate uneti podatke.", Snackbar.LENGTH_LONG)
                            .show();
                }
            }
        });

        accNameToolbar();
        fab.setImageDrawable(Utils.getMaterialIconDrawable(getActivity(), MaterialDrawableBuilder.IconValue.ARROW_RIGHT, R.color.colorTextWhite));
        fab.show();

        // Inflate the layout for this fragment
        return view;
    }

    private void accNameToolbar() {
        toolbar.setTitle("");
        //setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(Utils.getMaterialIconDrawable(getActivity(), MaterialDrawableBuilder.IconValue.ARROW_LEFT, R.color.colorTextWhite));
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MyApplication.hideKeyboard(getActivity());
                        //showSignInFragment();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                        // Show signIn fragment
                        SignInFragment signInFragment = SignInFragment.newInstance(toolbar, fab);
                        fragmentTransaction.replace(R.id.fragment_container, signInFragment);
                        fragmentTransaction.commit();
                    }
                }
        );

        TextView tv = (TextView) toolbar.findViewById(R.id.tv_toolbar_txt);
        tv.setText("");
    }


}
