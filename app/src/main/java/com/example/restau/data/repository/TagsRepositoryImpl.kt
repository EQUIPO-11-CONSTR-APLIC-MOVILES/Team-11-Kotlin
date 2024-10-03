package com.example.restau.data.repository

import android.util.Log
import com.example.restau.domain.repository.TagsRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class TagsRepositoryImpl (
    private val db: FirebaseFirestore
) : TagsRepository {
    override suspend fun getTags(): Map<String, Pair<Int, List<String>>> {
        val tags = emptyMap<String, Pair<Int, List<String>>>().toMutableMap()
        try {
            val snapshot = db
                .collection("Preference Tags")
                .document("tags")
                .get()
                .await()

            Log.d("TAGS", snapshot.data.toString())

            for (document in snapshot.data!!) {
                val tag = document.key
                val tagData = document.value as Map<*, *>
                val tagCount = tagData["index"] as Long
                val tagSynonyms = tagData["list"] as List<*>
                tags[tag] = Pair(tagCount.toInt(), tagSynonyms) as Pair<Int, List<String>>
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
        Log.d("TAGS", tags.toString())
        return tags
    }
}