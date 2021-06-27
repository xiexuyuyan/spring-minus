package org.yuyan.room.annotation.processor

import com.squareup.kotlinpoet.*
import org.yuyan.room.base.RoomDatabase
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement
import kotlin.reflect.KClass


fun ifProcess(annotations: MutableSet<out TypeElement>?, roundEnv: RoundEnvironment?): Boolean {
    when (roundEnv?.processingOver()) {
        true -> return false
    }
    when (annotations.isNullOrEmpty()) {
        true -> return false
    }
    return true
}

fun findAnnotatedTypeElementByName(clsName: String, inputElements: MutableSet<out Element>?): HashSet<TypeElement>{
    val annotatedTypeElements = HashSet<TypeElement>()
    inputElements?.forEach { element ->
        val mirrors = element.annotationMirrors
        mirrors.forEach { annotationMirror ->
            when (annotationMirror.annotationType.toString()) {
                clsName -> {
                    annotatedTypeElements.add(element = element as TypeElement)
                }
            }
        }
    }
    return annotatedTypeElements
}

fun formClassBuilder(element: TypeElement): TypeSpec.Builder {
    val pkgName: String = element.enclosingElement.toString()
    val clsName: String = element.simpleName.toString()
    val clsImplName = ClassName(
        pkgName, "${clsName}${RoomDatabase.DB_IMPL_SUFFIX}"
    )
    return TypeSpec.classBuilder(clsImplName)
        .addSuperinterface(ClassName(pkgName, clsName))
}

fun formMethodBuilder(element: ExecutableElement): FunSpec.Builder{
    val methodBuilder = FunSpec.builder(element.simpleName.toString())

    methodBuilder.addModifiers(KModifier.OVERRIDE)
    /*element.modifiers.forEach {
        methodBuilder.addModifiers(KModifier.valueOf(it.name))
    }*/
    element.parameters.forEach {
        methodBuilder.addParameter(
            ParameterSpec.builder(
                it.simpleName.toString()
                , it.asType().asTypeName()
            ).build()
        )
    }
    methodBuilder.returns(element.returnType.asTypeName())
    return methodBuilder
}