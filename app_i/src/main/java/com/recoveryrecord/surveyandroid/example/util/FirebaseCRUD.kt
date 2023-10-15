package com.recoveryrecord.surveyandroid.example.util

import com.google.firebase.firestore.CollectionReference
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
    onError: (Exception) -> Unit = {}
) {
    try {
        withContext(Dispatchers.IO) {
            document.update(newData).await()
        }
        onSuccess()
    } catch (e: Exception) {
        Timber.w("Firestore update failed$e")
        onError(e)
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

/**
 * Adds a new document to this collection with the specified data, assigning it a document ID automatically.
 */
suspend fun addRemote(
    document: CollectionReference,
    newData: HashMap<String, Any>,
    onSuccess: () -> Unit = {},
//    onError: (Exception) -> Unit
) {
    try {
        withContext(Dispatchers.IO) {
            document.add(newData).await()
        }
        onSuccess()
    } catch (e: Exception) {
        Timber.w("Firestore insert failed$e")
    }
}