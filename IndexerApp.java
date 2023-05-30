import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

public class IndexerApp
{
    public static final String INVERTED_INDEX_FILE_PATH = "C:\\Users\\lucia\\Desktop\\RecInf\\proyecto\\salida\\terms.txt";
    public static final String DOCUMENTS_LENGTH_FILE_PATH = "C:\\Users\\lucia\\Desktop\\RecInf\\proyecto\\salida\\docs.txt";
    static private final String CORPUS_FOLDER_PATH  = "C:/Users/lucia/Desktop/RecInf/proyecto/corpus";
    static private final String RESULT_FOLDER_PATH  = "C:/Users/lucia/Desktop/RecInf/proyecto/salida";

    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);
        String input;
        System.out.println("note!! if you change the corpus or run one for the first time, you need to run the indexer first!");
        System.out.println("\t1. Indexer\n\t2. Recover\nexit() to finish");
        do{
            input = scanner.nextLine();
        }while(!Objects.equals(input, "1") && !Objects.equals(input, "2") && !Objects.equals(input, "exit()"));
        switch (input)
        {
            case "1":
                try {
                    new Indexer(CORPUS_FOLDER_PATH, INVERTED_INDEX_FILE_PATH, DOCUMENTS_LENGTH_FILE_PATH).processDocument();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
            case "2":
                try {
                    new Recover(RESULT_FOLDER_PATH).process();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
        }
        scanner.close();
    }
}
