import agent.KnowledgeBasedAgent;
import model.Input;
import util.FileReader;
import util.FileWriter;

import java.util.List;

public class LogicAgent {
    public static void main(String[] args) {
        long time = System.currentTimeMillis();
        try {
            //Step 1: Read input file
            String filename = "input.txt";
            FileReader fileReader = new FileReader(".\\resources\\" + filename);
            Input input = fileReader.readFile();

            //Step 2: Perform Resolution
            KnowledgeBasedAgent agent = new KnowledgeBasedAgent();
            List<String> output = agent.resolution(input);

            //Step 3: Write output to the output.txt file
            FileWriter writer = new FileWriter("output.txt");
            writer.writeToFile(output);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println(System.currentTimeMillis() - time);
        }
    }
}
