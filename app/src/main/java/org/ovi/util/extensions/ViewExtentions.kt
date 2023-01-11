package org.ovi.util.extensions

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.ContextWrapper
import android.content.res.Configuration
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Parcelable
import android.util.DisplayMetrics
import android.util.SparseArray
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.TranslateAnimation
import android.view.inputmethod.InputMethodManager
import android.view.inputmethod.InputMethodManager.SHOW_IMPLICIT
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.view.children
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import org.ovi.util.DarkTheme
import org.ovi.util.GridLayoutManagerWrapper
import org.ovi.util.LinearLayoutManagerWrapper
import com.klinker.android.link_builder.Link
import com.klinker.android.link_builder.applyLinks
import org.ovi.R
import java.util.*


fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}


fun View.animHide() {
    animGone(1)
}

fun ViewGroup.inflater() = LayoutInflater.from(context)


fun AppCompatTextView.showIfNotNull(txt: String?) {
    if (txt.isNullOrEmpty())
        gone()
    else {
        text = txt
        show()
    }
}

fun View.showIfNotNull(txt: String?) {
    if (txt.isNullOrEmpty())
        gone()
    else
        show()
}

fun Toolbar.enableScroll() {
    val params = layoutParams as AppBarLayout.LayoutParams
    params.apply {
        scrollFlags =
            AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS
    }
}

fun Toolbar.disabledScroll() {
    val params = layoutParams as AppBarLayout.LayoutParams
    params.scrollFlags = 0
}

fun View.animGone(type: Int = 0) {

    if (visibility != View.VISIBLE)
        return

    animate().alpha(0f).setDuration(300).setListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator) {
            super.onAnimationEnd(animation)
            visibility = View.GONE
        }
    })
}


fun NestedScrollView.scrollToBottomNow(toView: View?) {
    toView?.let {
        postDelayed({
            scrollTo(0, it.bottom)
        }, 100)
    }
}

fun NestedScrollView.scrollToBottom(toView: View) {
    postDelayed({
        scrollTo(0, toView.bottom)
    }, 500)
}

fun NestedScrollView.scrollToTop(toView: View) {
    smoothScrollTo(0, toView.y.toInt())
}


fun Array<ViewGroup>.showAnimAll() {
    this.forEach {
        it.animShow()
    }
}

fun Array<ViewGroup>.goneAnimAll() {
    this.forEach {
        it.animGone()
    }
}

fun Array<View>.showAll() {
    this.forEach {
        it.show()
    }
}

fun Array<View>.goneAll() {
    this.forEach {
        it.gone()
    }
}


fun View.isVisible() = visibility == View.VISIBLE

fun View.animShow() {

    if (visibility == View.VISIBLE)
        return

    animate().alpha(1f).setDuration(300).setListener(object : AnimatorListenerAdapter() {
        override fun onAnimationStart(animation: Animator) {
            super.onAnimationStart(animation)
            visibility = View.VISIBLE
        }
    })
}

// slide the view from below itself to the current position
fun View.expandView() {
    visibility = View.VISIBLE
    val animate = TranslateAnimation(
        0F,  // fromXDelta
        0F,  // toXDelta
        height.toFloat(),  // fromYDelta
        0F
    ) // toYDelta
    animate.duration = 300
    animate.fillAfter = true
    startAnimation(animate)
}

// slide the view from its current position to below itself
fun slideDown(view: View) {
    val animate = TranslateAnimation(
        0F,  // fromXDelta
        0F,  // toXDelta
        0F,  // fromYDelta
        view.height.toFloat()
    ) // toYDelta
    animate.duration = 500
    animate.fillAfter = true
    view.startAnimation(animate)
}

fun ViewGroup.getInflatedLayout(layoutToInflate: Int): View =
    LayoutInflater.from(context).inflate(layoutToInflate, this, false)

fun Context.getInflatedLayout(layoutToInflate: Int): View? {
    val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    return inflater.inflate(layoutToInflate, null)
}

fun AppCompatImageView.rotateClockWise() {
    val anim = ObjectAnimator.ofFloat(this, "rotation", 180f, 0f)
    anim.duration = 300
    anim.start()
}

fun AppCompatImageView.rotateAntiClockWise() {
    val anim = ObjectAnimator.ofFloat(this, "rotation", 0f, 180f)
    anim.duration = 500
    anim.start()
}

fun View?.findSuitableParent(): ViewGroup? {
    var view = this
    var fallback: ViewGroup? = null
    do {
        if (view is CoordinatorLayout) {
            // We've found a CoordinatorLayout, use it
            return view
        } else if (view is FrameLayout) {
            if (view.id == android.R.id.content) {
                // If we've hit the decor content view, then we didn't find a CoL in the
                // hierarchy, so use it.
                return view
            } else {
                // It's not the content view but we'll use it as our fallback
                fallback = view
            }
        }

        if (view != null) {
            // Else, we will loop and crawl up the view hierarchy and try to find a parent
            val parent = view.parent
            view = if (parent is View) parent else null
        }
    } while (view != null)

    // If we reach here then we didn't find a CoL or a suitable content view so we'll fallback
    return fallback
}

