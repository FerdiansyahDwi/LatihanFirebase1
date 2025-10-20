package com.example.latihanfirebase1

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.latihanfirebase1.databinding.ActivityEditTodoBinding
import com.example.latihanfirebase1.entity.Todo
import com.example.latihanfirebase1.usecase.TodoUseCase
import kotlinx.coroutines.launch

class EditTodoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditTodoBinding
    private lateinit var todoUseCase: TodoUseCase
    private lateinit var todoItemId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityEditTodoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        todoItemId = intent.getStringExtra("todo_item_id").toString()
        todoUseCase = TodoUseCase()
        registerEvents()
    }

    fun loadTodo() {
        lifecycleScope.launch {
            val todo = todoUseCase.getTodo(todoItemId)
            if (todo == null) {
                displayMessage("Data yang akan di edit tidak tersedia")
                back()
            }

            binding.title.setText(todo?.title)
            binding.description.setText(todo?.description)
        }
    }

    fun back() {
        val intent = Intent(this@EditTodoActivity, TodoActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onStart() {
        super.onStart()
        loadTodo()
    }

    fun registerEvents() {
        binding.tombolEdit.setOnClickListener {
            lifecycleScope.launch {
                val title = binding.title.text.toString()
                val description = binding.description.text.toString()
                val payload = Todo(
                    id = todoItemId,
                    title = title,
                    description = description,
                )

                try {
                    todoUseCase.updateTodo(payload)
                    displayMessage("Berhasil memperbarui data")
                    back()
                } catch (exc: Exception) {
                    displayMessage("Gagal memperbarui data : ${exc.message}")
                }
            }
        }
    }

    fun displayMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}