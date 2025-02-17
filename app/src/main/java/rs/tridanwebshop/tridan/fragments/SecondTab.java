package rs.tridanwebshop.tridan.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import rs.tridanwebshop.tridan.MainActivity;
import rs.tridanwebshop.tridan.OneArticleActivity;
import rs.tridanwebshop.tridan.R;
import rs.tridanwebshop.tridan.common.config.AppConfig;
import rs.tridanwebshop.tridan.common.utils.Log;
import rs.tridanwebshop.tridan.common.utils.SharedPreferencesUtils;
import rs.tridanwebshop.tridan.models.articles.Article;
import rs.tridanwebshop.tridan.models.articles.articles_filtered_by_category.ArticlesFilteredByCategory;
import rs.tridanwebshop.tridan.models.articles.products_of_the_week.Product;
import rs.tridanwebshop.tridan.models.categories.you_may_also_like_categories.YMALCategory;
import rs.tridanwebshop.tridan.models.one_article.OneArticle;
import rs.tridanwebshop.tridan.models.you_may_also_like.GridItem;
import rs.tridanwebshop.tridan.network.PullWebContent;
import rs.tridanwebshop.tridan.network.UrlEndpoints;
import rs.tridanwebshop.tridan.network.VolleySingleton;
import rs.tridanwebshop.tridan.network.WebRequestCallbackInterface;
import rs.tridanwebshop.tridan.views.adapters.GridViewAdapter;

public class SecondTab extends Fragment {
    VolleySingleton mVolleySingleton;
    private ViewGroup drop_down;
    private RelativeLayout dropdown_layout;
    private ScrollView flow_layout_scroll;
    private ImageView dropdown_image;
    private TextView dropdown_text;
    private RotateAnimation rotateUp;
    private RotateAnimation rotateDown;
    private Animation slide_down;
    private Animation slide_up;
    private View last_clicked_btn;
    private ViewGroup.LayoutParams param;
    private List<Article> mArticles = new ArrayList<>();
    private List<GridItem> mGridItems = new ArrayList<>();
    private GridView gridView;
    private GridViewAdapter gridAdapter;

    private void searchArticlesByCategory(int id, int from, int to, int sort) {
        PullWebContent<ArticlesFilteredByCategory> content = new PullWebContent<>(ArticlesFilteredByCategory.class, UrlEndpoints.getRequestUrlSearchArticlesByCategory(id, from, to, AppConfig.URL_VALUE_CURRENCY_RSD, AppConfig.URL_VALUE_LANGUAGE_SRB_LAT, sort), mVolleySingleton);
        content.setCallbackListener(new WebRequestCallbackInterface<ArticlesFilteredByCategory>() {
            @Override
            public void webRequestSuccess(boolean success, ArticlesFilteredByCategory articlesFilteredByCategory) {
                if (success) {
                    mArticles = articlesFilteredByCategory.getArtikli();
                    mGridItems.clear();
                    for (Article article: mArticles
                         ) {
                            GridItem gi = new GridItem();
                            gi.setArtikalId(article.getArtikalId());
                            gi.setArtikalNaziv(article.getArtikalNaziv());
                            gi.setSlike(article.getSlike());
                            mGridItems.add(gi);
                    }
                    gridAdapter.updateContent(mGridItems);
                }
            }

            @Override
            public void webRequestError(String error) {

            }
        });
        content.pullList();

    }

