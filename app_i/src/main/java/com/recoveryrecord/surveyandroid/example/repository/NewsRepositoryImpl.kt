package com.recoveryrecord.surveyandroid.example.repository

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class NewsRepositoryImpl
    @Inject
    constructor(
        private val db: FirebaseFirestore,
    ) : NewsRepository {
        override suspend fun getNews(
            source: String,
            category: String,
            pageSize: Long,
        ): Result<QuerySnapshot> {
            val query = createQuery(source, category)
            return try {
                val result = query.limit(pageSize).get().await()
                Result.success(result)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

        override suspend fun getMoreNews(
            source: String,
            category: String,
            pageSize: Long,
            lastVisibleDocument: DocumentSnapshot?,
        ): Result<QuerySnapshot> {
            val query = createQuery(source, category)
            return try {
                val result =
                    query.startAfter(lastVisibleDocument?.getTimestamp("pubdate")).limit(pageSize).get()
                        .await()
                Result.success(result)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

        private fun createQuery(
            source: String,
            category: String,
        ): Query {
            return if (source == "storm") {
                db.collection("news")
                    .whereEqualTo("media", source)
                    .whereArrayContains("category", category)
                    .orderBy("pubdate", Query.Direction.DESCENDING)
            } else {
                db.collection("news")
                    .whereEqualTo("media", source)
                    .whereEqualTo("category", category)
                    .orderBy("pubdate", Query.Direction.DESCENDING)
            }
        }
    }
