package com.mars.tool

import android.content.Context
import com.mars.toolapi.IDynamic

/**
 * Created by geyan on 2020/10/16
 */

class Dynamic : IDynamic {

    override fun getStringForResId(context: Context): String {
        return context.resources.getString(R.string.hello_tool_plugin)
    }

}