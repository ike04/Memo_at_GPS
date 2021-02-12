package com.google.codelab.memoatgps

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import java.util.*

class AddActivity : AppCompatActivity() {
    private lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        realm = Realm.getDefaultInstance()

        val lat = intent.getDoubleExtra("lat", 0.0)
        val lng = intent.getDoubleExtra("lng", 0.0)

        val saveBtn = findViewById<Button>(R.id.saveBtn)
        val cancelBtn = findViewById<Button>(R.id.cancelBtn)
        val memoEdit = findViewById<EditText>(R.id.memoEdit)

        saveBtn.setOnClickListener {
            val memoStr = memoEdit.text?.toString() ?: ""
            realm.executeTransaction {
                val maxId = realm.where<Memo>().max("id")
                val nextId = (maxId?.toLong() ?: 0L) + 1L
                val memo = realm.createObject<Memo>(nextId)
                memo.dateTime = Date()
                memo.lat = lat
                memo.lng = lng
                memo.memo = memoStr
            }
            Toast.makeText(this, "保存しました", Toast.LENGTH_LONG).show()
            finish()
        }

        cancelBtn.setOnClickListener {
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }
}
