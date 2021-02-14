package com.sample.sliide.presentation

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.sample.sliide.R
import com.sample.sliide.databinding.ActivityMainBinding
import com.sample.sliide.databinding.AddUserDialogBinding

class MainActivity : AppCompatActivity() {

    private val viewModel: UserViewModel by viewModels()

    private lateinit var binding: ActivityMainBinding

    private val usersAdapter = UsersAdapter(mutableListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initView()
        addObservers()
    }

    private fun addObservers() {
        viewModel.users.observe(this, { usersAdapter.addUsers(it) })
        viewModel.errorDialog.observe(this, { showErrorDialog(it) })
        viewModel.addUserDialog.observe(this, { showInputDialog() })
        viewModel.addUser.observe(this, { usersAdapter.addUsers(listOf(it)) })
        viewModel.removeUser.observe(this, { usersAdapter.removeUserWithId(it) })
        viewModel.removeUserDialog.observe(this, { showWarningDialog() })
    }

    private fun showWarningDialog() {
        AlertDialog.Builder(this)
            .setMessage(getString(R.string.user_removal_dialog_message))
            .setPositiveButton(getString(R.string.ok)) { _, _ -> viewModel.onRemoveWarningConfirmed() }
            .create()
            .show()
    }

    private fun showErrorDialog(error: String) {
        AlertDialog.Builder(this)
            .setMessage(error)
            .setTitle(getString(R.string.error))
            .create()
            .show()
    }

    private fun showInputDialog() {
        val dialogBinding = AddUserDialogBinding.inflate(layoutInflater)

        AlertDialog.Builder(this)
            .setView(dialogBinding.root)
            .setTitle(getString(R.string.add_user))
            .setPositiveButton(getString(R.string.add)) { _, _ ->
                onPositiveButtonClick(dialogBinding)
            }
            .create()
            .show()
    }

    private fun onPositiveButtonClick(dialogBinding: AddUserDialogBinding) {
        viewModel.onAddUserDialogClicked(
            dialogBinding.addUserName.text.toString(),
            dialogBinding.addUserEmail.text.toString()
        )
    }

    private fun initView() {
        setSupportActionBar(findViewById(R.id.main_toolbar))
        usersAdapter.addLongOnClickListener(viewModel::onUserViewHolderClick)

        binding.mainContent.mainUserList.adapter = usersAdapter

        binding.mainAddUser.setOnClickListener {
            viewModel.onAddUserClick()
        }
    }
}