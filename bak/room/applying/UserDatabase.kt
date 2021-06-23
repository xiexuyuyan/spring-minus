package room.applying

import room.framework.base.RoomDatabase
import room.framework.database.Database

@Database(entities = [User::class], version = 1)
abstract class UserDatabase: RoomDatabase(){
    abstract fun userDao(): UserDao

    fun run() {
        println("run: here is user database.")
    }
}