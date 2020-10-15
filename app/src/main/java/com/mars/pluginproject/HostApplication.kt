package com.mars.pluginproject

import android.app.Application
import android.content.Context

/**
 * Created by geyan on 2020/10/16
 */
class HostApplication : Application() {

     override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        sContext = base
    }

    companion object {

        private var sContext: Context? = null

        val context: Context?
            get() = sContext
    }
}
