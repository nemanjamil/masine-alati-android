package rs.tridanwebshop.tridan;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import rs.tridanwebshop.tridan.common.config.AppConfig;
import rs.tridanwebshop.tridan.common.dialogs.ProgressDialogCustom;
import rs.tridanwebshop.tridan.common.utils.BaseActivity;
import rs.tridanwebshop.tridan.common.utils.Log;
import rs.tridanwebshop.tridan.customview.CustomRecyclerView;
import rs.tridanwebshop.tridan.customview.LinearLayoutManagerAutoMeasure;
import rs.tridanwebshop.tridan.models.articles.Article;
import rs.tridanwebshop.tridan.models.one_article.OneArticle;
import rs.tridanwebshop.tridan.network.PullWebContent;
import rs.tridanwebshop.tridan.network.UrlEndpoints;
import rs.tridanwebshop.tridan.network.VolleySingleton;
import rs.tridanwebshop.tridan.network.WebRequestCallbackInterface;
import rs.tridanwebshop.tridan.views.adapters.RecyclerViewSelectedProducts;

public class OffersActivity extends BaseActivity {

    private CustomRecyclerView mRecyclerView;
    private LinearLayoutManagerAutoMeasure mLayoutManager;
    private RecyclerViewSelectedProducts mAdapter;
    private String mSubCategoryName;

    private VolleySingleton mVolleySingleton;

    private List<Article> articles = new ArrayList<>();

    @SuppressWarnings("unchecked")
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.offers_activity);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView mTextView = (TextView) findViewById(R.id.title);

        Intent intent = getIntent();
        mSubCategoryName = intent.getStringExtra("Artikli");
        // this cast is unchecked, but guaranteed to work
        articles = (List<Article>) intent.getSerializableExtra("AllCategories");
        if (mTextView != null) mTextView.setText(mSubCategoryName);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ImageButton icSearch = (ImageButton) findViewById(R.id.toolbar_btn_search);
        icSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(intent);

            }
        });

        mRecyclerView = (CustomRecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setNestedScrollingEnabled(true);
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManagerAutoMeasure(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mVolleySingleton = VolleySingleton.getsInstance(this);

        mAdapter = new RecyclerViewSelectedProducts(this, articles, true, 0, new RecyclerViewSelectedProducts.OnItemClickListener() {
            @Override
            public void onItemClick(Article item, View view) {
                ///Start Intent for Single Item Activity
                viewArticle(item.getArtikalId());
            }
        });
        mRecyclerView.setAdapter(mAdapter);

    }

    public void viewArticle(int itemID) {

        final ProgressDialogCustom progressDialog = new ProgressDialogCustom(OffersActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.showDialog("Učitavanje...");
        android.util.Log.d("itemID3", String.valueOf(itemID));

        PullWebContent<OneArticle> content =
                new PullWebContent<>(OneArticle.class, UrlEndpoints.getRequestUrlArticleById(itemID), mVolleySingleton);


        Log.logInfo("LALALA", String.valueOf(itemID));
        content.setCallbackListener(new WebRequestCallbackInterface<OneArticle>() {
            @Override
            public void webRequestSuccess(boolean success, OneArticle oneArticle) {
                if (success) {
                    Log.logInfo("LALALA", "SUCCESS");
                    Intent intent = new Intent(getApplicationContext(), OneArticleActivity.class);
                    intent.putExtra(AppConfig.ABOUT_PRODUCT, oneArticle);
                    startActivity(intent);
                    progressDialog.hideDialog();

                    Log.logInfo("LALALA", oneArticle.getArtikal().getArtikalNaziv());

                } else {
                    progressDialog.hideDialog();
                    Log.logInfo("LALALA", "FAILED");
                }
            }

            @Override
            public void webRequestError(String error) {
                progressDialog.hideDialog();

            }
        });

        content.pullList();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (mSubCategoryName.equalsIgnoreCase(AppConfig.FIRST_TAB_ITEMS[0])) {

            menu.findItem(R.id.item_sale).setEnabled(false);
            SpannableString s = new SpannableString(menu.findItem(R.id.item_sale).getTitle());
            s.setSpan(new ForegroundColorSpan(Color.GRAY), 0, s.length(), 0);
            menu.findItem(R.id.item_sale).setTitle(s);

        }
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
