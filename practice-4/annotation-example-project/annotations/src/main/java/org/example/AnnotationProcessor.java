package org.example;

import com.google.auto.service.AutoService;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@AutoService(Processor.class)
public class AnnotationProcessor extends AbstractProcessor {
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Set.of(AddDtoMapping.class.getCanonicalName());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(AddDtoMapping.class)) {
            String className = element.getSimpleName().toString();
            String packageName = processingEnv.getElementUtils().getPackageOf(element).toString();
            List<VariableElement> fields = new ArrayList<>();

            element.getEnclosedElements().forEach(e -> {
                if (e.getKind() == ElementKind.FIELD) {
                    fields.add((VariableElement) e);
                }
            });
            validateGettersSetters(element, fields);
            generateDto(className, packageName, fields);
            generateMapper(className, packageName, fields);
        }
        return true;
    }

    private void generateDto(String className, String packageName, List<VariableElement> fields) {
        try (Writer writer = processingEnv.getFiler()
                .createSourceFile(packageName + "." + className + "Dto")
                .openWriter()) {

            writer.write("package " + packageName + ";\n\n");
            writer.write("public class " + className + "Dto {\n\n");

            for (VariableElement var : fields) {
                String fieldType = var.asType().toString();
                String fieldName = var.getSimpleName().toString();
                writer.write("    private " + fieldType + " " + fieldName + ";\n");
            }
            writer.write("\n");

            for (VariableElement var : fields) {
                String fieldType = var.asType().toString();
                String fieldName = var.getSimpleName().toString();
                String capitalized = getCapitalized(fieldName);

                writer.write("    public " + fieldType + " get" + capitalized + "() {\n");
                writer.write("        return this." + fieldName + ";\n");
                writer.write("    }\n\n");

                writer.write("    public void set" + capitalized + "(" + fieldType + " " + fieldName + ") {\n");
                writer.write("        this." + fieldName + " = " + fieldName + ";\n");
                writer.write("    }\n\n");
            }

            writer.write("    @Override\n");
            writer.write("    public String toString() {\n");
            writer.write("        String res = \"\";\n");
            for (VariableElement var : fields) {
                String fieldName = var.getSimpleName().toString();
                writer.write("        res += \"" + fieldName + ": \" + this." + fieldName + " + \"; \";\n");
            }
            writer.write("        return \"{\" + res + \"}\";\n");
            writer.write("    }\n");

            writer.write("}\n");

        } catch (IOException e) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, e.getMessage());
        }
    }

    private void generateMapper(String className, String packageName, List<VariableElement> fields){
        try (Writer writer = processingEnv.getFiler()
                .createSourceFile(packageName + "." + className + "Mapper")
                .openWriter()) {

            writer.write("package " + packageName + ";\n\n");
            writer.write("public class " + className + "Mapper {\n\n");

            writer.write("    public " + className + "Dto toDto(" + className + " obj) {\n");
            writer.write(className + "Dto res = new " + className + "Dto();\n");
            for (VariableElement var : fields) {
                String fieldName = var.getSimpleName().toString();
                String capitalized = getCapitalized(fieldName);
                writer.write("    res.set" + capitalized + "(obj.get" + capitalized + "());\n");
            }
            writer.write("    return res;\n");
            writer.write("    }\n\n");

            writer.write("    public " + className + " fromDto(" + className + "Dto dto) {\n");
            writer.write(className + " res = new " + className + "();\n");
            for (VariableElement var : fields) {
                String fieldName = var.getSimpleName().toString();
                String capitalized = getCapitalized(fieldName);
                writer.write("    res.set" + capitalized + "(dto.get" + capitalized + "());\n");
            }
            writer.write("    return res;\n");
            writer.write("    }\n\n");

            writer.write("}\n");

        } catch (IOException e) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, e.getMessage());
        }
    }

    private void validateGettersSetters(Element classElement, List<VariableElement> fields) {
        List<String> methodNames = new ArrayList<>();
        for (Element member : classElement.getEnclosedElements()) {
            if (member.getKind() == ElementKind.METHOD) {
                methodNames.add(member.getSimpleName().toString());
            }
        }

        for (VariableElement var : fields) {
            String fieldName = var.getSimpleName().toString();
            String capitalized = getCapitalized(fieldName);
            String getter = "get" + capitalized;
            String setter = "set" + capitalized;

            if (!methodNames.contains(getter)) {
                processingEnv.getMessager().printMessage(
                        Diagnostic.Kind.ERROR,
                        "Missing getter for field: " + fieldName,
                        var
                );
            }

            if (!methodNames.contains(setter)) {
                processingEnv.getMessager().printMessage(
                        Diagnostic.Kind.ERROR,
                        "Missing setter for field: " + fieldName,
                        var
                );
            }
        }
    }

    private String getCapitalized(String str){
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }
}
