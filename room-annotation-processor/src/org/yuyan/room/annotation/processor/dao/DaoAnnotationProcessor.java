package org.yuyan.room.annotation.processor.dao;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import org.yuyan.room.annotation.processor.temp.AnnotationProcessorHelper;
import org.yuyan.room.dao.Dao;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.lang.System.out;

@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class DaoAnnotationProcessor extends AbstractProcessor {
    private final List<Class<?>> supportClassList = new ArrayList<>();


    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        supportClassList.add(Dao.class);
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> supportClassNames = new HashSet<>();
        for (Class<?> aClass : supportClassList) {
            supportClassNames.add(aClass.getCanonicalName());
        }
        return supportClassNames;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (!AnnotationProcessorHelper.processable(annotations, roundEnv)) {
            return false;
        }
        roundEnv.getElementsAnnotatedWith(Dao.class).forEach(elementAnnotatedDatabase -> {
            String pkgName = elementAnnotatedDatabase.getEnclosingElement().toString();
            String clsName = elementAnnotatedDatabase.getSimpleName().toString();

            TypeSpec.Builder defaultClassBuilder = TypeSpec.classBuilder(
                    clsName + AnnotationProcessorHelper.DB_IMPL_SUFFIX);
            defaultClassBuilder.addModifiers(Modifier.PUBLIC);
            defaultClassBuilder.addSuperinterface(ClassName.get(pkgName, clsName));

            ((TypeElement) elementAnnotatedDatabase).getEnclosedElements().forEach(elementInDatabase -> {
                switch (elementInDatabase.getKind()) {
                    case METHOD:
                        MethodSpec.Builder methodBuilder = AnnotationProcessorHelper.formMethodBuilder((ExecutableElement) elementInDatabase);
                        defaultClassBuilder.addMethod(methodBuilder.build());
                        break;
                    default:
                        break;
                }
            });
            JavaFile.Builder fileBuilder = JavaFile.builder(pkgName, defaultClassBuilder.build());
            JavaFile javaFile = fileBuilder.build();
            try {
                javaFile.writeTo(out);
                javaFile.writeTo(processingEnv.getFiler());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });




        return false;
    }
}