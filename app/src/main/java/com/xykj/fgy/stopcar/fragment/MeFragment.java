package com.xykj.fgy.stopcar.fragment;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.pkmmte.view.CircularImageView;
import com.xykj.fgy.stopcar.R;
import com.xykj.fgy.stopcar.adapter.MeAdapter;
import com.xykj.fgy.stopcar.widgets.CommomDialog;
import com.xykj.fgy.stopcar.widgets.IconSelectWindow;

import org.hybridsquad.android.library.CropHandler;
import org.hybridsquad.android.library.CropHelper;
import org.hybridsquad.android.library.CropParams;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MeFragment extends Fragment {


    @BindView(R.id.listview)
    ListView listview;
    @BindView(R.id.btnexit)
    Button btnexit;
    @BindView(R.id.CircularImageView)
    CircularImageView circularImageView;
    @BindView(R.id.username)
    TextView username;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_me, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 更新头像
        circularImageView.setOnClickListener(new View.OnClickListener() {

            private IconSelectWindow mSelectWindow;

            @Override
            public void onClick(View v) {
                // 点击头像会弹出一个选择的PopupWindow
                if (mSelectWindow == null) {
                    mSelectWindow = new IconSelectWindow(getActivity(),listener);
                }
                if (mSelectWindow.isShowing()) {
                    mSelectWindow.dismiss();
                    return;
                }
                mSelectWindow.show();
            }
        });
        String txts[] = {
                "账号管理", "我的车辆", "停车记录", "预约记录", "消息中心", "意见反馈"
        };
        int images[] = {
                R.mipmap.wdzl_icon, R.mipmap.wddp_icon, R.mipmap.fsyq_icon,
                R.mipmap.jcgx_icon, R.mipmap.fsyq_icon, R.mipmap.wdzl_icon};
        MeAdapter meAdapter = new MeAdapter(getActivity(), txts, images);
        listview.setAdapter(meAdapter);

    }

    @OnClick(R.id.btnexit)
    public void Onclick() {
        CommomDialog commomDialog = new CommomDialog(getContext(), R.style.dialog, "您确定删除此信息？", new CommomDialog.OnCloseListener() {
            @Override
            public void onClick(Dialog dialog, boolean confirm) {
                if (confirm == true) {
                    dialog.dismiss();
                    getActivity().finish();
                } else {
                    dialog.dismiss();
                }


            }
        });
        commomDialog.setTitle("提示").show();
    }

    private IconSelectWindow.Listener listener = new IconSelectWindow.Listener() {
        @Override
        public void toGallery() {

            // 清除上一次剪切的图片的缓存
            CropHelper.clearCachedCropFile(cropHandler.getCropParams().uri);

            // 到相册
            Intent intent = CropHelper.buildCropFromGalleryIntent(cropHandler.getCropParams());
            startActivityForResult(intent, CropHelper.REQUEST_CROP);
        }

        @Override
        public void toCamera() {
            // 清除上一次剪切的图片的缓存
            CropHelper.clearCachedCropFile(cropHandler.getCropParams().uri);

            // 到相机
            Intent intent = CropHelper.buildCaptureIntent(cropHandler.getCropParams().uri);
            startActivityForResult(intent, CropHelper.REQUEST_CAMERA);
        }
    };
    // 图片处理
    private CropHandler cropHandler = new CropHandler() {

        // 图片剪切完以后结果
        @Override
        public void onPhotoCropped(Uri uri) {
            // 拿到剪切完成以后的图片文件
            File file = new File(uri.getPath());
            // TODO: 2017/6/18   要进行将图片上传到服务器：头像上传、更新一下

        }

        // 取消
        @Override
        public void onCropCancel() {
            Toast.makeText(getContext(), "取消", Toast.LENGTH_SHORT).show();
        }

        // 失败
        @Override
        public void onCropFailed(String message) {
            Toast.makeText(getContext(), "失败", Toast.LENGTH_SHORT).show();

        }

        // 剪切的选项设置：Uri(剪切图片保存的路径)
        @Override
        public CropParams getCropParams() {
            CropParams cropParams = new CropParams();
            return cropParams;
        }

        // 拿到上下文
        @Override
        public Activity getContext() {
            return getActivity();
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}