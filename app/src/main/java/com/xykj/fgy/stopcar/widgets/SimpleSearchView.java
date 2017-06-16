package com.xykj.fgy.stopcar.widgets;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xykj.fgy.stopcar.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by gqq on 2017/3/2.
 */
// 搜索控件：组合控件
public class SimpleSearchView extends LinearLayout implements TextWatcher, TextView.OnEditorActionListener {

    @BindView(R.id.button_clear)
    ImageButton mBtnClear;
    @BindView(R.id.edit_query)
    EditText mEtQuery;
    @BindView(R.id.button_search)
    ImageButton mBtnSearch;

    private OnSearchListener mOnSearchListener;

    //代码中
    public SimpleSearchView(Context context) {
        super(context);
        init(context);
    }

    // 布局中，不设置style
    public SimpleSearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    // 布局，设置了style
    public SimpleSearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    // 视图和控件的初始化
    public void init(Context context){

        LayoutInflater.from(context).inflate(R.layout.widget_simple_search_view,this);
        ButterKnife.bind(this);

        // 设置监听
        mEtQuery.addTextChangedListener(this);
        // 设置软键盘的操作：回车变成搜索的图标
        mEtQuery.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        // 设置输入的类型：文本类型
        mEtQuery.setInputType(EditorInfo.TYPE_CLASS_TEXT);
        // 设置软键盘的操作事件监听
        mEtQuery.setOnEditorActionListener(this);

    }

    @OnClick({R.id.button_search,R.id.button_clear})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.button_clear:
                // 清除图标事件
                mEtQuery.setText(null);
                search();
                break;
            case R.id.button_search:
                // 搜索图标事件
                search();
                break;
        }
    }

    // 文本输入变化前
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    // 文本输入中
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    // 文本输入完
    @Override
    public void afterTextChanged(Editable s) {
        // 文本输入完
        String query = mEtQuery.getText().toString();
        int visible = TextUtils.isEmpty(query)? View.INVISIBLE: View.VISIBLE;
        mBtnClear.setVisibility(visible);
    }

    // 输入键盘的操作
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

        if (actionId== EditorInfo.IME_ACTION_SEARCH){
            // 搜索事件
            search();
            return true;
        }

        return false;
    }

    // 拿到字符串，然后搜索
    private void search() {
        String query = mEtQuery.getText().toString();
        if (mOnSearchListener!=null){
            mOnSearchListener.search(query);
        }
        // 关闭软键盘
        closeKeyBoard();
    }

    // 关闭软键盘
    private void closeKeyBoard() {

        // 失去焦点
        mEtQuery.clearFocus();

        // 关闭软键盘
        InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(mEtQuery.getWindowToken(),0);
    }

    // 给控件写一个设置监听的方法
    public void setOnSearchListener(OnSearchListener onSearchListener) {
        mOnSearchListener = onSearchListener;
    }

    // 为控件设置一个监听，可以让使用者实现监听
    public interface OnSearchListener{

        // 搜索的方法，具体的实现让使用者来实现
        void search(String query);
    }
}
