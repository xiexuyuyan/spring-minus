package com.yuyan.harp.room

import org.yuyan.room.base.RoomDatabase
import org.yuyan.room.database.DaoMethod
import org.yuyan.room.database.Database

@Database(entities = [User::class], version = 1)
abstract class UserDatabase: RoomDatabase(){
    @DaoMethod
    abstract fun userDao(): UserDao
    @DaoMethod
    abstract fun wxSessionDao(): WxSessionDao
    @DaoMethod
    abstract fun wxUserDao(): WxUserDao
    @DaoMethod
    abstract fun webSessionDao(): WebSessionDao
}