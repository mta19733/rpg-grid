package com.aau.dnd

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.button_connect
import kotlinx.android.synthetic.main.activity_main.connect_indicator
import kotlinx.android.synthetic.main.activity_main.text_status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        button_connect.setOnClickListener {
            connect_indicator.visibility = View.VISIBLE
            button_connect.isEnabled = false

            GlobalScope.launch(Dispatchers.Main) {
                (0..10).forEach { idx ->
                    val prefix = getString(R.string.text_connecting_prefix)
                    val suffix = ".".repeat(idx % 4)
                    val status = prefix + suffix

                    text_status.text = status

                    delay(500)
                }

                startActivity(Intent(this@MainActivity, DataActivity::class.java))

                connect_indicator.visibility = View.GONE
                button_connect.isEnabled = true
                text_status.text = ""
            }
        }
    }
}