    public void setDefaultContent(){
        // get products of the week
        mArticles.clear();
        List<Product> products = SharedPreferencesUtils.getArrayListProducts(getContext(), AppConfig.THE_PRODUCTS_OF_THE_WEEK);
        mGridItems.clear();
        for (Product product: products
                ) {
            GridItem gi = new GridItem();
            gi.setArtikalId(product.getArtikalId());
            gi.setArtikalNaziv(product.getArtikalNaziv());
            gi.setSlike(product.getSlike());
            mGridItems.add(gi);
        }
//        mArticles = articlesFilteredByCategory.getArtikli();
        gridAdapter.updateContent(mGridItems);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.you_may_also_like_product, container, false);
        gridView = (GridView) mView.findViewById(R.id.gridView);
        gridAdapter = new GridViewAdapter(getContext(), R.layout.grid_item_layout, mGridItems);
        gridView.setAdapter(gridAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                int itemID = ((GridItem) gridView.getItemAtPosition(position)).getArtikalId();
                //     mArticles.get(position).getArtikalId()
                Log.logInfo("LALALA", "SUCCESS" + itemID);
                android.util.Log.d("itemID7", String.valueOf(itemID));

                PullWebContent<OneArticle> content =
                        new PullWebContent<>(OneArticle.class, UrlEndpoints.getRequestUrlArticleById(itemID), mVolleySingleton);


                Log.logInfo("LALALA", String.valueOf(itemID));
                content.setCallbackListener(new WebRequestCallbackInterface<OneArticle>() {
                    @Override
                    public void webRequestSuccess(boolean success, OneArticle oneArticle) {
                        if (success) {
                            Log.logInfo("LALALA", "SUCCESS");
                            Intent intent = new Intent(getActivity(), OneArticleActivity.class);
                            intent.putExtra(AppConfig.ABOUT_PRODUCT, oneArticle);

                            startActivity(intent);


                            Log.logInfo("LALALA", oneArticle.getArtikal().getArtikalNaziv());

                        } else {
                            Log.logInfo("LALALA", "FAILED");
                        }
                    }

                    @Override
                    public void webRequestError(String error) {

                    }
                });

                Log.logInfo("LALALA", "LIST");
                content.pullList();
            }
        });
        mVolleySingleton = VolleySingleton.getsInstance(getContext());
        mArticles = new ArrayList<>();
        mGridItems = new ArrayList<>();

        final MainActivity mainActivity = (MainActivity) getActivity();

        drop_down = (ViewGroup) mView.findViewById(R.id.flow_layout);
        flow_layout_scroll = (ScrollView) mView.findViewById(R.id.flow_layout_scroll);
        dropdown_layout = (RelativeLayout) mView.findViewById(R.id.dropdown_layout);
        dropdown_image = (ImageView) mView.findViewById(R.id.img_drop_arrow);
        dropdown_text = (TextView) mView.findViewById(R.id.txt_drop);

        flow_layout_scroll.setVisibility(ViewGroup.GONE);

        //List<Category> categories = mainActivity.getCategoriesList();
        List<YMALCategory> categories = mainActivity.getYMALCategories();

        param = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);


        //add button for showing all products
        ///Button btn = addNewButton("Svi proizvodi");
        dropdown_text.setText("Izaberite kategoriju...");
        ///btn.setSelected(true);
        //last_clicked_btn = btn;

        // creating buttons

//        for (YMALCategory category : categories
//                ) {
//            for (Child child : category.getChild()
//                    ) {
//                addNewButton(child);
//            }
//        }

        for (YMALCategory category : categories) {
            addNewButton(category);
        }

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

        slide_down = AnimationUtils.loadAnimation(container.getContext(),
                R.anim.slide_down);

        slide_up = AnimationUtils.loadAnimation(container.getContext(), R.anim.slide_up);

        dropdown_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flow_layout_scroll.getVisibility() != ViewGroup.GONE) {
                    flow_layout_scroll.startAnimation(slide_up);
                    flow_layout_scroll.setVisibility(ViewGroup.GONE);
                    dropdown_image.startAnimation(rotateDown);
                } else {
                    flow_layout_scroll.startAnimation(slide_down);
                    flow_layout_scroll.setVisibility(ViewGroup.VISIBLE);
                    dropdown_image.startAnimation(rotateUp);
                }
            }
        });

        // Set default content to be products of the week
        setDefaultContent();
        return mView;
    }

    private Button addNewButton(YMALCategory child) {
        Button btn = new Button(drop_down.getContext());
        btn.setLayoutParams(param);
        btn.setBackgroundResource(R.drawable.rounded_btn_normal);
        btn.setPadding(10, 0, 10, 0);

        btn.setText(child.getNazivKategorije());
        btn.setAllCaps(false);
        btn.setTextColor(ContextCompat.getColor(drop_down.getContext(), R.color.btnTextColor));
        btn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
        btn.setMinHeight(80);
        btn.setMinimumHeight(80);
        btn.setId(child.getKategorijaArtikalaId());
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (last_clicked_btn != null)
                    last_clicked_btn.setSelected(false);
                v.setSelected(true);
                last_clicked_btn = v;

                int id = v.getId();
                searchArticlesByCategory(id, 1, 100, 2);

                // Set selection
                dropdown_text.setText(((Button) v).getText());
                // Roll up
                flow_layout_scroll.startAnimation(slide_up);
                flow_layout_scroll.setVisibility(ViewGroup.GONE);
                dropdown_image.startAnimation(rotateDown);

            }
        });
        drop_down.addView(btn);
        return btn;
    }

}
