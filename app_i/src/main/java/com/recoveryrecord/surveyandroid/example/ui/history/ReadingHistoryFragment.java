package com.recoveryrecord.surveyandroid.example.ui.history;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.recoveryrecord.surveyandroid.example.adapter.NewsRecycleViewAdapter;
import com.recoveryrecord.surveyandroid.example.R;
import com.recoveryrecord.surveyandroid.example.model.NewsModel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//import static com.recoveryrecord.surveyandroid.example.ConstantsOld.PUSH_ESM_TARGET_TITLE;
import static com.recoveryrecord.surveyandroid.example.config.Constants.READING_BEHAVIOR_COLLECTION;
import static com.recoveryrecord.surveyandroid.example.config.Constants.READING_BEHAVIOR_DEVICE_ID;
import static com.recoveryrecord.surveyandroid.example.config.Constants.READING_BEHAVIOR_OUT_TIME;
import static com.recoveryrecord.surveyandroid.example.config.Constants.READING_BEHAVIOR_TITLE;
import static com.recoveryrecord.surveyandroid.example.config.Constants.READ_HISTORY_LIMIT_PER_PAGE;

public class ReadingHistoryFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private RecyclerView courseRV;
    private ArrayList<NewsModel> dataModalArrayList;
    private NewsRecycleViewAdapter dataRVAdapter;
    private FirebaseFirestore db;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String device_id = "";

    public ReadingHistoryFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static ReadingHistoryFragment newInstance() {
        ReadingHistoryFragment fragment = new ReadingHistoryFragment();

        return fragment;
    }

    @SuppressLint("HardwareIds")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        device_id = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
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
        dataRVAdapter = new NewsRecycleViewAdapter(dataModalArrayList, getActivity());

        // setting adapter to our recycler view.
        courseRV.setAdapter(dataRVAdapter);

        loadrecyclerViewData();

        return view;
    }

    private void loadrecyclerViewData() {
//        db.collection(TEST_USER_COLLECTION)
//                .document(device_id)
//                .collection(READING_BEHAVIOR_COLLECTION)
//                .orderBy(READING_BEHAVIOR_OUT_TIME, Query.Direction.DESCENDING)
//                .limit(READ_HISTORY_LIMIT_PER_PAGE)
//                .get()
        db.collection(READING_BEHAVIOR_COLLECTION)
                .whereEqualTo(READING_BEHAVIOR_DEVICE_ID, device_id)
                .orderBy(READING_BEHAVIOR_OUT_TIME, Query.Direction.DESCENDING)
                .limit(READ_HISTORY_LIMIT_PER_PAGE)
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
                Log.d("lognewsselect", String.valueOf(e));
                // if we do not get any data or any error we are displaying
                // a toast message that we do not get any data
//                Toast.makeText(TestNewsOneActivity.this, "Fail to get the data.", Toast.LENGTH_SHORT).show();
            }
        });
    }

}