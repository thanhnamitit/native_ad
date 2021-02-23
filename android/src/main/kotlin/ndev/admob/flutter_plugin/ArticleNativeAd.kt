package ndev.admob.flutter_plugin

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView

fun <T : View> View.bind(id: Int) = lazy { findViewById<T>(id) }

class ArticleNativeAd @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
        private val nativeAd: NativeAd
) : CardView(context, attrs, defStyleAttr) {
    val adView by bind<NativeAdView>(R.id.ad_view)
    val mediaView by bind<MediaView>(R.id.ad_media)
    val icon by bind<ImageView>(R.id.ad_icon)
    val advertiser by bind<TextView>(R.id.ad_advertiser)
    val headline by bind<TextView>(R.id.ad_headline)
    val body by bind<TextView>(R.id.ad_body)
    val stars by bind<RatingBar>(R.id.ad_stars)
    val callToAction by bind<Button>(R.id.ad_call_to_action)


    init {
        LayoutInflater.from(context).inflate(R.layout.native_ad_song_style_1, this)
        bindViewToAdView()
        bindDataToView()
    }

    private fun bindViewToAdView() {
        adView.mediaView = mediaView
        adView.iconView = icon
        adView.advertiserView = advertiser
        adView.headlineView = headline
        adView.bodyView = body
        adView.starRatingView = stars
        adView.callToActionView = callToAction
    }

    private fun bindDataToView() {

        // Some assets are guaranteed to be in every UnifiedNativeAd.
        mediaView?.setMediaContent(nativeAd.mediaContent)
        mediaView?.setImageScaleType(ImageView.ScaleType.CENTER_INSIDE)

        headline.text = nativeAd.headline
        body?.text = nativeAd.body
        (adView.callToActionView as Button).text = nativeAd.callToAction

        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        val icon = nativeAd.icon

        if (icon == null) {
            adView.iconView.visibility = View.GONE
        } else {
            (adView.iconView as ImageView).setImageDrawable(icon.drawable)
            adView.iconView.visibility = View.VISIBLE
        }

//        if (nativeAd.price == null) {
//            adPrice?.visibility = View.INVISIBLE
//        } else {
//            adPrice?.visibility = View.VISIBLE
//            adPrice?.text = nativeAd.price
//        }
//
//        if (nativeAd.store == null) {
//            adStore?.visibility = View.INVISIBLE
//        } else {
//            adStore?.text = nativeAd.store
//        }

        if (nativeAd.starRating == null) {
            adView.starRatingView.visibility = View.INVISIBLE
        } else {
            (adView.starRatingView as RatingBar).rating = nativeAd.starRating!!.toFloat()
            adView.starRatingView.visibility = View.VISIBLE
        }

        if (nativeAd.advertiser == null) {
            advertiser.visibility = View.INVISIBLE
        } else {
            advertiser.visibility = View.VISIBLE
            advertiser.text = nativeAd.advertiser
        }

        // Assign native ad object to the native view.
        adView.setNativeAd(nativeAd)
    }
}