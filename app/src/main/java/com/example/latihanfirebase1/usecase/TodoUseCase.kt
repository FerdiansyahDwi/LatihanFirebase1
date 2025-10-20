package com.example.latihanfirebase1.usecase

import com.example.latihanfirebase1.entity.Todo
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class TodoUseCase {
    val db = Firebase.firestore

    suspend fun getTodo(): List<Todo> {
        try {
            val data = db.collection("todo")
                .get()
                .await()

            if (!data.isEmpty) {
                return data.documents.map {
                    Todo(
                        id = it.id,
                        title = it.getString("title").toString(),
                        description = it.getString("description").toString()
                    )
                }
            }
            return arrayListOf<Todo>();
        } catch (exc: Exception) {
            throw Exception(exc.message)
        }
    }

    suspend fun getTodo(id: String): Todo? {
        val data = db.collection("todo")
            .document(id)
            .get()
            .await()

        if (data.exists()) {
            return Todo(
                id = data.id,
                title = data.getString("title").toString(),
                description = data.getString("description").toString()
            )
        }

        return null
    }

    suspend fun createTodo(todo: Todo): Todo {
        val data = hashMapOf(
            "title" to todo.title,
            "description" to todo.description
        )

        try {
            val docRef = db.collection("todo")
                .add(data)
                .await()

            return todo.copy(id = docRef.id)
        } catch (exc: Exception) {
            throw Exception(exc.message)
        }
    }

    suspend fun updateTodo(todo: Todo) {
        val payload = hashMapOf(
            "title" to todo.title,
            "description" to todo.description
        )

        try {
            db.collection("todo")
                .document(todo.id)
                .set(payload)
                .await()
        } catch (exc: Exception) {
            throw Exception(exc.message)
        }
    }

    suspend fun deleteTodo(id: String) {
        try {
            db.collection("todo")
                .document(id)
                .delete()
                .await()
        } catch (exc: Exception) {
            throw Exception(exc.message)
        }
    }
}