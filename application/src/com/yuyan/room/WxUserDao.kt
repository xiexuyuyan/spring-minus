package com.yuyan.room

import org.yuyan.room.dao.*
import org.yuyan.room.entity.ColumnInfo


@Dao
interface WxUserDao {
    @Insert(entity = WxUser::class)
    fun insert(wxUser: WxUser)

    @Query(statement = "select * from wxuser where uid like :uid limit 1")
    fun getWxUserByUid(uid: Int): WxUser

    @Query(statement = "select * from wxuser where openid like ':openid' limit 1")
    fun getWxUserByOpenid(openid: String): WxUser

    @Update(entity = WxUser::class)
    fun update(wxUser: WxUser)
}