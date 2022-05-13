package wao.flutter.application.project.android_gallery_picker

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.webkit.WebView
import android.widget.Toast
import androidx.annotation.NonNull
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry

/** AndroidGalleryPickerPlugin */
class AndroidGalleryPickerPlugin: FlutterPlugin, MethodCallHandler, ActivityAware {
  private lateinit var channel : MethodChannel
  private lateinit var context: Context
  private lateinit var activity:Activity


  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    context = flutterPluginBinding.applicationContext
    channel = MethodChannel(flutterPluginBinding.binaryMessenger, "flutter.io/gallery")
    channel.setMethodCallHandler(this)
  }

  override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
    if (call.method == "gallery") {
        activity.startActivityForResult(Intent(context, GalleryActivity::class.java), 105)
        UtilProject.result = result
        var obj:Map<String,String>? = call.arguments()
        UtilProject.multiPick = obj!!["multiPick"]
        if(obj["limitMultiPick"] != null) {
          UtilProject.limitMultiPick = (obj!!["limitMultiPick"])!!.toInt()
        }
        else {
          UtilProject.limitMultiPick = 3
        }
        UtilProject.titleAppBar = obj!!["titleAppBar"]
        UtilProject.colorAppBar = obj!!["colorAppBar"]
    } else {
      result.notImplemented()
    }
  }

  override fun onDetachedFromActivity() {
//     TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
//     TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun onAttachedToActivity(binding: ActivityPluginBinding) {
    activity = binding.activity
  }

  override fun onDetachedFromActivityForConfigChanges() {
//     TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }
  override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    channel.setMethodCallHandler(null)
  }
}

object UtilProject {
  var webViewUrl: String = ""
  var colorAppBar:String? = null
  var titleAppBar:String? = null
  var multiPick:String? = null
  var limitMultiPick:Int = 3
  var result: MethodChannel.Result? = null
  var pendingResult: MethodChannel.Result? = null
  var webView: WebView? = null
  var webViewHeight:String? = null

  fun onDestroy() {
    result = null
    colorAppBar = null
    titleAppBar = null
    webView = null
    webViewHeight = null
  }

  fun reloadWebView() {
    val handler = Handler()
    handler.postDelayed({ webView?.loadDataWithBaseURL(null, UtilProject.webViewUrl, "text/html", "UTF-8", null) }, 10)
  }
}
