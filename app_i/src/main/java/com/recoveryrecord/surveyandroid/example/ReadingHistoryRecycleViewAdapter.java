package com.recoveryrecord.surveyandroid.example;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.recoveryrecord.surveyandroid.example.model.NewsModel;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

//import com.squareup.picasso.Picasso;

public class ReadingHistoryRecycleViewAdapter extends RecyclerView.Adapter<ReadingHistoryRecycleViewAdapter.ViewHolder> {
    private ArrayList<NewsModel> dataModelArrayList;
    private Context context;
    String my_media = "";
    // constructor class for our Adapter
    public ReadingHistoryRecycleViewAdapter(ArrayList<NewsModel> dataModalArrayList, Context context) {
        this.dataModelArrayList = dataModalArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ReadingHistoryRecycleViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // passing our layout file for displaying our card item
        return new ReadingHistoryRecycleViewAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.readinghistory_rv_item, parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull final ReadingHistoryRecycleViewAdapter.ViewHolder holder, int position) {
        // setting data to our views in Recycler view items.
        final NewsModel model = dataModelArrayList.get(position);
        holder.newsTitle.setText(model.getTitle());
        Date date = model.getPubdate().toDate();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        List<String> my_tt = new ArrayList<String>(Arrays.asList(formatter.format(date).split(" ")));
        holder.newsPubTime.setText(String.format("%s %s", my_tt.get(0), my_tt.get(2)));

        holder.newsImg.setVisibility(View.GONE);
//        if(model.getImage()!=null && model.getImage()!="NA"){
//            new NewsRecycleViewAdapter.DownloadImageTask(holder.newsImg).execute(model.getImage());
//
//            holder.newsImg.setAdjustViewBounds(true);
//            holder.newsImg.setMaxHeight(200);
//        } else {
//            holder.newsImg.setVisibility(View.GONE);
//        }

        String media_name = model.getMedia();//from data base
        switch (media_name) {
            case "cna":
                media_name = "中央社";
                break;
            case "chinatimes":
                media_name = "中時";
                break;
            case "cts":
                media_name = "華視";
                break;
            case "ebc":
                media_name = "東森";
                break;
            case "ltn":
                media_name = "自由時報";
                break;
            case "storm":
                media_name = "風傳媒";
                break;
            case "udn":
                media_name = "聯合";
                break;
            case "ettoday":
                media_name = "ettoday";
                break;
            case "setn":
                media_name = "三立";
                break;
            default:
//                media_name = "";
                break;

        }
        holder.newsMedia.setText(media_name);
    }

    @Override
    public int getItemCount() {
        // returning the size of array list.
        return dataModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // creating variables for our
        // views of recycler items.
        private final TextView newsTitle;
        private final TextView newsPubTime;
        private final TextView newsMedia;
        private final ImageView newsImg;
//        private ImageView courseIV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing the views of recycler views.
            newsTitle = itemView.findViewById(R.id.text_view_title);
            newsPubTime = itemView.findViewById(R.id.text_view_pubtime);
            newsMedia = itemView.findViewById(R.id.text_view_media);
            newsImg = itemView.findViewById(R.id.imgView);
//            courseIV = itemView.findViewById(R.id.idIVimage);
//            itemView.setCardBackgroundColor(Color.parseColor("#e0efff"));
            // 點擊項目時
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Toast.makeText(view.getContext(), "click " +getAdapterPosition(),Toast.LENGTH_SHORT).show();
                    final NewsModel model = dataModelArrayList.get(getAdapterPosition());
                    final Intent intent = new Intent();
                    intent.setClass(context, NewsModuleActivity.class);
                    intent.putExtra("trigger_by", "self_trigger");
                    intent.putExtra("news_id", model.getId());
                    intent.putExtra("media", model.getMedia());
                    Log.d("lognewsselect", "MY ID  " +  model.getId());
                    Log.d("lognewsselect", "MY IDddd  " +  model.getMedia());

                    context.startActivity(intent);
                    //TestActivityRecognitionActivity.this.finish();
                }
            });

        }
    }
    //image download ###############################################################################
    private static class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
