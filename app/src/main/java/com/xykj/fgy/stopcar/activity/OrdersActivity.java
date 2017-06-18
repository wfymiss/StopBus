package com.xykj.fgy.stopcar.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.xykj.fgy.stopcar.R;
import com.xykj.fgy.stopcar.fragment.DoneFragment;
import com.xykj.fgy.stopcar.fragment.OrderFragment;
import com.xykj.fgy.stopcar.fragment.UndoneFragment;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OrdersActivity extends AppCompatActivity {

    @BindView(R.id.order_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.order_vp)
    ViewPager mOrderVp;
    @BindViews({R.id.done, R.id.undone, R.id.subscribe})
    TextView[] textViews;
    @BindView(R.id.order_title)
    TextView mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        init();

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) finish();
        return super.onOptionsItemSelected(item);
    }

    private void init() {
        textViews[0].setSelected(true);
        mOrderVp.setAdapter(adapter);
        mOrderVp.setCurrentItem(0);
        mOrderVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < textViews.length; i++) {
                    textViews[i].setSelected(false);

                }
                textViews[position].setSelected(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private FragmentStatePagerAdapter adapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new UndoneFragment();
                case 1:
                    return new DoneFragment();
                case 2:
                    return new OrderFragment();


            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }
    };

    //textview点击事件
    @OnClick({R.id.undone, R.id.done, R.id.subscribe})
    public void onClick(TextView view) {
        for (int i = 0; i < textViews.length; i++) {
            textViews[i].setSelected(false);
            textViews[i].setTag(i);
        }
        //设置选择效果
        view.setSelected(true);
        //参数false代表瞬间切换，而不是平滑过渡
        mOrderVp.setCurrentItem((Integer) view.getTag(), false);
    }


}
