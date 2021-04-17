package com.recoveryrecord.surveyandroid.example.model;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.recoveryrecord.surveyandroid.example.NewsRecycleViewAdapter;
import com.recoveryrecord.surveyandroid.example.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class Pagers extends RelativeLayout {
    private RecyclerView courseRV;
    private ArrayList<NewsModel> dataModalArrayList;
    private NewsRecycleViewAdapter dataRVAdapter;
//    private SwipeRefreshLayout swipeRefreshLayout;
//    private RelativeLayout errorLayout;
//    private ImageView errorImage;
//    private TextView errorTitle, errorMessage;
//    private Button btnRetry;
    private FirebaseFirestore db;
    public Pagers(final Context context, int pageNumber, String pageTag, String media_name, String category) {
        super(context);

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_pagers, null);
//        TextView textView = view.findViewById(R.id.textView);
//        textView.setText("第"+pageNumber+"頁");

//        //error
//        errorLayout = findViewById(R.id.errorLayout);
////        errorImage = findViewById(R.id.errorImage);
//        errorTitle = findViewById(R.id.errorTitle);
//        errorMessage = findViewById(R.id.errorMessage);

        // initializing our variables.
        courseRV = view.findViewById(R.id.idRVItems);


        // initializing our variable for firebase
        // firestore and getting its instance.
        db = FirebaseFirestore.getInstance();

        // creating our new array list
        dataModalArrayList = new ArrayList<>();
        courseRV.setHasFixedSize(true);

        // adding horizontal layout manager for our recycler view.
//        courseRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        courseRV.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

        // adding our array list to our recycler view adapter class.
        dataRVAdapter = new NewsRecycleViewAdapter(dataModalArrayList, context);

        // setting adapter to our recycler view.
        courseRV.setAdapter(dataRVAdapter);

        loadrecyclerViewData(media_name, context, category);
//        Log.d("log: pager", "news id");

        //remember!!!!!!!!!!!!!!!!!
        addView(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

    }
    private void loadrecyclerViewData(final String media_nn, final Context cc, final String cat) {
        if(media_nn.equals("chinatimes")){
            Log.d("logpager", media_nn + " " + cat);
            //neet to
            String tmp_cat = cat;//"政治";
            db.collection("medias")
                    .document(media_nn)
                    .collection("news")
//                    .whereEqualTo("category", "體育")
                    .orderBy("pubdate", Query.Direction.DESCENDING)//           .orderBy("pubdate", Query.Direction.DESCENDING)
                    .limit(50)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if (!queryDocumentSnapshots.isEmpty()) {
                                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                                for (DocumentSnapshot d : list) {
                                    NewsModel dataModal = d.toObject(NewsModel.class);
                                    dataModalArrayList.add(dataModal);
                                }
                                dataRVAdapter.notifyDataSetChanged();
                            } else {
                                Log.d("logpager", "No data found " + media_nn);
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("logpager", "Fail to get the data." + media_nn);
                }
            });
        } else {
            Log.d("logpager", media_nn);
            db.collection("medias")
                    .document(media_nn)
                    .collection("news")////
                    .orderBy("pubdate", Query.Direction.DESCENDING)//           .orderBy("pubdate", Query.Direction.DESCENDING)
                    .limit(50)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            // after getting the data we are calling on success method
                            // and inside this method we are checking if the received
                            // query snapshot is empty or not.
                            if (!queryDocumentSnapshots.isEmpty()) {
                                // if the snapshot is not empty we are hiding our
                                // progress bar and adding our data in a list.
                                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                                for (DocumentSnapshot d : list) {
                                    // after getting this list we are passing that
                                    // list to our object class.
                                    NewsModel dataModal = d.toObject(NewsModel.class);
                                    // and we will pass this object class
                                    // inside our arraylist which we have
                                    // created for recycler view.
                                    dataModalArrayList.add(dataModal);
                                }
                                // after adding the data to recycler view.
                                // we are calling recycler view notifyDataSetChanged
                                // method to notify that data has been changed in recycler view.
                                dataRVAdapter.notifyDataSetChanged();
                            } else {
                                // if the snapshot is empty we are
                                // displaying a toast message.
//                            TextView myTextViewsEmpty = new TextView(cc);
//                            myTextViewsEmpty.setText("no result");
                                Log.d("logpager", "No data found " + media_nn);
//                            vv.addView(myTextViewsEmpty);
//                            Toast.makeText(cc, "No data found in Database", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // if we do not get any data or any error we are displaying
                    // a toast message that we do not get any data
                    Log.d("logpager", "Fail to get the data." + media_nn);
//                Toast.makeText(cc, "Fail to get the data." + media_nn, Toast.LENGTH_SHORT).show();
//                showErrorMessage("Oops..", "Network failure, Please Try Again\n", media_nn, cc);
                }
            });
        }

    }
}
