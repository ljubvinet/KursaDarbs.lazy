package net.ljubvi.kursadarbslazy.Activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils.split
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_about.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_lecturer.*

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
        val f = mutableListOf(resources.getStringArray(R.array.lecturers))

        var item:Lecturer

        for (strings in f) {
            for (string in strings) {
               var f = split(string,",")
                    item = Lecturer(f[0],f[1])
                    items.add(item)
            }


        }

        adapter = LecturerRecyclerAdapter(listener = this, items = items)
        lecturers.adapter = adapter




    }

     fun sendSMS(number:String, body:String) {
        val sms_uri: Uri = Uri.parse("smsto:$number")
        val sms_intent = Intent(Intent.ACTION_SENDTO, sms_uri)
        sms_intent.putExtra("sms_body", body)
        startActivity(sms_intent)
    }


override fun itemClicked(item:Lecturer){
    Toast.makeText(this,item.phone,Toast.LENGTH_SHORT).show()
    sendSMS(item.phone,"Paldies!!!")
}

    // sendSMS("+37126366670","Tests2")
}