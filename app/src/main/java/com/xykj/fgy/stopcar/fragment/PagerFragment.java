package com.xykj.fgy.stopcar.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.xykj.fgy.stopcar.R;
import com.xykj.fgy.stopcar.widgets.BannerLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class PagerFragment extends Fragment {


    @BindView(R.id.banner)
    BannerLayout banner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pager, container, false);
        ButterKnife.bind(this, view);
        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final List<Integer> urls = new ArrayList<Integer>();
        urls.add(R.mipmap.banner1);
        urls.add(R.mipmap.banner2);
        urls.add(R.mipmap.banner3);
//        urls.add("http://img3.imgtn.bdimg.com/it/u=2674591031,2960331950&fm=23&gp=0.jpg");
        banner.setImageLoader(new GlideImageLoader());
        //bannerLayout.setViewUrls(urls);
        banner.setViewRes(urls);
    }

    private class GlideImageLoader implements BannerLayout.ImageLoader {
        @Override
        public void displayImage(Context context, String path, ImageView imageView) {
            Glide.with(context).load(path).centerCrop().into(imageView);
        }
    }
}
