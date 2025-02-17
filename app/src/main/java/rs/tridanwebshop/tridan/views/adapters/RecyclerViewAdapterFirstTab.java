package rs.tridanwebshop.tridan.views.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rs.tridanwebshop.tridan.MainActivity;
import rs.tridanwebshop.tridan.OffersActivity;
import rs.tridanwebshop.tridan.OneArticleActivity;
import rs.tridanwebshop.tridan.R;
import rs.tridanwebshop.tridan.common.config.AppConfig;
import rs.tridanwebshop.tridan.common.dialogs.ProgressDialogCustom;
import rs.tridanwebshop.tridan.common.utils.Log;
import rs.tridanwebshop.tridan.customview.GridLayoutManagerAutoMeasure;
import rs.tridanwebshop.tridan.customview.image_slider_with_dot_indicator.ImageSlider2Products;
import rs.tridanwebshop.tridan.customview.image_slider_with_dot_indicator.ImageSlider3Brands;
import rs.tridanwebshop.tridan.models.articles.Article;
import rs.tridanwebshop.tridan.models.articles.brands.Brand;
import rs.tridanwebshop.tridan.models.articles.products_of_the_week.Product;
import rs.tridanwebshop.tridan.models.one_article.OneArticle;
import rs.tridanwebshop.tridan.network.PullWebContent;
import rs.tridanwebshop.tridan.network.UrlEndpoints;
import rs.tridanwebshop.tridan.network.VolleySingleton;
import rs.tridanwebshop.tridan.network.WebRequestCallbackInterface;


