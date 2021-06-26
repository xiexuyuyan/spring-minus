package com.yuyan.room

import org.yuyan.room.base.RoomDatabase
import org.yuyan.room.database.Database

@Database(entities = [User::class], version = 1)
abstract class UserDatabase: RoomDatabase(){
    abstract fun userDao(): UserDao
}