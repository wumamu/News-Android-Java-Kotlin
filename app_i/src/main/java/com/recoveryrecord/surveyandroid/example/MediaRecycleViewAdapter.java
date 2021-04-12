package com.recoveryrecord.surveyandroid.example;


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

import com.recoveryrecord.surveyandroid.example.model.MediaModel;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

//import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MediaRecycleViewAdapter extends RecyclerView.Adapter<MediaRecycleViewAdapter.ViewHolder> implements ItemTouchHelperAdapter{
    private ArrayList<MediaModel> dataModelArrayList;
    private Context context;
    private ItemTouchHelper mTouchHelper;

    // constructor class for our Adapter
    public MediaRecycleViewAdapter(ArrayList<MediaModel> dataModalArrayList, Context context) {
        this.dataModelArrayList = dataModalArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public MediaRecycleViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // passing our layout file for displaying our card item
        return new MediaRecycleViewAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.media_rv_item, parent, false));
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull MediaRecycleViewAdapter.ViewHolder holder, int position) {
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
            case "聯合(udn)":
                dr = context.getResources().getDrawable(R.drawable.udn);
//                holder.media_icon.setImageResource(R.drawable.swap_vert);
                break;
            case "ETTODAY":
                dr = context.getResources().getDrawable(R.drawable.ettoday);
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
//        int imageResource = getResources().getIdentifier(uri, null, getPackageName());
//
//        imageview= (ImageView)findViewById(R.id.imageView);
//        Drawable res = getResources().getDrawable(imageResource);
//        imageView.setImageDrawable(res);
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
    }

    @Override
    public void onItemSwiped(int position) {

    }

    public void setTouchHelper(ItemTouchHelper touchHelper){
        this.mTouchHelper = touchHelper;
    }

//    @Override
//    public void onItemMove(int fromPosition, int toPosition) {
//        MediaModel fromNote = dataModelArrayList.get(fromPosition);
//        dataModelArrayList.remove(fromNote);
//        dataModelArrayList.add(toPosition, fromNote);
//        notifyItemMoved(fromPosition, toPosition);
//    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // creating variables for our
        // views of recycler items.
        private TextView newsMedia;
        private ImageView media_icon;
//        private ImageView courseIV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing the views of recycler views.
//            newsTitle = itemView.findViewById(R.id.text_view_title);
//            newsPubTime = itemView.findViewById(R.id.text_view_pubtime);
            newsMedia = itemView.findViewById(R.id.text_view_media_rank);
            media_icon = itemView.findViewById(R.id.mediaiconView);
//            courseIV = itemView.findViewById(R.id.idIVimage);
//            itemView.setCardBackgroundColor(Color.parseColor("#e0efff"));
            // 點擊項目時
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
////                    Toast.makeText(view.getContext(), "click " +getAdapterPosition(),Toast.LENGTH_SHORT).show();
//                    NewsModel model = dataModelArrayList.get(getAdapterPosition());
//                    Intent intent = new Intent();
//                    intent.setClass(context, NewsModuleActivity.class);
//                    intent.putExtra("trigger_from", "self_trigger");
//                    intent.putExtra("news_id", model.getId());
//                    intent.putExtra("media", model.getMedia());
//
//                    Log.d("log: onClick", model.getTitle());
////                    Toast.makeText(view.getContext(), "click " +model.getTitle(),Toast.LENGTH_SHORT).show();
//                    context.startActivity(intent);
//                    //MainActivity.this.finish();
//                }
//            });

        }
    }
}
