package room.application

import room.applying.User
import room.applying.UserDao
import room.applying.UserDatabase
import room.framework.base.Room
import kotlin.reflect.full.memberProperties

class Main {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val user = User(uid = 12, name = "xiaowang", mail = "xw@git.com")

            val kClass = user.javaClass.kotlin
            println(kClass.simpleName)
            kClass.memberProperties.forEach { println(it.name) }

            val db = Room.databaseBuilder(UserDatabase::class.java
                    , "database-name").build()
            db.run()
            val userDao: UserDao = db.userDao()
            userDao.delete(user = user)
        }
    }
}