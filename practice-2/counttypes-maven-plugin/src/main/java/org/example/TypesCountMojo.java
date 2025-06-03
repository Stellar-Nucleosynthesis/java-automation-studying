package org.example;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Mojo(name="ctypes")
public class TypesCountMojo extends AbstractMojo {

    private static final List<String> PRIMITIVE_TYPES = Arrays.asList(
            "byte", "short", "int", "long", "float", "double", "boolean", "char"
    );

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        File currentDir = new File(".");
        Map<String, Integer> typesCounts = parseJavaFiles(currentDir);

        getLog().info("Primitive variable counts:");
        for (String type : PRIMITIVE_TYPES) {
            getLog().info(type + ": " + typesCounts.getOrDefault(type, 0));
        }
    }

    private static Map<String, Integer> parseJavaFiles(File directory) {
        Map<String, Integer> typesCounts = new HashMap<>();

        if (directory == null || !directory.exists())
            return typesCounts;

        File[] files = directory.listFiles();
        if (files == null) return typesCounts;

        for (File file : files) {
            if (file.isDirectory()) {
                merge(typesCounts, parseJavaFiles(file));
            } else if (file.getName().endsWith(".java")) {
                merge(typesCounts,analyzeTypes(file));
            }
        }

        return typesCounts;
    }

    private static void merge(Map<String, Integer> target, Map<String, Integer> source) {
        for (Map.Entry<String, Integer> entry : source.entrySet()) {
            if (!target.containsKey(entry.getKey())) {
                target.put(entry.getKey(), entry.getValue());
            } else {
                target.put(entry.getKey(), target.get(entry.getKey()) + entry.getValue());
            }
        }
    }

    private static Map<String, Integer> analyzeTypes(File file) {
        Map<String, Integer> typesCounts = new HashMap<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                for (String type : PRIMITIVE_TYPES) {
                    String regex = "\\b" + type + "\\b\\s+\\w+\\s*";
                    Pattern pattern = Pattern.compile(regex);
                    Matcher matcher = pattern.matcher(line);
                    while (matcher.find()) {
                        String declaration = matcher.group();
                        String[] parts = declaration
                                .replaceFirst("\\b" + type + "\\b", "")
                                .replaceAll("[;=].*", "")
                                .split(",");
                        int count = (int) Arrays.stream(parts)
                                .map(String::trim)
                                .filter(s -> !s.isEmpty())
                                .count();
                        typesCounts.put(type, typesCounts.getOrDefault(type, 0) + count);
                    }
                }
            }
        } catch (IOException e) {
            return typesCounts;
        }
        return typesCounts;
    }
}