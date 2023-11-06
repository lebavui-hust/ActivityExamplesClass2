package vn.edu.hust.activityexamples

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView.AdapterContextMenuInfo
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.view.ActionMode

class MainActivity : AppCompatActivity() {
    val items = arrayListOf<String>()

    var actionMode: ActionMode? = null
    val actionModeCallback = object: ActionMode.Callback {
        override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            menuInflater.inflate(R.menu.sub_menu, menu)
            return true
        }

        override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            return false
        }

        override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
            if (item?.itemId == R.id.action_share) {
                Log.v("TAG", "Share something")
            } else if (item?.itemId == R.id.action_download) {
                Log.v("TAG", "Download file")
            }
            return true
        }

        override fun onDestroyActionMode(mode: ActionMode?) {
            actionMode = null
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val edit1 = findViewById<EditText>(R.id.edit1)
        val edit2 = findViewById<EditText>(R.id.edit2)
        val textResult = findViewById<TextView>(R.id.text_result)

        val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val result = it.data?.getDoubleExtra("result", 0.0)
                textResult.text = "$result"
            } else {
                textResult.text = "Canceled"
            }
        }

        findViewById<Button>(R.id.button_open).setOnClickListener {
            val intent = Intent(this, SecondActivity::class.java)
            intent.putExtra("param1", edit1.text.toString())
            intent.putExtra("param2", edit2.text.toString())
            launcher.launch(intent)


//            val intent = Intent(Intent.ACTION_SEND)
//            intent.type = "text/plain"
//            intent.putExtra(Intent.EXTRA_EMAIL, "lebavui@gmail.com")
//            intent.putExtra(Intent.EXTRA_SUBJECT, "Title")
//            intent.putExtra(Intent.EXTRA_TEXT, "Content")
//            startActivity(intent)
        }

        // registerForContextMenu(findViewById(R.id.imageView))

        repeat(50) {items.add("Item $it")}
        val listView = findViewById<ListView>(R.id.listView)
        listView.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, items)

        registerForContextMenu(listView)

        findViewById<ImageView>(R.id.imageView).setOnClickListener {
            if (actionMode == null)
                actionMode = startSupportActionMode(actionModeCallback)
        }
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.sub_menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val pos = (item.menuInfo as AdapterContextMenuInfo).position
        if (item.itemId == R.id.action_share) {
            Log.v("TAG", "Share ${items[pos]}")
        } else if (item.itemId == R.id.action_download) {
            Log.v("TAG", "Download ${items[pos]}")
        }
        return super.onContextItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_share) {
            Log.v("TAG", "Share something")
        } else if (item.itemId == R.id.action_download) {
            Log.v("TAG", "Download file")
        } else if (item.itemId == R.id.action_settings) {
            Log.v("TAG", "Open settings")
        }

        return super.onOptionsItemSelected(item)
    }
}