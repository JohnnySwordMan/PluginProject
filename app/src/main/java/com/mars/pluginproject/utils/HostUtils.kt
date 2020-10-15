package com.mars.pluginproject.utils

import android.content.Context
import android.content.res.AssetManager
import com.mars.pluginproject.HostApplication
import java.io.*


/**
 * Created by geyan on 2020/10/16
 */

object HostUtils {
    /**
     * 把Assets里面得文件复制到 /data/data/files 目录下
     *
     * @param context
     * @param sourceName
     */
    fun extractAssets(context: Context, sourceName: String?) {
        val am: AssetManager = context.getAssets()
        var `is`: InputStream? = null
        var fos: FileOutputStream? = null
        try {
            `is` = am.open(sourceName!!)
            val extractFile: File = context.getFileStreamPath(sourceName)
            fos = FileOutputStream(extractFile)
            val buffer = ByteArray(1024)
            var count = 0
            while (`is`.read(buffer).also({ count = it }) > 0) {
                fos.write(buffer, 0, count)
            }
            fos.flush()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            closeSilently(`is`)
            closeSilently(fos)
        }
    }

    /**
     * 待加载插件经过opt优化之后存放odex得路径
     */
    fun getPluginOptDexDir(packageName: String): File? {
        return enforceDirExists(File(getPluginBaseDir(packageName), "odex"))
    }

    /**
     * 插件得lib库路径, 这个demo里面没有用
     */
    fun getPluginLibDir(packageName: String): File? {
        return enforceDirExists(File(getPluginBaseDir(packageName), "lib"))
    }

    // --------------------------------------------------------------------------
    private fun closeSilently(closeable: Closeable?) {
        if (closeable == null) {
            return
        }
        try {
            closeable.close()
        } catch (e: Throwable) {
            // ignore
        }
    }

    private var sBaseDir: File? = null

    // 需要加载得插件得基本目录 /data/data/<package>/files/plugin/
    private fun getPluginBaseDir(packageName: String): File? {
        if (sBaseDir == null) {
            sBaseDir = HostApplication.context!!.getFileStreamPath("plugin")
            enforceDirExists(sBaseDir!!)
        }
        return enforceDirExists(File(sBaseDir, packageName))
    }

    @Synchronized
    private fun enforceDirExists(sBaseDir: File): File? {
        if (!sBaseDir.exists()) {
            val ret: Boolean = sBaseDir.mkdir()
            if (!ret) {
                throw RuntimeException("create dir " + sBaseDir + "failed")
            }
        }
        return sBaseDir
    }
}