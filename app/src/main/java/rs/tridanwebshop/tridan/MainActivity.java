package rs.tridanwebshop.tridan;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;


import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rs.tridanwebshop.tridan.common.application.MyApplication;
import rs.tridanwebshop.tridan.common.application.SessionManager;
import rs.tridanwebshop.tridan.common.config.AppConfig;
import rs.tridanwebshop.tridan.common.dialogs.ProgressDialogCustom;
import rs.tridanwebshop.tridan.common.utils.SharedPreferencesUtils;
import rs.tridanwebshop.tridan.fcm.Config;
import rs.tridanwebshop.tridan.fcm.MyPreferenceManager;
import rs.tridanwebshop.tridan.models.User;
import rs.tridanwebshop.tridan.models.articles.Article;
import rs.tridanwebshop.tridan.models.articles.brands.Brand;
import rs.tridanwebshop.tridan.models.articles.products_of_the_week.Product;
import rs.tridanwebshop.tridan.models.categories.all_categories.Category;
import rs.tridanwebshop.tridan.models.categories.categories_by_id.BreadCrupmByID;
import rs.tridanwebshop.tridan.models.categories.you_may_also_like_categories.YMALCategory;
import rs.tridanwebshop.tridan.models.one_article.OneArticle;
import rs.tridanwebshop.tridan.network.PullWebContent;
import rs.tridanwebshop.tridan.network.UrlEndpoints;
import rs.tridanwebshop.tridan.network.VolleySingleton;
import rs.tridanwebshop.tridan.network.WebRequestCallbackInterface;
import rs.tridanwebshop.tridan.signin.AccountActivity;
import rs.tridanwebshop.tridan.views.adapters.ViewPagerAdapter;

public class MainActivity extends FragmentActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";

    AppBarLayout mAppBar;

    TabLayout mTabLayout;
    private List<Category> mAllCategories = new ArrayList<>();
    private ViewPager mViewPager;
    private Intent intent;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private SessionManager session;

    private VolleySingleton mVolleySingleton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        // ImageButton icMore = (ImageButton) findViewById(R.id.toolbar_ic_more);

        updateCartToolbarIcon();
        ImageButton icSearch = (ImageButton) findViewById(R.id.toolbar_btn_search);
        icSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(intent);

            }
        });

        //  icMore.setVisibility(View.GONE);

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                session = MyApplication.getInstance().getSessionManager();
                if (session.isLoggedIn()) {
                    setUserDrawerInfo();
                } else {
                    clearUserDrawerInfo();
                }
            }

        };
        drawer.addDrawerListener(toggle);

        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        // Prepare navigation drawer menu notifications enable
        MenuItem notifications_en = navigationView.getMenu().getItem(3);
        MyPreferenceManager prefs = MyApplication.getInstance().getPrefManager();
        if (!prefs.getNotificationsEnabled()) {
            notifications_en.setTitle(R.string.enable_push_notifications);
            notifications_en.setIcon(R.drawable.ic_nav_notifications_off);
        } else {
            notifications_en.setTitle(R.string.disable_push_notifications);
            notifications_en.setIcon(R.drawable.ic_nav_notifications_on);
        }

        navigationView.setNavigationItemSelectedListener(this);

        initializeTabs();

        mAppBar = (AppBarLayout) findViewById(R.id.appBar);


        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Config.SET_USER_INFO)) {
                    setUserDrawerInfo();
                } else if (intent.getAction().equals(Config.CLEAR_USER_INFO)) {
                    clearUserDrawerInfo();
                } else if (intent.getAction().equals(Config.UPDATE_CART_TOOLBAR_ICON)) {
                    updateCartToolbarIcon();
                } else if (intent.getAction().equals(Config.SHOW_ARTICLE_DETAILS)) {
                    Log.e(TAG, "SHOW ARTICLE DETAILS");
                    int articleID = intent.getIntExtra("show_article", 0);
                    viewArticle(articleID);
                }
            }
        };

        mVolleySingleton = VolleySingleton.getsInstance(this);

        // Handle possible data accompanying notification message.
        // [START handle_data_extras]
        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                String value = getIntent().getExtras().getString(key);
                // let's see what's inside
                Log.e("MainActivity", "Key: " + key + " Value: " + value);
            }
        }

        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey("articleID")) {
            int articleID = Integer.valueOf(intent.getExtras().getString("articleID"));
            viewArticle(articleID);
        }
        // [END handle_data_extras]


        // [START subscribe_topics]
        FirebaseMessaging.getInstance().subscribeToTopic(BuildConfig.TOPIC);
        Log.e(TAG, "Subscribed to " + BuildConfig.TOPIC + " topic");
        // [END subscribe_topics]

