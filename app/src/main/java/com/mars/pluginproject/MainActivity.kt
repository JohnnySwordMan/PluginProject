package com.mars.pluginproject

import android.content.Context
import android.content.res.AssetManager
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mars.pluginproject.utils.HostUtils
import com.mars.pluginproject.utils.log
import com.mars.toolapi.IDynamic
import dalvik.system.DexClassLoader
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.Exception

class MainActivity : AppCompatActivity() {

    private lateinit var mClassLoader: DexClassLoader
    private lateinit var mDexPath: String
    private var mAssetManager: AssetManager? = null
    private var mResources: Resources? = null
    private var mTheme: Resources.Theme? = null

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
        try {
            HostUtils.extractAssets(newBase!!, TOOL_APK_NAME)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val extractFile = getFileStreamPath(TOOL_APK_NAME)
        mDexPath = extractFile.path
        val fileRelease = getDir("dex", Context.MODE_PRIVATE)
        mClassLoader = DexClassLoader(mDexPath, fileRelease.absolutePath, null, classLoader)

        log("extractFile = $extractFile, mDexPath = $mDexPath, fileRelease = $fileRelease")

        loadResInPlugin.setOnClickListener {
            loadResources()
            try {
                val dynamicClazz = mClassLoader.loadClass("com.mars.tool.Dynamic")
                val dynamic = dynamicClazz.newInstance() as IDynamic
                val tipsFromPlugin = dynamic.getStringForResId(this)
                log("tipsFromPlugin = $tipsFromPlugin")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }

    private fun loadResources() {
        try {
            val assetManager = AssetManager::class.java.newInstance()
            val addAssetPath = assetManager.javaClass.getMethod("addAssetPath", String::class.java)
            addAssetPath.invoke(assetManager, mDexPath)
            mAssetManager = assetManager
        } catch (e: Exception) {
            e.printStackTrace()
        }
        mResources = Resources(
            mAssetManager,
            super.getResources().displayMetrics,
            super.getResources().configuration
        )
        mTheme = mResources?.newTheme()
        mTheme?.setTo(super.getTheme())
    }

    override fun getAssets(): AssetManager {
        if (mAssetManager == null) {
            return super.getAssets();
        }
        return mAssetManager!!
    }

    override fun getResources(): Resources {
        if (mResources == null) {
            return super.getResources()
        }
        return mResources!!
    }

    override fun getTheme(): Resources.Theme {
        return mTheme ?: super.getTheme()
    }


    companion object {

        private const val TOOL_APK_NAME = "tool-debug.apk"
    }
}