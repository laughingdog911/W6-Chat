package com.sbl.homework.week_06.chat.helper

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.sbl.homework.week_06.chat.models.ChatDataModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ChatHelper {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    companion object {
        var chatList = MutableLiveData<MutableList<ChatDataModel>>().apply {
            value = mutableListOf()
        }
    }

    private fun createId(opponent: String, completion: (String) -> Unit) {
        val docRef = db.collection("Chat").document()

        docRef.set(
            mapOf(
                "participants" to listOf(auth.currentUser?.uid ?: "", opponent),
                "isActive" to true
            )
        )
            .addOnCompleteListener {
                completion(docRef.id)
                return@addOnCompleteListener
            }.addOnFailureListener {
                it.printStackTrace()
                completion("")
                return@addOnFailureListener
            }
    }

    private fun getId(opponent: String, completion: (String) -> Unit) {
        db.collection("Chat")
            .whereArrayContains("participants", auth.currentUser?.uid ?: "")
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val snapshot = it.result

                    if(snapshot.documents.isNotEmpty()){
                        for(document in snapshot.documents){
                            if((document.get("participants") as ArrayList<String>).contains(opponent)){
                                completion(document.id)
                                return@addOnCompleteListener
                            }
                        }

                        createId(opponent){
                            completion(it)
                            return@createId
                        }
                    } else{
                        createId(opponent){
                            completion(it)
                            return@createId
                        }
                    }
                } else {
                    createId(opponent) {
                        completion(it)
                        return@createId
                    }
                }
            }
            .addOnFailureListener {
                it.printStackTrace()
                createId(opponent) {
                    completion(it)
                    return@createId
                }
            }
    }

    private fun convertDate(dateAsString: String): String {
        val dateFormatterOriginal = SimpleDateFormat("MM. dd. yyyy. kk:mm:ss.SSSS", Locale.US)
        val date = dateFormatterOriginal.parse(dateAsString)

        val dateFormatterForDay = SimpleDateFormat("MM. dd. yyyy.", Locale.US)
        val current = dateFormatterForDay.format(Date())
        val dateAsDay = dateFormatterForDay.format(date)

        if(current == dateAsDay){
            val dateFormatter = SimpleDateFormat("kk:mm", Locale.US)

            return dateFormatter.format(date)
        } else {
            val dateFormatter = SimpleDateFormat("MM. dd. kk:mm", Locale.US)

            return dateFormatter.format(date)
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun getMessageList(opponent: String, completion: (Boolean) -> Unit) {
        chatList.value!!.clear()

        getId(opponent){
            if(it == ""){
                completion(false)
                return@getId
            } else {
                db.collection("Chat").document(it).collection("Messages").addSnapshotListener { value, error ->
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
                        try {
                            coroutineScope {
                                val tasks = value.documentChanges.map{ dc ->
                                    async {
                                        when(dc.type){
                                            DocumentChange.Type.ADDED, DocumentChange.Type.MODIFIED -> {
                                                val data = chatList.value!!.find {
                                                    it.id == dc.document.id
                                                }

                                                if(data != null){
                                                    chatList.value!!.remove(data)
                                                }

                                                val id = dc.document.id
                                                val message = dc.document.get("message") as? String ?: ""
                                                val sender = dc.document.get("sender") as? String ?: ""
                                                val sentTimeFull = dc.document.get("sentTime") as? String ?: ""
                                                val sentTime = convertDate(sentTimeFull)
                                                val isMine = (sender == auth.currentUser?.uid ?: "")

                                                chatList.value!!.add(
                                                    ChatDataModel(
                                                        id, message, sender, sentTime, sentTimeFull, isMine
                                                    )
                                                )
                                            }

                                            DocumentChange.Type.REMOVED -> {
                                                val data = chatList.value!!.find {
                                                    it.id == dc.document.id
                                                }

                                                if(data != null){
                                                    chatList.value!!.remove(data)
                                                } else {

                                                }
                                            }
                                        }
                                    }
                                }

                                tasks.awaitAll()
                            }

                            withContext(Dispatchers.Main){
                                chatList.value!!.sortBy {
                                    it.sentTimeFull
                                }

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
        }
    }

    fun sendMessage(opponent: String, message: String, completion: (Boolean) -> Unit){
        val dateFormatter = SimpleDateFormat("MM. dd. yyyy. kk:mm:ss.SSSS", Locale.US)

        getId(opponent){
            if(it == ""){
                completion(false)
                return@getId
            }

            db.collection("Chat").document(it).collection("Messages").add(
                mapOf(
                    "sender" to auth.currentUser?.uid,
                    "message" to message,
                    "sentTime" to dateFormatter.format(Date())
                )
            ).addOnCompleteListener {
                completion(it.isSuccessful)
            }.addOnFailureListener {
                it.printStackTrace()
                completion(false)
                return@addOnFailureListener
            }
        }
    }
}