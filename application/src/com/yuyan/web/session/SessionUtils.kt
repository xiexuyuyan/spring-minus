package com.yuyan.web.session

import com.yuyan.data.ResultInWeb
import com.yuyan.room.UserDatabase
import com.yuyan.room.WebSession
import javax.servlet.http.HttpServletRequest

fun getUidFromSession(request: HttpServletRequest, database: UserDatabase): Pair<Int, ResultInWeb> {
    val sessionKey = request.getHeader("session_key")
        ?: return Pair(0, ResultInWeb.create(10002).with("details", "in request header, [session_key] is missed"))

    if (!sessionKey.matches(Regex("^[1-9]\\d{3}$"))) {
        return Pair(0, ResultInWeb.create(10002).with("details", "$sessionKey is not matches a number"))
    }

    val webSession: WebSession = database.webSessionDao().getSessionBySessionKey(sessionKey.toInt())
    if (webSession.uid == 0) {
        return Pair(0, ResultInWeb.create(10002).with("session_key", sessionKey))
    }
    return Pair(webSession.uid, ResultInWeb.success())
}