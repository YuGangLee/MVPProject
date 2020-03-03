package me.yugang.mvp

import me.yugang.mvp.channel.Channel
import me.yugang.mvp.channel.intf.PresenterCallback
import java.lang.IllegalStateException

abstract class BasePresenter : PresenterCallback {
    var channel: Channel? = null
    val requireChannel: Channel
        get() {
            if (channel == null) {
                throw IllegalStateException("required channel closed or not init.")
            }
            return channel!!
        }

    final override fun onChannelClose() {
        beforeChannelClose()
        channel = null
    }

    abstract fun beforeChannelClose()
}