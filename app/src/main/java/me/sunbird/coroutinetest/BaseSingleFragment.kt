package me.sunbird.coroutinetest

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

/**
 * 作者：王豪
 * 日期：2021/7/15
 * 描述：<必填>
 */
class BaseSingleFragment : Fragment() {

    var intentGenerator: ((context: Context) -> Intent) = {
        throw RuntimeException("you should provide a intent here to start activity")
    }

    var resultParser: (resultCode: Int, data: Intent?) -> Unit = { resultCode, data ->
        throw RuntimeException("you should parse result data yourself")
    }

    companion object {
        const val REQUEST_CODE_GET_RESULT = 100
        const val RESULT_ARG_TAG_RESULT = "the_result"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val context = requireContext()
        startActivityForResult(intentGenerator.invoke(context), REQUEST_CODE_GET_RESULT)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_GET_RESULT) {
            resultParser.invoke(resultCode, data)
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }


    /**
     * add current fragment to FragmentManager
     */
    fun addToActivity(fragmentManager: FragmentManager) {
        fragmentManager.beginTransaction().add(this, this::class.simpleName)
            .commitAllowingStateLoss()
    }

    /**
     * remove current fragment from FragmentManager
     */
    fun removeFromActivity(fragmentManager: FragmentManager) {
        fragmentManager.beginTransaction().remove(this).commitAllowingStateLoss()
    }
}
