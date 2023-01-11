package org.ovi.base

import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import org.ovi.R
import org.ovi.util.extensions.setImageRoundCornerCenterCrop


object DataBindingAdapter {

    @BindingAdapter("android:src")
    fun setImageUri(view: ImageView, imageUri: String?) {
        if (imageUri == null) {
            view.setImageURI(null)
        } else {
            view.setImageURI(Uri.parse(imageUri))
        }
    }

    @BindingAdapter("android:src")
    fun setImageUri(view: ImageView, imageUri: Uri?) {
        view.setImageURI(imageUri)
    }

    @BindingAdapter("android:src")
    fun setImageDrawable(view: ImageView, drawable: Drawable?) {
        view.setImageDrawable(drawable)
    }

    @JvmStatic
    @BindingAdapter("app:src")
    fun setImageResource(imageView: ImageView, resource: Int) {
        imageView.setImageResource(resource)
    }

    @JvmStatic
    @BindingAdapter("app:pic")
    fun setImage(imageView: AppCompatImageView, url : String?) {
//        Log.d("BindingAdapter", "setImage: url $url")
        imageView.setImageRoundCornerCenterCrop(url, 10, R.drawable.ic_placeholder_loading)
    }

    @JvmStatic
    @BindingAdapter("app:setImageRoundedCorner")
    fun setImageRoundedCorner(imageView: AppCompatImageView, url : String?) {
//        Log.d("BindingAdapter", "setImage: url $url")
        imageView.setImageRoundCornerCenterCrop(url, 10,R.drawable.ic_placeholder_loading)
    }

    @JvmStatic
    @BindingAdapter("message", "argId")
    fun setFormattedText(textView: TextView,message : String, argId: Int) {
        textView.text = textView.context.getString(argId, message)
    }

}