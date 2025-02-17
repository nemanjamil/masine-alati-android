package rs.tridanwebshop.tridan.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.GridLayoutManager;

import java.io.Serializable;
import java.util.List;

import rs.tridanwebshop.tridan.OneArticleActivity;
import rs.tridanwebshop.tridan.R;
import rs.tridanwebshop.tridan.SubCategoryArticlesActivity;
import rs.tridanwebshop.tridan.common.config.AppConfig;
import rs.tridanwebshop.tridan.common.utils.Log;
import rs.tridanwebshop.tridan.customview.CustomRecyclerView;
import rs.tridanwebshop.tridan.customview.GridLayoutManagerAutoMeasure;
import rs.tridanwebshop.tridan.models.articles.Article;
import rs.tridanwebshop.tridan.models.one_article.OneArticle;
import rs.tridanwebshop.tridan.network.PullWebContent;
import rs.tridanwebshop.tridan.network.UrlEndpoints;
import rs.tridanwebshop.tridan.network.VolleySingleton;
import rs.tridanwebshop.tridan.network.WebRequestCallbackInterface;
import rs.tridanwebshop.tridan.views.adapters.RecyclerViewSelectedProducts;


public class ArticlesGrid extends Fragment {

    private CustomRecyclerView mRecyclerView;
    private RecyclerViewSelectedProducts mAdapter;

    private VolleySingleton mVolleySingleton;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_articles, container, false);

        SubCategoryArticlesActivity activity = (SubCategoryArticlesActivity) getActivity();

        mRecyclerView = (CustomRecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setFlingFactor(1);
        mRecyclerView.setNestedScrollingEnabled(true);

        // use a linear layout manager
        GridLayoutManagerAutoMeasure mLayoutManager = new GridLayoutManagerAutoMeasure(getActivity(), 2);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mVolleySingleton = VolleySingleton.getsInstance(this.getActivity());

        mAdapter = new RecyclerViewSelectedProducts(getActivity(), activity.getArticlesList(), false, 1, new RecyclerViewSelectedProducts.OnItemClickListener() {
            @Override
            public void onItemClick(Article item, View view) {

                //Start Intent for Single Item Activity


                int itemID = item.getArtikalId();
                android.util.Log.d("itemID2", String.valueOf(itemID));
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
                            intent.putExtra("breadCrump", (Serializable) ((SubCategoryArticlesActivity)getActivity()).getBreadCrump());

                            //OneArticleActivity articleDetails = new OneArticleActivity();
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
                content.pullList();

                Log.logInfo("LALALA", "GRID");
            }
        });
        mLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (mAdapter.getItemViewType(position)) {
                    case RecyclerViewSelectedProducts.TYPE_HEADER:
                        return 2;
                    case RecyclerViewSelectedProducts.TYPE_ITEM:
                        return 1;
                    default:
                        return -1;
                }
            }
        });
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

    public void updateFragment(List<Article> products) {

        mAdapter.updateContent(products);
    }

    public void scrollToTop() {
        mRecyclerView.scrollToPosition(0);
    }

}
