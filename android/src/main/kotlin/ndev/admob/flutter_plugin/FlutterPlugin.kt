package ndev.admob.flutter_plugin

import android.content.Context
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result

enum class CallMethod {
    loadNativeAd,
    dispose
}

/** FlutterPlugin */
class FlutterPlugin : FlutterPlugin, MethodCallHandler {

    private lateinit var channel: MethodChannel
    private lateinit var context: Context
    private lateinit var messenger: BinaryMessenger
    private val nativeAdManager by lazy { NativeAdManager(context, messenger) }
    override fun onAttachedToEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        messenger = binding.binaryMessenger
        context = binding.applicationContext
        channel = MethodChannel(messenger, "ndev.plugin.admob")
        channel.setMethodCallHandler(this)
        binding.platformViewRegistry.registerViewFactory(
                "ndev.plugin.admob/native_ad",
                NativeViewFactory(nativeAdManager)
        )
    }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }

    override fun onMethodCall(call: MethodCall, result: Result) {
        when (CallMethod.valueOf(call.method)) {
            CallMethod.loadNativeAd -> {
                val numOfAd = call.argument<Int>("numOfAds") ?: 1
                val key = requireNotNull(call.argument<String>("key"))
                val adUnit = requireNotNull(call.argument<String>("adUnit"))
                loadNativeAds(adUnit, key, numOfAd)
            }
            else -> {
                val adUnit = requireNotNull(call.argument<String>("adUnit"))
                nativeAdManager.removeController(adUnit)
            }
        }
    }

    private fun loadNativeAds(adUnit: String, key: String, numOfAd: Int) {
        nativeAdManager.load(adUnit, key, numOfAd)
    }
}
