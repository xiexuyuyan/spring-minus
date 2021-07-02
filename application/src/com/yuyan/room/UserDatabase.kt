package com.yuyan.room

import org.yuyan.room.base.RoomDatabase
import org.yuyan.room.dao.Dao
import org.yuyan.room.database.DaoMethod
import org.yuyan.room.database.Database
import java.sql.Connection

@Database(entities = [User::class], version = 1)
abstract class UserDatabase: RoomDatabase(){
    @DaoMethod
    abstract fun userDao(): UserDao
}