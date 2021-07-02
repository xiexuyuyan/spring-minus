package org.yuyan.room.annotation.processor.dao;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import org.yuyan.room.annotation.processor.entity.ColumnType;
import org.yuyan.room.annotation.processor.entity.EntityType;
import org.yuyan.room.annotation.processor.entity.EntityHolder;
import org.yuyan.room.annotation.processor.utils.StringUtils;
import org.yuyan.room.dao.Insert;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

public class CurdMethod {
    /**
     * override public/protected fun method(param: Type): return{}
     * */
    public static MethodSpec.Builder baseBuilder(ExecutableElement element) {
        String methodName = element.getSimpleName().toString();
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(methodName);

        ClassName override = ClassName.get("java.lang", "Override");
        methodBuilder.addAnnotation(override);

        element.getModifiers().forEach(modifier -> {
            switch (modifier) {
                case PUBLIC:
                case PROTECTED:
                    methodBuilder.addModifiers(modifier);
                    break;
            }
        });

        element.getParameters().forEach(variableElement -> {
            String paramName = variableElement.getSimpleName().toString();
            TypeName clsType = TypeName.get(variableElement.asType());
            ParameterSpec.Builder paramBuilder = ParameterSpec.builder(clsType, paramName);
            methodBuilder.addParameter(paramBuilder.build());
        });

        TypeName typeName = TypeName.get(element.getReturnType());
        methodBuilder.returns(typeName);

        return methodBuilder;
    }


    /**
     * the best universal way to insert singleton Entity object
     * @Insert(entity = User::class)
     * fun insert(user: User)
     *
     * so, usual we build the base method in CurdMethod.baseBuilder()
     * thus we just to create the statement with annotation info
     * */
    public static void setBasicInsertion(MethodSpec.Builder methodBuilder
            , ExecutableElement element) {
        // --- info from annotation
        // as we know this element is annotated with @Insert
        // but we have to get the annotation mirror whose type is @Insert
        // traverse all annotations in target element
        // to find the target annotation
        // , usual it  is @Insert
        EntityType entityType = null;
        String entityCanonicalName = null;
        for (AnnotationMirror annotationMirror : element.getAnnotationMirrors()) {
            String annotationTypeName = annotationMirror.getAnnotationType().toString();
            if (annotationTypeName.equals(Insert.class.getCanonicalName())) {
                // this phase is to get the params on annotation @Insert
                // usual it is entity()=...
                for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry
                        : annotationMirror.getElementValues().entrySet()) {
                    String annotationMethodName = entry.getKey().getSimpleName().toString();
                    entityCanonicalName = entry.getValue().getValue().toString();
                    System.out.println(annotationMethodName + " : "+ entityCanonicalName);
                    // todo(make it "entity" to be a global constant)
                    if ("entity".equals(annotationMethodName)) {
                        entityType = EntityHolder.entities.get(entityCanonicalName);
                        System.out.println(entityType.toString());
                    }
                }
            }
        }
        if (entityType == null || entityCanonicalName == null) {
            return;
        }

        // --- info from param
        String paramName = null;
        for (VariableElement parameter : element.getParameters()) {
            TypeName clsType = TypeName.get(parameter.asType());
            if (clsType.toString().equals(entityCanonicalName)) {
                paramName = parameter.getSimpleName().toString();
                break;
            }
        }
        if (paramName == null) {
            return;
        }

        List<ColumnType> columns = entityType.getColumns();
        methodBuilder.addCode("try{\n");
        methodBuilder.addCode("    $T connection = configure.createConnection();\n", Connection.class);
        methodBuilder.addCode("    $T statement = connection.createStatement();\n", Statement.class);
        methodBuilder.addCode("    String sql = \"insert into \" + \n");
        methodBuilder.addCode("        \"`$N`(", entityType.getTableName());
        for (int i = 0; i < columns.size(); i++) {
            if (i < columns.size() - 1) {
                methodBuilder.addCode("`$N`, ", columns.get(i).getColumnName());
            } else {
                methodBuilder.addCode("`$N`) \" + \n", columns.get(i).getColumnName());
            }
        }
        methodBuilder.addCode("        \"values(\" + \n");
        for (int i = 0; i < columns.size(); i++) {
            String methodName = StringUtils.upperCase(columns.get(i).getVarName());
            if (i < columns.size() - 1) {
                methodBuilder.addCode("            \"'\" + $N.get$N() + \"', \" + \n", paramName, methodName);
            } else {
                methodBuilder.addCode("            \"'\" + $N.get$N() + \"')\";\n", paramName, methodName);
            }
        }
        methodBuilder.addCode("    System.out.println(sql);\n");
        methodBuilder.addCode("    int rowCount = statement.executeUpdate(\n");
        methodBuilder.addCode("        sql, $T.RETURN_GENERATED_KEYS);\n", Statement.class);
        methodBuilder.addCode("    $T rs = statement.getGeneratedKeys();\n", ResultSet.class);
        methodBuilder.addCode("} catch ($T e) {\n", SQLException.class);
        methodBuilder.addCode("    e.printStackTrace();\n");
        methodBuilder.addCode("}\n");
    }

}