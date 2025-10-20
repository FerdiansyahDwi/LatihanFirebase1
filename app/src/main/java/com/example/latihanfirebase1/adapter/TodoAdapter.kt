package com.example.latihanfirebase1.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.latihanfirebase1.databinding.ItemTodoBinding
import com.example.latihanfirebase1.entity.Todo

class TodoAdapter (
    private val dataset: MutableList<Todo>,
    private val events: TodoItemEvents
) : RecyclerView.Adapter<TodoAdapter.CustomViewHolder>() {

    interface TodoItemEvents {
        fun onDelete(todo: Todo)
        fun onEdit(todo: Todo)
    }

    inner class CustomViewHolder(val view: ItemTodoBinding)
        : RecyclerView.ViewHolder(view.root) {

        fun bindData(item: Todo) {
            view.title.text = item.title
            view.description.text= item.description

            view.root.setOnLongClickListener {
                events.onDelete(todo = item)
                true
            }

            view.root.setOnClickListener {
                events.onEdit(item)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CustomViewHolder {
        val binding = ItemTodoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return CustomViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(
        holder: CustomViewHolder,
        position: Int
    ) {
        val data = dataset[position]
        holder.bindData(data)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newData: List<Todo>) {
        dataset.clear()
        dataset.addAll(newData)
        notifyDataSetChanged()
    }
}