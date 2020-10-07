package net.ljubvi.kursadarbslazy

import android.view.View
import net.ljubvi.kursadarbslazy.DataClasses.Lecturer
import net.ljubvi.kursadarbslazy.DataClasses.ShoppingItem

interface AdapterClickListener {

    fun itemClicked(item: ShoppingItem)

    fun deleteClicked(item: ShoppingItem)

    fun doneClicked(item: ShoppingItem, position: Int)

    fun itemLongClicked(item: ShoppingItem, position: Int, viem: View)

}

interface LecturerClickListener{
    fun itemClicked(item:Lecturer)
}
