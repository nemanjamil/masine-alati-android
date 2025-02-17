package rs.tridanwebshop.tridan;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;

import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rs.tridanwebshop.tridan.common.config.AppConfig;
import rs.tridanwebshop.tridan.common.dialogs.ProgressDialogCustom;
import rs.tridanwebshop.tridan.common.utils.BaseActivity;
import rs.tridanwebshop.tridan.common.utils.Conversions;
import rs.tridanwebshop.tridan.common.utils.FlipAnimation;
import rs.tridanwebshop.tridan.common.utils.Log;
import rs.tridanwebshop.tridan.common.utils.SharedPreferencesUtils;
import rs.tridanwebshop.tridan.common.utils.SortUtils;
import rs.tridanwebshop.tridan.fragments.ArticlesGrid;
import rs.tridanwebshop.tridan.fragments.ArticlesList;
import rs.tridanwebshop.tridan.fragments.FilterFragmentDialog;
import rs.tridanwebshop.tridan.models.articles.Article;
import rs.tridanwebshop.tridan.models.articles.ArticleSpec;
import rs.tridanwebshop.tridan.models.articles.Brendovus;
import rs.tridanwebshop.tridan.models.articles.articles_filtered_by_category.ArticlesFilteredByCategory;
import rs.tridanwebshop.tridan.models.categories.categories_by_id.BreadCrump;
import rs.tridanwebshop.tridan.models.categories.categories_by_id.CategoriesByID;
import rs.tridanwebshop.tridan.network.PullWebContent;
import rs.tridanwebshop.tridan.network.UrlEndpoints;
import rs.tridanwebshop.tridan.network.VolleySingleton;
import rs.tridanwebshop.tridan.network.WebRequestCallbackInterface;

public class SubCategoryArticlesActivity extends BaseActivity implements Serializable {

    public int selectedSubcategoryId = 0;
    private List<Article> mArticles = new ArrayList<>();
    private List<Article> allSubcategoryArticles = new ArrayList<>();
    private List<Article> filteredArticles = new ArrayList<>();
    private List<Brendovus> mBrands = new ArrayList<>();
    private HashMap<Integer, ArrayList<Integer>> selectedSpecifications = new HashMap<>();
    private ImageView filterImg;
    private TextView msgNoResults;
    private FrameLayout cardFace;
    private FrameLayout cardBack;
    private AppBarLayout mHeader;
    private RelativeLayout mFooter;
    private VolleySingleton mVolleySingleton;
    private boolean nextImgStateGrid = true;
    private boolean filtered = false;
    private int mArticleId;
    private int sortOption = 0;
    private boolean addedFragments = false;
    private String mSubCategoryName;
    private int numberOfResults = 0;
    private ViewGroup pathList;
    private List<BreadCrump> breadCrumpList;

    public List<Article> getArticlesList() {
        return mArticles;
    }

    public int getmArticleId() {
        return mArticleId;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subcategory_articles_activity);

        msgNoResults = (TextView) findViewById(R.id.no_results_message);
        cardFace = (FrameLayout) findViewById(R.id.articles_content_list);
        cardBack = (FrameLayout) findViewById(R.id.articles_content_grid);
        mHeader = (AppBarLayout) findViewById(R.id.sort_header);

        cardBack.setVisibility(View.GONE);
        pathList = (ViewGroup) findViewById(R.id.flow_layout_finalpath);

