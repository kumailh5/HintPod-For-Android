package com.kumail.sample

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kumail.hintpod.HintPod

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        HintPod().verify("k5@gmail.com", "68815057670", "JG5PbMdemnUKbu2wBNJjCvRLlr23", "Kumail")
        HintPod().setTitle("HintPod")
        HintPod().setHintPodTheme(R.style.AppTheme)
        // Loads animals into the ArrayList
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_toolbar, menu)
        return true
    }

    // actions on click menu items
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_search -> {
            msgShow("Search")
            true
        }
        R.id.action_hintpod -> {
            HintPod().start(this)
            true
        }
        R.id.action_setting -> {
            msgShow("Setting")
            true
        }
        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }

    fun msgShow(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }


}
