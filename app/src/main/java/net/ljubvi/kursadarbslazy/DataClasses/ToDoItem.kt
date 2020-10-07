package net.ljubvi.kursadarbslazy.DataClasses


import androidx.room.*

@Entity(tableName = "todo_item")
data class ToDoItem(
    val name: String,
    var done: Boolean = false,
    val color: String = "3",
    val hasImage: Boolean = false,
    val imageUri: String = "",
    @PrimaryKey(autoGenerate = true) var uid: Long = 0
)

@Dao
interface ToDoItemDao {
    @Query("SELECT * FROM todo_item ORDER BY done ASC, uid DESC")
    fun getAll(): List<ToDoItem>

    @Query("SELECT * FROM todo_item WHERE done = 0 ORDER BY uid DESC")
    fun getIncomplete(): List<ToDoItem>

    @Query("SELECT * FROM todo_item WHERE uid = :itemId")
    fun getItemById(itemId: Long): ToDoItem


    @Insert
    fun insertAll(vararg items: ToDoItem): List<Long>

    @Update
    fun update(item: ToDoItem)

    @Delete
    fun delete(item: ToDoItem)
}