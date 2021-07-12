package com.company.task5.main;

import com.company.task5.entity.Truck;
import com.company.task5.exception.LogisticException;
import com.company.task5.parser.DataParser;
import com.company.task5.reader.DataReader;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    private static final String RELATIVE_FILE_PATH = "data/data.txt";

    public static void main(String[] args) {
        ClassLoader classLoader = Main.class.getClassLoader();
        URL resource = classLoader.getResource(RELATIVE_FILE_PATH);
        String absolutePath = new File(resource.getFile()).getAbsolutePath();
        DataReader reader = new DataReader();
        DataParser parser = new DataParser();

        List<String> dataStrings = null;

        try {
            dataStrings = reader.readFromFile(absolutePath);
        } catch (LogisticException e) {
            e.printStackTrace();
        }

        List<Truck> trucks = new ArrayList<>();

        for (String dataString : dataStrings) {
            Truck truck = parser.parseTruck(dataString);
            trucks.add(truck);
        }

        ExecutorService executorService = Executors.newFixedThreadPool(trucks.size());
        trucks.forEach(executorService::execute);
        executorService.shutdown();
    }
}
