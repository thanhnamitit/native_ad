package ndev.admob.flutter_plugin

import android.content.Context
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import io.flutter.app.FlutterApplication
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result

enum class CallMethod {
    loadNativeAd,
    loadOpenAd,
    showOpenAd,
    dispose
}

/** FlutterPlugin */
class FlutterPlugin : FlutterPlugin, MethodCallHandler {

    private lateinit var channel: MethodChannel
    private lateinit var context: Context
    private lateinit var messenger: BinaryMessenger
    private val nativeAdManager by lazy { NativeAdManager(context, messenger) }
    private var appOpenAd: AppOpenAd? = null
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
            CallMethod.dispose -> {
                val adUnit = requireNotNull(call.argument<String>("adUnit"))
                nativeAdManager.removeController(adUnit)
            }
            CallMethod.loadOpenAd -> {
                val adUnit = requireNotNull(call.argument<String>("adUnit"))
                val key = requireNotNull(call.argument<String>("key"))
                loadOpenAd(adUnit, key)
            }
            CallMethod.showOpenAd -> {
                val key = requireNotNull(call.argument<String>("key"))
                showAppOpenAd(key)
            }
        }
    }

    private fun loadNativeAds(adUnit: String, key: String, numOfAd: Int) {
        nativeAdManager.load(adUnit, key, numOfAd)
    }

    private fun loadOpenAd(adUnit: String, key: String) {
        Log.i("NativeAdManager", "Loading open ad: $adUnit")
        val methodChannel = MethodChannel(messenger, key)
        val request = AdRequest.Builder().build()
        AppOpenAd.load(
                context, adUnit, request,
                AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, object : AppOpenAd.AppOpenAdLoadCallback() {
            override fun onAdFailedToLoad(p0: LoadAdError) {
                super.onAdFailedToLoad(p0)
                Log.i("NativeAdManager", "onAdFailedToLoad openAd")
                methodChannel.invokeMethod("onAdFailedToLoad", null)
            }

            override fun onAdLoaded(appOpenAd: AppOpenAd) {
                super.onAdLoaded(appOpenAd)
                Log.i("NativeAdManager", "onAdLoaded openAd")
                methodChannel.invokeMethod("onAdLoaded", null)
                this@FlutterPlugin.appOpenAd = appOpenAd
            }
        })
    }

    private fun showAppOpenAd(key: String) {
        val methodChannel = MethodChannel(messenger, key)
        appOpenAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdFailedToShowFullScreenContent(p0: AdError?) {
                methodChannel.invokeMethod("onAdDismissed", null)
                super.onAdFailedToShowFullScreenContent(p0)
            }

            override fun onAdDismissedFullScreenContent() {
                methodChannel.invokeMethod("onAdDismissed", null)
                super.onAdDismissedFullScreenContent()
            }
        }
        appOpenAd?.show((context as FlutterApplication).currentActivity)
    }
}
