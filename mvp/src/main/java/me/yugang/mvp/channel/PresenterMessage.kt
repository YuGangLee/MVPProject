package me.yugang.mvp.channel

data class PresenterMessage(
    val requestCode: Int,
    val message: Any?
)