        Intent intent = getIntent();
        mSubCategoryName = intent.getStringExtra("Artikli");
        mArticleId = intent.getIntExtra("ArtikalId", 0);
        breadCrumpList = (List<BreadCrump>) intent.getSerializableExtra("breadCrump");
        Log.logDebug("passedData1",mSubCategoryName);
        Log.logDebug("passedData2", String.valueOf(mArticleId));
        Log.logDebug("passedData3", breadCrumpList.toString());
        if (breadCrumpList.size() > 0) {
            pathList.addView(addNewButton("Sve kategorije", "0"));
            if (breadCrumpList.size() > 1) {
                pathList.addView(addSeparator());
            }
            for (int i = 0; i < breadCrumpList.size() - 1; i++) {
                pathList.addView(addNewButton(breadCrumpList.get(i).getIme(), breadCrumpList.get(i).getIdBc().toString()));

                if (breadCrumpList.size() > 0 && i < breadCrumpList.size() - 2) {
                    pathList.addView(addSeparator());
                }
            }
        }
        Log.logInfo("VISINA", ""+pathList.getMeasuredHeight());

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, pathList.getHeight(), 0, 0);
        cardFace.setLayoutParams(params);
        cardBack.setLayoutParams(params);
        Spinner mSpinner = (Spinner) findViewById(R.id.spinner_sort);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.spinner_item, getResources().getStringArray(R.array.sort_options));

        adapter.setDropDownViewResource(R.layout.drop_down_spinner_view);
        if (mSpinner != null) {
            mSpinner.setAdapter(adapter);
            mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    if (position == 0 && sortOption != 0) {
                        sortOption = 0;
                        // mArticles = SortUtils.sortArticlesByPriceAscending(mArticles);
                        //Log.logInfo("SORT", ""+mArticles.size());
                        updateList(SortUtils.sortArticlesByNumberOfViewsDescending(filteredArticles));

                        //searchArticlesByCategory(getmArticleId(), 0, 100, AppConfig.SORT_ASCENDING);
                    } else if (position == 1 && sortOption != 1) {
                        sortOption = 1;
                        //searchArticlesByCategory(getmArticleId(), 0, 100, AppConfig.SORT_DESCENDING);
                        // mArticles = SortUtils.sortArticlesByPriceDescending(mArticles);
                        Log.logInfo("SORT", "" + filteredArticles.size());
                        updateList(SortUtils.sortArticlesByPriceAscending(filteredArticles));

                    } else if (position == 2 && sortOption != 2) {
                        sortOption = 2;
                        //searchArticlesByCategory(getmArticleId(), 0, 100, AppConfig.SORT_DESCENDING);
                        // mArticles = SortUtils.sortArticlesByPriceDescending(mArticles);
                        Log.logInfo("SORT", "" + filteredArticles.size());
                        updateList(SortUtils.sortArticlesByPriceDescending(filteredArticles));

                    } else if (position == 3 && sortOption != 3) {
                        sortOption = 3;
                        //searchArticlesByCategory(getmArticleId(), 0, 100, AppConfig.SORT_DESCENDING);
                        // mArticles = SortUtils.sortArticlesByPriceDescending(mArticles);
                        Log.logInfo("SORT", "" + filteredArticles.size());
                        updateList(SortUtils.sortArticlesByNameAscending(filteredArticles));

                    } else if (position == 4 && sortOption != 4) {
                        sortOption = 4;
                        //searchArticlesByCategory(getmArticleId(), 0, 100, AppConfig.SORT_DESCENDING);
                        // mArticles = SortUtils.sortArticlesByPriceDescending(mArticles);
                        Log.logInfo("SORT", "" + filteredArticles.size());
                        updateList(SortUtils.sortArticlesByIdDescending(filteredArticles));

                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        mVolleySingleton = VolleySingleton.getsInstance(this);

        ImageButton icSearch = (ImageButton) findViewById(R.id.toolbar_btn_search);
        icSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(intent);

            }
        });

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView mTextView = (TextView) findViewById(R.id.title);
        if (mTextView != null) mTextView.setText(mSubCategoryName);

        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        final ImageButton listGridChangeBtn = (ImageButton) findViewById(R.id.list_grid_change_btn);

        if (listGridChangeBtn != null) {
            listGridChangeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (nextImgStateGrid) {
                        listGridChangeBtn.setImageResource(R.drawable.ic_reorder_black_24dp);
                        nextImgStateGrid = false;
                        ((ArticlesList) getFragmentManager().findFragmentById(R.id.articles_content_list)).scrollToTop();


                    } else {
                        listGridChangeBtn.setImageResource(R.drawable.ic_view_module_black_24dp);
                        nextImgStateGrid = true;
                        ((ArticlesGrid) getFragmentManager().findFragmentById(R.id.articles_content_grid)).scrollToTop();

                    }
                    flipCard();

                }
            });
        }

        //Button for transition to the searh filter form
        mFooter = (RelativeLayout) findViewById(R.id.filter_layout);
        mFooter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFooter.setSelected(true);
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        mFooter.setSelected(false);
                    }
                }, 1000);

                setClickedSubcategoryId(mArticleId);
                setNumberOfResults(filteredArticles.size());
                setBrands(mBrands);

                FragmentTransaction ft = getFragmentManager().beginTransaction();
                FilterFragmentDialog frag = new FilterFragmentDialog();
                frag.show(ft, "txn_tag");
            }
        });

        filterImg = (ImageView) findViewById(R.id.img_filter);

        searchArticlesByCategory(getmArticleId(), 0, 100, AppConfig.SORT_ASCENDING);

    }

    private void flipCard() {
        View rootLayout = findViewById(R.id.main_activity_root);

        FlipAnimation flipAnimation = new FlipAnimation(cardFace, cardBack);
        if (cardFace != null) {
            if (cardFace.getVisibility() == View.GONE) {
                flipAnimation.reverse();
            }
            if (rootLayout != null) rootLayout.startAnimation(flipAnimation);
        }
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

    private void searchArticlesByCategory(int id, int from, final int to, int sort) {

        final ProgressDialogCustom progressDialog = new ProgressDialogCustom(SubCategoryArticlesActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.showDialog("Učitavanje...");
        Log.logDebug("passedData4", String.valueOf(id));

        PullWebContent<ArticlesFilteredByCategory> content = new PullWebContent<>(ArticlesFilteredByCategory.class, UrlEndpoints.getRequestUrlSearchArticlesByCategory(id, from, to, AppConfig.URL_VALUE_CURRENCY_RSD, AppConfig.URL_VALUE_LANGUAGE_SRB_LAT, sort), mVolleySingleton);
        content.setCallbackListener(new WebRequestCallbackInterface<ArticlesFilteredByCategory>() {
            @Override
            public void webRequestSuccess(boolean success, ArticlesFilteredByCategory articlesFilteredByCategory) {
                if (success) {

                    mArticles = SortUtils.sortArticlesByNumberOfViewsDescending(articlesFilteredByCategory.getArtikli());
                    filteredArticles = mArticles;
                    allSubcategoryArticles = articlesFilteredByCategory.getArtikli();

                    mBrands = articlesFilteredByCategory.getBrendovi();

                    if (mArticles.size() > 0) {
                        mHeader.setVisibility(View.VISIBLE);
                        mFooter.setVisibility(View.VISIBLE);
                        if (!addedFragments) {

                            addedFragments = true;

                            getFragmentManager()
                                    .beginTransaction()
                                    .add(R.id.articles_content_list, new ArticlesList())
                                    // Add this transaction to the back stack, allowing users to press Back
                                    // to get to the front of the card.
                                    .addToBackStack(null)
                                    // Commit the transaction.
                                    .commit();
                            getFragmentManager()
                                    .beginTransaction()
                                    .add(R.id.articles_content_grid, new ArticlesGrid())
                                    // Add this transaction to the back stack, allowing users to press Back
                                    // to get to the front of the card.
                                    .addToBackStack(null)
                                    // Commit the transaction.
                                    .commit();
                        } else {

                            updateList(mArticles);

                        }
                    } else {
                        Log.logDebug("noResults","1");
                        noResults();
                    }
                    progressDialog.hideDialog();
                }
            }

            @Override
            public void webRequestError(String error) {
                progressDialog.hideDialog();
                Log.logDebug("noResults","2" +error);
                noResults();
            }
        });
        content.pullList();

    }

    private void getCategories(int id, final String categoryName) {

        final ProgressDialogCustom progressDialog = new ProgressDialogCustom(SubCategoryArticlesActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.showDialog("Učitavanje...");

        PullWebContent<CategoriesByID> content =
                new PullWebContent<>(CategoriesByID.class, UrlEndpoints.getRequestUrlCategoriesById(id), mVolleySingleton);
        content.setCallbackListener(new WebRequestCallbackInterface<CategoriesByID>() {
            @Override
            public void webRequestSuccess(boolean success, CategoriesByID categoriesByID) {
                if (success) {

                    Intent intent = new Intent(getApplicationContext(), SubCategoriesActivity.class);
                    intent.putExtra("Potkategorije", (Serializable) categoriesByID.getKategorije());
                    intent.putExtra("breadCrump", (Serializable) categoriesByID.getBreadCrump());
                    intent.putExtra("Title", categoryName);
                    startActivity(intent);
                    progressDialog.hideDialog();

                }
            }

            @Override
            public void webRequestError(String error) {
                progressDialog.hideDialog();
            }
        });
        content.pullList();
    }

    public int getClickedSubcategoryId() {
        return selectedSubcategoryId;
    }

    private void setClickedSubcategoryId(int id) {

        selectedSubcategoryId = id;
    }

    public void updateList(List<Article> articles) {

        Log.logInfo("SORT ACTIVITY", "" + articles.size());
        Log.logInfo("SORT ACTIVITY", "" + allSubcategoryArticles.size());
        setNumberOfResults(articles.size());
        ((ArticlesList) getFragmentManager().findFragmentById(R.id.articles_content_list)).updateFragment(articles);
        ((ArticlesGrid) getFragmentManager().findFragmentById(R.id.articles_content_grid)).updateFragment(articles);
        Log.logInfo("SORT", "" + articles.size());
        if (articles.size() == 0) {
            noSearchResults();
        } else {
            positiveResults();
        }
    }

    public void resetFilter() {

        updateList(mArticles);
    }

    public int getNumberOfResults() {
        return numberOfResults;
    }

    private void setNumberOfResults(int number) {
        numberOfResults = number;
    }

    public List<Brendovus> getBrands() {
        return mBrands;
    }

    private void setBrands(List<Brendovus> brands) {
        mBrands = brands;
    }

    public void setSelectedSpecifications(Integer key, List<Integer> values) {

        if (selectedSpecifications.containsKey(key)) {

            selectedSpecifications.remove(key);
        }

        selectedSpecifications.put(key, new ArrayList<>(values));
    }

    public HashMap<Integer, ArrayList<Integer>> getSelectedSpecification() {


        return selectedSpecifications;
    }

    public void clearSelectedSpecification() {


        selectedSpecifications = new HashMap<>();
    }

    //check if some of filter's options is selected

    public boolean isFiltered() {
        return filtered;
    }

    //set filter's state and indicator color
    public void setFiltered(boolean filtered) {
        this.filtered = filtered;
        changeFilterButtonColor();
    }

    //filter articles by selected options
    public void filterArticles(double down, double up) {

        filteredArticles = new ArrayList<>();
        filtered = true;
        Log.logInfo("SORT ALL", "" + mArticles.size());
        Log.logInfo("SORT SIZE", "" + getSelectedSpecification().size());
        List<String> selectedBrands = SharedPreferencesUtils.getArrayList(this, AppConfig.SELECTED_BRANDS_KEY);
        //price filter
        for (Article article : mArticles) {

            if (hasSpecifications(article)) {
                if (Conversions.priceStringToFloat(article.getCenaSamoBrojFormat()) >= down && Conversions.priceStringToFloat(article.getCenaSamoBrojFormat()) <= up)
                    //brands filter
                    if (selectedBrands.size() > 0) {
                        if (selectedBrands.contains(article.getBrendIme())) {
                            filteredArticles.add(article);
                        }
                    } else {
                        filteredArticles.add(article);
                    }
            }

        }
        updateList(filteredArticles);
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        SharedPreferencesUtils.clearSharedPreferences(this, AppConfig.SELECTED_BRANDS_KEY);
        SharedPreferencesUtils.clearSharedPreferences(this, AppConfig.SELECTED_PRICES_KEY);
        setFiltered(false);
    }

    @Override
    protected void onStop() {
        super.onStop();
        filteredArticles = mArticles;
        SharedPreferencesUtils.clearSharedPreferences(this, AppConfig.SELECTED_BRANDS_KEY);
        SharedPreferencesUtils.clearSharedPreferences(this, AppConfig.SELECTED_PRICES_KEY);
        setFiltered(false);
    }

    //set filter indicator color
    private void changeFilterButtonColor() {

        if (isFiltered()) {
            filterImg.setColorFilter(ContextCompat.getColor(this, R.color.primary_dark));

        } else {
            filterImg.setColorFilter(Color.BLACK);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public boolean hasSpecifications(Article article) {

        int filterItems = getSelectedSpecification().size();
        int passFilter = 0;
        boolean inc;

        if (getSelectedSpecification().size() == 0) return true;
        if (article.getSpec().size() > 0) {

            for (Map.Entry<Integer, ArrayList<Integer>> entry : getSelectedSpecification().entrySet()) {
                inc = false;

                for (ArticleSpec spec : article.getSpec()) {

                    if (spec.getIdSpecGrupe().equals(entry.getKey()) && entry.getValue().contains(spec.getIdSpecVrednosti())) {
                        passFilter++;
                        inc = true;
                    }

                    if (inc) break;
                }


            }
            if (filterItems == passFilter) return true;
        }
        return false;
    }

    private void noResults() {
        mHeader.setVisibility(View.GONE);
        mFooter.setVisibility(View.GONE);
        cardBack.setVisibility(View.GONE);
        cardFace.setVisibility(View.GONE);

        msgNoResults.setVisibility(View.VISIBLE);
        msgNoResults.setText(getString(R.string.msg_no_articles, mSubCategoryName));
    }

    private void positiveResults() {
        mHeader.setVisibility(View.VISIBLE);
        mFooter.setVisibility(View.VISIBLE);
        if (nextImgStateGrid) {
            cardBack.setVisibility(View.GONE);
            cardFace.setVisibility(View.VISIBLE);
        } else {
            cardBack.setVisibility(View.VISIBLE);
            cardFace.setVisibility(View.GONE);
        }

        msgNoResults.setVisibility(View.GONE);
    }

    private void noSearchResults() {
        mHeader.setVisibility(View.GONE);
        cardBack.setVisibility(View.GONE);
        cardFace.setVisibility(View.GONE);

        msgNoResults.setVisibility(View.VISIBLE);
        msgNoResults.setText(getString(R.string.msg_no_search_results));
    }

    private TextView addNewButton(final String subcategory, final String id) {

        RecyclerView.LayoutParams param = new RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.WRAP_CONTENT,
                RecyclerView.LayoutParams.WRAP_CONTENT);

        SpannableString content = new SpannableString(subcategory);
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);

        final TextView tv = new TextView(pathList.getContext());

        tv.setLayoutParams(param);
        tv.setPadding(2, 2, 2, 2);
        tv.setGravity(Gravity.CENTER);
        tv.setClickable(true);
        tv.setMaxLines(1);
        tv.setEllipsize(TextUtils.TruncateAt.END);

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (subcategory.equalsIgnoreCase("Sve kategorije")) {
                    Intent intent = new Intent(getApplicationContext(), AllCategoriesActivity.class);
                    // Not going to put allCategories in an intent, better get it from shared prefs
                    //intent.putExtra("SveKategorije", (Serializable) mAllCategories);
                    startActivity(intent);
                } else {
                    getCategories(Integer.parseInt(id), subcategory);
                }
            }
        });

        tv.setText(content);
        tv.setAllCaps(false);
        tv.setTextColor(ContextCompat.getColor(pathList.getContext(), R.color.primary_dark));
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
        tv.setMinHeight(10);
        tv.setMaxWidth(300);
        tv.setMinimumHeight(10);


        return tv;
    }

    private TextView addSeparator() {

        RecyclerView.LayoutParams param = new RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.WRAP_CONTENT,
                RecyclerView.LayoutParams.WRAP_CONTENT);

        final TextView tv = new TextView(pathList.getContext());

        tv.setLayoutParams(param);
        tv.setPadding(2, 2, 2, 2);
        tv.setGravity(Gravity.CENTER);
        tv.setMaxLines(1);
        tv.setEllipsize(TextUtils.TruncateAt.END);

        tv.setText(">");
        tv.setAllCaps(false);
        tv.setTextColor(ContextCompat.getColor(pathList.getContext(), R.color.btnTextColor));
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
        tv.setMinHeight(10);
        tv.setMaxWidth(300);
        tv.setMinimumHeight(10);


        return tv;
    }

    public List<BreadCrump> getBreadCrump() {
        return breadCrumpList;
    }

}




