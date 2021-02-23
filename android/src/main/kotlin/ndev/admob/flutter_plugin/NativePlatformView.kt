package ndev.admob.flutter_plugin

import android.content.Context
import android.view.View
import com.google.android.gms.ads.nativead.NativeAd
import io.flutter.plugin.platform.PlatformView


class NativePlatformView(
        context: Context,
        private val nativeAd: NativeAd
) : PlatformView {

    private val view: View

    init {
        view = ArticleNativeAd(context = context, nativeAd = nativeAd)
    }

    override fun getView(): View = view

    override fun dispose() {}
}