package com.xykj.fgy.stopcar;


import android.graphics.PixelFormat;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xykj.fgy.stopcar.fragment.GoodFragment;
import com.xykj.fgy.stopcar.fragment.MeFragment;
import com.xykj.fgy.stopcar.fragment.PagerFragment;


import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.title)
    TextView title;
    @BindViews({R.id.pager_tv, R.id.stop_tv, R.id.me_tv})
    TextView[] textViews;
    @BindView(R.id.lv)
    LinearLayout lv;
    private boolean isExit = false;
    private PagerFragment pagerFragment;
    private GoodFragment goodFragment;
    private MeFragment meFragment;
    private FragmentTransaction transaction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        ButterKnife.bind(this);

        // 初始化toolbar
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        pagerFragment = new PagerFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.ly_content, pagerFragment).commit();
    }

    //隐藏所有Fragment
    private void hideAllFragment(FragmentTransaction fragmentTransaction) {
        if (pagerFragment != null) fragmentTransaction.hide(pagerFragment);
        if (goodFragment != null) fragmentTransaction.hide(goodFragment);
        if (meFragment != null) fragmentTransaction.hide(meFragment);
    }

    @OnClick({R.id.pager_tv, R.id.stop_tv, R.id.me_tv})
    public void Onclick(TextView view) {
        transaction = getSupportFragmentManager().beginTransaction();
        hideAllFragment(transaction);
        switch (view.getId()) {
            case R.id.pager_tv:
                title.setText(textViews[0].getText());
                if (!pagerFragment.isAdded()) {
                    transaction.add(R.id.ly_content, pagerFragment);
                } else {
                    transaction.show(pagerFragment);
                }
                break;
            case R.id.stop_tv:
                title.setText(textViews[1].getText());
                if (goodFragment == null) goodFragment = new GoodFragment();
                if (!goodFragment.isAdded()) {
                    transaction.add(R.id.ly_content, goodFragment);
                } else {
                    transaction.show(goodFragment);
                }
                break;
            case R.id.me_tv:
                title.setText(textViews[2].getText());
                if (meFragment == null) {
                    meFragment = new MeFragment();
                }
                if (!meFragment.isAdded()) {
                    transaction.add(R.id.ly_content, meFragment);
                } else {
                    transaction.show(meFragment);
                }
                break;
        }
        transaction.commit();
    }

    /**
     * 连续按两次返回键就退出
     */
    private long firstTime;

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN
                && event.getRepeatCount() == 0) {
            if (System.currentTimeMillis() - firstTime < 3000) {
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(0);
                finish();

            } else {
                firstTime = System.currentTimeMillis();
                Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
            }

            return true;
        }
        return super.dispatchKeyEvent(event);
    }
}

