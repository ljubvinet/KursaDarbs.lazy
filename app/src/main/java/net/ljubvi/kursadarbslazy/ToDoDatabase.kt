package net.ljubvi.kursadarbslazy

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import net.ljubvi.kursadarbslazy.DataClasses.ToDoItem
import net.ljubvi.kursadarbslazy.DataClasses.ToDoItemDao

@Database(version = 1, entities = [ToDoItem::class])
abstract class ToDoDatabase : RoomDatabase() {

    abstract fun ToDoItemDao(): ToDoItemDao

}

object Database {

    private var instance: ToDoDatabase? = null

    fun getInstance(context: Context) = instance ?: Room.databaseBuilder(
        context.applicationContext, ToDoDatabase::class.java, "ToDo-db"
    )
        .allowMainThreadQueries()
        .build()
        .also { instance = it }
}