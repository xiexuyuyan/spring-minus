package org.yuyan.room.base


class MysqlDBConfigure(user: String, password: String, databaseName: String
                       , url: String
                       , port: String
                       , vararg suffixes: String)
    : DatabaseConfigure(user, password) {

    override fun getUrl(): String {
        return dbURL
    }

    companion object {
        const val JDBC_DRIVER = "com.mysql.cj.jdbc.Driver"
        const val DB_URL_PREFIX = "jdbc:mysql://"
        const val DB_URL_SUFFIX = "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC"
    }

    private var dbURL: String

    init {
        val suffixStr = when(suffixes.size) {
            0 -> {
                ""
            }
            else -> {
                var s: String = ""
                for (suffix in suffixes) {
                    s += "&$suffix"
                }
                s
            }
        }
        suffixes.size
        dbURL = "$DB_URL_PREFIX$url:$port/$databaseName$DB_URL_SUFFIX$suffixStr"
    }
}