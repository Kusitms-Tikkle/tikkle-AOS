package com.team7.tikkle.roomdb

import android.provider.ContactsContract.CommonDataKinds.Nickname
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class UserViewModel(private val userDao: UserDao) : ViewModel()  {
    fun insert(user: User) {
        viewModelScope.launch {
            userDao.insert(user)
        }
    }
    suspend fun getUser(userAccessToken: String): User? {
        return userDao.getUser(userAccessToken)
    }
    suspend fun getNickname(nickname: String): User? {
        return userDao.getNickname(nickname)
    }


    class Factory(private val userDao: UserDao) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(UserViewModel::class.java)){
                @Suppress("UNCHECKED_CAST")
                return UserViewModel(userDao) as T
            }
            throw java.lang.IllegalArgumentException("Unknown ViewModel Class")
        }
    }
}