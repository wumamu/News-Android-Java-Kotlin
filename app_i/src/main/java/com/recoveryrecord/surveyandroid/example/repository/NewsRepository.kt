package com.recoveryrecord.surveyandroid.example.repository

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot

interface NewsRepository {
    suspend fun getNews(source: String, category: String, pageSize: Long): Result<QuerySnapshot>
    suspend fun getMoreNews(
        source: String,
        category: String,
        pageSize: Long,
        lastVisibleDocument: DocumentSnapshot?
    ): Result<QuerySnapshot>
}