fun RecyclerView.verticalView(context: Context, stackEnd: Boolean = false) {
    layoutManager = LinearLayoutManagerWrapper(context).apply { stackFromEnd = stackEnd }
    layoutManager
}

fun RecyclerView.horizontalView(context: Context) {
    layoutManager = LinearLayoutManagerWrapper(context, LinearLayoutManager.HORIZONTAL, false)
}

fun RecyclerView.gridView(context: Context, spanCount: Int) {
    layoutManager = GridLayoutManagerWrapper(context, spanCount)
}

fun AppCompatImageView.setUnlocked() {
    colorFilter = null
    imageAlpha = 255
}

inline fun <T : Any> guardLet(vararg elements: T?, closure: () -> Nothing): List<T> {
    return if (elements.all { it != null }) {
        elements.filterNotNull()
    } else {
        closure()
    }
}

inline fun <T : Any> ifLet(vararg elements: T?, closure: (List<T>) -> Unit) {
    if (elements.all { it != null }) {
        closure(elements.filterNotNull())
    }
}


fun View.setMargins(left: Int? = null, top: Int? = null, right: Int? = null, bottom: Int? = null) {
    val lp = layoutParams as? ViewGroup.MarginLayoutParams ?: return
    lp.setMargins(
        left ?: lp.leftMargin,
        top ?: lp.topMargin,
        right ?: lp.rightMargin,
        bottom ?: lp.rightMargin
    )
    this.layoutParams = lp
}

fun BottomNavigationView.uncheckAllItems() {
    menu.setGroupCheckable(0, true, false)
    for (i in 0 until menu.size()) {
        menu.getItem(i).isChecked = false
    }
    menu.setGroupCheckable(0, true, true)
}


enum class TYPE {
    SUCCESS, FAILURE, VALIDATION, NONE
}

fun Float.toDp(displayMetrics: DisplayMetrics) =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, displayMetrics)


fun Context.dp(px: Number): Float {
    val resources = this.resources
    val metrics = resources.displayMetrics
    return (px.toFloat() /
            (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT.toFloat()))
}

fun Context.px(dp: Number): Float {
    val resources = this.resources
    val metrics = resources.displayMetrics
    return (dp.toFloat() *
            (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT.toFloat()))
}


fun Context.getOviDrawable(res: Int): Drawable? {
    return ContextCompat.getDrawable(this, res)
}

fun Context.getOviColor(res: Int): Int {
    return ContextCompat.getColor(this, res)
}

fun Context.getOviFont(res: Int): Typeface {
    return ResourcesCompat.getFont(this, res)!!
}

@SuppressLint("AppBundleLocaleChanges")
fun Context.getOviString(@StringRes stringRes: Int, vararg formatArgs: Any): String {
    val configuration = Configuration(resources.configuration)
    configuration.setLocale(Locale(org.ovi.util.LocaleHelper.getLanguage(this)))
    return createConfigurationContext(configuration).resources.getString(stringRes, *formatArgs)
}

fun Context.showToast(text: String?) {
    Toast.makeText(this, text, Toast.LENGTH_LONG).show()
}


fun TabLayout.setPaddingX(){
    val tabs = getChildAt(0) as ViewGroup

    for (i in 0 until tabs.childCount ) {
        val tab = tabs.getChildAt(i)
        val layoutParams = tab.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.topMargin = 10.px
        layoutParams.leftMargin = 10.px
        layoutParams.rightMargin = 10.px
        tab.layoutParams = layoutParams
        tab.requestLayout()
    }
}

fun TabLayout.setTabBG(tabStartRes: Int, tabCenterRes: Int, tabEndRes: Int) {
    val tabStrip = getChildAt(0) as ViewGroup

    tabStrip.getChildAt(0)?.let {
        val paddingStart = it.paddingStart
        val paddingTop = it.paddingTop
        val paddingEnd = it.paddingEnd
        val paddingBottom = it.paddingBottom
        ViewCompat.setBackground(
            it,
            AppCompatResources.getDrawable(it.context, tabStartRes)
        )
        ViewCompat.setPaddingRelative(
            it,
            paddingStart,
            paddingTop,
            paddingEnd,
            paddingBottom
        )
    }
    tabStrip.getChildAt(1)?.let {

        val paddingStart = it.paddingStart
        val paddingTop = it.paddingTop
        val paddingEnd = it.paddingEnd
        val paddingBottom = it.paddingBottom
        ViewCompat.setBackground(
            it,
            AppCompatResources.getDrawable(it.context, tabCenterRes)
        )
        ViewCompat.setPaddingRelative(
            it,
            paddingStart,
            paddingTop,
            paddingEnd,
            paddingBottom
        )
    }
    tabStrip.getChildAt(2).let {
        val paddingStart = it.paddingStart
        val paddingTop = it.paddingTop
        val paddingEnd = it.paddingEnd
        val paddingBottom = it.paddingBottom
        ViewCompat.setBackground(
            it,
            AppCompatResources.getDrawable(it.context, tabEndRes)
        )
        ViewCompat.setPaddingRelative(
            it,
            paddingStart,
            paddingTop,
            paddingEnd,
            paddingBottom
        )
    }
}

