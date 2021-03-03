
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileSort {

    int divisionCount = 1;  //number of binary divisions, depending on max RAM, line length and file size

    public static void main(String[] args) {

        try {
            System.out.println(Runtime.getRuntime().totalMemory());
            FileSort program = new FileSort();
            program.divideAndSort();
            program.merge();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    //this method is for dividing our big file into smaller sorted ones
    private void divideAndSort() throws IOException {

        int strCount = 0; // counting lines in file
        int maxLength = 0;  // maximum line length
        long maxSymbolsAllowed = Runtime.getRuntime().totalMemory() / 8; //divided by charSize and by 4 for safety
        // long maxSymbolsAllowed = 200;  //instead of RAM. For testing


        //Go through file to get values described above
        BufferedReader reader = new BufferedReader(new FileReader("input.txt"));
        String line;
        while ((line = reader.readLine()) != null) {
            strCount++;
            if (line.length() > maxLength) maxLength = line.length();
        }
        System.out.println(maxLength);
        reader.close();

        //get number of divisions needed
        while ( (((double) strCount / divisionCount) * maxLength) > maxSymbolsAllowed) {
            divisionCount *= 2;
        }

        //sort main file by pieces and put sorted sequence into new files
        reader = new BufferedReader(new FileReader("input.txt"));
        for (int i = 1; i <= divisionCount ; i++ ) {
            List<String> strList = new ArrayList<>();
            for (int j = 0; (j <= strCount / divisionCount) ; j++) {
                if ((line = reader.readLine()) == null) {
                    break;
                } else
                    strList.add(line);
            }
            strList.sort(String.CASE_INSENSITIVE_ORDER);

            //write into new temporary file
            BufferedWriter writer = new BufferedWriter(new FileWriter("temporary" + i + ".txt"));
            for (String s : strList) {
                writer.write(s);
                writer.newLine();
            }
            writer.close();
        }
        reader.close();

    }

    //this method is needed for merging sorted until there is only one file left
    public void merge() throws IOException {

         while (divisionCount >= 1) {
            int newFilesCount = 1;
            for (int i = 1; i < divisionCount; i += 2) {
                BufferedReader firstReader = new BufferedReader(new FileReader("temporary" + i + ".txt"));
                BufferedReader secondReader = new BufferedReader(new FileReader("temporary" + (i + 1) + ".txt"));
                BufferedWriter writer = new BufferedWriter(new FileWriter("temp.txt"));
                String firstString = firstReader.readLine();
                String secondString = secondReader.readLine();

                while (firstString != null && secondString != null) {
                    if (firstString.compareTo(secondString) > 0) {
                        writer.write(secondString);
                        writer.newLine();
                        secondString = secondReader.readLine();
                    } else  {
                        writer.write(firstString);
                        writer.newLine();
                        firstString = firstReader.readLine();
                    }
                }

                //if one file ended, put the rest of second file into result
                if (firstString == null) {
                    while (secondString != null) {
                        writer.write(secondString);
                        writer.newLine();
                        secondString = secondReader.readLine();
                    }
                } else if (secondString == null) {
                    while (firstString != null) {
                        writer.write(firstString);
                        writer.newLine();
                        firstString = firstReader.readLine();
                    }
                }

                firstReader.close();
                secondReader.close();
                writer.close();

                //deleting used files, renaming new one
                File file1 = new File("temporary" + i + ".txt");
                file1.delete();
                File file2 = new File("temporary" + (i + 1) + ".txt");
                file2.delete();
                File newFile = new File("temp.txt");
                newFile.renameTo(new File("temporary" + newFilesCount + ".txt"));

                newFilesCount++;
            }
             divisionCount /= 2;
        }
        new File("temporary1.txt").renameTo(new File("output.txt"));
    }


}
