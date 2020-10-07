package net.ljubvi.kursadarbslazy



import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.item_todo.view.*
import net.ljubvi.kursadarbslazy.DataClasses.ToDoItem

class ToDoItemRecyclerAdapter(
    private val listener: AdapterClickListener,
    private val items: MutableList<ToDoItem>
) :
    RecyclerView.Adapter<ToDoItemRecyclerAdapter.ToDoViewHolder>() {

    class ToDoViewHolder(view: View) : RecyclerView.ViewHolder(view)



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_todo, parent, false)

        return ToDoViewHolder(view)
    }


    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        val item = items[position]
        val context = holder.itemView.context

        holder.itemView.shoppingName.text = item.name
        holder.itemView.shoppingCheck.isChecked = item.done
        if (item.done){
            val alpha:Float = 0.2f
            holder.itemView.alpha = alpha
        } else holder.itemView.alpha = 1f
        val color_array = mutableListOf<Int>(
            ContextCompat.getColor(context,R.color.green),
            ContextCompat.getColor(context,R.color.blue),
            ContextCompat.getColor(context,R.color.red),
            ContextCompat.getColor(context,R.color.yellow)
        )
        if (item.hasImage) holder.itemView.hasImage.visibility = View.VISIBLE else holder.itemView.hasImage.visibility = View.INVISIBLE

        if (item.color != "") {holder.itemView.setBackgroundColor(color_array[item.color.toInt()])}
       //if (item.color != "") {holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.green))}

        holder.itemView.setOnClickListener {
           // listener.itemClicked(items[position])
        }

        holder.itemView.setOnLongClickListener{

            val popup = PopupMenu(it.shoppingName.context, it.shoppingName)
            popup.inflate(R.menu.longclick_menu)
            popup.setForceShowIcon(true)
            popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { menuitem: MenuItem? ->

                when (menuitem!!.itemId) {
                    R.id.delbutton -> {
                        listener.deleteClicked(item)
                        items.removeAt(position)
                        notifyDataSetChanged()
                        Snackbar.make(it,R.string.deleteditem, Snackbar.LENGTH_SHORT).show()
                    }
                    R.id.editButton -> {
                        listener.itemClicked(item)
                    }
                 }

                true
            })
            popup.show()
            true

        }




        holder.itemView.shoppingCheck.setOnClickListener {

            listener.doneClicked(items[position], position)
            items.sortedWith(compareBy({ items[position].done }, {-items[position].uid  }))
            notifyDataSetChanged()


        }
    }
}