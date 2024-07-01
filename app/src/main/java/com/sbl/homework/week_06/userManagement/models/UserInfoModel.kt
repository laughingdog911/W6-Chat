package com.sbl.homework.week_06.userManagement.models

data class UserInfoModel(
    val uid: String,
    val email: String,
    val name: String,
    val phoneNumber: String,
    var userType: UserTypeModel
)
