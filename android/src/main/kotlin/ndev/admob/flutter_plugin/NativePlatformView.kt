package ndev.admob.flutter_plugin

import android.content.Context
import android.view.View
import com.google.android.gms.ads.nativead.NativeAd
import io.flutter.plugin.platform.PlatformView


class NativePlatformView(
    context: Context,
    nativeAd: NativeAd,
    creationParams: Map<String?, Any?>
) : PlatformView {

    private val view: View

    init {
        view = ArticleNativeAd(
            context = context,
            nativeAd = nativeAd,
            titleColor = creationParams["titleColor"] as? Long?,
            accentColor = creationParams["accentColor"] as? Long?
        )
    }

    override fun getView(): View = view

    override fun dispose() {
    }
}