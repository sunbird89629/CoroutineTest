package me.sunbird.coroutinetest

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    companion object {
        const val REQUEST_CODE_EDIT = 100
    }

    val btnEditByTradition by lazy {
        findViewById<Button>(R.id.btn_edit_by_tradition)
    }

    val btnEditByCoroutine by lazy {
        findViewById<Button>(R.id.btn_edit_by_coroutine)
    }

    val tvContent by lazy {
        findViewById<TextView>(R.id.tv_content)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnEditByTradition.setOnClickListener {
            val content = tvContent.text.toString().trim()
            val intent = Intent(this, EditActivity::class.java).apply {
                putExtra(EditActivity.ARG_TAG_CONTENT, content)
            }
            startActivityForResult(intent, REQUEST_CODE_EDIT)
        }

        btnEditByCoroutine.setOnClickListener {
            GlobalScope.launch {
                val content = tvContent.text.toString().trim()
                val newContent = EditActivity.editContent(this@MainActivity, content)
                if (!newContent.isNullOrBlank()) {
                    tvContent.text = newContent
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_CODE_EDIT -> {
                if (resultCode == RESULT_OK && data != null) {
                    val newContent = data.getStringExtra(EditActivity.RESULT_TAG_NEW_CONTENT)
                    tvContent.text = newContent
                }
            }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }

    }
}