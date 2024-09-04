package com.example.listview

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class MainActivity : AppCompatActivity() {

    private lateinit var nameEditText: EditText
    private lateinit var ageEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var userListView: ListView
    private lateinit var userAdapter: ArrayAdapter<String>
    private val userList = mutableListOf<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolBar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.app_name)

        nameEditText = findViewById(R.id.nameEditText)
        ageEditText = findViewById(R.id.ageEditText)
        saveButton = findViewById(R.id.saveButton)
        userListView = findViewById(R.id.userListView)

        userAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, mutableListOf())
        userListView.adapter = userAdapter

        saveButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val age = ageEditText.text.toString().toIntOrNull()

            if (name.isNotEmpty() && age != null) {
                val user = User(name, age)
                userList.add(user)
                userAdapter.add("${user.name}, ${user.age}")
                nameEditText.text.clear()
                ageEditText.text.clear()
            }
        }

        userListView.setOnItemClickListener { _, _, position, _ ->
            val user = userList[position]
            showDeleteConfirmationDialog(position, user)
        }
    }

    private fun showDeleteConfirmationDialog(position: Int, user: User) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(" MyDialog")
        builder.setMessage("Вы уверены что хотите удалить ${user.name}?")
        builder.setPositiveButton("Да") { _, _ ->
            userList.removeAt(position)
            userAdapter.remove(userAdapter.getItem(position))
        }
        builder.setNegativeButton("Нет") { dialog, _ ->
            dialog.dismiss()
        }
        builder.create().show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.exit -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
