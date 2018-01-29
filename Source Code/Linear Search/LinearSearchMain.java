import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

public class LinearSearchMain {
    public static void main(String[] args) throws IOException {

          //Read the input files and populate the wordlist and hashmap
           MiscOprns miscOprns=new MiscOprns();
           HashMap<String, Integer> wordListMap=miscOprns.getWordListMap("sentence.txt");
           HashMap<String,String> dictHashMap=miscOprns.populateDictionaryHashmap("vocab.txt");
           int maxDistanceValue=miscOprns.getMaxDistanceValue("MaxDistance.txt");
           //By default wordlist is passed by reference, any
           miscOprns.flagMisspeltWords(dictHashMap,wordListMap);
           //Time to perform only the linear search operation
           long startTime = System.currentTimeMillis();

           //Search the misspelt words in the given wordlist compared with the dictionary
           LinearSearch linearSearchOprn=new LinearSearch();
           linearSearchOprn.populateMisSpeltWordFile(dictHashMap,wordListMap,maxDistanceValue);

           long endTime   = System.currentTimeMillis();
           long totalTime = endTime - startTime;
           System.out.println("Time taken to execute :: "+totalTime+" ms");
    }
}