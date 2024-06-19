package rs.tridanwebshop.tridan.customview.swipeable_layout;

/**
 * ******************************
 * Created by mirna on 8.4.2016.
 * ******************************
 */
public interface SwipeTrigger {
    void onPrepare();

    void onSwipe(int y, boolean isComplete);

    void onRelease();

    void complete();

    void onReset();
}
