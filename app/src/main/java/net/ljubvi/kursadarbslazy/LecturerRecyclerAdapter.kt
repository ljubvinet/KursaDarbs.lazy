package net.ljubvi.kursadarbslazy

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_lecturer.view.*
import net.ljubvi.kursadarbslazy.DataClasses.Lecturer



class LecturerRecyclerAdapter(
    private val listener: LecturerClickListener,
    private val items: MutableList<Lecturer>
) :
    RecyclerView.Adapter<LecturerRecyclerAdapter.LecturerViewHolder>() {

    class LecturerViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LecturerViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_lecturer, parent, false)
        return LecturerViewHolder(view)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: LecturerViewHolder, position: Int) {
        val item = items[position]
        val context = holder.itemView.context
        holder.itemView.lecturer.text = item.name


        holder.itemView.setOnClickListener {
            listener.itemClicked(items[position])
        }


    }
}