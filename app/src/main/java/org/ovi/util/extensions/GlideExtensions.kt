package org.ovi.util.extensions

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import org.ovi.R
import org.ovi.common.OviApp
import java.net.URL


//Glide
@SuppressLint("CheckResult")
fun ImageView.setProfile(imagePath: Any?) {

    val progressDrawable = CircularProgressDrawable(this.context)
    progressDrawable.strokeWidth = 5f
    progressDrawable.centerRadius = 30f
    progressDrawable.start()

    val options = RequestOptions()
    options.circleCrop()
    options.placeholder(progressDrawable)
    options.fallback(R.drawable.ic_placeholder_profile_circle)
    Glide.with(this.context).load(imagePath).apply(options).into(this)
}


fun AppCompatImageView.loadBanner(imagePath: String?, radius: Int = 5) {

    val options = RequestOptions()
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .placeholder(R.drawable.ic_placeholder_loading)
        .transform(CenterCrop(), RoundedCorners(radius))

    Glide.with(this)
        .asBitmap()
        .load(imagePath)
        .apply(options)
        .into(this)
}

@SuppressLint("CheckResult")
fun AppCompatImageView.setImageRoundCornerCenterCrop(
    imgUrl: Any?,
    radius: Int = 0,
    placeholder: Int //= R.drawable.ic_placeholder_profile_circle
) {

    val options = RequestOptions()
        .placeholder(placeholder)
        .error(placeholder)
        .diskCacheStrategy(DiskCacheStrategy.ALL)

    if (radius > 0)
        options.transform(CenterCrop(), RoundedCorners(radius))

    Glide.with(this.context)
        .load(imgUrl)
        .apply(options)
        .into(this)
}

fun AppCompatImageView.loadImageUrl(url: String) {
    Glide.with(this.context)
        .load(url)
        .into(this)
}

@SuppressLint("CheckResult")
fun AppCompatImageView.setImageRoundCorner(
    imgUrl: Any?,
    radius: Int = 0,
    placeholder: Int = R.drawable.ic_placeholder_profile_circle,
    rotate: Float = -1F
) {

    //Log.d("GlideX", "setImageRoundCorner: imgUrl $imgUrl")

    val options = RequestOptions()
        .placeholder(placeholder)
        .error(placeholder)
        .diskCacheStrategy(DiskCacheStrategy.ALL)

    if (rotate != -1F) {
        Glide.with(this.context)
            .load(imgUrl)
            //.transform(RotateTransformation(context, 90F))
            .apply(options)
            .into(this)
    } else {
        if (radius > 0)
            options.transform(FitCenter(), RoundedCorners(radius))
        Glide.with(this.context)
            .load(imgUrl)
            .apply(options)
            .into(this)

    }

}

fun AppCompatImageView.setImageCircleCrop(
    imgUrl: Any?,
    placeholder: Int = R.drawable.ic_placeholder_profile_circle
) {

    val options = RequestOptions()
        .placeholder(placeholder)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .transform(org.ovi.util.CircularTransformGlide())
        .priority(Priority.HIGH)

    Glide.with(this.context)
        .load(imgUrl)
        .apply(options)
        .into(this)
}

fun ImageView.setImageSq(
    imgUrl: Any?,
    placeholder: Int = R.drawable.ic_placeholder_profile_square
) {

    val options = RequestOptions()
        .placeholder(placeholder)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .transform(RoundedCorners(20))
        .priority(Priority.HIGH)

    Glide.with(this.context)
        .load(imgUrl)
        .apply(options)
        .into(this)
}

fun ImageView.setImage(imgUrl: Any?, placeholder: Int = R.drawable.ic_placeholder_loading) {

    val options = RequestOptions()
        .placeholder(placeholder)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .priority(Priority.HIGH)

    Glide.with(this.context)
        .load(imgUrl)
        .apply(options)
        .into(this)
}

fun ImageView.setImageWithListener(imgUrl: Any?, function: (Boolean) -> Unit) {

    val circularProgressDrawable = CircularProgressDrawable(OviApp.getAppContext())
    circularProgressDrawable.strokeWidth = 5f
    circularProgressDrawable.centerRadius = 30f
    circularProgressDrawable.start()

    val options = RequestOptions()
        .placeholder(circularProgressDrawable)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .error(null)
        .priority(Priority.NORMAL)

    Glide.with(this.context)
        .load(imgUrl)
        .apply(options)
        .listener(object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                circularProgressDrawable.stop()
                function.invoke(true)
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                circularProgressDrawable.stop()
                function.invoke(false)
                return false
            }
        })
        .into(this)
}


fun ImageView.loadProductImageWithThumbnail(imgUrl: String?, thumbUrl: String?) {
    Glide.with(context)
        .load(imgUrl)
        .placeholder(R.drawable.ic_placeholder_loading)
        .thumbnail(
            Glide.with(context)
                .load(thumbUrl)
        )
        .into(this)
}


fun ImageView.loadImageIntoViewWithLoading(imgUrl: String?) {

    val circularProgressDrawable = CircularProgressDrawable(OviApp.getAppContext())
    circularProgressDrawable.strokeWidth = 5f
    circularProgressDrawable.centerRadius = 30f
    circularProgressDrawable.start()

    val options = RequestOptions()
        .placeholder(circularProgressDrawable)
        .error(circularProgressDrawable)
        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)


    Glide.with(this)
        .load(imgUrl)
        .apply(options)
        .into(this)
}



