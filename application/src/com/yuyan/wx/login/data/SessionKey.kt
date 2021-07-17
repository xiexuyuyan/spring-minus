package com.yuyan.wx.login.data

import com.google.gson.annotations.SerializedName

data class SessionKey(@SerializedName("session_key") val key: String
                      , @SerializedName("openid") val openid: String) {
    override fun toString(): String {
        return "key= $key, openid= $openid"
    }
}