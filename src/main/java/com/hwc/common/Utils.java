package com.hwc.common;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static List<Path> getAllPathInDirectory(Path directory, String... extentions) throws IOException {
        List<Path> wosPathList = new ArrayList<>();
        Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path path, BasicFileAttributes attrs)
                    throws IOException {

                String extension = FilenameUtils.getExtension(path.toString());

                if (ArrayUtils.isEmpty(extentions) || ArrayUtils.contains(extentions, extension)) {
                    wosPathList.add(path);
                }
                return FileVisitResult.CONTINUE;
            }
        });

        return wosPathList;
    }


}
