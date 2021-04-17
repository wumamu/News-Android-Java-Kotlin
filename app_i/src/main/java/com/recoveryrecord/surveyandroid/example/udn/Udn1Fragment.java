package com.recoveryrecord.surveyandroid.example.udn;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.recoveryrecord.surveyandroid.example.NewsRecycleViewAdapter;
import com.recoveryrecord.surveyandroid.example.R;
import com.recoveryrecord.surveyandroid.example.model.NewsModel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Udn1Fragment extends Fragment {
    private RecyclerView courseRV;
    private ArrayList<NewsModel> dataModalArrayList;
    private NewsRecycleViewAdapter dataRVAdapter;
    private FirebaseFirestore db;
    public static Udn1Fragment newInstance(int position) {
        Udn1Fragment fragment = new Udn1Fragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        fragment.setArguments(args);

        return fragment;
    }

    public Udn1Fragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            /* mPosition = getArguments().getInt("position");*/
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nest1_category, container, false);
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
        courseRV.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        // adding our array list to our recycler view adapter class.
        dataRVAdapter = new NewsRecycleViewAdapter(dataModalArrayList, getActivity());

        // setting adapter to our recycler view.
        courseRV.setAdapter(dataRVAdapter);

        loadrecyclerViewData();

        return view;
    }
    private void loadrecyclerViewData() {
//.orderBy("name").limit(3)//                db.collectionGroup("news") //
        db.collection("medias")
                .document("udn")
                .collection("news")
                .whereEqualTo("category", "地方")
                .orderBy("pubdate", Query.Direction.DESCENDING)
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
//                            Toast.makeText(TestNewsOneActivity.this, "No data found in Database", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // if we do not get any data or any error we are displaying
                // a toast message that we do not get any data
//                Toast.makeText(TestNewsOneActivity.this, "Fail to get the data.", Toast.LENGTH_SHORT).show();
            }
        });
    }

}