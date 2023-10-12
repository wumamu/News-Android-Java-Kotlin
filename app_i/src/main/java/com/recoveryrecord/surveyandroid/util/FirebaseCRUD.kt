package com.recoveryrecord.surveyandroid.util

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import timber.log.Timber

suspend fun fetchRemote(
    query: Query,
    onSuccess: (QuerySnapshot) -> Unit = {}
) {
    try {
        val querySnapshot = withContext(Dispatchers.IO) {
            query.get().await()
        }
        onSuccess(querySnapshot) // main thread
    } catch (e: Exception) {
        Timber.w("Firestore fetch failed $e")
    }
}

suspend fun updateRemote(
    document: DocumentReference,
    newData: HashMap<String, Any>,
    onSuccess: () -> Unit = {},
) {
    try {
        withContext(Dispatchers.IO) {
            document.update(newData).await()
        }
        onSuccess()
    } catch (e: Exception) {
        Timber.w("Firestore u[date failed$e")
    }
}

suspend fun insertRemote(
    document: DocumentReference,
    newData: HashMap<String, Any>,
    onSuccess: () -> Unit = {},
//    onError: (Exception) -> Unit
) {
    try {
        withContext(Dispatchers.IO) {
            document.set(newData).await()
        }
        onSuccess()
    } catch (e: Exception) {
        Timber.w("Firestore insert failed$e")
    }
}