public class RecyclerViewAdapterFirstTab extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_FOOTER = 2;

    private HashMap<String, List<Article>> items = new HashMap<>();
    private ArrayList<Product> products_of_the_week;
    private ArrayList<Brand> allBrands;

    private TextView categoryName;
    private Button viewMore;

    private ImageSlider3Brands imageViewPagerWDotIndicator_three_imgs;
    private ImageSlider2Products imageViewPagerWDotIndicator_two_imgs;

    private Context context;
    private RecyclerView mRecyclerView;

    private RelativeLayout mFirstButton;
    private RelativeLayout mSecondButton;
    private RelativeLayout mThirdButton;
    private RelativeLayout mFourthButton;
    private VolleySingleton mVolleySingleton;

    public RecyclerViewAdapterFirstTab(HashMap<String, List<Article>> items, Context context, ArrayList<Product> products_of_the_week, ArrayList<Brand> allBrands) {
        this.items = items;
        this.context = context;
        this.products_of_the_week = products_of_the_week;
        this.allBrands = allBrands;

        mVolleySingleton = VolleySingleton.getsInstance(this.context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.four_products, parent, false);

            return new MyViewHolder(itemView);
        } else if (viewType == TYPE_HEADER) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.first_tab_header, parent, false);
            return new ViewHolderHeader(itemView);
        } else if (viewType == TYPE_FOOTER) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recyclerview_footer, parent, false);
            return new ViewHolderFooter(itemView);
        }


        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        holder.setIsRecyclable(false);


        if (holder instanceof MyViewHolder) {

            categoryName.setText(AppConfig.FIRST_TAB_ITEMS[position - 1]);
            RecyclerViewSelectedProducts mAdapter = new RecyclerViewSelectedProducts(context, items.get(AppConfig.FIRST_TAB_ITEMS[position - 1]).subList(AppConfig.START_POSITION, AppConfig.NUMBER_OF_ITEMS), false, 0, new RecyclerViewSelectedProducts.OnItemClickListener() {
                @Override
                public void onItemClick(Article item, View view) {

                    //Start Intent for Single Item Activity
                    final ProgressDialogCustom progressDialog = new ProgressDialogCustom(context);
                    progressDialog.setCancelable(false);
                    progressDialog.showDialog("Učitavanje...");

                    int itemID = item.getArtikalId();
                    android.util.Log.d("itemID8", String.valueOf(itemID));

                    PullWebContent<OneArticle> content =
                            new PullWebContent<>(OneArticle.class, UrlEndpoints.getRequestUrlArticleById(itemID), mVolleySingleton);


                    Log.logInfo("LALALA", String.valueOf(itemID));
                    content.setCallbackListener(new WebRequestCallbackInterface<OneArticle>() {
                        @Override
                        public void webRequestSuccess(boolean success, OneArticle oneArticle) {
                            if (success) {

                                Log.logInfo("LALALA", "SUCCESS");
                                Intent intent = new Intent(context, OneArticleActivity.class);
                                intent.putExtra(AppConfig.ABOUT_PRODUCT, oneArticle);
//                                Log.logInfo("getArtikal", String.valueOf(oneArticle.getSuccess()));
//                                Log.logInfo("getArtikal", String.valueOf(oneArticle));
//                                Log.logInfo("getArtikal", String.valueOf(oneArticle.getArtikal().getKategorijaArtikalId()));
                                ((MainActivity) context).getBreadCrupmListByCategoryId(oneArticle.getArtikal().getKategorijaArtikalId(), intent);
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

                    Log.logInfo("LALALA", "RecyclerViewAdapterFirstTab");

                }
            });
            mRecyclerView.hasFixedSize();
            mRecyclerView.setNestedScrollingEnabled(false);
            mRecyclerView.setAdapter(mAdapter);

            viewMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                /*    viewMore.setSelected(true);
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            viewMore.setSelected(false);
                        }
                    }, 1000);*/

                    Intent intent = new Intent(context, OffersActivity.class);
                    intent.putExtra("Artikli", AppConfig.FIRST_TAB_ITEMS[holder.getAdapterPosition() - 1]);
                    intent.putExtra("AllCategories", (Serializable) items.get(AppConfig.FIRST_TAB_ITEMS[holder.getAdapterPosition() - 1]));
                    ((MainActivity) context).startActivityOneArticle(intent);

                }
            });

        } else if (holder instanceof ViewHolderHeader) {
            imageViewPagerWDotIndicator_three_imgs.setAllBrands(allBrands);
            imageViewPagerWDotIndicator_two_imgs.setProductsOfTheWeek(products_of_the_week);

            mFirstButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((MainActivity) context).info(context.getString(R.string.contact));
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            mFirstButton.setSelected(false);
                        }
                    }, 1000);

                }
            });
            mSecondButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) context).info(context.getString(R.string.how_to_buy));
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            mSecondButton.setSelected(false);
                        }
                    }, 1000);
                }
            });
            mThirdButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mThirdButton.setSelected(true);
                    ((MainActivity) context).articlesOnSale();
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            mThirdButton.setSelected(false);
                        }
                    }, 1000);
                }
            });
            mFourthButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mFourthButton.setSelected(true);
                    ((MainActivity) context).viewAllCategories();
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            mFourthButton.setSelected(false);
                        }
                    }, 1000);
                }
            });

        }

    }

    @Override
    public int getItemCount() {
        return AppConfig.FIRST_TAB_ITEMS.length + 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;
        if (isPositionFooter(position))
            return TYPE_FOOTER;

        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    private boolean isPositionFooter(int position) {
        return position == (getItemCount() - 1);
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {

        MyViewHolder(View view) {

            super(view);

            categoryName = (TextView) view.findViewById(R.id.categoryName);
            viewMore = (Button) view.findViewById(R.id.buttonViewMore);
            mRecyclerView = (RecyclerView) view.findViewById(R.id.gridView);

            int spacing = context.getResources().getDimensionPixelSize(R.dimen.recycler_view_space);
            GridLayoutManagerAutoMeasure mLayoutManager;
            if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(4, spacing, 0));
                mLayoutManager = new GridLayoutManagerAutoMeasure(context, 4, GridLayoutManager.VERTICAL, false);

            } else {
                mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(2, spacing, 0));
                mLayoutManager = new GridLayoutManagerAutoMeasure(context, 2, GridLayoutManager.VERTICAL, false);

            }

            mRecyclerView.setLayoutManager(mLayoutManager);


        }

    }

    private class ViewHolderHeader extends RecyclerView.ViewHolder {

        ViewHolderHeader(View itemView) {
            super(itemView);
            imageViewPagerWDotIndicator_three_imgs = (ImageSlider3Brands) itemView.findViewById(R.id.view_pager_dot_ind_0);
            imageViewPagerWDotIndicator_two_imgs = (ImageSlider2Products) itemView.findViewById(R.id.view_pager_dot_ind_1);

            mFirstButton = (RelativeLayout) itemView.findViewById(R.id.first_round_button);
            mSecondButton = (RelativeLayout) itemView.findViewById(R.id.second_round_button);
            mThirdButton = (RelativeLayout) itemView.findViewById(R.id.third_round_button);
            mFourthButton = (RelativeLayout) itemView.findViewById(R.id.fourth_round_button);

        }
    }

    private class ViewHolderFooter extends RecyclerView.ViewHolder {

        ViewHolderFooter(View itemView) {
            super(itemView);
        }
    }


}
