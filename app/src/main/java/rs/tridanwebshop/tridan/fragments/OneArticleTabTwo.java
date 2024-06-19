package rs.tridanwebshop.tridan.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import rs.tridanwebshop.tridan.OneArticleActivity;
import rs.tridanwebshop.tridan.R;
import rs.tridanwebshop.tridan.common.utils.Log;
import rs.tridanwebshop.tridan.common.utils.SharedPreferencesUtils;

public class OneArticleTabTwo extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View mView = inflater.inflate(R.layout.web_view, container, false);
        OneArticleActivity mActivity = (OneArticleActivity) getActivity();


        byte[] data = Base64.decode(SharedPreferencesUtils.getString(getContext(), "INFO_HOW_TO_BUY"), Base64.DEFAULT);

        LinearLayout.LayoutParams webViewParams = new

                LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

       WebView mWebViewTab = (WebView) mView.findViewById(R.id.mWebViewTab);
       /*  if (Build.VERSION.SDK_INT >= 19) {
            mWebViewTab.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            mWebViewTab.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }*/
        mWebViewTab.setLayoutParams(webViewParams);


        mWebViewTab.getSettings().setJavaScriptEnabled(true);

        mWebViewTab.loadDataWithBaseURL(null, new String(data), "text/html", "UTF-8", null);

        //  mWebViewTab.loadDataWithBaseURL(null, mActivity.opis(), "text/html", "utf-8", null);
        // mWebViewTab.requestLayout();

        Log.logInfo("LALALA.........", "jjj>" + mActivity.opis().length());
        return mView;
    }

}
