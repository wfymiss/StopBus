package com.xykj.fgy.stopcar.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.poisearch.PoiSearch;
import com.xykj.fgy.stopcar.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/14.
 */

public class PoiSearchAdapter extends BaseAdapter {
    private List<PoiItem> list = new ArrayList<PoiItem>();
    private Context context;


    public PoiSearchAdapter(Activity activity, List<PoiItem> poiItems) {
        this.list = poiItems;
        this.context = activity;
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public PoiItem getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.location_item, null);
            holder.location = (TextView) convertView.findViewById(R.id.location);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name.setText(list.get(position).getTitle());
        holder.location.setText(list.get(position).getSnippet());

//            Log.e("TAG_MAIN", "POI 的行政区划代码和名称=" + poiResult.getPois().get(i).getAdCode() + "," + poiResult.getPois().get(i).getAdName());
//            Log.e("TAG_MAIN", "POI的所在商圈=" + poiResult.getPois().get(i).getBusinessArea());
//            Log.e("TAG_MAIN", "POI的城市编码与名称=" + poiResult.getPois().get(i).getCityCode() + "," + poiResult.getPois().get(i).getCityName());
//            Log.e("TAG_MAIN", "POI 的经纬度=" + poiResult.getPois().get(i).getLatLonPoint());
            Log.e("TAG_MAIN", "POI的地址=" + list.get(position).getSnippet());
//            Log.e("TAG_MAIN", "POI的名称=" + poiResult.getPois().get(i).getTitle());


        return convertView;
    }

    class ViewHolder {
        TextView location, name;


    }
}
