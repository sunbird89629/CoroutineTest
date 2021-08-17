package me.sunbird.coroutinetest

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
  companion object {
    const val REQUEST_CODE_EDIT = 100
    private val TAG = MainActivity::class.simpleName
  }


  val getNewContent = registerForActivityResult(EditContentContract()) { newContent: String? ->
    if (!newContent.isNullOrEmpty()) {
      tvContent.text = newContent
    } else {
      //nothing to do
    }
  }

  val btnEditByTradition by lazy {
    findViewById<Button>(R.id.btn_edit_by_tradition)
  }

  val btnEditByCoroutine by lazy {
    findViewById<Button>(R.id.btn_edit_by_coroutine)
  }

  val btnEditByRegister by lazy {
    findViewById<Button>(R.id.btn_edit_by_register)
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

        //调用EditActivity的 editContent 方法，content为要编辑的内容，editContent 即为编辑后的结果
        val newContent = EditActivity.editContent(this@MainActivity, content)

        if (!newContent.isNullOrBlank()) {
          tvContent.text = newContent
        }
      }
    }

    with(btnEditByRegister) {
      visibility = View.VISIBLE
      setOnClickListener {
        val content = tvContent.text.toString().trim()
        getNewContent.launch(content)
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

  class EditContentContract : ActivityResultContract<String, String?>() {
    override fun createIntent(context: Context, input: String?): Intent {
      return Intent(context, EditActivity::class.java).apply {
        putExtra(EditActivity.ARG_TAG_CONTENT, input)
      }
    }

    override fun parseResult(resultCode: Int, intent: Intent?): String? {
      return if (resultCode == RESULT_OK && intent != null) {
        intent.getStringExtra(EditActivity.RESULT_TAG_NEW_CONTENT)
      } else {
        null
      }
    }
  }
}