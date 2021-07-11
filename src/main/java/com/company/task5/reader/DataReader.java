package com.company.task5.reader;


import com.company.task5.exception.LogisticException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class DataReader {
    public List<String> readFromFile(String filePath) throws LogisticException {
        try {
            Path path = Path.of(filePath);
            return Files.lines(path)
                    .collect(Collectors.toList());
        } catch (InvalidPathException | IOException e) {
            throw new LogisticException("Unable to open file: " + filePath, e);
        }
    }
}
