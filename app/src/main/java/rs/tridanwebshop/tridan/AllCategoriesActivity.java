package rs.tridanwebshop.tridan;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import rs.tridanwebshop.tridan.common.config.AppConfig;
import rs.tridanwebshop.tridan.common.utils.BaseActivity;
import rs.tridanwebshop.tridan.common.utils.Log;
import rs.tridanwebshop.tridan.common.utils.SharedPreferencesUtils;
import rs.tridanwebshop.tridan.customview.StaggeredGridLayoutManagerAutoMeasure;
import rs.tridanwebshop.tridan.fragments.DeleteHistoryDialog;
import rs.tridanwebshop.tridan.models.categories.all_categories.Category;
import rs.tridanwebshop.tridan.models.categories.categories_by_id.BreadCrump;
import rs.tridanwebshop.tridan.views.adapters.RecyclerViewAllCategories;

public class AllCategoriesActivity extends BaseActivity {

    private List<Category> allCategories = new ArrayList<>();

    private ArrayList<String> mHistory;
    private ArrayList<String> mHistoryID;
    private ViewGroup historyList;

    private BottomSheetBehavior behavior;
    private int state = 0;

    private RotateAnimation rotateUp;
    private RotateAnimation rotateDown;

    private int existHistory;

    private ImageButton deleteBtn;
    private ImageView dropdown_image;
    private View bottomSheet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.all_categories_activity);
        ImageButton icSearch = (ImageButton) findViewById(R.id.toolbar_btn_search);
        icSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(intent);

            }
        });

        // Get all categories from shared prefs
        allCategories = SharedPreferencesUtils.getArrayListCategories(this, AppConfig.ALL_CATEGORIES);

        dropdown_image = (ImageView) findViewById(R.id.img_drop_arrow_history);


        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView mTextView = (TextView) findViewById(R.id.title);
        mTextView.setText("Sve kategorije");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rotateUp = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateUp.setDuration(300);
        rotateUp.setFillAfter(true);
        rotateUp.setFillEnabled(true);
        rotateUp.setInterpolator(new LinearInterpolator());

        rotateDown = new RotateAnimation(180, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateDown.setDuration(300);
        rotateDown.setFillAfter(true);
        rotateDown.setFillEnabled(true);
        rotateDown.setInterpolator(new LinearInterpolator());


        historyList = (ViewGroup) findViewById(R.id.flow_layout_history);
        deleteBtn = (ImageButton) findViewById(R.id.img_delete);

        bottomSheet = findViewById(R.id.bottom_sheet);
        bottomSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (state == 0) {
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });
        behavior = BottomSheetBehavior.from(bottomSheet);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                // React to state change
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    if (state != 1) {
                        state = 1;
                        dropdown_image.startAnimation(rotateUp);
                    }


                } else {
                    if (state != 0) {
                        state = 0;
                        dropdown_image.startAnimation(rotateDown);
                    }

                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                // React to dragging events

            }
        });

        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_all_categories);

        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(true);

        StaggeredGridLayoutManagerAutoMeasure mLayoutManager;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // use a linear layout manager
            mLayoutManager = new StaggeredGridLayoutManagerAutoMeasure(3, StaggeredGridLayoutManager.VERTICAL);
        } else {
            mLayoutManager = new StaggeredGridLayoutManagerAutoMeasure(2, StaggeredGridLayoutManager.VERTICAL);
        }
        mRecyclerView.setLayoutManager(mLayoutManager);

        RecyclerViewAllCategories mAdapter = new RecyclerViewAllCategories(this, allCategories, new RecyclerViewAllCategories.OnItemClickListener() {
            @Override
            public void onItemClick(Category item) {


                Intent intent = new Intent(getApplicationContext(), SubCategoriesActivity.class);
                intent.putExtra("Potkategorije", (Serializable) item.getChild());
                intent.putExtra("breadCrump", new ArrayList<BreadCrump>());
                intent.putExtra("Title", item.getKatsrblat());
                startActivity(intent);

            }
        });
        mRecyclerView.setAdapter(mAdapter);

        populateRecyclerView();
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

    @Override
    protected void onResume() {
        super.onResume();
        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        populateRecyclerView();

    }

    public void populateRecyclerView() {
        bottomSheet.setVisibility(View.GONE);
        historyList.removeAllViews();

        this.mHistory = SharedPreferencesUtils.getArrayList(this, AppConfig.HISTORY_KEY);
        this.mHistoryID = SharedPreferencesUtils.getArrayList(this, AppConfig.HISTORY_ID_KEY);

        existHistory = (mHistory.size() != 0) ? 1 : 0;
        Log.logInfo("HISTORY", "" + mHistory.size());

        if (existHistory == 1) {
            bottomSheet.setVisibility(View.VISIBLE);
            for (String subcategory : mHistory
                    ) {

                historyList.addView(addNewButton(subcategory, mHistoryID.get(mHistory.indexOf(subcategory))));

            }
            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    DeleteHistoryDialog dialog = new DeleteHistoryDialog();
                    dialog.setCancelable(false);
                    dialog.show(getFragmentManager(), "Dialog");

                }
            });
        }


    }

    private TextView addNewButton(final String subcategory, final String id) {

        RecyclerView.LayoutParams param = new RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.WRAP_CONTENT,
                RecyclerView.LayoutParams.WRAP_CONTENT);

        final TextView tv = new TextView(historyList.getContext());

        tv.setLayoutParams(param);
        tv.setBackgroundResource(R.drawable.history_btn);
        tv.setPadding(20, 20, 20, 20);
        tv.setGravity(Gravity.CENTER);
        tv.setClickable(true);
        tv.setMaxLines(1);
        tv.setEllipsize(TextUtils.TruncateAt.END);

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                shortcutArticles(subcategory, id);
            }
        });

        tv.setText(subcategory);
        tv.setAllCaps(false);
        tv.setTextColor(ContextCompat.getColor(historyList.getContext(), R.color.btnTextColor));
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
        tv.setMinHeight(80);
        tv.setMinimumHeight(80);


        return tv;
    }

    public void shortcutArticles(String subcategory, String id) {
        Intent intent = new Intent(getApplicationContext(), SubCategoryArticlesActivity.class);
        intent.putExtra("Artikli", subcategory);
        intent.putExtra("ArtikalId", Integer.parseInt(id));
        startActivity(intent);
    }


}
