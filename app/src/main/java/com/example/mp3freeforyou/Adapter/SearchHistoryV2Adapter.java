package com.example.mp3freeforyou.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mp3freeforyou.R;
import com.example.mp3freeforyou.Ultils.PreferenceUtils;

import java.util.ArrayList;
import java.util.Arrays;

import static com.example.mp3freeforyou.Fragment.Fragment_Tim_Kiem.searchView;

public class SearchHistoryV2Adapter extends RecyclerView.Adapter<SearchHistoryV2Adapter.ViewHolder> {
    ArrayList<String> ListHistoryString;

    public SearchHistoryV2Adapter(ArrayList<String> listHistoryString, Context context) {
        ListHistoryString = listHistoryString;
        this.context = context;
    }

    Context context;

    @NonNull
    @Override
    public SearchHistoryV2Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.row_searchhistory,parent,false);
        return new SearchHistoryV2Adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchHistoryV2Adapter.ViewHolder holder, int position) {
        String item=ListHistoryString.get(position);
        //nếu chuỗi có  "bh_"
        if(ListHistoryString.get(position).contains("bh_")){
            holder.txtQuery.setCompoundDrawablesWithIntrinsicBounds(R.drawable.iconsongnote_20,0,0,0);
            holder.txtQuery.setText(item.substring(3));
            holder.imgbtnRevmove.setClickable(false);
            holder.imgbtnRevmove.setVisibility(View.GONE);
        }else{
            if(ListHistoryString.get(position).contains("cs_")){
                holder.txtQuery.setCompoundDrawablesWithIntrinsicBounds(R.drawable.iconmicro_20,0,0,0);
                holder.txtQuery.setText(item.substring(3));
                holder.imgbtnRevmove.setClickable(false);
                holder.imgbtnRevmove.setVisibility(View.GONE);
            }else{
                if(ListHistoryString.get(position).contains("ab_")){
                    holder.txtQuery.setCompoundDrawablesWithIntrinsicBounds(R.drawable.iconalbum_20,0,0,0);
                    holder.txtQuery.setText(item.substring(3));
                    holder.imgbtnRevmove.setClickable(false);
                    holder.imgbtnRevmove.setVisibility(View.GONE);
                }else {
                    if(ListHistoryString.get(position).contains("pl_")){
                        holder.txtQuery.setCompoundDrawablesWithIntrinsicBounds(R.drawable.iconplaylist_20,0,0,0);
                        holder.txtQuery.setText(item.substring(3));
                        holder.imgbtnRevmove.setClickable(false);
                        holder.imgbtnRevmove.setVisibility(View.GONE);
                    }else {
                        if(ListHistoryString.get(position).contains("tl_")){
                            holder.txtQuery.setCompoundDrawablesWithIntrinsicBounds(R.drawable.iconplaylist_20,0,0,0);
                            holder.txtQuery.setText(item.substring(3));
                            holder.imgbtnRevmove.setClickable(false);
                            holder.imgbtnRevmove.setVisibility(View.GONE);
                        }else{
                            holder.txtQuery.setCompoundDrawablesWithIntrinsicBounds(R.drawable.iconclock_grey_20,0,0,0);
                            holder.txtQuery.setText(item);
                            holder.imgbtnRevmove.setClickable(true);
                            holder.imgbtnRevmove.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        }
        //nút xóa query
        holder.imgbtnRevmove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Log.d("DeleteFhistory", "old:"+PreferenceUtils.getSearchHistory(context));
                PreferenceUtils.saveSearchHistoryByGiveStringThatNeedToRemove(ListHistoryString.get(position),context);
                Log.d("DeleteFhistory", "new:"+PreferenceUtils.getSearchHistory(context));
                //ẩn nó đi lại trang khi bỏ thích
                ListHistoryString.remove(position);
                //notifyItemRemoved(position); //remove item khỏi layout
                //notifyItemRangeChanged(position,ListHistoryString.size());//cap nhat kích cơ re
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return ListHistoryString.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtQuery;
        ImageButton imgbtnRevmove;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtQuery=itemView.findViewById(R.id.txtHistoryQuery);
            imgbtnRevmove=itemView.findViewById(R.id.btnRemoveFromHistory);

            txtQuery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    searchView.setQuery(txtQuery.getText(),false);
                }
            });


        }


    }

}
