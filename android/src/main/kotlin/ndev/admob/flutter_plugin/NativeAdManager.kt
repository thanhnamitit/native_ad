package ndev.admob.flutter_plugin

import android.content.Context
import android.util.Log
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodChannel

class NativeAdManager(private val context: Context, private val messenger: BinaryMessenger) {

    private val map = HashMap<String, List<NativeAd>>()

    fun load(adUnit: String, key: String, numOfAds: Int) {
        Log.i("NativeAdManager", "Loading native ad: $adUnit")
        map[adUnit] = emptyList()
        val channel = MethodChannel(messenger, key)
        AdLoader.Builder(context, adUnit)
                .forNativeAd { ad ->
                    val newAds = map[adUnit].orEmpty().plus(ad)
                    Log.i("NativeAdManager", "loaded: ${newAds.size} for $adUnit")
                    map[adUnit] = newAds
                    channel.invokeMethod(
                            "numOfAdsChanged",
                            mapOf("num" to newAds.size)
                    )
                }.withAdListener(object : AdListener() {
                    override fun onAdFailedToLoad(p0: LoadAdError?) {
                        super.onAdFailedToLoad(p0)
                        Log.i("NativeAdManager", "onAdFailedToLoad: ${p0?.message}")
                    }
                })
                .withNativeAdOptions(NativeAdOptions.Builder().build())
                .build().loadAds(AdRequest.Builder().build(), numOfAds)
    }

    fun removeController(adUnit: String) {
        map[adUnit].orEmpty().forEach { it.destroy() }
        map.remove(adUnit)
    }

    fun getAds(adUnit: String) = map[adUnit].orEmpty()
}