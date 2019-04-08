package com.kumail.showme

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.kumail.hintpod.HintPod
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    // Initializing an empty ArrayList to be filled with animals
    val animals: ArrayList<String> = ArrayList()
    val desc: ArrayList<String> = ArrayList()
    val status: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        HintPod().verify("k5@gmail.com","765678298", "Kumail")

        // Loads animals into the ArrayList
        addAnimals()
        addDesc()
        addStatus()

        // Creates a vertical Layout Manager
        rv_suggestions.layoutManager = LinearLayoutManager(this)

        // Access the RecyclerView Adapter and load the data into it
        rv_suggestions.adapter = SuggestionsAdapter(animals, desc, status, this)


//        btn.setOnClickListener {
//            // Handler code here.
//            Log.d("TAG", "btn pressed")
//            t.start(this, "Here's HintPod")
//        }
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

    // Adds animals to the empty animals ArrayList
    fun addAnimals() {
        animals.add("dog")
        animals.add("cat")
        animals.add("owl")
        animals.add("cheetah")
        animals.add("raccoon")
        animals.add("bird")
        animals.add("snake")
        animals.add("lizard")
        animals.add("hamster")
        animals.add("bear")
        animals.add("lion")
        animals.add("tiger")
        animals.add("horse")
        animals.add("frog")
        animals.add("fish")
        animals.add("shark")
        animals.add("turtle")
        animals.add("elephant")
        animals.add("cow")
        animals.add("beaver")
        animals.add("bison")
        animals.add("porcupine")
        animals.add("rat")
        animals.add("mouse")
        animals.add("goose")
        animals.add("deer")
        animals.add("fox")
        animals.add("moose")
        animals.add("buffalo")
        animals.add("monkey")
        animals.add("penguin")
        animals.add("cheetah")
    }

    fun addDesc() {
        desc.add("brown tiny cute")
        desc.add("brown tiny cute")
        desc.add("brown tiny cute")
        desc.add("brown tiny cute")
        desc.add("brown tiny cute")
        desc.add("brown tiny cute")
        desc.add("brown tiny cute")
        desc.add("brown tiny cute")
        desc.add("brown tiny cute")
    }

    fun addStatus() {
        status.add("Pending")
        status.add("Approved")
        status.add("Rejected")
        status.add("Rejected")
        status.add("Rejected")
        status.add("Rejected")
        status.add("Rejected")
        status.add("Rejected")
        status.add("Rejected")
        status.add("Rejected")
        status.add("Rejected")
        status.add("Rejected")
        status.add("Rejected")
        status.add("Rejected")
        status.add("Rejected")
        status.add("Rejected")
        status.add("Rejected")
        status.add("Rejected")
        status.add("Rejected")
        status.add("Rejected")
        status.add("Rejected")
        status.add("Rejected")
        status.add("Rejected")
        status.add("Rejected")
    }


}