//        Log.e(TAG, "InstanceID token: " + FirebaseInstanceId.getInstance().getToken());

        // register receivers
        registerReceivers();

    }

    private void updateCartToolbarIcon() {
        // Display badge over cart icon if there are some items in the cart
        ImageButton icCart = (ImageButton) findViewById(R.id.toolbar_btn_cart);
        TextView tvItemCount = (TextView) findViewById(R.id.badge_textView);
        session = MyApplication.getInstance().getSessionManager();
        int itemCount = session.getCartItemCount();
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


    private void registerReceivers() {
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.SET_USER_INFO));

        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.CLEAR_USER_INFO));

        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.UPDATE_CART_TOOLBAR_ICON));

        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.SHOW_ARTICLE_DETAILS));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onDestroy();
    }

    private void setUserDrawerInfo() {
        // Update user email, name and photo
        User user = MyApplication.getInstance().getPrefManager().getUser();
        TextView user_email = (TextView) findViewById(R.id.id_user_email);
        if (user_email != null)
            user_email.setText(user.getEmail());
        TextView tv_user_name = (TextView) findViewById(R.id.id_user_name);
        if (tv_user_name != null) {
            tv_user_name.setText(user.getName());
            tv_user_name.setOnClickListener(null);
        }
        ImageView user_photo = (ImageView) findViewById(R.id.id_user_photo);
        if (user_photo != null)
            user_photo.setImageURI(user.getPhoto());
    }

    private void clearUserDrawerInfo() {
        // Update user email, name and photo

        TextView user_email = (TextView) findViewById(R.id.id_user_email);
        if (user_email != null)
            user_email.setText("");
        TextView tv_user_name_unavailable = (TextView) findViewById(R.id.id_user_name);
        if (tv_user_name_unavailable != null) {
            tv_user_name_unavailable.setText(getString(R.string.user_name_unavailable));
            tv_user_name_unavailable.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle the login action
                    Intent intent = new Intent(getApplicationContext(), AccountActivity.class);
                    startActivity(intent);
                    // close drawer
                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(GravityCompat.START);
                }
            });
        }
        ImageView user_photo = (ImageView) findViewById(R.id.id_user_photo);
        if (user_photo != null)
            user_photo.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.chainsaw_128));

    }

    public void initializeTabs() {
        intent = getIntent();
        getIntentExtras();

        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mTabLayout.addTab(mTabLayout.newTab().setText(getResources().getString(R.string.first_tab)));
        mTabLayout.addTab(mTabLayout.newTab().setText(getResources().getString(R.string.second_tab)));
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        final ViewPagerAdapter adapter = new ViewPagerAdapter
                (getSupportFragmentManager());
        mViewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
                if (tab.getPosition() == 0) {
                    mAppBar.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.primary_dark));
                } else if (tab.getPosition() == 1) {
                    mAppBar.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.primary));
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_account) {
            // Handle the login action
            Intent intent = new Intent(this, AccountActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_home) {

        } else if (id == R.id.nav_chart) {
            Intent intent = new Intent(getApplicationContext(), CartActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_best) {
            Intent intent = new Intent(this, OffersActivity.class);
            intent.putExtra("Artikli", AppConfig.FIRST_TAB_ITEMS[2]);
            intent.putExtra("AllCategories", (Serializable) getBestSellingProducts());
            startActivityOneArticle(intent);

        } else if (id == R.id.nav_new) {

            Intent intent = new Intent(this, OffersActivity.class);
            intent.putExtra("Artikli", AppConfig.FIRST_TAB_ITEMS[1]);
            intent.putExtra("AllCategories", (Serializable) getNewProducts());
            startActivityOneArticle(intent);


        } else if (id == R.id.nav_sale) {
            articlesOnSale();

        } else if (id == R.id.nav_how_to_buy) {
            info(getString(R.string.how_to_buy));

        } else if (id == R.id.nav_help) {
            Intent intent = new Intent(this, QuestionActivity.class);
            startActivityOneArticle(intent);

        } else if (id == R.id.nav_contact) {
            info(getString(R.string.contact));

        } else if (id == R.id.nav_notifications) {
            MyPreferenceManager prefs = MyApplication.getInstance().getPrefManager();
            boolean isEn = !prefs.getNotificationsEnabled();
            prefs.setNotificationsEnabled(isEn);
            if (!isEn) {
                item.setTitle(R.string.enable_push_notifications);
                item.setIcon(R.drawable.ic_nav_notifications_off);
                Snackbar.make(findViewById(android.R.id.content), "Notifikacije isključene.", Snackbar.LENGTH_SHORT).show();
            } else {
                item.setTitle(R.string.disable_push_notifications);
                item.setIcon(R.drawable.ic_nav_notifications_on);
                Snackbar.make(findViewById(android.R.id.content), "Notifikacije uključene.", Snackbar.LENGTH_SHORT).show();
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public List<Category> getCategoriesList() {
        // mAllCategories = (List<Category>) intent.getSerializableExtra("AllCategories");
        mAllCategories = SharedPreferencesUtils.getArrayListCategories(this, AppConfig.ALL_CATEGORIES);
        return mAllCategories;
    }

    public List<Article> getProductsOnSale() {
        List<Article> mProductsOnSale;
        mProductsOnSale = SharedPreferencesUtils.getArrayListArticle(this, AppConfig.SALE);
        return mProductsOnSale;
    }

    public List<Article> getNewProducts() {
        List<Article> mNewProducts;
        mNewProducts = SharedPreferencesUtils.getArrayListArticle(this, AppConfig.NEW);
        return mNewProducts;
    }

    public List<Article> getBestSellingProducts() {
        List<Article> mBestSelling;
        mBestSelling = SharedPreferencesUtils.getArrayListArticle(this, AppConfig.BEST);
        return mBestSelling;
    }

    public ArrayList<Product> getProductsOfTheWeek() {
        List<Product> products;
        products = SharedPreferencesUtils.getArrayListProducts(this, AppConfig.THE_PRODUCTS_OF_THE_WEEK);
        return new ArrayList<>(products);
    }

    public ArrayList<Brand> getAllBrands() {
        List<Brand> brands;
        brands = SharedPreferencesUtils.getArrayListBrands(this, AppConfig.ALL_BRANDS);

        return new ArrayList<>(brands);
    }

    public List<YMALCategory> getYMALCategories() {
        List<YMALCategory> ymalCategories;
        ymalCategories = SharedPreferencesUtils.getArrayListYAML(this, AppConfig.YOU_MAY_ALSO_LIKE_CATEGORIES);
        return ymalCategories;
    }

    private void getIntentExtras() {

        getCategoriesList();

    }

    public HashMap<String, List<Article>> getFirstTabItems() {

        HashMap<String, List<Article>> productsOnSale = new HashMap<>();
        productsOnSale.put(AppConfig.FIRST_TAB_ITEMS[0], getProductsOnSale());
        productsOnSale.put(AppConfig.FIRST_TAB_ITEMS[1], getNewProducts());
        productsOnSale.put(AppConfig.FIRST_TAB_ITEMS[2], getBestSellingProducts());
        return productsOnSale;

    }

    public void moveToNextTab() {

        mViewPager.setCurrentItem(mTabLayout.getSelectedTabPosition() + 1);

    }

    public void viewAllCategories() {

        intent = new Intent(getApplicationContext(), AllCategoriesActivity.class);
        // Not going to put allCategories in an intent, better get it from shared prefs
        //intent.putExtra("SveKategorije", (Serializable) mAllCategories);
        startActivity(intent);
    }

    public void info(String title) {

        intent = new Intent(this, InfoActivity.class);
        intent.putExtra("infoTip", title);
        startActivity(intent);
    }

    public void articlesOnSale() {
        Log.d("itemID", String.valueOf("itemID"));

        Intent intent = new Intent(this, OffersActivity.class);
        intent.putExtra("Artikli", AppConfig.FIRST_TAB_ITEMS[0]);
        intent.putExtra("AllCategories", (Serializable) getProductsOnSale());
        startActivityOneArticle(intent);
    }


    public void startActivityOneArticle(Intent intent) {
        startActivity(intent);
    }

    public void viewArticle(int itemID) {

        final ProgressDialogCustom progressDialog = new ProgressDialogCustom(MainActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.showDialog("Učitavanje...");

        Log.d("itemID11", String.valueOf(itemID));
        PullWebContent<OneArticle> content =
                new PullWebContent<>(OneArticle.class, UrlEndpoints.getRequestUrlArticleById(itemID), mVolleySingleton);

        content.setCallbackListener(new WebRequestCallbackInterface<OneArticle>() {
            @Override
            public void webRequestSuccess(boolean success, OneArticle oneArticle) {
                if (success) {
                    Intent intent = new Intent(getApplicationContext(), OneArticleActivity.class);
                    intent.putExtra(AppConfig.ABOUT_PRODUCT, oneArticle);
                    getBreadCrupmListByCategoryId(oneArticle.getArtikal().getKategorijaArtikalId(), intent);
                    progressDialog.hideDialog();
                } else {
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

    public void getBreadCrupmListByCategoryId(int id, final Intent intent) {


        PullWebContent<BreadCrupmByID> content = new PullWebContent<>(BreadCrupmByID.class, UrlEndpoints.getBreadCrump(id), mVolleySingleton);
        content.setCallbackListener(new WebRequestCallbackInterface<BreadCrupmByID>() {
            @Override
            public void webRequestSuccess(boolean success, BreadCrupmByID breadCrumpList) {
                if (success) {

                    intent.putExtra("breadCrump", (Serializable) breadCrumpList.getBreadCrump());
                    startActivity(intent);

                }
            }


            @Override
            public void webRequestError(String error) {

            }
        });
        content.pullList();

    }
}
