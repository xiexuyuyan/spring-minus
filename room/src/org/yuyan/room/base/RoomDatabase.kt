package org.yuyan.room.base

abstract class RoomDatabase {
    companion object {
        const val DB_IMPL_SUFFIX: String = "_Impl"
    }

    lateinit var configure: DatabaseConfigure

    fun initialize(configure: DatabaseConfigure){
        when (configure) {
            is MysqlDBConfigure -> {
                Class.forName(MysqlDBConfigure.JDBC_DRIVER)
                this.configure = configure
            }
            else -> {
            }
        }
    }


    class Builder<T : RoomDatabase>(private val klass: Class<T>, private val databaseName: String) {
        fun build(): T {
            val name: String = "yuyan"
            val password: String = "123456"
            val url:String = "localhost"
            val port: String = "3306"
            val configuration: MysqlDBConfigure = MysqlDBConfigure(
                    user = name
                    , password = password
                    , databaseName = databaseName
                    , url = url
                    , port = port
                    , suffixes = arrayOf("rewriteBatchedStatements=true")
            )
            val database: T = getGeneratedImplementation(klass, DB_IMPL_SUFFIX)
            database.initialize(configuration)
            return database
        }
    }
}