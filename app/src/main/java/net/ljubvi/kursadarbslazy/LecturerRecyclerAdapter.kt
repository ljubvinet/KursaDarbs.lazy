package net.ljubvi.kursadarbslazy

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_lecturer.view.*
import net.ljubvi.kursadarbslazy.DataClasses.Lecturer



class LecturerRecyclerAdapter (
    private val listener: LecturerClickListener,
    private val items: MutableList<Lecturer>
    ) :
    RecyclerView.Adapter<LecturerRecyclerAdapter.LecturerViewHolder>() {

    class LecturerViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):LecturerRecyclerAdapter.LecturerViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_lecturer, parent, false)

        return LecturerViewHolder(view)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: LecturerRecyclerAdapter.LecturerViewHolder, position: Int) {

        val item = items[position]
        val context = holder.itemView.context
        holder.itemView.lecturer.text = item.name.toString()
        Toast.makeText(context,"bound!",Toast.LENGTH_SHORT).show()

        holder.itemView.setOnClickListener {
             listener.itemClicked(items[position])

        }

    }


}