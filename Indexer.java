import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;


public class Indexer
{
    private final File folderPath;
    private final String invertedIndexFilePath;
    private final String documentsLengthFilePath;

    public Indexer(String folderPath, String invertedIndexFilePath, String documentsLengthFilePath)
    {
        this.folderPath = new File(folderPath);
        this.invertedIndexFilePath = invertedIndexFilePath;
        this.documentsLengthFilePath = documentsLengthFilePath;
    }
    public void processDocument() throws IOException
    {
        String text;
        CalculateTFIDF TF = new CalculateTFIDF(folderPath.listFiles());
        HashMap<String, Double> tf;
        FilterManager FM = new FilterManager();
        ArrayList<String> terms;
        StringToArray strToArray = new StringToArray();
        FM.add(new FilterToLowerCase());
        FM.add(new FilterSpecial());
        FM.add(new FilterCarNoNecessary());
        FM.add(new FilterNumbers());
        FM.add(new FilterNewLine());
        FM.add(new FilterDoubleSpace());
        for (final File fileEntry : Objects.requireNonNull(folderPath.listFiles()))
        {
            text = new String(Files.readAllBytes(Paths.get(folderPath.getAbsolutePath() + '/' + fileEntry.getName())));
            text = FM.process(text);
            terms = strToArray.toArray(text);
            tf = TF.obtainTF(terms);
            TF.obtainTFIDF(tf);
        }

        HashMap<String, Double> docs = TF.calculateIDF();

        File file = new File(documentsLengthFilePath);
        try (BufferedWriter bf = new BufferedWriter(new FileWriter(file))) {

            for (Map.Entry<String, Double> entry : docs.entrySet()) {
                bf.write(entry.getKey() + "=" + entry.getValue().toString());
                bf.newLine();
            }
            bf.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        File file2 = new File(invertedIndexFilePath);
        try (BufferedWriter bf2 = new BufferedWriter(new FileWriter(file2))) {
            HashMap<String, Double> doc_peso;
            Pair<Double, HashMap<String, Double>> value;
            double IDF;
            for (String term : TF.invertedIndex.keySet()) {
                value = TF.invertedIndex.get(term);
                IDF = value.first;
                doc_peso = value.second;
                bf2.write(term + "," + IDF);
                for (String f : doc_peso.keySet()) {
                    bf2.write("," + f + "=" + doc_peso.get(f));
                }
                bf2.write("\n");
            }
            bf2.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
