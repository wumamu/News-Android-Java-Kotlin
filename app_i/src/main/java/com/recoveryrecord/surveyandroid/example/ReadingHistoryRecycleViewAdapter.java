package com.recoveryrecord.surveyandroid.example;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.recoveryrecord.surveyandroid.example.model.NewsModel;

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
//        if(model.getPubdate()==null){
//            FirebaseFirestore db = FirebaseFirestore.getInstance();
//            DocumentReference docRef = db.collection("server_push_notifications").document(model.getId());
//            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                    if (task.isSuccessful()) {
//                        DocumentSnapshot document = task.getResult();
//                        assert document != null;
//                        if (document.exists()) {
//                            Date date = document.getTimestamp("pubdate").toDate();
//                            @SuppressLint("SimpleDateFormat")
//                            SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
//                            List<String> my_tt = new ArrayList<String>(Arrays.asList(formatter.format(date).split(" ")));
//                            holder.newsPubTime.setText(String.format("%s %s", my_tt.get(0), my_tt.get(2)));
//                            holder.newsMedia.setText(document.getString("media"));
//                        } else {
//                            Log.d("lognewsselect", "No such document");
//                        }
//                    } else {
//                        Log.d("lognewsselect", "get failed with ", task.getException());
//                    }
//                }
//            });
//        } else {
//            Date date = model.getPubdate().toDate();
//            @SuppressLint("SimpleDateFormat")
//            SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
//            List<String> my_tt = new ArrayList<String>(Arrays.asList(formatter.format(date).split(" ")));
//            holder.newsPubTime.setText(String.format("%s %s", my_tt.get(0), my_tt.get(2)));
//            holder.newsMedia.setText(model.getMedia());
//        }


        // we are using Picasso to load images
        // from URL inside our image view.
//        Picasso.get().load(modal.getImgUrl()).into(holder.courseIV);
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // setting on click listener
//                // for our items of recycler items.
//                Toast.makeText(context, "Clicked item is " + modal.getName(), Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    @Override
    public int getItemCount() {
        // returning the size of array list.
        return dataModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // creating variables for our
        // views of recycler items.
        private TextView newsTitle, newsPubTime, newsMedia;
//        private ImageView courseIV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing the views of recycler views.
            newsTitle = itemView.findViewById(R.id.text_view_title);
            newsPubTime = itemView.findViewById(R.id.text_view_pubtime);
            newsMedia = itemView.findViewById(R.id.text_view_media);
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
//                    Log.d("lognewsselect", "MY ID  " +  model.getId());
//                    if(model.getMedia()==null){
////                        String my_media = "";
//                        FirebaseFirestore db = FirebaseFirestore.getInstance();
//                        DocumentReference docRef = db.collection("server_push_notifications").document(model.getId());
//                        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                            @Override
//                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                                if (task.isSuccessful()) {
//                                    DocumentSnapshot document = task.getResult();
//                                    assert document != null;
//                                    if (document.exists()) {
//                                        my_media = document.getString("media");
//                                        Log.d("lognewsselect", "my_media " +  my_media);
////                                        intent.putExtra("media", document.getString("media"));
//                                    } else {
//                                        Log.d("lognewsselect", "No such document");
//                                    }
//                                } else {
//                                    Log.d("lognewsselect", "get failed with ", task.getException());
//                                }
//                            }
//                        });
//                        Log.d("lognewsselect", "MY MEDIA " +  my_media);
//                        intent.putExtra("media", my_media);
//                    } else {
//                        Log.d("lognewsselect", "MY MEDIA ** " +  model.getMedia());
//                        intent.putExtra("media", model.getMedia());
//                    }


//                    Log.d("log: onClick", model.getTitle());
//                    Toast.makeText(view.getContext(), "click " +model.getTitle(),Toast.LENGTH_SHORT).show();
                    context.startActivity(intent);
                    //TestActivityRecognitionActivity.this.finish();
                }
            });

        }
    }
}