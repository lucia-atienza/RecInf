import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Recover
{
    private final String folderPath;
    private final HashMap<String, Double> docsHashmap;
    public final HashMap<String, Pair<Double, HashMap<String, Double>>> invertedIndex;
    public Set<String> stopWords = new HashSet<>();

    public Recover(String folderPath) throws FileNotFoundException
    {
        this.folderPath = folderPath;
        this.docsHashmap = new HashMap<>();
        this.invertedIndex = new HashMap<>();
        Scanner infile = new Scanner(new File("C:\\Users\\lucia\\Desktop\\RecInf\\proyecto\\stop_words_english.txt"));
        while (infile.hasNext())
            this.stopWords.add(infile.next());
        infile.close();
    }

    public void process() throws IOException
    {
        String docs, terms;
        docs = new String(Files.readAllBytes(Paths.get(folderPath + '/' + "docs.txt")));
        String[] docsSep = docs.split("\n"), aux;
        for(String doc : docsSep)
        {
            aux = doc.split("=");
            docsHashmap.put(aux[0], Double.parseDouble(aux[1]));
        }
        terms = new String(Files.readAllBytes(Paths.get(folderPath + '/' + "terms.txt")));
        String[] termsSep = terms.split("\n"), aux2;
        HashMap<String, Double> hashMapAux;
        for(String term : termsSep)
        {
            hashMapAux = new HashMap<>();
            aux2 = term.split(",");
            for(int i = 2; i < aux2.length; i++)
            {
                String[] pair = aux2[i].split("=");
                hashMapAux.put(pair[0], Double.parseDouble(pair[1]));
            }
            invertedIndex.put(aux2[0], new Pair<Double, HashMap<String, Double>>(Double.parseDouble(aux2[1]), hashMapAux));
        }
        execute();
    }
    public void execute()
    {
        String consult, text;
        HashMap<String, Double> ranking;
        ArrayList<String> terms, termsProcessed = new ArrayList<>();
        StringToArray strToArray = new StringToArray();
        FilterManager FM = new FilterManager();
        FM.add(new FilterToLowerCase());
        FM.add(new FilterSpecial());
        FM.add(new FilterCarNoNecessary());
        FM.add(new FilterNumbers());
        FM.add(new FilterNewLine());
        FM.add(new FilterDoubleSpace());
        Scanner entrada = new Scanner(System.in);
        do{
            termsProcessed = new ArrayList<>();
            System.out.print("Introduce the query terms (exit() to finish): ");
            consult = entrada.nextLine();
            if(!Objects.equals(consult, "exit()")) {
                text = FM.process(consult);
                terms = strToArray.toArray(text);
                deleteStopWords(terms, termsProcessed);
                ranking = recoverDocuments(termsProcessed);
                printRanking(ranking);
            }

        }while(!Objects.equals(consult, "exit()"));
        entrada.close();
    }

    private static void printRanking(HashMap<String, Double> ranking)
    {
        System.out.println("Recovered files:");
        List<Map.Entry<String, Double>> list = new ArrayList<>(ranking.entrySet());
        if(list.isEmpty())
        {
            System.out.println(":( looks like there's nothing here. No files recovered");
        } else {
            list.sort(Map.Entry.comparingByValue());
            Collections.reverse(list);
            for(int i = 0; i < 10; i++)
                if(i < list.size())
                    System.out.println(list.get(i));
        }

    }

    private HashMap<String, Double> recoverDocuments(ArrayList<String> termsProcessed)
    {
        HashMap<String, Double> ranking = new HashMap<>();
        Pair<Double, HashMap<String, Double>> p;
        double IDF, aux;
        HashMap<String, Double> doc_peso;
        for(String term : termsProcessed)
        {
            if(invertedIndex.containsKey(term))
            {
                p = invertedIndex.get(term);
                IDF = p.first;
                doc_peso = p.second;
                for(String file : doc_peso.keySet())
                {
                    if(ranking.containsKey(file)) {
                        aux = ranking.get(file);
                        ranking.put(file, aux+IDF*doc_peso.get(file));
                    } else {
                        ranking.put(file, IDF*doc_peso.get(file));
                    }
                }
            }
        }

        for(String file : ranking.keySet())
        {
            aux = ranking.get(file);
            ranking.put(file, aux/docsHashmap.get(file));
        }
        return ranking;
    }

    private void deleteStopWords(ArrayList<String> terms, ArrayList<String> termsProcessed) {
        for (String term : terms) {
            if (!this.stopWords.contains(term))
                termsProcessed.add(term);
        }
    }
}
