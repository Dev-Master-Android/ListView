package com.example.listview

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.listview.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var nameEditText: EditText
    private lateinit var ageEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var userListView: ListView
    private lateinit var toolbar: Toolbar
    private lateinit var binding: ActivityMainBinding
    private lateinit var userAdapter: ArrayAdapter<String>
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toolbar = binding.toolBar
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.app_name)

        nameEditText = binding.nameEditText
        ageEditText = binding.ageEditText
        saveButton = binding.saveButton
        userListView = binding.userListView

        userAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            mutableListOf()
        )

        userListView.adapter = userAdapter

        userViewModel.userList.observe(this) { users ->
            userAdapter.clear()
            userAdapter.addAll(users.map { "${it.name} (${it.age})" })
            userAdapter.notifyDataSetChanged()
        }

        saveButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val age = ageEditText.text.toString().toIntOrNull()
            if (name.isNotEmpty() && age != null) {
                val user = User(name, age)
                userViewModel.addUser(user)
                nameEditText.text.clear()
                ageEditText.text.clear()
            }
        }

        userListView.setOnItemClickListener { _, _, position, _ ->
            val user = userViewModel.userList.value?.get(position)
            if (user != null) {
                showDeleteConfirmationDialog(user)
            }
        }
    }

    private fun showDeleteConfirmationDialog(user: User) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.my_dialog))
        builder.setMessage(getString(R.string.are_you_sure, user.name))
        builder.setPositiveButton(getString(R.string.yes)) { _, _ ->
            userViewModel.deleteUser(user)
            Toast.makeText(this,    getString(R.string.delete, user.name), Toast.LENGTH_LONG).show()
        }
        builder.setNegativeButton(getString(R.string.no)) { dialog, _ ->
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
