import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MiscOprns {
    //Get the words list given the sentence.txt file
    public HashMap<String,Integer> getWordListMap(String sentenceFileName) {
        HashMap<String,Integer> wordListMap = new LinkedHashMap<>();;
        //open the sentence.txt file and read line by line
        try (BufferedReader br = Files.newBufferedReader(Paths.get(sentenceFileName))) {
            for (String line = null; (line = br.readLine()) != null; ) {
                //for every sentence in the file, split it into a list of words
                //avoiding punctuation,white space,numbers and all other possible characters
                Pattern p = Pattern.compile("[\\p{Alpha}]+");
                Matcher m = p.matcher(line);
                while(m.find()) {
                    wordListMap.put(line.substring(m.start(), m.end()).toLowerCase(), 0);
                }
            }
        } catch (IOException e) {
            System.out.println("Exception in getting the words list :: " + e.fillInStackTrace());
        }
        //printMap(wordListMap);
        return wordListMap;
    }

    //Seperate correct words with comma
    public void seperateCorrWordsWithComma(BufferedWriter writer,List strList){
        MiscOprns miscOprns=new MiscOprns();
        //Omitting the last comma, so iterating only till second last string
        for(int strLen=0;strLen<strList.size()-1;strLen++) {
            miscOprns.writeToMisspeltWordsFile(writer,strList.get(strLen).toString());
        }
    }
    //To print the map given
    private void printMap(HashMap mapToPrint){
        mapToPrint.forEach((key,value)->{
            System.out.println(key.toString()+"    "+value.toString());
        });
    }

    //Populate the words in the dictionary file into the hash map
    public HashMap<String,String> populateDictionaryHashmap(String dictionaryFileName) {
        //Declare a linked hashmap to maintain the order
        HashMap<String, String> dictionaryHashMap = new LinkedHashMap<>();
        //Read the file line by line
        try (BufferedReader br = Files.newBufferedReader(Paths.get(dictionaryFileName))) {
            for (String line = null; (line = br.readLine()) != null; ) {
                //Populate the hashmap with the same key and value pair
                dictionaryHashMap.put(line,line);
            }
        } catch (IOException e) {
            System.out.println("Exception in populating the hashmap :: " + e.fillInStackTrace());
        }
        //printMap(dictionaryHashMap);
        //return hashmap
        return dictionaryHashMap;
    }


    //A function to get the list of misspelt words, given the wordlist
    //and the dictionary hashmap
    public void flagMisspeltWords(HashMap dictHashMap,HashMap wordListMap){
        //Iterate over the wordlist map
        Iterator it = wordListMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            //Check if the dictionary hashmap contains the given word
            //If not, add the word to the misspelt words list
            if(!dictHashMap.containsValue(pair.getKey().toString())){
                //All words flagged 1 aren't in the dictionary
                pair.setValue(1);
            }
        }
    }

    //Get the max distance value, given the file name
    public int getMaxDistanceValue(String maxDistFileName){
        int maxDistValue=0;
        try (BufferedReader br = Files.newBufferedReader(Paths.get(maxDistFileName))) {
            for (String line = null; (line = br.readLine()) != null; ) {
                maxDistValue=Integer.parseInt(line);
            }
        } catch (IOException e) {
            System.out.println("Exception in getting the max. distance value :: "
                    + e.fillInStackTrace());
        }
        return maxDistValue;
    }
    //Create the misspelt words.txt file
    public BufferedWriter createFile(String FileName){
        try  {
            FileWriter fw = new FileWriter(FileName);
            BufferedWriter writer = new BufferedWriter(fw);
            return writer;
        }
        catch(IOException e){
            System.out.println("Exception in accessing misspelled words file :: "+e.fillInStackTrace());
        }
        return null;
    }
    //Write to misspelt words file
    public void writeToMisspeltWordsFile(BufferedWriter writer,String stringToWrite)  {
        try {
            writer.write(stringToWrite);
        }
        catch (IOException e) {
            System.out.println("Exception in writing to misspelled words file :: "+e.fillInStackTrace());
        }
    }
}