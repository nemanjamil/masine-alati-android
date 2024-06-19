package rs.tridanwebshop.tridan.common.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.io.Serializable;

import rs.tridanwebshop.tridan.CartActivity;
import rs.tridanwebshop.tridan.InfoActivity;
import rs.tridanwebshop.tridan.MainActivity;
import rs.tridanwebshop.tridan.OffersActivity;
import rs.tridanwebshop.tridan.QuestionActivity;
import rs.tridanwebshop.tridan.R;
import rs.tridanwebshop.tridan.SearchActivity;
import rs.tridanwebshop.tridan.common.application.MyApplication;
import rs.tridanwebshop.tridan.common.config.AppConfig;
import rs.tridanwebshop.tridan.fcm.Config;
import rs.tridanwebshop.tridan.signin.AccountActivity;


public class BaseActivity extends AppCompatActivity {
    private BroadcastReceiver mUpdateToolbarBroadcastReceiver;

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mUpdateToolbarBroadcastReceiver,
                new IntentFilter(Config.UPDATE_CART_TOOLBAR_ICON));

        updateCartToolbarIcon();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mUpdateToolbarBroadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Config.UPDATE_CART_TOOLBAR_ICON)) {
                    updateCartToolbarIcon();
                    setSearchButton();
                }
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.pop_up_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;

        switch (item.getItemId()) {

            case R.id.item_home:
                intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                return true;

            case R.id.item_signing:
                // Handle the login action
                intent = new Intent(this, AccountActivity.class);
                startActivity(intent);

                return true;

            case R.id.item_sale:
                intent = new Intent(this, OffersActivity.class);
                intent.putExtra("Artikli", AppConfig.FIRST_TAB_ITEMS[0]);
                intent.putExtra("AllCategories", (Serializable) SharedPreferencesUtils.getArrayListArticle(this, "SALE"));
                startActivity(intent);
                return true;
            case R.id.item_instructions:
                intent = new Intent(this, InfoActivity.class);
                intent.putExtra("infoTip", getString(R.string.how_to_buy));
                startActivity(intent);
                return true;
            case R.id.item_question:
                intent = new Intent(this, QuestionActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mUpdateToolbarBroadcastReceiver);

    }

    protected void updateCartToolbarIcon() {
        // Display badge over cart icon if there are some items in the cart
        ImageButton icCart = (ImageButton) findViewById(R.id.toolbar_btn_cart);
        TextView tvItemCount = (TextView) findViewById(R.id.badge_textView);
        int itemCount = MyApplication.getInstance().getSessionManager().getCartItemCount();
        assert icCart != null;
        assert tvItemCount != null;
        if (itemCount == 0) {
            tvItemCount.setVisibility(View.GONE);
        } else {
            tvItemCount.setText(String.valueOf(itemCount));
            tvItemCount.setVisibility(View.VISIBLE);
        }
        icCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CartActivity.class);
                startActivity(intent);
            }
        });
    }

    protected void setSearchButton() {
        ImageButton icSearch = (ImageButton) findViewById(R.id.toolbar_btn_search);
        icSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(intent);

            }
        });
    }

}
