package rs.tridanwebshop.tridan.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import net.steamcrafted.materialiconlib.MaterialDrawableBuilder;

import rs.tridanwebshop.tridan.Interface.SignInCallbackInterface;
import rs.tridanwebshop.tridan.R;
import rs.tridanwebshop.tridan.common.application.MyApplication;
import rs.tridanwebshop.tridan.common.utils.Utils;

public class SignInFragment extends Fragment {
    private Button mLoginButton;
    private Button mCreateAcc;
    private SignInCallbackInterface mCallback;
    private Toolbar toolbar;
    private FloatingActionButton fab;

    public SignInFragment() {
        // Required empty public constructor
    }

    public static SignInFragment newInstance(Toolbar toolbar, FloatingActionButton fab) {
        SignInFragment f = new SignInFragment();
        f.toolbar = toolbar;
        f.fab = fab;
        
        //Bundle args = new Bundle();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);

        // Assign fields
        mLoginButton = (Button) view.findViewById(R.id.sign_in_login_button);
        mCreateAcc = (Button) view.findViewById(R.id.create_acc_button);

        // Set click listeners
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.onHaveAccClick();
            }
        });

        mCreateAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.onCreateNewAccClick();
            }
        });

        signInToolbar();
        fab.hide();
        MyApplication.hideKeyboard(getActivity());
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

    public void signInToolbar() {
        toolbar.setTitle("");
        //setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(Utils.getMaterialIconDrawable(getActivity(), MaterialDrawableBuilder.IconValue.CLOSE, R.color.colorTextWhite));
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getActivity().finish();
                    }
                }
        );

        TextView tv = (TextView) toolbar.findViewById(R.id.tv_toolbar_txt);
        tv.setVisibility(View.GONE);
//        tv.setText(R.string.login);
//        tv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//
//                // Show Log in fragment
//                LoginFragment logInFragment = LoginFragment.newInstance(toolbar, fab);
//                fragmentTransaction.replace(R.id.sign_in_fragment_container, logInFragment);
//                fragmentTransaction.commit();
//
//            }
//        });
    }
}
