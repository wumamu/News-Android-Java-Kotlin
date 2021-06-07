package com.recoveryrecord.surveyandroid.example;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.recoveryrecord.surveyandroid.example.model.NewsModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static com.recoveryrecord.surveyandroid.example.Constants.MEDIA_COLLECTION;
import static com.recoveryrecord.surveyandroid.example.Constants.NEWS_COLLECTION;
import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_ESM_NOTIFICATION_EXIST;
import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_ESM_NOTI_ARRAY;
import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_ESM_NOT_SAMPLE_NOTIFICATION_FAR;
import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_ESM_NOT_SAMPLE_READ_FAR;
import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_ESM_NOT_SAMPLE_READ_SHORT;
import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_ESM_READ_ARRAY;
import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_ESM_READ_EXIST;

//import com.squareup.picasso.Picasso;

public class NewsRecycleViewAdapter extends RecyclerView.Adapter<NewsRecycleViewAdapter.ViewHolder> {
    private ArrayList<NewsModel> dataModelArrayList;
    private Context context;
    String my_media = "";
    // constructor class for our Adapter
    public NewsRecycleViewAdapter(ArrayList<NewsModel> dataModalArrayList, Context context) {
        this.dataModelArrayList = dataModalArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public NewsRecycleViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // passing our layout file for displaying our card item
        return new NewsRecycleViewAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.news_rv_item, parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull final NewsRecycleViewAdapter.ViewHolder holder, int position) {
        // setting data to our views in Recycler view items.
        final NewsModel model = dataModelArrayList.get(position);
        holder.newsTitle.setText(model.getTitle());
        Date date = model.getPubdate().toDate();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        List<String> my_tt = new ArrayList<String>(Arrays.asList(formatter.format(date).split(" ")));
        holder.newsPubTime.setText(String.format("%s %s", my_tt.get(0), my_tt.get(2)));
        holder.newsMedia.setText(model.getMedia());
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
