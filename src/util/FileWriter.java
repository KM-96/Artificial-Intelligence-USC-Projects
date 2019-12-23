package util;

import exceptions.FileException;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

public class FileWriter {

    private java.io.FileWriter fileWriter;
    private BufferedWriter bufferedWriter;
    private String location;

    public FileWriter(String location) throws FileException {
        try {
            this.location = location;
            this.fileWriter = new java.io.FileWriter(location);
            this.bufferedWriter = new BufferedWriter(fileWriter);
        } catch (IOException e) {
            throw new FileException(e.getMessage(), e);
        }
    }

    public void writeToFile(List<String> output) throws FileException {
        try {
            for (int i = 0; i < output.size(); i++) {
                bufferedWriter.write(output.get(i).toUpperCase().trim());
                if (i != output.size() - 1) {
                    bufferedWriter.write("\n");
                }
            }
        } catch (Exception e) {
            throw new FileException(e.getMessage(), e);
        } finally {
            try {
                this.bufferedWriter.close();
            } catch (IOException e) {
                throw new FileException(e.getMessage(), e);
            }
        }
    }
}
