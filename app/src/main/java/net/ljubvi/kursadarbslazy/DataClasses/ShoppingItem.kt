package net.ljubvi.kursadarbslazy.DataClasses


import android.graphics.Color
import android.net.Uri
import androidx.core.net.toUri
import androidx.room.*

@Entity(tableName = "shopping_item")
data class ShoppingItem(
    val name: String,
    var done: Boolean = false,
    val color: String = "3",
    val hasImage: Boolean = false,
    val imageUri: String = "",
    @PrimaryKey(autoGenerate = true) var uid: Long = 0
)

@Dao
interface ShoppingItemDao {
    @Query("SELECT * FROM shopping_item ORDER BY done ASC, uid DESC")
    fun getAll(): List<ShoppingItem>

    @Query("SELECT * FROM shopping_item WHERE done = 0 ORDER BY uid DESC")
    fun getIncomplete(): List<ShoppingItem>

    @Query("SELECT * FROM shopping_item WHERE uid = :itemId")
    fun getItemById(itemId: Long): ShoppingItem


    @Insert
    fun insertAll(vararg items: ShoppingItem): List<Long>

    @Update
    fun update(item: ShoppingItem)

    @Delete
    fun delete(item: ShoppingItem)
}