package com.recoveryrecord.surveyandroid.example.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.recoveryrecord.surveyandroid.example.R;
import com.recoveryrecord.surveyandroid.example.model.MediaModel;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

//import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MediaRankRecycleViewAdapter extends RecyclerView.Adapter<MediaRankRecycleViewAdapter.ViewHolder> implements ItemTouchHelperAdapter{
    private ArrayList<MediaModel> dataModelArrayList;
    private Context context;
    private ItemTouchHelper mTouchHelper;
//    private OnCustomerListChangedListener mListChangedListener;

    // constructor class for our Adapter
    public MediaRankRecycleViewAdapter(ArrayList<MediaModel> dataModalArrayList, Context context) {
        this.dataModelArrayList = dataModalArrayList;
        this.context = context;
//        mListChangedListener = listChangedListener;
    }

    @NonNull
    @Override
    public MediaRankRecycleViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // passing our layout file for displaying our card item
        return new MediaRankRecycleViewAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.media_rv_item, parent, false));
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull MediaRankRecycleViewAdapter.ViewHolder holder, int position) {
        // setting data to our views in Recycler view items.
        MediaModel model = dataModelArrayList.get(position);
//        holder.newsTitle.setText(model.getTitle());
//        holder.newsPubTime.setText(model.getPubdate());
        holder.newsMedia.setText(model.getMedia());
//        String uri = "@drawable/myresource";
        // Read your drawable from somewhere
        @SuppressLint("UseCompatLoadingForDrawables")
        Drawable dr = context.getResources().getDrawable(R.drawable.storm);
        switch (model.getMedia()) {
            case "中時":
                dr = context.getResources().getDrawable(R.drawable.ct);
//                holder.media_icon.setImageResource(R.drawable.error);
                break;
            case "中央社":
                dr = context.getResources().getDrawable(R.drawable.cna);
//                holder.media_icon.setImageResource(R.drawable.swap_vert);
                break;
            case "華視":
                dr = context.getResources().getDrawable(R.drawable.cts);
//                holder.media_icon.setImageResource(R.drawable.swap_vert);
                break;
            case "東森":
                dr = context.getResources().getDrawable(R.drawable.ebc);
//                holder.media_icon.setImageResource(R.drawable.swap_vert);
                break;
            case "自由時報":
                dr = context.getResources().getDrawable(R.drawable.ltn);
//                holder.media_icon.setImageResource(R.drawable.swap_vert);
                break;
            case "風傳媒":
                dr = context.getResources().getDrawable(R.drawable.storm);
//                holder.media_icon.setImageResource(R.drawable.storm);
                break;
            case "聯合":
                dr = context.getResources().getDrawable(R.drawable.udn);
//                holder.media_icon.setImageResource(R.drawable.swap_vert);
                break;
            case "ettoday":
                dr = context.getResources().getDrawable(R.drawable.ettoday);
//                holder.media_icon.setImageResource(R.drawable.swap_vert);
                break;
            case "三立":
                dr = context.getResources().getDrawable(R.drawable.setn);
//                holder.media_icon.setImageResource(R.drawable.swap_vert);
                break;
            default:
//                holder.media_icon.setImageResource(R.drawable.swap_vert);
                break;
        }
        Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
        // Scale it to 50 x 50
        Drawable d = new BitmapDrawable(context.getResources(), Bitmap.createScaledBitmap(bitmap, 36, 36, true));
        // Set your new, scaled drawable "d"
        holder.media_icon.setImageDrawable(d);
    }

    @Override
    public int getItemCount() {
        // returning the size of array list.
        return dataModelArrayList.size();
    }


    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        MediaModel fromNote = dataModelArrayList.get(fromPosition);
        dataModelArrayList.remove(fromNote);
        dataModelArrayList.add(toPosition, fromNote);
        notifyItemMoved(fromPosition, toPosition);
//        Log.d("onItemMove", "front " + String.valueOf(fromPosition));
//        Log.d("onItemMove", "to " + String.valueOf(toPosition));
//        Log.d("onItemMove", "mm" + fromNote.getMedia());
    }

    @Override
    public void onItemSwiped(int position) { }

    public void setTouchHelper(ItemTouchHelper touchHelper){
        this.mTouchHelper = touchHelper;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        // creating variables for our
        // views of recycler items.
        private TextView newsMedia;
        private ImageView media_icon;
//        private ImageView courseIV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            newsMedia = itemView.findViewById(R.id.text_view_media_rank);
            media_icon = itemView.findViewById(R.id.mediaiconView);
        }
    }
}
