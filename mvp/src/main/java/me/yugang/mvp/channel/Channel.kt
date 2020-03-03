package me.yugang.mvp.channel

import android.content.Context
import android.os.Handler
import android.os.Looper
import me.yugang.mvp.channel.intf.PresenterCallback
import me.yugang.mvp.channel.intf.ViewCallback

class Channel(
    private val viewCallback: ViewCallback,
    private val presenterCallback: PresenterCallback
) {
    private var isClose = false
    private val handler = Handler(Looper.getMainLooper())

    @Throws(IllegalStateException::class)
    fun requireContext(): Context {
        if (isClose) {
            // notify context destroy
            throw IllegalStateException("View closed")
        }
        return viewCallback.injectContext()
    }

    // resolve message on main thread
    fun pushPresenterMessage(message: PresenterMessage) {
        if (isClose) {
            // notify context destroy
            throw IllegalStateException("View closed")
        }
        handler.post {
            viewCallback.onMessageReceive(message)
        }
    }

    fun close() {
        presenterCallback.onChannelClose()
        isClose = true
    }

    fun isClose(): Boolean {
        return isClose
    }
}