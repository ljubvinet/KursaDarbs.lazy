package net.ljubvi.kursadarbslazy.Activities


import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import kotlinx.android.synthetic.main.activity_main.*
import net.ljubvi.kursadarbslazy.AdapterClickListener
import net.ljubvi.kursadarbslazy.DataClasses.ShoppingItem
import net.ljubvi.kursadarbslazy.Database
import net.ljubvi.kursadarbslazy.R
import net.ljubvi.kursadarbslazy.ToDoItemRecyclerAdapter



class MainActivity : AppCompatActivity(), AdapterClickListener {

    private val refreshType = listOf("full", "visible")

    private val db get() = Database.getInstance(this)
    private val items = mutableListOf<ShoppingItem>()
    private lateinit var adapter: ToDoItemRecyclerAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        refresh("auto")


        adapter = ToDoItemRecyclerAdapter(this, items)
        mainItems.adapter = adapter


        newbutton.setOnClickListener{
            rotateButton(it)
            quickInput()
        }

        newbutton.setOnLongClickListener{
            rotateButton(it)
            val ex:Long = -1
            val intent = Intent(this, DetailActivity::class.java)
                .putExtra(EXTRA_ID, ex)
            startActivityForResult(intent, REQUEST_CODE_DETAILS)
            true
        }

    }

    public fun refresh(refreshType: String = "auto"){
        items.clear()
        if(refreshType == "full") {
            items.addAll(db.shoppingItemDao().getAll())
        }
        if (refreshType == "auto"){

            val sharedPref = this?.getSharedPreferences(PREFERENCES_FILE,Context.MODE_PRIVATE)
            val defaultValue:Boolean = false
            val set = sharedPref.getBoolean(getString(R.string.show_unmarked), defaultValue)


            if (set==true) refresh("limited") else refresh("full")
        }
        if (refreshType == "limited"){
            items.addAll(db.shoppingItemDao().getIncomplete())
        }
    }

    private fun appendItem(itemName: String) {
        val item = ShoppingItem(itemName, false)
        item.uid = db.shoppingItemDao().insertAll(item).first()
        items.add(item)
        items.sortByDescending { it.uid }
        adapter.notifyDataSetChanged()
    }

    override fun itemClicked(item: ShoppingItem) {
        val intent = Intent(this, DetailActivity::class.java)
            .putExtra(EXTRA_ID, item.uid)
        startActivityForResult(intent, REQUEST_CODE_DETAILS)
    }

    override fun itemLongClicked(item: ShoppingItem, position: Int, viem: View) {
    }


    override fun deleteClicked(item: ShoppingItem) {
        if (item.hasImage){
            val u:Uri = item.imageUri.toUri()
            val s:String? = u.lastPathSegment
            val r = this.deleteFile(s)
        }

    db.shoppingItemDao().delete(item)
    }

    override fun doneClicked(item: ShoppingItem, position: Int) {
        item.done = !item.done
        db.shoppingItemDao().update(item)
        items.clear()
        refresh()
        adapter.notifyDataSetChanged()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_DETAILS && resultCode == RESULT_OK && data != null) {
            val id = data.getLongExtra(EXTRA_ID, 0)
            val new = data.getLongExtra(EXTRA_NEW, 0)
            val item = db.shoppingItemDao().getItemById(id)
            println(data)
            if (new.toString()=="-1") { // if new item was created, we need to add it to list
                items.add(item)
                refresh()
            }else{ // if existing item was edited, we need to update it
                val position = items.indexOfFirst { it.uid == item.uid }
                items[position] = item
                adapter.notifyDataSetChanged()
            }
        }
        if (requestCode == REQUEST_CODE_SETTINGS && resultCode == RESULT_OK && data != null) {
            refresh("auto")
            adapter.notifyDataSetChanged()

        }

    }

    companion object {
        const val EXTRA_ID = "net.ljubvi.lazy.kursadarbs.item_id"
        const val EXTRA_NEW = "net.ljubvi.lazy.kursadarbs.item_new"
        const val REQUEST_CODE_DETAILS = 1234
        const val REQUEST_CODE_SETTINGS = 4
        const val PREFERENCES_FILE = "net.ljubvi.lazy.kursadarbs.settings"

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.settings_menu, menu)
        return true
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.incompleteToggle -> {
                item.isChecked = !item.isChecked
                if (item.isChecked) refresh("limited") else refresh("full")
                adapter.notifyDataSetChanged()
                true
            }
            R.id.about -> {
                val intent = Intent(this, AboutActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.setting -> {
                val intent = Intent(this, SettingsActivity::class.java)

                startActivityForResult(intent, REQUEST_CODE_SETTINGS)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun quickInput(){
        val alert: AlertDialog.Builder = AlertDialog.Builder(this)
        var textInput: EditText? = EditText(this)
            alert.setMessage(R.string.input)
                .setTitle(R.string.quick_input)
                .setView(textInput)
                textInput?.requestFocus()
            alert.setNegativeButton(R.string.cancel) { _, _ -> }
            alert.setPositiveButton(R.string.enter){ _, _ ->
                    if (textInput?.text.toString()!="") {
                        appendItem(textInput?.text.toString())
                    } else {
                        Toast.makeText(this, R.string.must_enter, Toast.LENGTH_SHORT).show()
                    }
                }
        val dialog = alert.create()
        dialog.show()
    }


    private fun rotateButton(view: View){
        view.animate().rotationBy(-360f).setDuration(400)
    }




}

