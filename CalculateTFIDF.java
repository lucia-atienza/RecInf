import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import static java.lang.Math.log;
import static java.lang.Math.sqrt;

public class CalculateTFIDF {
    private final File[] files;
    private int index;

    public HashMap<String, Pair<Double, HashMap<String, Double>>> invertedIndex;
    public Set<String> stopWords = new HashSet<>();

    CalculateTFIDF(File[] files) throws FileNotFoundException {
        this.files = files;
        this.index = -1;
        this.invertedIndex = new HashMap<>();
        Scanner infile = new Scanner(new File("C:\\Users\\lucia\\Desktop\\RecInf\\proyecto\\stop_words_english.txt"));
        while (infile.hasNext())
            this.stopWords.add(infile.next());
        infile.close();
    }

    public HashMap<String, Double> obtainTF(ArrayList<String> terms) {
        HashMap<String, Double> TF = new HashMap<>();
        Double f;
        for (String term : terms) {
            if (!this.stopWords.contains(term)) {
                if (TF.containsKey(term)) {
                    f = TF.get(term);
                    TF.put(term, f + 1);
                } else {
                    TF.put(term, 1.0);
                }
            }
        }

        return TF;
    }

    public void obtainTFIDF(HashMap<String, Double> TF) {
        index++;
        Pair<Double, HashMap<String, Double>> pair;
        double value, tf;
        for (String term : TF.keySet()) {
            pair = new Pair(0, new HashMap<String, Double>());
            value = TF.get(term);
            tf = (double) 1 + log(value) / Math.log(2);
            if (invertedIndex.containsKey(term)) {
                pair = invertedIndex.get(term);
            } else {
                pair.first = 0.0;
            }
            pair.second.put(files[index].toString(), tf);
            invertedIndex.put(term, pair);
        }
    }

    public HashMap<String, Double> calculateIDF() {
        HashMap<String, Double> docs = new HashMap<>();
        HashMap<String, Double> doc_peso;
        Pair<Double, HashMap<String, Double>> value;
        double weight;
        for (String name : invertedIndex.keySet()) {
            value = invertedIndex.get(name);
            doc_peso = value.second;
            value.first = log((double) files.length / doc_peso.size()) / log(2);
            for (String file : doc_peso.keySet()) {
                weight = doc_peso.get(file);
                weight *= value.first;
                doc_peso.put(file, weight);
                if (docs.containsKey(file)) {
                    weight = docs.get(file);
                    docs.put(file, weight + doc_peso.get(file) * doc_peso.get(file));
                } else {
                    docs.put(file, doc_peso.get(file) * doc_peso.get(file));
                }
            }
        }
        docs.replaceAll((f, v) -> sqrt(docs.get(f)));
        return docs;
    }
}