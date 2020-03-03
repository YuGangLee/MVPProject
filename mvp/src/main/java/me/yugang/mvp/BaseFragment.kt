package me.yugang.mvp

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import me.yugang.mvp.channel.Channel
import me.yugang.mvp.channel.PresenterMessage
import me.yugang.mvp.channel.intf.ViewCallback

abstract class BaseFragment<P : BasePresenter> : Fragment() {
    protected var channel: Channel? = null
    protected lateinit var presenter: P
    private lateinit var viewCallback: ViewCallback

    private var isInit = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(bindLayout(), container, false)
        preSet()
        presenter = injectPresenter()
        injectChannel()
        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
        if (!isInit) {
            loadData(arguments)
            isInit = true
        }
    }

    private fun preSet() {
        viewCallback = object : ViewCallback {
            override fun injectContext(): Context {
                return this@BaseFragment.requireContext()
            }

            override fun onMessageReceive(message: PresenterMessage) {
                this@BaseFragment.onMessageReceive(message)
            }
        }
    }

    private fun injectChannel() {
        channel = Channel(viewCallback, presenter)
        presenter.channel = channel
    }

    fun runOnUiThread(runnable: () -> Unit) {
        requireActivity().runOnUiThread(runnable)
    }

    fun runOnUiThread(runnable: Runnable) {
        requireActivity().runOnUiThread(runnable)
    }

    @LayoutRes
    abstract fun bindLayout(): Int

    abstract fun injectPresenter(): P

    abstract fun initView()

    abstract fun loadData(data: Bundle?)

    abstract fun onMessageReceive(message: PresenterMessage)

    open fun onBackPressed() {
        requireActivity().onBackPressed()
    }

    override fun onDestroy() {
        channel?.close()
        super.onDestroy()
    }
}