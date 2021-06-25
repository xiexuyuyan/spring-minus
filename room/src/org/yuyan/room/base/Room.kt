package org.yuyan.room.base

class Room {
    companion object {
        fun <T, C> getGeneratedImplementation(klass: Class<C>, suffix: String): T {
            // todo(ensure correct full classname is posted to reflect method)
            val fullPackage = klass.getPackage().name
            val name = klass.canonicalName
            val postPackageName =
                    if (fullPackage.isEmpty())
                        name
                    else
                        name.substring(fullPackage.length + 1)
            val implName = postPackageName
                    .replace('.', '_') + suffix
            val fullClassName =
                    if (fullPackage.isEmpty())
                        implName
                    else
                        "$fullPackage.$implName"
            println(fullClassName)
            @Suppress("UNCHECKED_CAST")
            val tClass: Class<T> = Class.forName(fullClassName) as Class<T>
            return tClass.newInstance()
        }


        /**
         * Creates a RoomDatabase.Builder for a persistent database. Once a database is built, you
         * should keep a reference to it and re-use it.
         *
         * @param klass The abstract class which is annotated with {@link Database} and extends
         * {@link RoomDatabase}.
         * @param name The name of the database file.
         * @param <T> The type of the database class.
         * @return A {@code RoomDatabase Builder<T>} which you can use to create the database.
         */
        fun <T : RoomDatabase> databaseBuilder(klass: Class<T>, name: String)
                : RoomDatabase.Builder<T> {

            require(name.trim { it <= ' ' }.isNotEmpty()) {
                ("Cannot build a database with null or empty name."
                        + " If you are trying to create an in memory database, use Room"
                        + ".inMemoryDatabaseBuilder")
            }

            return RoomDatabase.Builder(klass, name)
        }
    }




}