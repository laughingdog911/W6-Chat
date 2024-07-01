package com.sbl.homework.week_06.userManagement.models

enum class UserTypeModel {
    STUDENT {
        override fun getString() = "Student"
    },

    PROFESSOR {
        override fun getString() = "Professor"
    },

    EMPLOYEE {
        override fun getString() = "Employee"
    };

    abstract fun getString(): String
}