fun EditText.showKeyboard2() {
    if (requestFocus()) {
        (getActivity()?.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager)
            .showSoftInput(this, SHOW_IMPLICIT)
        setSelection(text.length)
    }
}


fun View.getActivity(): AppCompatActivity? {
    var context = this.context
    while (context is ContextWrapper) {
        if (context is AppCompatActivity) {
            return context
        }
        context = context.baseContext
    }
    return null
}

fun ViewGroup.restoreChildViewStates(childViewStates: SparseArray<Parcelable>) {
    children.forEach { child -> child.restoreHierarchyState(childViewStates) }
}

fun ViewGroup.saveChildViewStates(): SparseArray<Parcelable> {
    val childViewStates = SparseArray<Parcelable>()
    children.forEach { child -> child.saveHierarchyState(childViewStates) }
    return childViewStates
}


val LIST_CHAT: HashMap<String, Int> = HashMap()


fun getGoto(goto: String): Int? = LIST_CHAT[goto]

fun View.setSelectedIfDarkTheme(context: Context) {
    isSelected = DarkTheme.isDarkThemeEnabled(context)
}


fun View.applyWrapMatchContent() {
    layoutParams = RecyclerView.LayoutParams(
        ViewGroup.LayoutParams.WRAP_CONTENT,
        ViewGroup.LayoutParams.MATCH_PARENT
    )
}

fun View.applyMatchWrapContent() {
    layoutParams = ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )
}

fun View.applyMatchParent() {
    layoutParams = ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT
    )
}

fun View.applyWrapContent() {
    layoutParams = ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.WRAP_CONTENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )
}

fun View.applyCustomHeight(height: Int) {
    layoutParams = ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )
    layoutParams.height = height.px
}

fun View.applyCustomWidthHeight(width: Int, height: Int) {
    layoutParams = ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.WRAP_CONTENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )
    layoutParams.width = width.px
    layoutParams.height = height.px
}

fun NestedScrollView.handleToolbar(view : View){
    setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
        if (scrollY > oldScrollY) {
            //Log.i(TAG, "Scroll DOWN");
            view.hide()
        }
        if (scrollY < oldScrollY) {
            //Log.i(TAG, "Scroll UP");
            view.show()
        }

        if (scrollY == 0) {
            //Log.i(TAG, "TOP SCROLL");
        }

        if (scrollY == ((v as ViewGroup).getChildAt(0).measuredHeight - v.measuredHeight)) {
            //Log.i(TAG, "BOTTOM SCROLL");
        }
    }
}


fun AppCompatTextView.applyTextHyperLink(
    entries: Array<CharSequence>,
    linkTextColor: Int = R.color.purple_100,
    isUnderLined: Boolean = true,
    hyperLinkTextFont: Int,// = R.font.archer_book,
    onItemClicked: (String?) -> Unit
) {
    val listOfHyperLinks = arrayListOf<Link>()

    entries.forEach { txt ->
        val link = Link(txt.toString())
            .setTextColor(ContextCompat.getColor(context, linkTextColor))
            .setUnderlined(isUnderLined)
            .setTypeface(ResourcesCompat.getFont(context, hyperLinkTextFont)!!)
            .setOnClickListener {
                onItemClicked.invoke(it)
            }
        listOfHyperLinks.add(link)
    }

    if (listOfHyperLinks.isNotEmpty()) {
        this.applyLinks(listOfHyperLinks)
    }
}


fun RecyclerView.handleToolbar(view : View){
    addOnScrollListener(object : RecyclerView.OnScrollListener(){
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            when {
                dx > 0 -> {
                    println("Scrolled Right")
                }
                dx < 0 -> {
                    println("Scrolled Left")
                }
                else -> {
                    println("No Horizontal Scrolled")
                }
            }

            when {
                dy > 0 -> {
                    println("Scrolled Downwards")
                    view.show()
                }
                dy < 0 -> {
                    println("Scrolled Upwards")
                    view.hide()
                }
                else -> {
                    println("No Vertical Scrolled")
                }
            }
        }
    })
}

fun View.hasAttended(tv: AppCompatTextView) {
    this.isClickable=false
    this.isEnabled=false
    this.alpha=0.3f
    tv.compoundDrawablePadding=16
    tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_correct,0,0,0)
}
fun View.notAttended(tv: AppCompatTextView) {
    this.isClickable=true
    this.isEnabled=true
    this.alpha=1f
    tv.compoundDrawablePadding=0
    tv.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0)
}



