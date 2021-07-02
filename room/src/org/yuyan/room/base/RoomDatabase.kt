package org.yuyan.room.base

import java.util.HashMap

abstract class RoomDatabase {
    companion object {
        const val DB_IMPL_SUFFIX: String = "_Impl"
    }

    class Builder<T : RoomDatabase>(private val klass: Class<T>, private val name: String) {
        fun build(): T {
            return Room.getGeneratedImplementation(klass, DB_IMPL_SUFFIX)
        }
    }
}