package com.sbl.homework.week_06.chat.models

data class ChatDataModel(
    val id: String,
    val message: String,
    val sender: String,
    val sentTime: String,
    val sentTimeFull: String,
    val isMine: Boolean
)
