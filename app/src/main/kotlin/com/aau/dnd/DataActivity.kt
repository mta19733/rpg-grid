package com.aau.dnd

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_data.button_send_data
import kotlinx.android.synthetic.main.activity_data.text_status

class DataActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_data)

        button_send_data.setOnClickListener {
            text_status.text = getString(R.string.text_sending_data)
        }
    }
}
