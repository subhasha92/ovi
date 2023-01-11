package org.ovi.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.CallSuper
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewbinding.ViewBinding
import org.ovi.data.pref.OVIPreferences
import org.ovi.util.extensions.hideSoftKeyboard
import org.ovi.widget.OviSnackBar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.ovi.R
import org.ovi.data.local.PopupModel


abstract class BaseFragment<B : ViewBinding> : Fragment(), BaseFragmentCallbacks {

    lateinit var binding: B
    var hasInitializedRootView = false
    private var snackBar: OviSnackBar? = null
    val pref by lazy { OVIPreferences() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getViewBinding()
        (this as? BaseFragmentCallbacks)?.let { onFragmentCreated() }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) =
        binding.root

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (this as? BaseFragmentCallbacks)?.let {
            setupView()
            bindViewEvents()
            bindViewModel()
        }
    }

    override fun onFragmentCreated() {}

    @CallSuper
    override fun bindViewEvents() {
        requireNotNull(view).setOnClickListener {
            requireActivity().hideSoftKeyboard()
        }
    }

    protected inline infix fun <T> Flow<T>.bindTo(crossinline action: (T) -> Unit) {
        with(viewLifecycleOwner) {
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    collect { action(it) }
                }
            }
        }
    }

    protected fun showSnackBar(message: String?, type: OviSnackBar.SnackType,view: View?=null){
        binding.root.let {
            snackBar = OviSnackBar
                .Builder()
                .type(type)
                .message(message)
                .setCallBack(snackListener)
                .make(it)
                .setAnchorView(view)
                .showSnack()
        }
    }

    private val snackListener = object : OviSnackBar.Builder.ISnackListener {
        override fun onClosed(view: View) {
            snackBar?.dismiss()
        }
    }

    fun runOnMainThread(job: suspend () -> Unit){
        lifecycleScope.launch {
            withContext(Dispatchers.Main){
                job.invoke()
            }
        }
    }

    fun runDelayed(delayMilliSec : Long,job: suspend () -> Unit) =
        lifecycleScope.launch {
            withContext(Dispatchers.IO){
                delay(delayMilliSec)
                withContext(Dispatchers.Main){
                    job.invoke()
                }
            }
        }

    open fun onBackPressed() {}


    private fun handleColor(textView: TextView, popupModel: PopupModel) {

        val color: Int = if(!popupModel.highlight){
            when {
                popupModel.isSelected -> R.color.grey_300
                !popupModel.isSelected -> R.color.black_1000
                else -> R.color.black_1000
            }
        }else R.color.snackColorRed
        textView.setTextColor(ContextCompat.getColor(requireContext(), color))
    }

    protected val onClickListener = View.OnClickListener {
        requireActivity().hideSoftKeyboard()
        onClick(it)
    }
    protected abstract fun onClick(view: View)
    abstract fun getViewBinding(): B

}