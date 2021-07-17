package me.sunbird.coroutinetest

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class EditActivity : AppCompatActivity() {
    companion object {
        const val ARG_TAG_CONTENT = "content"
        const val RESULT_TAG_NEW_CONTENT = "new_content"

        @JvmStatic
        suspend fun editContent(activity: FragmentActivity, content: String): String? =
            suspendCoroutine { continuation ->
                val editFragment = BaseSingleFragment().apply {
                    intentGenerator = {
                        Intent(it, EditActivity::class.java).apply {
                            putExtra(ARG_TAG_CONTENT, content)
                        }
                    }
                    resultParser = { resultCode, data ->
                        if (resultCode == RESULT_OK && data != null) {
                            val result = data.getStringExtra(RESULT_TAG_NEW_CONTENT)
                            continuation.resume(result)
                        }
                    }
                }
                editFragment.addToActivity(activity.supportFragmentManager)
            }
    }

    private val etContent by lazy {
        findViewById<EditText>(R.id.et_content)
    }

    private val btnSave by lazy {
        findViewById<Button>(R.id.btn_save)
    }

    private val mContent by lazy {
        intent.getStringExtra(ARG_TAG_CONTENT)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        etContent.setText(mContent)

        btnSave.setOnClickListener {
            val newContent = etContent.text.toString().trim()
            setResult(RESULT_OK, Intent().apply {
                putExtra(RESULT_TAG_NEW_CONTENT, newContent)
            })
            finish()
        }
    }
}