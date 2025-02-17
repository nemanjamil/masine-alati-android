package rs.tridanwebshop.tridan.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import rs.tridanwebshop.tridan.MainActivity;
import rs.tridanwebshop.tridan.R;
import rs.tridanwebshop.tridan.customview.CustomRecyclerView;
import rs.tridanwebshop.tridan.customview.LinearLayoutManagerAutoMeasure;
import rs.tridanwebshop.tridan.customview.swipeable_layout.OnLoadMoreListener;
import rs.tridanwebshop.tridan.customview.swipeable_layout.SwipeableLayout;
import rs.tridanwebshop.tridan.views.adapters.RecyclerViewAdapterFirstTab;

public class FirstTab extends Fragment implements OnLoadMoreListener {
    private SwipeableLayout mSwipeableLayout;
    private MainActivity mainActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View mView = inflater.inflate(R.layout.first_tab, container, false);
        mainActivity = (MainActivity) getActivity();

        mSwipeableLayout = (SwipeableLayout) mView.findViewById(R.id.swipeToLoadLayout);
        mSwipeableLayout.setOnLoadMoreListener(this);

        CustomRecyclerView mRecyclerView = (CustomRecyclerView) mView.findViewById(R.id.recycler_view);

        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        LinearLayoutManagerAutoMeasure mLayoutManager = new LinearLayoutManagerAutoMeasure(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        RecyclerViewAdapterFirstTab mAdapter = new RecyclerViewAdapterFirstTab(mainActivity.getFirstTabItems(), getActivity(), mainActivity.getProductsOfTheWeek(), mainActivity.getAllBrands());
        mRecyclerView.setAdapter(mAdapter);
        return mView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onLoadMore() {
        mSwipeableLayout.setLoadingMore(true);
        mSwipeableLayout.setLoadingMore(false);
        mainActivity.moveToNextTab();
    }
}
