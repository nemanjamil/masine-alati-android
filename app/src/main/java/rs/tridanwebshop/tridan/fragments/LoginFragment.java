package rs.tridanwebshop.tridan.fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
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

import rs.tridanwebshop.tridan.Interface.SignInCallbackInterface;
import rs.tridanwebshop.tridan.R;
import rs.tridanwebshop.tridan.common.application.MyApplication;
import rs.tridanwebshop.tridan.common.utils.Utils;


public class LoginFragment extends Fragment {
    EditText etEmailLogin;
    EditText etPasswordLogin;

    private TextView tv_passToggle;
    private FloatingActionButton fab;
    private Toolbar toolbar;
    private String email;
    private String pass;

    private SignInCallbackInterface mCallback;

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance(Toolbar toolbar, FloatingActionButton fab) {
        LoginFragment f = new LoginFragment();
        
        f.fab = fab;
        f.toolbar = toolbar;
        //Bundle args = new Bundle();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        etPasswordLogin = (EditText) view.findViewById(R.id.et_password);
        tv_passToggle = (TextView) view.findViewById(R.id.tv_pass_toggle_label);
        tv_passToggle.setTag(0);
        tv_passToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((Integer) view.getTag() == 0) {
                    tv_passToggle.setText(R.string.pass_toggle_label_hide);
                    tv_passToggle.setTag(1);
                    etPasswordLogin.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    etPasswordLogin.setSelection(etPasswordLogin.getText().length());
                } else if ((Integer) view.getTag() == 1) {
                    tv_passToggle.setText(R.string.pass_toggle_label_show);
                    tv_passToggle.setTag(0);
                    etPasswordLogin.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    etPasswordLogin.setSelection(etPasswordLogin.getText().length());
                }
            }
        });

        etEmailLogin = (EditText) view.findViewById(R.id.et_email);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = etEmailLogin.getText().toString();
                pass = etPasswordLogin.getText().toString();
                if (email.trim().length() > 0 && pass.trim().length() > 0) {
                    mCallback.onLogInClick(email, pass);
                } else {
                    // show snackbar to enter credentials
                    Snackbar.make(fab, "Morate uneti podatke.", Snackbar.LENGTH_LONG)
                            .show();
                }
            }
        });

        loginToolbar();
        fab.setImageDrawable(Utils.getMaterialIconDrawable(getActivity(), MaterialDrawableBuilder.IconValue.ARROW_RIGHT, R.color.colorTextWhite));
        fab.show();

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (SignInCallbackInterface) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement SignInCallbackInterface");
        }
    }

    private void loginToolbar() {
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
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
