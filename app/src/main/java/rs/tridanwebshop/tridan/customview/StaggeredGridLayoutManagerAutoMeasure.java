package rs.tridanwebshop.tridan.customview;

import android.content.Context;

import android.util.AttributeSet;

import androidx.recyclerview.widget.StaggeredGridLayoutManager;

public class StaggeredGridLayoutManagerAutoMeasure extends StaggeredGridLayoutManager {
    public StaggeredGridLayoutManagerAutoMeasure(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public StaggeredGridLayoutManagerAutoMeasure(int spanCount, int orientation) {
        super(spanCount, orientation);
    }

    @Override
    public boolean isAutoMeasureEnabled() {
        return true;
    }
}
