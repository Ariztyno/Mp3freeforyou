package com.example.mp3freeforyou.Adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.cursoradapter.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mp3freeforyou.R;
import com.example.mp3freeforyou.Ultils.PreferenceUtils;

import java.util.ArrayList;
import java.util.List;

import static com.example.mp3freeforyou.Fragment.Fragment_Tim_Kiem.searchView;

public class SearchHistoryAdapter extends CursorAdapter{
    private List<String> items;

    public SearchHistoryAdapter(Context context, Cursor cursor, List<String> items) {

        super(context, cursor, false);

        this.items =  items;

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.row_searchhistory, parent, false);

        TextView text;
        ImageButton imgbtnDelete;

        text = view.findViewById(R.id.txtHistoryQuery);
        imgbtnDelete = view.findViewById(R.id.btnRemoveFromHistory);
        if(items.get(cursor.getPosition()).equals("")){
            view.setVisibility(View.GONE);
        }else{
            //nếu chuỗi có  "bh_"
            if(items.get(cursor.getPosition()).contains("bh_")){
                text.setCompoundDrawablesWithIntrinsicBounds(R.drawable.iconsongnote_20,0,0,0);
                text.setText(items.get(cursor.getPosition()).substring(3));
                imgbtnDelete.setClickable(false);
                imgbtnDelete.setVisibility(View.INVISIBLE);
            }else{
                if(items.get(cursor.getPosition()).contains("cs_")){
                    text.setCompoundDrawablesWithIntrinsicBounds(R.drawable.iconmicro_20,0,0,0);
                    imgbtnDelete.setClickable(false);
                    imgbtnDelete.setVisibility(View.INVISIBLE);
                }else{
                    if(items.get(cursor.getPosition()).contains("ab_")){
                        text.setCompoundDrawablesWithIntrinsicBounds(R.drawable.iconalbum_20,0,0,0);
                        text.setText(items.get(cursor.getPosition()).substring(3));
                        imgbtnDelete.setClickable(false);
                        imgbtnDelete.setVisibility(View.INVISIBLE);
                    }else {
                        if(items.get(cursor.getPosition()).contains("pl_")){
                            text.setCompoundDrawablesWithIntrinsicBounds(R.drawable.iconplaylist_20,0,0,0);
                            text.setText(items.get(cursor.getPosition()).substring(3));
                            imgbtnDelete.setClickable(false);
                            imgbtnDelete.setVisibility(View.INVISIBLE);
                        }else {
                            text.setCompoundDrawablesWithIntrinsicBounds(R.drawable.iconclock_grey_20,0,0,0);
                            text.setText(items.get(cursor.getPosition()));
                            imgbtnDelete.setClickable(true);
                        }
                    }
                }
            }
        }


        /*imgbtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                items.remove(cursor.getPosition());
                //lưu lại danh sách sau khi remove phần tử đã chọn
                if(items.size()==0){
                    PreferenceUtils.saveSearchHistory("",context);
                }else{
                    PreferenceUtils.saveSearchHistory(items,context);
                }
                //ẩn nó đi lại trang khi bỏ thích
                view.setVisibility(View.GONE);
            }
        });*/

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setQuery(cursor.getString(cursor.getColumnIndex("text")),false);
                Log.d("svSuggestOnclickAdapter",items.get(cursor.getPosition())+":"+cursor.getString(cursor.getColumnIndex("text")));
            }
        });

    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    /*
    public SearchHistoryAdapter(Context context, Cursor cursor, List<String> items) {

        super(context, cursor, false);

        this.items = items;

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.row_searchhistory, parent, false);

        TextView text;
        ImageButton imgbtnDelete;

        text = view.findViewById(R.id.txtHistoryQuery);
        imgbtnDelete = view.findViewById(R.id.btnRemoveFromHistory);
        if(items.get(cursor.getPosition()).equals("")){
            view.setVisibility(View.GONE);
        }else{
            //nếu chuỗi có  "bh_"
            if(items.get(cursor.getPosition()).contains("bh_")){
                text.setCompoundDrawablesWithIntrinsicBounds(R.drawable.iconsongnote_20,0,0,0);
                text.setText(items.get(cursor.getPosition()).substring(3));
                imgbtnDelete.setClickable(false);
                imgbtnDelete.setVisibility(View.INVISIBLE);
            }else{
                if(items.get(cursor.getPosition()).contains("cs_")){
                    text.setCompoundDrawablesWithIntrinsicBounds(R.drawable.iconmicro_20,0,0,0);
                    imgbtnDelete.setClickable(false);
                    imgbtnDelete.setVisibility(View.INVISIBLE);
                }else{
                    if(items.get(cursor.getPosition()).contains("ab_")){
                        text.setCompoundDrawablesWithIntrinsicBounds(R.drawable.iconalbum_20,0,0,0);
                        text.setText(items.get(cursor.getPosition()).substring(3));
                        imgbtnDelete.setClickable(false);
                        imgbtnDelete.setVisibility(View.INVISIBLE);
                    }else {
                        if(items.get(cursor.getPosition()).contains("pl_")){
                            text.setCompoundDrawablesWithIntrinsicBounds(R.drawable.iconplaylist_20,0,0,0);
                            text.setText(items.get(cursor.getPosition()).substring(3));
                            imgbtnDelete.setClickable(false);
                            imgbtnDelete.setVisibility(View.INVISIBLE);
                        }else {
                            text.setCompoundDrawablesWithIntrinsicBounds(R.drawable.iconclock_grey_20,0,0,0);
                            text.setText(items.get(cursor.getPosition()));
                            imgbtnDelete.setClickable(true);
                        }
                    }
                }
            }
        }


        imgbtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                items.remove(cursor.getPosition());
                //lưu lại danh sách sau khi remove phần tử đã chọn
                if(items.size()==0){
                    PreferenceUtils.saveSearchHistory("",context);
                }else{
                    PreferenceUtils.saveSearchHistory(items,context);
                }
                //ẩn nó đi lại trang khi bỏ thích
                view.setVisibility(View.GONE);
            }
        });
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

    }*/


    /*Context context;
    ArrayList<String> mangtimkiem;

    Handler handler=new Handler();
    Runnable runnable;

    public SearchHistoryAdapter(Context context, ArrayList<String> mangtimkiem) {
        this.context = context;
        this.mangtimkiem = mangtimkiem;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.row_searchhistory,parent,false);
        return new SearchHistoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String item=mangtimkiem.get(position);
        holder.txtQuery.setText(item);
    }

    @Override
    public int getItemCount() {
        return mangtimkiem.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtQuery;
        ImageButton imgbtnRevmove;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtQuery=itemView.findViewById(R.id.txtHistoryQuery);
            imgbtnRevmove=itemView.findViewById(R.id.btnRemoveFromHistory);

            //nút xóa query
            imgbtnRevmove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mangtimkiem.remove(getLayoutPosition());
                    //lưu lại danh sách sau khi remove phần tử đã chọn
                    PreferenceUtils.saveSearchHistory(mangtimkiem,context);
                    //ẩn nó đi lại trang khi bỏ thích
                    itemView.setVisibility(View.GONE);
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }*/
}
