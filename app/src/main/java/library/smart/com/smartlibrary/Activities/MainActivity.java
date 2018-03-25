package library.smart.com.smartlibrary.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import library.smart.com.smartlibrary.Adapters.SliderAdapter;
import library.smart.com.smartlibrary.R;

public class MainActivity extends AppCompatActivity {

    private ViewPager mSlideViewPager;
    private LinearLayout mDotLayout;
    private SliderAdapter sliderAdapter;
    private TextView[] mDots;
    private Button back, next;
    private int mCurrentPage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        back = findViewById(R.id.back);
        next = findViewById(R.id.next);
        mSlideViewPager = findViewById(R.id.viewPager);
        mDotLayout = findViewById(R.id.dotLayout);
        sliderAdapter = new SliderAdapter(this);

        mSlideViewPager.setAdapter(sliderAdapter);
        addDotsIndicator(0);

        mSlideViewPager.addOnPageChangeListener(viewListener);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSlideViewPager.setCurrentItem(mCurrentPage + 1);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSlideViewPager.setCurrentItem(mCurrentPage - 1);
            }
        });
    }

    public void addDotsIndicator(int position) {
        mDots = new TextView[3];
        mDotLayout.removeAllViews();
        for (int i = 0; i < mDots.length; i++) {
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226;"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.colorTransparentWhite));
            mDotLayout.addView(mDots[i]);
        }
        if (mDots.length > 0) {
            mDots[position].setTextColor(getResources().getColor(R.color.colorWhite));
        }
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDotsIndicator(position);
            mCurrentPage = position;
            if (position == 0) {
                next.setEnabled(true);
                back.setEnabled(false);
                back.setVisibility(View.INVISIBLE);
                next.setText("Next");
                back.setText("");
            } else if (position == mDots.length - 1) {
                next.setEnabled(true);
                back.setEnabled(true);
                back.setVisibility(View.VISIBLE);
                next.setText("Finish");
                next.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                        Intent intent = new Intent(MainActivity.this, LoginMethod.class);
                        startActivity(intent);

                    }
                });
                back.setText("Back");
            } else {
                next.setEnabled(true);
                back.setEnabled(true);
                back.setVisibility(View.VISIBLE);
                next.setText("Next");
                back.setText("Back");
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}
