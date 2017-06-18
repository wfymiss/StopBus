package com.xykj.fgy.stopcar.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xykj.fgy.stopcar.R;


public class MeAdapter extends BaseAdapter {
    private LayoutInflater mInflater; //得到一个LayoutInfalter对象用来导入布局

    private String txts[];
    private int images[];
    private Context context;

    public MeAdapter(Context context, String txts[], int images[]) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.txts = txts;
        this.images = images;
    }

    @Override
    public int getCount() {
        return txts.length; //返回数组的长度
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.z_my_item, null);
            holder = new ViewHolder();
            holder.name_tv = (TextView) convertView.findViewById(R.id.name_tv);
            holder.t_iv = (ImageView) convertView.findViewById(R.id.t_iv);
            convertView.setTag(holder); //绑定ViewHolder对象
        } else {
            holder = (ViewHolder) convertView.getTag(); //取出ViewHolder对象
        }
        holder.name_tv.setText(txts[position]);
        holder.t_iv.setImageResource(images[position]);

        return convertView;
    }

    @Override
    public Object getItem(int position) {

        return txts[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /*存放控件 的ViewHolder*/
    public final class ViewHolder {
        public TextView name_tv;
        public ImageView t_iv;
    }

}
