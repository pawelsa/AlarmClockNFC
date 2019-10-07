package com.helpfulapps.base.base

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import io.reactivex.disposables.CompositeDisposable

abstract class BaseFragment<T : BaseViewModel, DB : ViewDataBinding> : Fragment() {
    val TAG: String = this::class.java.simpleName
    abstract val viewModel: T
    abstract val layoutId: Int
    protected val mainActivity: BaseActivity<*, *>?
        get() = (activity as BaseActivity<*, *>?)
    protected open val navController: NavController?
        get() = view?.let { Navigation.findNavController(it) }
    protected var actionBar: Toolbar?
        set(value) {
            (activity as AppCompatActivity?)?.setSupportActionBar(value)
        }
        get() = activity?.actionBar as Toolbar?
    open lateinit var binding: DB
    protected lateinit var disposables: CompositeDisposable
    protected lateinit var onStopDisposables: CompositeDisposable


    private fun init(inflater: LayoutInflater, container: ViewGroup) {
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        init(inflater, container!!)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    fun showMessage(text: String) {
        Log.d(TAG, "showMessage: $text")
        mainActivity?.showMessage(text)
    }

    protected open fun onBackPressed() {
        mainActivity?.onBackPressed()
    }

    override fun onStart() {
        super.onStart()
        onStopDisposables = CompositeDisposable()
    }

    override fun onStop() {
        onStopDisposables.dispose()
        super.onStop()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        disposables = CompositeDisposable()
    }

    override fun onDetach() {
        disposables.dispose()
        super.onDetach()
    }

    abstract fun init()

}