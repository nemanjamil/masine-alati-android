package rs.tridanwebshop.tridan;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

import github.chenupt.springindicator.SpringIndicator;
import rs.tridanwebshop.tridan.views.adapters.FullScreenImageAdapter;

public class FullScreenImageViewActivity extends AppCompatActivity {
    private ViewPager vp;
    private FullScreenImageAdapter adapter;
    private ArrayList<String> _imageUrls;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_full_screen_image_slider);
        SpringIndicator si = (SpringIndicator) findViewById(R.id.indicator);
        position = getIntent().getExtras().getInt("position");
        _imageUrls = getIntent().getExtras().getStringArrayList("urls");
        vp = (ViewPager) findViewById(R.id.vp_full_image);
        adapter = new FullScreenImageAdapter(this, _imageUrls);
        vp.setAdapter(adapter);
        vp.setCurrentItem(position);
        si.setViewPager(vp);
    }
}
