package net.ljubvi.kursadarbslazy

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import net.ljubvi.kursadarbslazy.DataClasses.ShoppingItem
import net.ljubvi.kursadarbslazy.DataClasses.ShoppingItemDao

@Database(version = 1, entities = [ShoppingItem::class])
abstract class ShoppingDatabase : RoomDatabase() {

    abstract fun shoppingItemDao(): ShoppingItemDao

}

object Database {

    private var instance: ShoppingDatabase? = null

    fun getInstance(context: Context) = instance ?: Room.databaseBuilder(
        context.applicationContext, ShoppingDatabase::class.java, "ToDo-db"
    )
        .allowMainThreadQueries()
        .build()
        .also { instance = it }
}