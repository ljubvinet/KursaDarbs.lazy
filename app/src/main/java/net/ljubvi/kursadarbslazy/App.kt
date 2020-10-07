package net.ljubvi.kursadarbslazy

import android.app.Application
import androidx.room.Room

class App : Application() {

    val db by lazy {
        Room.databaseBuilder(this, ToDoDatabase::class.java, "ToDo-db")
            .allowMainThreadQueries()
            .build()
    }
}