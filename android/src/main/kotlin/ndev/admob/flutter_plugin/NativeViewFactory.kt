package ndev.admob.flutter_plugin

import android.content.Context
import io.flutter.plugin.common.StandardMessageCodec
import io.flutter.plugin.platform.PlatformView
import io.flutter.plugin.platform.PlatformViewFactory

class NativeViewFactory(
    private val nativeAdManager: NativeAdManager
) : PlatformViewFactory(StandardMessageCodec.INSTANCE) {
    override fun create(context: Context, viewId: Int, args: Any?): PlatformView {
        val creationParams = args as Map<String?, Any?>
        val adPosition = creationParams["position"] as Int
        val adUnit = creationParams["adUnit"] as String
        val nativeAd = (nativeAdManager.getAds(adUnit)).let {
            it[adPosition % it.size]
        }
        return NativePlatformView(context, nativeAd, creationParams);
    }
}
