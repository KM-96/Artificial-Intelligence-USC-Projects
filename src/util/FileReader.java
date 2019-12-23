package util;

import exceptions.FileException;
import model.Input;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileReader {

    private File file;
    private BufferedReader bufferedReader;
    private String location;

    public FileReader(String location) throws FileException {
        try {
            this.location = location;
            this.file = new File(this.location);
            this.bufferedReader = new BufferedReader(new java.io.FileReader(file));
        } catch (FileNotFoundException e) {
            throw new FileException(e.getMessage(), e);
        }
    }

    public Input readFile() {
        Input input = new Input();
        try {
            input.setQueryCount(Integer.parseInt(this.bufferedReader.readLine().trim()));

            for (int i = 0; i < input.getQueryCount(); i++) {
                input.getQueries().add(this.bufferedReader.readLine().trim());
            }

            input.setN(Integer.parseInt(this.bufferedReader.readLine().trim()));

            for (int i = 0; i < input.getN(); i++) {
                input.getSentences().add(this.bufferedReader.readLine().trim());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                this.bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return input;
    }
}