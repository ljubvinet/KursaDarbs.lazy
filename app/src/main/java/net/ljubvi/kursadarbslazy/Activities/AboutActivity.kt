package net.ljubvi.kursadarbslazy.Activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils.split
import kotlinx.android.synthetic.main.activity_about.*

import net.ljubvi.kursadarbslazy.DataClasses.Lecturer
import net.ljubvi.kursadarbslazy.LecturerClickListener

import net.ljubvi.kursadarbslazy.LecturerRecyclerAdapter
import net.ljubvi.kursadarbslazy.R



class AboutActivity : AppCompatActivity(), LecturerClickListener {
    private val items = mutableListOf<Lecturer>()
    private lateinit var adapter: LecturerRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        getActionBar()?.setDisplayHomeAsUpEnabled(true)
        adapter = LecturerRecyclerAdapter(listener = this, items = items)
        lecturers.adapter = adapter

        val f = mutableListOf(resources.getStringArray(R.array.lecturers))

        var item:Lecturer

        for (strings in f) {
            for (string in strings) {
               val f1 = split(string,",")
                    item = Lecturer(f1[0],f1[1])
                items.add(item)
            }
        }
        adapter.notifyDataSetChanged()
    }

     fun sendSMS(number:String, body:String) {
        val sms_uri: Uri = Uri.parse("smsto:$number")
        val sms_intent = Intent(Intent.ACTION_SENDTO, sms_uri)
        sms_intent.putExtra("sms_body", body)
        startActivity(sms_intent)
    }


    override fun itemClicked(item:Lecturer){
        sendSMS(item.phone,"Paldies!!! :)")
    }
}