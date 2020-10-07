package net.ljubvi.kursadarbslazy

import android.view.View
import net.ljubvi.kursadarbslazy.DataClasses.Lecturer
import net.ljubvi.kursadarbslazy.DataClasses.ToDoItem

interface AdapterClickListener {

    fun itemClicked(item: ToDoItem)

    fun deleteClicked(item: ToDoItem)

    fun doneClicked(item: ToDoItem, position: Int)

    fun itemLongClicked(item: ToDoItem, position: Int, viem: View)

}

interface LecturerClickListener{
    fun itemClicked(item:Lecturer)
}
