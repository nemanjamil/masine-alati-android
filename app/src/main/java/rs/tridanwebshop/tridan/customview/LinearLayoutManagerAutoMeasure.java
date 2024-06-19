package rs.tridanwebshop.tridan.customview;

import android.content.Context;
import android.util.AttributeSet;

import androidx.recyclerview.widget.LinearLayoutManager;

public class LinearLayoutManagerAutoMeasure extends LinearLayoutManager {

    public LinearLayoutManagerAutoMeasure(Context context) {
        super(context);
    }

    public LinearLayoutManagerAutoMeasure(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public LinearLayoutManagerAutoMeasure(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean isAutoMeasureEnabled() {
        return true;
    }
}
