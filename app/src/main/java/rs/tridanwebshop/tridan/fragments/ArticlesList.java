package rs.tridanwebshop.tridan.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import rs.tridanwebshop.tridan.OneArticleActivity;
import rs.tridanwebshop.tridan.R;
import rs.tridanwebshop.tridan.SearchActivity;
import rs.tridanwebshop.tridan.SubCategoryArticlesActivity;
import rs.tridanwebshop.tridan.common.config.AppConfig;
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


public class ArticlesList extends Fragment {

    private static int firstVisibleInRecyclerView;
    private CustomRecyclerView mRecyclerView;
    private LinearLayoutManagerAutoMeasure mLayoutManager;

    private Activity activity;
    private FrameLayout mHeader;
    private RecyclerViewSelectedProducts mAdapter;

    private VolleySingleton mVolleySingleton;

    private List<Article> articles = new ArrayList<>();

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_articles, container, false);


        activity = getActivity();

        mRecyclerView = (CustomRecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setFlingFactor(1);

        mRecyclerView.setNestedScrollingEnabled(true);


        // use a linear layout manager
        mLayoutManager = new LinearLayoutManagerAutoMeasure(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        firstVisibleInRecyclerView = mLayoutManager.findFirstVisibleItemPosition();

        mVolleySingleton = VolleySingleton.getsInstance(this.getActivity());

        if (activity instanceof SubCategoryArticlesActivity) {
            articles = ((SubCategoryArticlesActivity) activity).getArticlesList();
        } else {
            articles = ((SearchActivity) activity).getArticlesList();
        }

        mAdapter = new RecyclerViewSelectedProducts(getActivity(), articles, true, 1, new RecyclerViewSelectedProducts.OnItemClickListener() {
            @Override
            public void onItemClick(Article item, View view) {
                ///Start Intent for Single Item Activity
                int itemID = item.getArtikalId();
                android.util.Log.d("itemID4", String.valueOf(itemID));

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
                            if (getActivity() instanceof SubCategoryArticlesActivity) {
                                intent.putExtra("breadCrump", (Serializable) ((SubCategoryArticlesActivity) getActivity()).getBreadCrump());
                                startActivity(intent);
                            } else {
                                ((SearchActivity) getActivity()).getBreadCrupmListByCategoryId(oneArticle.getArtikal().getKategorijaArtikalId(), intent);
                            }
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
        mRecyclerView.setAdapter(mAdapter);


        return view;
    }

    public void updateFragment(List<Article> products) {
        Log.logInfo("SORT FRAGMENT", "" + products.size());
        mAdapter.updateContent(products);
        mAdapter.notifyDataSetChanged();

    }

    public void scrollToTop() {
        mRecyclerView.scrollToPosition(0);
    }

}
