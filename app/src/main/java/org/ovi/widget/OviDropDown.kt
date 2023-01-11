package org.ovi.widget

import android.content.Context
import android.content.res.TypedArray
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.util.Log
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.hollywoodfeed.widgets.popup.MaterialPopupMenu
import org.ovi.R
import org.ovi.data.local.PopupModel
import org.ovi.databinding.WidgetOviDropDownBinding
import org.ovi.util.extensions.*
import org.ovi.util.popup.ViewBoundCallback
import org.ovi.util.popup.popupMenuBuilder

class OviDropDown : ConstraintLayout {

    lateinit var binding: WidgetOviDropDownBinding

    private var popupItems: ArrayList<PopupModel>? = null
    var iDropDownListener: IDropDownListener? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, attrSet: AttributeSet) : super(context, attrSet) {
        init(attrSet)
    }

    constructor(context: Context, attrSet: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrSet,
        defStyleAttr
    ) {
        init(attrSet)
    }

    private fun init(attrSet: AttributeSet) {
        binding = WidgetOviDropDownBinding.inflate(LayoutInflater.from(context), this, true)
        val typedArray = context.obtainStyledAttributes(attrSet, R.styleable.OviDropDown)
        assignProps(typedArray)
        typedArray.recycle()
    }

    fun showDropDown() {
//        binding.dropDownLay.show()
        binding.ivDropDown.isEnabled = true
        binding.dropDownLay.isEnabled=true
        binding.selectedItem.isEnabled=true
    }

    fun setError(error: String) {
        getTV().error = error
        binding.lLayContent.requestFocus()
        getTV().isFocusable=true
        getTV().isFocusableInTouchMode=true
    }

    fun removeError() {
        getTV().error = null
    }

    fun hideDropDown() {
        binding.dropDownLay.gone()
    }

    fun setTvLabel(title:String){
        binding.tvHint.text=title
    }

    fun disableDropDown() {
        binding.ivDropDown.isEnabled = false
        binding.dropDownLay.isEnabled=false
        binding.selectedItem.isEnabled=false
//        hideDropDown()
    }

    private fun assignProps(typedArray: TypedArray) {
        with(typedArray) {
            setHint(getString(R.styleable.OviDropDown_dd_hint))
            setTitle(getString(R.styleable.OviDropDown_dd_title))
            setHint(getString(R.styleable.OviDropDown_dd_default_txt))
            setIconTint(getResourceId(R.styleable.OviDropDown_android_tint, -1))
            setTextColor(getResourceId(R.styleable.OviDropDown_android_textColor, -1))
            setBackgroundD(getResourceId(R.styleable.OviDropDown_dd_background, -1))
            setLabelColor(getColor(R.styleable.OviDropDown_dd_label_color, -1))
            setFontFamily(getResourceId(R.styleable.OviDropDown_android_fontFamily,-1))
            handleEditState()
            initView()
        }
    }

    private fun setFontFamily(resourceId: Int) {
        if (resourceId!=-1){
            binding.tvHint.typeface = context.getOviFont(resourceId)
//            binding.selectedItem.typeface = context.getOviFont(resourceId)
        }

    }


    private fun setLabelColor(color: Int) {
        if (color != -1)
            binding.tvHint.setTextColor(color)

    }

    private fun setBackgroundD(resourceId: Int) {

        if (resourceId != -1)
            binding.lLayContent.background = context.getOviDrawable(resourceId)

    }

    private fun setTextColor(resourceId: Int) {
        if (resourceId != -1)
            binding.selectedItem.setTextColor(ContextCompat.getColor(context, resourceId))
    }

    private fun setIconTint(resourceId: Int) {
        if (resourceId != -1)
            binding.ivDropDown.setColorFilter(ContextCompat.getColor(context, resourceId))

    }


    fun isOptionSelected(): Boolean {
        popupItems?.forEach {
            if (it.isSelected && it.id != 0)
                return true
        }
        return false
    }

    fun getSelectedOption(): String? {
        popupItems?.forEach {
            if (it.isSelected && it.id != 0)
                return it.title
        }
        return null
    }

    fun getSelectedOption2(): String? {
        popupItems?.forEach {
            if (it.isSelected)
                return it.title
        }
        return null
    }

    fun loadPopupItems(list: List<String>) {
        val popupList = ArrayList<PopupModel>()
        list.forEachIndexed { index, s ->
            popupList.add(PopupModel(index, s, index == 0, false))
        }
        popupItems = popupList

        arrayOf(binding.dropDownLay, binding.selectedItem, binding.ivDropDown).forEach {
            it.setOnClickListener { vw ->
                iDropDownListener?.onDDClicked()
                buildPopup(
                    popupItems!!,
                    ::onPopupItemClicked,
                    true,
                    (binding.vwBtm.height * 300) + 30
                ).show(context, binding.vwBtm)
            }
        }
    }

    private fun initView() {

    }

    private fun onPopupItemClicked(popupModel: PopupModel) {
        binding.selectedItem.apply {
            iDropDownListener?.onOptionClicked(binding.root, popupModel)
            text = popupModel.title
            removeError()
            setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.black_1000
                )
            )
        }
    }

