package me.yugang.mvp

import android.content.Context
import android.os.Bundle
import androidx.annotation.LayoutRes
import me.yugang.mvp.channel.Channel
import me.yugang.mvp.channel.PresenterMessage
import me.yugang.mvp.channel.intf.ViewCallback

abstract class BaseActivity<P : BasePresenter> : KeyboardActivity() {
    protected var isStartingActivity: Boolean = false
    protected var isFront: Boolean = false
    protected var channel: Channel? = null
    protected lateinit var presenter: P
    private lateinit var viewCallback: ViewCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(bindLayout())
        preSet()
        presenter = injectPresenter()
        injectChannel()
        initView()
        loadData(intent.extras)
    }

    private fun preSet() {
        viewCallback = object : ViewCallback {
            override fun injectContext(): Context {
                return this@BaseActivity
            }

            override fun onMessageReceive(message: PresenterMessage) {
                this@BaseActivity.onMessageReceive(message)
            }
        }
    }

    private fun injectChannel() {
        channel = Channel(viewCallback, presenter)
        presenter.channel = channel
    }

    @LayoutRes
    abstract fun bindLayout(): Int

    abstract fun injectPresenter(): P

    abstract fun initView()

    abstract fun loadData(data: Bundle?)

    abstract fun onMessageReceive(message: PresenterMessage)

    override fun onDestroy() {
        channel?.close()
        super.onDestroy()
    }
}