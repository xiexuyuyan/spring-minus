package org.yuyan.room.annotation.processor.temp

import com.squareup.kotlinpoet.*
import org.yuyan.room.dao.Dao
import org.yuyan.room.dao.Delete
import org.yuyan.room.dao.Insert
import org.yuyan.room.dao.Query
import java.lang.System.out
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.*

@SupportedSourceVersion(SourceVersion.RELEASE_8)
class TDaoAnnotationProcessor: AbstractProcessor() {

    private val annotationClass = HashSet<Class<*>>()

    override fun init(processingEnv: ProcessingEnvironment?) {
        super.init(processingEnv)
        with(annotationClass){
            add(Dao::class.java)
            add(Query::class.java)
            add(Insert::class.java)
            add(Delete::class.java)
        }
    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return annotationClass.let {classList ->
            return@let with(HashSet<String>()){
                classList.forEach {cls ->
                    add(cls.canonicalName)
                }
                this
            }
        }
    }

    override fun process(annotations: MutableSet<out TypeElement>?, roundEnv: RoundEnvironment?): Boolean {
        if (!ifProcess(annotations, roundEnv)) {
            return false
        }

        println("root elements:-------------")
        roundEnv?.rootElements?.forEach {
            println(it.toString())
        }

        println("Dao element:-------------")
        roundEnv?.getElementsAnnotatedWith(Dao::class.java)?.forEach {daoAnnotatedElement ->
            println("who annotated with Dao: "+daoAnnotatedElement.simpleName)
            val clsBuilder: TypeSpec.Builder = formClassBuilder(daoAnnotatedElement as TypeElement)

            daoAnnotatedElement.enclosedElements?.forEach {elementInDao ->
                println("element in Dao:-------------" + elementInDao.simpleName)
                elementInDao.getAnnotation(Delete::class.java)?.let {
                    clsBuilder.addFunction(formMethodBuilder(elementInDao as ExecutableElement).build())
                }
                elementInDao.getAnnotation(Insert::class.java)?.let {
                    val insertElement = elementInDao as ExecutableElement
                    insertElement.parameters.forEach {
                        println(it.simpleName.toString() + ": " + it.asType())
                        // println(ClassName.bestGuess(it.asType().toString()).toString())
                        // ParameterSpec.builder("a", it.asType().asTypeName()).build()
                    }
                    clsBuilder.addFunction(formMethodBuilder(elementInDao as ExecutableElement).build())
                }
            }

            val pkgName: String = daoAnnotatedElement.enclosingElement.toString()
            val clsName: String = daoAnnotatedElement.simpleName.toString()
            val kotlinFile = FileSpec.builder(pkgName, "${clsName}_Impl")
                    .addType(clsBuilder.build())
                    .build()
            kotlinFile.writeTo(out)
            val a= kotlinFile.toJavaFileObject()


            // kotlinFile.writeTo(filer = processingEnv.filer)
        }




        /*val targetAnnotationName: String = supportedAnnotationTypes.elementAt(0)
        val annotatedTypeElements = findAnnotatedTypeElementByName(targetAnnotationName
            , roundEnv?.rootElements)
        println(targetAnnotationName)

        annotatedTypeElements.forEach {element ->
            val clsBuilder: TypeSpec.Builder = formClassBuilder(element)
            println("class: ${clsBuilder.build().name}----------------")

            ElementFilter.methodsIn(element.enclosedElements).forEach {
                println("method:${it.modifiers} ${it.simpleName}")
                clsBuilder.addFunction(formMethodBuilder(element = it).build())
            }

            val pkgName: String = element.enclosingElement.toString()
            val clsName: String = element.simpleName.toString()
            val kotlinFile = FileSpec.builder(pkgName, clsName)
                .addType(clsBuilder.build())
                .build()
            kotlinFile.writeTo(out)
            // kotlinFile.writeTo(filer = processingEnv.filer)
        }*/
        return false
    }

}
/*

val clsBuilder: TypeSpec.Builder = formClassBuilder(daoAnnotatedElement as TypeElement)

println("enclosed elements in Dao element:-------------")
daoAnnotatedElement.enclosedElements?.forEach {elementInDao ->
    println(elementInDao.simpleName)
    println(elementInDao)
}

val pkgName: String = daoAnnotatedElement.enclosingElement.toString()
val clsName: String = daoAnnotatedElement.simpleName.toString()
val kotlinFile = FileSpec.builder(pkgName, "${clsName}_Impl")
        .addType(clsBuilder.build())
        .build()
kotlinFile.writeTo(out)
// kotlinFile.writeTo(filer = processingEnv.filer)


*/



/*
elementInDao.getAnnotation(Delete::class.java)?.let {
    val method = formMethodBuilder(elementInDao as ExecutableElement).build()
    clsBuilder.addFunction(method)
}
elementInDao.getAnnotation(Insert::class.java)?.let {
    val methodBuilder = FunSpec.builder(elementInDao.simpleName.toString())
    methodBuilder.addModifiers(KModifier.OVERRIDE)
    println("insert type params====================")

    */
/*(elementInDao as ExecutableElement).parameters.forEach {
        println(it.asType().toString())
        println(it.annotationMirrors)
    }*//*

    // clsBuilder.addFunction(methodBuilder.build())
    //clsBuilder.addFunction(formMethodBuilder(elementInDao as ExecutableElement).build())
}*/
