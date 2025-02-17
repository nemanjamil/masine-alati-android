package rs.tridanwebshop.tridan;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import rs.tridanwebshop.tridan.common.dialogs.ProgressDialogCustom;
import rs.tridanwebshop.tridan.common.utils.BaseActivity;
import rs.tridanwebshop.tridan.common.utils.FieldValidators;
import rs.tridanwebshop.tridan.common.utils.SharedPreferencesUtils;
import rs.tridanwebshop.tridan.network.VolleySingleton;

public class QuestionActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_activity);

        final VolleySingleton mVolleySingleton = VolleySingleton.getsInstance(this);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView mTextView = (TextView) findViewById(R.id.title);
        if (mTextView != null) mTextView.setText(getString(R.string.help_center));
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final EditText emailTxt = (EditText) findViewById(R.id.sender_email);
        emailTxt.setText(SharedPreferencesUtils.getUserEmail());
        emailTxt.setSelection(SharedPreferencesUtils.getUserEmail().length());

        final EditText questionTxt = (EditText) findViewById(R.id.question_txt);

        Button deleteMsg = (Button) findViewById(R.id.delete_msg);
        deleteMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                questionTxt.setText("");
            }
        });

        ImageButton icSearch = (ImageButton) findViewById(R.id.toolbar_btn_search);
        icSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(intent);

            }
        });

        Button sendMsg = (Button) findViewById(R.id.send_msg);
        sendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final ProgressDialogCustom progressDialog = new ProgressDialogCustom(QuestionActivity.this);
                if (!FieldValidators.isValidEmail(emailTxt.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Neispravno uneta email adresa!", Toast.LENGTH_LONG).show();
                } else if (questionTxt.getText().toString().length() == 0) {
                    Toast.makeText(getApplicationContext(), "Niste uneli tekst poruke!", Toast.LENGTH_LONG).show();
                } else {
                    progressDialog.setCancelable(false);
                    progressDialog.showDialog("Učitavanje...");

                    StringRequest postRequest = new StringRequest(Request.Method.POST, "http://masine.tridan.rs/parametri.php",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    // response
                                    Log.d("Response", response);
                                    Toast.makeText(getApplicationContext(), "Poruka je uspešno poslata!", Toast.LENGTH_LONG).show();
                                    progressDialog.hideDialog();
                                    onBackPressed();
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    // error
                                    Log.d("Error.Response", "Error"+error.getLocalizedMessage());
                                    progressDialog.hideDialog();
                                    Toast.makeText(getApplicationContext(), "Greška pri slanju poruke, pokušajte ponovo!", Toast.LENGTH_LONG).show();

                                }
                            }
                    ) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<>();

                            params.put("email", emailTxt.getText().toString());
                            params.put("pitanje", questionTxt.getText().toString());
                            params.put("action", "posaljiMailAndr");

                            return params;
                        }
                    };
                    mVolleySingleton.addToRequestQueue(postRequest);
                }
            }
        });
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.item_question).setEnabled(false);
        SpannableString s = new SpannableString(menu.findItem(R.id.item_question).getTitle());
        s.setSpan(new ForegroundColorSpan(Color.GRAY), 0, s.length(), 0);
        menu.findItem(R.id.item_question).setTitle(s);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
