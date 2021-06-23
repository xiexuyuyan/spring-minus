@file:JvmName("Hello")

package room

import room.applying.User
import room.applying.UserDatabase

class Hello {
    companion object {
        const val DB_IMPL_SUFFIX: String = "_Impl"

        @JvmStatic
        fun main(args: Array<String>) {
            val klass = UserDatabase::class.java
            val fullPackage = klass.getPackage().name
            val name = klass.canonicalName
            val postPackageName =
                    if (fullPackage.isEmpty())
                        name
                    else
                        name.substring(fullPackage.length + 1)
            val implName = postPackageName
                    .replace('.', '_') + DB_IMPL_SUFFIX
            val fullClassName =
                    if (fullPackage.isEmpty())
                        implName
                    else
                        "$fullPackage.$implName"
            println(fullClassName)
            @Suppress("UNCHECKED_CAST")
            val tClass: Class<UserDatabase> = Class.forName(fullClassName) as Class<UserDatabase>
        }
    }

    val id: Int? = null

    fun doAction(){
        println("print in do action")
        arrayOf(User::class)
    }


}