package com.yuyan.harp.room

import org.yuyan.room.dao.*


@Dao
interface WxSessionDao {
    @Insert(entity = Login::class)
    fun login(login: Login)

    @Insert(entity = WxSession::class)
    fun insert(wxSession: WxSession)

    @Update(entity = WxSession::class)
    fun update(wxSession: WxSession)

    @Query(statement = "select * from wx_session where uid like :uid limit 1")
    fun getSessionByUid(uid: Int): WxSession

    @Query(statement = "select * from wx_session where session_user like :sessionUser limit 1")
    fun getSessionBySessionUser(sessionUser: Int): WxSession
}