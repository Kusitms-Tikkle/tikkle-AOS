package com.team7.tikkle.roomdb

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "User")
data class User(
    @PrimaryKey
    @ColumnInfo(name = "userAccessToken")
    val userAccessToken: String,
    @ColumnInfo(name = "nickname")
    val nickname: String
)
