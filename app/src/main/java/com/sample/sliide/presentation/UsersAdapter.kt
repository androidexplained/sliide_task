package com.sample.sliide.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sample.sliide.R
import com.sample.sliide.databinding.UserViewHolderBinding
import com.sample.sliide.domain.User

class UsersAdapter(
    private val dataSet: MutableList<User>
) : RecyclerView.Adapter<UsersAdapter.ViewHolder>() {

    private lateinit var onLongClickListener: (Int) -> Unit

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding: UserViewHolderBinding = UserViewHolderBinding.bind(view)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.user_view_holder, viewGroup, false)

        return ViewHolder(view)
    }

    fun addLongOnClickListener(onClickListener: (Int) -> Unit) {
        this.onLongClickListener = onClickListener
    }

    fun addUsers(user: List<User>) {
        dataSet.addAll(0, user)
        notifyDataSetChanged()
    }

    fun removeUserWithId(id: Int) {
        dataSet.removeAll { it.id == id }
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.binding.run {
            dataSet[position].let { user ->
                userName.text = user.name
                userEmail.text = user.email
                userStatus.text = viewHolder.itemView.context.getString(
                    R.string.created_hours_ago,
                    user.createdHoursAgo
                )
                userRoot.setOnLongClickListener {
                    onLongClickListener(user.id)
                    true
                }
            }
        }
    }

    override fun getItemCount() = dataSet.size
}
