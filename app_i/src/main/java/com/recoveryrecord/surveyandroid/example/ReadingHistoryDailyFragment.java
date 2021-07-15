package com.recoveryrecord.surveyandroid.example;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.recoveryrecord.surveyandroid.example.model.NewsModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.recoveryrecord.surveyandroid.example.Constants.READING_BEHAVIOR_COLLECTION;
import static com.recoveryrecord.surveyandroid.example.Constants.READING_BEHAVIOR_DEVICE_ID;
import static com.recoveryrecord.surveyandroid.example.Constants.READING_BEHAVIOR_IN_TIME_LONG;
import static com.recoveryrecord.surveyandroid.example.Constants.READING_BEHAVIOR_TITLE;

//import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_ESM_TARGET_TITLE;

public class ReadingHistoryDailyFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static int position;
    private RecyclerView courseRV;
    private ArrayList<NewsModel> dataModalArrayList;
    private ReadingHistoryRecycleViewAdapter dataRVAdapter;
    private FirebaseFirestore db;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String device_id = "";
    Calendar start_time, end_time;
    Long start_long, end_long;
    Timestamp test;
    int mPosition;
    // TODO: Rename and change types and number of parameters
    public static ReadingHistoryDailyFragment newInstance(int pos) {
        ReadingHistoryDailyFragment fragment = new ReadingHistoryDailyFragment();
        position = pos;
        Bundle args = new Bundle();
        args.putInt("position", pos);
        fragment.setArguments(args);
        return fragment;
    }

    public ReadingHistoryDailyFragment() {
        // Required empty public constructor
    }

    @SuppressLint("HardwareIds")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPosition = getArguments().getInt("position");
        }
        device_id = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
        start_time = Calendar.getInstance();
        end_time = Calendar.getInstance();
        start_time.add(Calendar.DAY_OF_YEAR, -(mPosition-1));
        end_time.add(Calendar.DAY_OF_YEAR, -(mPosition-1));
        start_time.set(Calendar.HOUR_OF_DAY, 0);
        start_time.set(Calendar.MINUTE, 0);
        start_time.set(Calendar.SECOND, 0);
        end_time.set(Calendar.HOUR_OF_DAY, 23);
        end_time.set(Calendar.MINUTE, 59);
        end_time.set(Calendar.SECOND, 59);

        start_long = start_time.getTimeInMillis()/1000;
        end_long = end_time.getTimeInMillis()/1000;
        test = new Timestamp(1626349115, 0);
        Log.d("lognewsselect1231", String.valueOf(start_long));
        Log.d("lognewsselect1231", String.valueOf(end_long));
        Log.d("lognewsselect1231", String.valueOf(position));
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_nest1_category, container, false);
        View view = inflater.inflate(R.layout.nested_layer2_readinghistory, container, false);
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
        dataRVAdapter = new ReadingHistoryRecycleViewAdapter(dataModalArrayList, getActivity());

        // setting adapter to our recycler view.
        courseRV.setAdapter(dataRVAdapter);

        loadrecyclerViewData();

        return view;
    }

    private void loadrecyclerViewData() {
        db.collection(READING_BEHAVIOR_COLLECTION)
                .whereEqualTo(READING_BEHAVIOR_DEVICE_ID, device_id)
                .whereGreaterThanOrEqualTo(READING_BEHAVIOR_IN_TIME_LONG, start_long)//1626349115
                .whereLessThanOrEqualTo(READING_BEHAVIOR_IN_TIME_LONG, end_long)
                .orderBy(READING_BEHAVIOR_IN_TIME_LONG, Query.Direction.DESCENDING)
//                .startAfter(test)
//                .endBefore(Timestamp.now())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot d : list) {
                                if(d.get(READING_BEHAVIOR_TITLE)!=null && !d.get(READING_BEHAVIOR_TITLE).equals("NA")){
                                    NewsModel dataModal = d.toObject(NewsModel.class);
                                    dataModalArrayList.add(dataModal);
                                }
                            }
                            dataRVAdapter.notifyDataSetChanged();
                        } else {
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("lognewsselect1231", String.valueOf(e));
                // if we do not get any data or any error we are displaying
                // a toast message that we do not get any data
//                Toast.makeText(TestNewsOneActivity.this, "Fail to get the data.", Toast.LENGTH_SHORT).show();
            }
        });
    }

}