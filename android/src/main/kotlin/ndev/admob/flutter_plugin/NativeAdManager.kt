package ndev.admob.flutter_plugin

import android.content.Context
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodChannel

class NativeAdManager(private val context: Context, private val messenger: BinaryMessenger) {

    private val map = HashMap<String, List<NativeAd>>()

    fun load(adUnit: String, key: String, numOfAds: Int) {
        map[adUnit] = emptyList()
        val channel = MethodChannel(messenger, key)
        AdLoader.Builder(context, adUnit)
                .forNativeAd { ad ->
                    val newAds = map[adUnit].orEmpty().plus(ad)
                    map[adUnit] = newAds
                    channel.invokeMethod(
                            "numOfAdsChanged",
                            mapOf("num" to newAds.size)
                    )
                }
                .withNativeAdOptions(NativeAdOptions.Builder().build())
                .build().loadAds(AdRequest.Builder().build(), numOfAds)
    }

    fun removeController(adUnit: String) {
        map[adUnit].orEmpty().forEach { it.destroy() }
        map.remove(adUnit)
    }

    fun getAds(adUnit: String) = map[adUnit].orEmpty()
}