package test;

import agent.KnowledgeBasedAgent;
import model.Input;
import util.FileReader;

import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Test {
    public static void main(String[] args) throws IOException {
        long time = System.currentTimeMillis();
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter("test.txt");
            for (int i = 1; i <= 52; i++) {

                //Step 1: Read input file
                String filename = "input" + i + ".txt";
                FileReader fileReader = new FileReader("G:\\Masters CS\\Study\\Fall 2019\\Artificial Intelligence\\Homework\\Homework3\\homework3\\resources\\" + filename);
                Input input = fileReader.readFile();

                //Step 2: Perform Resolution
                KnowledgeBasedAgent agent = new KnowledgeBasedAgent();
                List<String> output = agent.resolution(input);


                fileWriter.write("\nThe output for " + i);
                for (String string : output) {
                    fileWriter.write("\n" + string);
                }
                Set<Integer> set = new LinkedHashSet<>();


            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("Total time taken for all the test cases: " + (System.currentTimeMillis() - time));
            fileWriter.close();
        }
    }
}
