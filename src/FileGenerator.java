
import java.io.*;
import java.util.Random;

public class FileGenerator {
    private long numberOfLines;
    private int maxLineLength;

    public static void main(String[] args)  {
        try {
            long timeBefore = System.currentTimeMillis();
            FileGenerator generator = new FileGenerator();
            generator.getParams();
            generator.generateFile();
            long timeAfter = System.currentTimeMillis();
            System.out.println((timeAfter - timeBefore) + "ms");
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void getParams() throws IOException {

            //Getting parameters from console
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Number of lines: ");
            numberOfLines = Integer.parseInt(reader.readLine());
            System.out.print("Line length: ");
            maxLineLength = Integer.parseInt(reader.readLine());
            reader.close();


    }


    private void generateFile () throws IOException {

            //write random chars into "input.txt" file
            BufferedWriter writer = new BufferedWriter(new FileWriter("input.txt"));
            for (int i = 0; i < numberOfLines; i++) {
                for (int j = 0; j < 1 + (new Random().nextInt(maxLineLength)); j++) {
                    Random random = new Random();
                    writer.write('a' + random.nextInt(26));
                }
                writer.newLine();
            }
            writer.close();

    }
}
