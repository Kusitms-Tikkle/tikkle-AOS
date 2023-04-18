package com.team7.tikkle.roomdb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User)

    @Query("SELECT * FROM user WHERE userAccessToken = :userAccessToken")
    suspend fun getUser(userAccessToken: String): User?

    @Query("SELECT * FROM user WHERE nickname = :nickname")
    suspend fun getNickname(nickname: String): User?

}