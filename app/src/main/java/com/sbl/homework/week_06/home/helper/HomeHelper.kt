package com.sbl.homework.week_06.home.helper

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.sbl.homework.week_06.home.models.FriendsDataModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class HomeHelper {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    companion object {
        var friendsList = MutableLiveData<MutableList<FriendsDataModel>>().apply {
            value = mutableListOf()
        }
    }

    fun getProfileMessage(completion: (String) -> Unit) {
        db.collection("Users").document(auth.currentUser?.uid ?: "").get().addOnCompleteListener {
            if (it.isSuccessful) {
                val document = it.result

                if (document != null && document.exists()) {
                    completion(document.get("profileMessage") as? String ?: "No profile message")
                }
            }
        }
    }

    fun setProfileMessage(profileMessage: String, completion: (Boolean) -> Unit) {
        db.collection("Users").document(auth.currentUser?.uid ?: "").update(
            mapOf(
                "profileMessage" to profileMessage
            )
        ).addOnCompleteListener {
            if (it.isSuccessful) {
                getProfileMessage {
                    completion(true)
                    return@getProfileMessage
                }
            } else {
                completion(false)
                return@addOnCompleteListener
            }
        }.addOnFailureListener {
            it.printStackTrace()
            completion(false)
            return@addOnFailureListener
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun getFriends(completion: (Boolean) -> Unit) {
        friendsList.value!!.clear()

        db.collection("Users").document(auth.currentUser?.uid ?: "").collection("Friends")
            .addSnapshotListener { value, error ->
                if (error != null) {
                    error.printStackTrace()
                    completion(false)
                    return@addSnapshotListener
                }

                if (value == null) {
                    completion(false)
                    return@addSnapshotListener
                }

                GlobalScope.launch(Dispatchers.IO) {
                    try{
                        coroutineScope {
                            val tasks = value.documentChanges.map { dc ->
                                async {
                                    when (dc.type) {
                                        DocumentChange.Type.ADDED, DocumentChange.Type.MODIFIED -> {
                                            val data = friendsList.value!!.find {
                                                it.uid == dc.document.id
                                            }

                                            if (data != null) {
                                                friendsList.value!!.remove(data)
                                            }

                                            val friendDoc = db.collection("Users").document(dc.document.id).get().await()

                                            if(friendDoc.exists()){
                                                val name = friendDoc.get("name") as? String ?: ""
                                                val profileMessage =
                                                    friendDoc.get("profileMessage") as? String ?: ""

                                                friendsList.value!!.add(
                                                    FriendsDataModel(
                                                        friendDoc.id, name, profileMessage
                                                    )
                                                )
                                            }
                                        }

                                        DocumentChange.Type.REMOVED -> {
                                            val data = friendsList.value!!.find {
                                                it.uid == dc.document.id
                                            }

                                            if (data != null) {
                                                friendsList.value!!.remove(data)
                                            }
                                        }
                                    }
                                }
                            }

                            tasks.awaitAll()
                        }

                        withContext(Dispatchers.Main){
                            completion(true)
                        }
                    } catch(e: Exception){
                        e.printStackTrace()
                        withContext(Dispatchers.Main){
                            completion(false)
                        }
                    }
                }
            }
    }

    fun addFriend(email: String, completion: (Boolean) -> Unit) {
        db.collection("Users").whereEqualTo("email", email).get()
            .addOnSuccessListener { queryResult ->
                if (queryResult.documents.isEmpty()) {
                    completion(false)
                    return@addOnSuccessListener
                } else {
                    val uid = queryResult.documents[0].id

                    db.collection("Users").document(auth.currentUser?.uid ?: "")
                        .collection("Friends").document(uid).set(
                            hashMapOf(
                                "isFriend" to true
                            )
                        ).addOnCompleteListener { friendDoc ->
                            if (friendDoc.isSuccessful) {
                                db.collection("Users").document(uid).collection("Friends")
                                    .document(auth.currentUser?.uid ?: "").set(
                                        hashMapOf(
                                            "isFriend" to true
                                        )
                                    ).addOnCompleteListener {
                                        if (it.isSuccessful) {
                                            completion(true)
                                            return@addOnCompleteListener
                                        } else {
                                            db.collection("Users")
                                                .document(auth.currentUser?.uid ?: "")
                                                .collection("Friends").document(uid).delete()
                                                .addOnCompleteListener {
                                                    completion(false)
                                                    return@addOnCompleteListener
                                                }.addOnFailureListener {
                                                    it.printStackTrace()
                                                    completion(false)
                                                    return@addOnFailureListener
                                                }
                                        }
                                    }
                            } else {
                                completion(false)
                                return@addOnCompleteListener
                            }
                        }.addOnFailureListener {
                            it.printStackTrace()
                            completion(false)
                            return@addOnFailureListener
                        }
                }
            }.addOnFailureListener {
                it.printStackTrace()
                completion(false)
                return@addOnFailureListener
            }
    }
}
