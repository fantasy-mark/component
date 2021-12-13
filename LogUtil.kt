package easy.coding.common.utils

import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.text.TextUtils
import easy.coding.common.base.SingletonHolder0
import easy.coding.common.support.EasyAndroid
import easy.coding.common.support.EasyLog
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * Mark Rec implementation
 */
class LogUtil private constructor() {
    companion object : SingletonHolder0<LogUtil>(::LogUtil) {
        private var DEFAULT_DIR_PATH: String = "/sdcard/runde_cap/log/"
            set(value) {
                field = value
            }

        @JvmStatic
        fun d() {
            log.d("")
        }

        @JvmStatic
        fun e() {
            log.e("")
        }

        @JvmStatic
        fun d(message: Any?) {
            log.d(message)
        }

        @JvmStatic
        fun e(message: Any?) {
            log.e(message)
        }

        @JvmStatic
        fun record(message: Any?, path: String) {
            if (TextUtils.isEmpty(path)) {
                return
            }

            log.record(message, path)
        }

        @JvmStatic
        fun record(message: Any?) {
            val dateFormat: DateFormat = SimpleDateFormat("yyyy_MM_dd")
            val time: String = dateFormat.format(Date())

            val path = DEFAULT_DIR_PATH + time + ".log"
            log.record(message, path)
        }

        fun d(str1: Any?, str2: Any?) {
            log.d("[$str1] $str2")
        }

        fun e(str1: Any?, str2: Any?) {
            log.e("[$str1] $str2")
        }

        private val log by lazy {
            val builder = EasyLog.newBuilder(LogUtil::class.java.canonicalName)
            builder.debug = true
            builder.addRule(
                "S",
                { trace, _ -> "${trace.methodName} (${trace.fileName ?: "unknown"}:${trace.lineNumber})" })
            builder.singleStyle = "#S #M <By Mark>"
            builder.formatStyle = """
            > #F (By Mark)
            >┌──────#T───────
            >│#M
            >└───────────────
            """.trimMargin(">")
            return@lazy builder.build()
        }
    }
}

//==================================================================================================
fun logTitle() {
    val pm: PackageManager = EasyAndroid.getApplicationContext().packageManager
    var packageInfo: PackageInfo?

    try {
        packageInfo = pm.getPackageInfo(EasyAndroid.getApplicationContext().packageName, 0)
        val versionName: String = packageInfo.versionName
        val versionCode: Int = packageInfo.versionCode
        val dateTime: Long = packageInfo.lastUpdateTime
        val time = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date(dateTime))
        LogUtil.e("\n $versionName (V$versionCode) Install time is $time");
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace();
    }
}

//扩展泛型使用
fun Any.log(msg: Any? = "") {
    LogUtil.e("[$this] $msg")
}