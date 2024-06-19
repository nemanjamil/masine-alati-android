package rs.tridanwebshop.tridan.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import rs.tridanwebshop.tridan.OneArticleActivity;
import rs.tridanwebshop.tridan.R;

public class OneArticleTabThree extends Fragment {

    public OneArticleTabThree() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View mView = inflater.inflate(R.layout.one_article_specification, container, false);
        OneArticleActivity mActivity = (OneArticleActivity) getActivity();

        LinearLayout linearLayout = (LinearLayout) mView.findViewById(R.id.linearLayoutSpecification);

        if (mActivity.getArticleSpecification() != null && mActivity.getArticleSpecification().size() > 0) {
            for (int i = 0; i < mActivity.getArticleSpecification().size(); i++) {
                TextView textView = new TextView(getContext());
                textView.setTextColor(Color.BLACK);
                textView.setText(getString(R.string.article_specification, mActivity.getArticleSpecification().get(i).getImeSpecGrupe(), mActivity.getArticleSpecification().get(i).getVredSpecGrupe()));
                linearLayout.addView(textView);
            }
        } else {
            TextView textView = new TextView(getContext());
            textView.setText(getString(R.string.no_spec));
            textView.setTextColor(Color.BLACK);
            linearLayout.addView(textView);
        }

        return mView;
    }

}