//    fun moveBgToTop() {
//        binding.lLayContent.background = null
//        binding.cLayDD.background = context.getOviDrawable(R.drawable.bg_white_bordered)
//    }

    private fun buildPopup(
        menuItems: ArrayList<PopupModel>,
        callBack: (PopupModel) -> Unit,
        withSelection: Boolean = true,
        widthSize: Int
    ): MaterialPopupMenu {
        return popupMenuBuilder {
            menuItems.forEachIndexed { parentIndex, popupModel ->
                section {
                    customItem {
                        layoutResId = R.layout.inflater_popup_menu
                        viewBoundCallback = ViewBoundCallback { view ->
                            val tv = view.findViewById<TextView>(R.id.tvPopupHeader)
                            tv?.apply {
                                //handleColor(this, popupModel)
                                text = popupModel.title
                                setOnClickListener {
                                    if (withSelection) {
                                        menuItems.forEachIndexed { index, popupModel ->
                                            popupModel.isSelected = (index == parentIndex)
                                        }
                                        handleColor(this, popupModel)
                                    } else {
                                        popupModel.isSelected = false
                                        handleColor(this, popupModel)
                                    }
                                    callBack.invoke(popupModel)
                                    dismissPopup()
                                }
                            }
                        }
                    }
                }
            }
            fixedContentWidthInPx = widthSize
        }.build()
    }

    private fun handleColor(textView: TextView, popupModel: PopupModel) {

        val color: Int = when {
            popupModel.isSelected -> R.color.grey_300
            !popupModel.isSelected -> R.color.black_1000
            else -> R.color.black_1000
        }
        textView.setTextColor(ContextCompat.getColor(context, color))
    }

    private fun handleEditState(
    ) {

    }

    private fun setTitle(title: String?) {
        title?.run {
            if (isNotEmpty())
                binding.tvHint.text = this
        }
    }

    private fun setHint(hint: String?) {
        hint?.run {
            if (isNotEmpty())
                binding.selectedItem.apply {
                    text = hint
                    setTextColor(ContextCompat.getColor(context, R.color.grey_500))
                }
        }
    }

    fun isTxtEmpty(): Boolean = getText().trim().isEmpty()
    fun getText(): String = binding.selectedItem.text.toString().trim()

    fun setText(txt: String?) {
        popupItems?.forEach {
            it.isSelected = (it.title == txt)
        }
        getTV().apply {
            text = txt
            setTextColor(ContextCompat.getColor(context, R.color.black_1000))
        }
    }

    private fun getTV(): AppCompatTextView = binding.selectedItem


    public override fun onSaveInstanceState(): Parcelable {
        return SavedState(super.onSaveInstanceState()).apply {
            childrenStates = saveChildViewStates()
            //here
        }
    }

    public override fun onRestoreInstanceState(state: Parcelable) {
        when (state) {
            is SavedState -> {
                super.onRestoreInstanceState(state.superState)
                state.childrenStates?.let { restoreChildViewStates(it) }
            }
            else -> super.onRestoreInstanceState(state)
        }
    }

    override fun dispatchSaveInstanceState(container: SparseArray<Parcelable>) {
        dispatchFreezeSelfOnly(container)
    }

    override fun dispatchRestoreInstanceState(container: SparseArray<Parcelable>) {
        dispatchThawSelfOnly(container)
    }

    fun setEditable(canEdit: Boolean) {
        invalidate()
    }


    internal class SavedState : BaseSavedState {

        internal var childrenStates: SparseArray<Parcelable>? = null
        private var hasError: String? = null

        constructor(superState: Parcelable?) : super(superState)

        @Suppress("UNCHECKED_CAST")
        constructor(source: Parcel) : super(source) {
            Log.i("SavedState", "Reading children children state from sparse array")
            childrenStates = source.readSparseArray(javaClass.classLoader)
            hasError = source.readString()
        }

        @Suppress("UNCHECKED_CAST")
        override fun writeToParcel(out: Parcel, flags: Int) {
            Log.i("SavedState", "Writing children state to sparse array")
            super.writeToParcel(out, flags)
            out.writeSparseArray(childrenStates as SparseArray<Any>)
            out.writeString(hasError)
        }

        companion object {
            @Suppress("UNUSED")
            @JvmField
            val CREATOR = object : Parcelable.Creator<SavedState> {
                override fun createFromParcel(source: Parcel) = SavedState(source)
                override fun newArray(size: Int): Array<SavedState?> = arrayOfNulls(size)
            }
        }
    }

}

interface IDropDownListener {
    fun onDDClicked()
    fun onOptionClicked(view: View, model: PopupModel)
}