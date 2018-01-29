import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class BkTreeMain {

    public static void main(String[] args) throws IOException {

        MiscOprns miscOprns=new MiscOprns();
        HashMap wordListMap=miscOprns.getWordListMap("sentence.txt");

        //Add the words as nodes to bktree
        //Initial building of the tree
        HashMap<String,String> dictHashMap=miscOprns.populateDictionaryHashmap("vocab.txt");
        //System.out.println("Root word :: "+dictHashMap.keySet().toArray()[0].toString());
        BkTree bkTree = new BkTree(dictHashMap.keySet().toArray()[0].toString());
        dictHashMap.forEach((dictKey,dictValue)->{
            bkTree.addNode(dictKey.toString());
        });

        //Flag the misspelt words, given the dictionary and wordlist maps
        miscOprns.flagMisspeltWords(dictHashMap,wordListMap);
        //Get the max distance value from the file
        int maxDistanceValue=miscOprns.getMaxDistanceValue("MaxDistance.txt");
        //Traverse the nodes for every word mapped as 1 in the word list
        //1 means the word isn't present in the dictionary
        long startTime = System.currentTimeMillis();

        bkTree.traverseNodes(wordListMap,maxDistanceValue);
        long endTime   = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println("Time taken to execute :: "+totalTime+" ms");
    }
}