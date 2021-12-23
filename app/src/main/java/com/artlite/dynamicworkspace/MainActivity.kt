package com.artlite.dynamicworkspace

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        performName()
        performType()
    }

    private fun performName() {
        val name = DSettings.name()
        DSettings.name.fetch(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
        DSettings.name.update(UUID.randomUUID().toString())
    }

    private fun performType() {
        val type = DSettings.type()
        DSettings.type.observe(this) {
            Toast.makeText(this, "$it", Toast.LENGTH_SHORT).show()
        }
        DSettings.type.update(TType.values()[1])
    }
}