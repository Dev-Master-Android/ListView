package com.example.listview
import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class UserViewModel(application: Application) : AndroidViewModel(application) {
    private val _userList = MutableLiveData<MutableList<User>>(mutableListOf())
    val userList: LiveData<MutableList<User>> get() = _userList

    private val sharedPreferences = application.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    init {
        loadUserList()
    }

    fun addUser(user: User) {
        val currentList = _userList.value ?: mutableListOf()
        currentList.add(user)
        _userList.value = currentList
        saveUserList(currentList)
    }

    fun deleteUser(user: User) {
        val currentList = _userList.value ?: mutableListOf()
        currentList.remove(user)
        _userList.value = currentList
        saveUserList(currentList)
    }

    private fun saveUserList(users: List<User>) {
        val editor = sharedPreferences.edit()
        val userListString = users.joinToString(";") { "${it.name},${it.age}" }
        editor.putString("user_list", userListString)
        editor.apply()
    }

    private fun loadUserList() {
        val userListString = sharedPreferences.getString("user_list", null)
        if (userListString != null) {
            val users = userListString.split(";").map {
                val userData = it.split(",")
                User(userData[0], userData[1].toInt())
            }
            _userList.value = users.toMutableList()
        }
    }
}
