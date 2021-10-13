package com.yuyan.harp.room

import org.yuyan.room.dao.Dao
import org.yuyan.room.dao.Insert
import org.yuyan.room.dao.Query
import org.yuyan.room.dao.Update


@Dao
interface WxUserDao {
    @Insert(entity = WxUser::class)
    fun insert(wxUser: WxUser)

    @Query(statement = "select * from wxuser where uid like :uid limit 1")
    fun getWxUserByUid(uid: Int): WxUser

    @Query(statement = "select * from wxuser where openid like :openid limit 1")
    fun getWxUserByOpenid(openid: String): WxUser

    @Update(entity = WxUser::class)
    fun update(wxUser: WxUser)
}