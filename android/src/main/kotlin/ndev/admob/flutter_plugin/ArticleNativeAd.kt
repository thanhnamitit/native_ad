package ndev.admob.flutter_plugin

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions.bitmapTransform
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import jp.wasabeef.glide.transformations.BlurTransformation

fun <T : View> View.bind(id: Int) = lazy { findViewById<T>(id) }

class ArticleNativeAd @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    private val nativeAd: NativeAd,
    titleColor: Long? = null,
    accentColor: Long? = null
) : FrameLayout(context, attrs, defStyleAttr) {
    val adView by bind<NativeAdView>(R.id.ad_view)


    init {
        LayoutInflater.from(context).inflate(R.layout.ad_unified, this)
        adView.mediaView = adView.findViewById<MediaView>(R.id.ad_media)
        adView.headlineView = adView.findViewById(R.id.ad_headline)
        adView.bodyView = adView.findViewById(R.id.ad_body)
        adView.callToActionView = adView.findViewById(R.id.ad_call_to_action)
        adView.iconView = adView.findViewById(R.id.ad_app_icon)
        adView.priceView = adView.findViewById(R.id.ad_price)
        adView.starRatingView = adView.findViewById(R.id.ad_stars)
        adView.storeView = adView.findViewById(R.id.ad_store)
        adView.advertiserView = adView.findViewById(R.id.ad_advertiser)

        // The headline and media content are guaranteed to be in every UnifiedNativeAd.

        val textColor = titleColor?.toInt() ?: Color.WHITE
        val accentColor = accentColor?.toInt() ?: Color.WHITE
        (adView.headlineView as TextView).apply {
            text = nativeAd.headline
            setTextColor(textColor)
        }

        adView.mediaView.setMediaContent(nativeAd.mediaContent)

        if (nativeAd.body == null) {
            adView.bodyView.visibility = View.INVISIBLE
        } else {
            adView.bodyView.visibility = View.VISIBLE
            (adView.bodyView as TextView).apply {
                text = nativeAd.body
                setTextColor(textColor)
            }
        }

        if (nativeAd.callToAction == null) {
            adView.callToActionView.visibility = View.GONE
        } else {
            adView.callToActionView.visibility = View.VISIBLE
            (adView.callToActionView as Button).apply {
                text = nativeAd.callToAction
//                setBackgroundColor(accentColor)
            }
        }

        val iconView = adView.iconView as ImageView

        if (nativeAd.icon != null) {
            iconView.setImageDrawable(
                nativeAd.icon.drawable
            )
        } else if (nativeAd.mediaContent.mainImage != null) {
            iconView.setImageDrawable(
                nativeAd.mediaContent.mainImage
            )
            iconView.scaleType = ImageView.ScaleType.CENTER_CROP
        }

        if (nativeAd.price == null) {
            adView.priceView.visibility = View.GONE
        } else {
            adView.priceView.visibility = View.VISIBLE
            (adView.priceView as TextView).apply {
                text = nativeAd.price
                setTextColor(textColor)
            }
        }

        if (nativeAd.store == null) {
            adView.storeView.visibility = View.GONE
        } else {
            adView.storeView.visibility = View.VISIBLE
            (adView.storeView as TextView).apply {
                text = nativeAd.store
                setTextColor(textColor)
            }
        }

        if (nativeAd.starRating == null) {
            adView.starRatingView.visibility = View.GONE
        } else {
            (adView.starRatingView as RatingBar).apply {
                rating = nativeAd.starRating!!.toFloat()
//                DrawableCompat.setTint(progressDrawable, accentColor)
            }
            adView.starRatingView.visibility = View.VISIBLE
        }

        if (nativeAd.advertiser == null) {
            adView.advertiserView.visibility = View.GONE
        } else {
            (adView.advertiserView as TextView).apply {
                text = nativeAd.advertiser
                setTextColor(textColor)
            }
            adView.advertiserView.visibility = View.VISIBLE
        }

        val blurImage = adView.findViewById<ImageView>(R.id.blur_image)

        Glide.with(this)
            .load(nativeAd.mediaContent.mainImage)
            .apply(bitmapTransform(BlurTransformation(25, 3)))
            .into(blurImage)

        adView.setNativeAd(nativeAd)
    }
}