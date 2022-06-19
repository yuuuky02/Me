package com.example.meee;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {

    ArrayList<ListViewData> list = new ArrayList<ListViewData>();

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final Context context = viewGroup.getContext();
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_list, viewGroup, false);
        }

        TextView tvlist1 = (TextView) view.findViewById(R.id.tvlist1);
        TextView tvlist2 = (TextView) view.findViewById(R.id.tvlist2);
        TextView tvlist3 = (TextView) view.findViewById(R.id.tvlist3);
        ListViewData listdata = list.get(i);
        tvlist1.setText("카테고리 : " + listdata.getCategory());
        tvlist2.setText("내용 : " + listdata.getContent());
        tvlist3.setText("주소 : " + listdata.getAddress());
        return view;
    }
    
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void addItemToList(int id, String date, String category, String content,
                              String address, byte[] camera, byte[] album, String emotion){
        
        ListViewData listdata = new ListViewData();
        listdata.setId(id);
        listdata.setDate(date);
        listdata.setCategory(category);
        listdata.setContent(content);
        listdata.setAddress(address);
        listdata.setEmotion(emotion);

        list.add(listdata);
    }
    
    
}
