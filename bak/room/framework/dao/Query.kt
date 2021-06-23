package room.framework.dao

@Target(AnnotationTarget.FUNCTION)
annotation class Query(val statement: String)