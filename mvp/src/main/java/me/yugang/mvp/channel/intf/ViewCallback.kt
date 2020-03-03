package me.yugang.mvp.channel.intf

import android.content.Context
import me.yugang.mvp.channel.PresenterMessage

interface ViewCallback {
    fun injectContext(): Context

    fun onMessageReceive(message: PresenterMessage)
}