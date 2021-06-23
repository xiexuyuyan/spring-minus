package room.framework.enty

/**
 * @param name
 * it's interfacing to a default rule
 * that we indicate a SQL field to be named with
 * "database name" + "_" + "field name"
 * etc: user_name, user_mail, ...
 * */
@Target(AnnotationTarget.PROPERTY)
annotation class ColumnInfo(val name: String)