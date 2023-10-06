package com.recoveryrecord.surveyandroid.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore
) : NewsRepository {
    override suspend fun getNews(source: String, category: String): Task<QuerySnapshot> {
        return db
            .collection("news")
            .whereEqualTo("media", source)
            .whereArrayContains("category", category)
            .orderBy("pubdate", Query.Direction.DESCENDING)
            .limit(30)
            .get()
    }
}
