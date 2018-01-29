import java.io.*;
import java.util.*;

public class LinearSearch {

    MiscOprns miscOprns=new MiscOprns();

    //Populate the misspelt words file
    public void populateMisSpeltWordFile(HashMap dictHashMap,
                                         HashMap wordListMap,int maxDistValue) throws IOException {
        //Read the input files and populate the wordlist and hashmap
        BufferedWriter writer=miscOprns.createFile("MisspelledWords.txt");
        iterateWordListMap(writer,dictHashMap,wordListMap,maxDistValue);
    }

    //Iterate the words list map
    public void iterateWordListMap(BufferedWriter writer,HashMap dictHashMap,
                                   HashMap wordListMap,int maxDistValue) throws IOException {
        wordListMap.forEach((wordKey, wordValue) -> {
            if (wordValue.toString().equals("1")) {
                try {
                    populateFileForMisspelledWord(writer,dictHashMap, wordKey.toString(), maxDistValue);
                } catch (IOException e) {
                    System.out.println("Exception in writing to file :: "+e.fillInStackTrace());
                }
            }
        });
        writer.close();
    }

    //populate the misspelt word file after checking edit dist
    public void populateFileForMisspelledWord(BufferedWriter writer,HashMap dictHashMap,
                                              String wordToCheck,int maxDistValue) throws IOException {
        final int[] currEditDistance = {0};
        //tem.out.println("Misspelt word :: " + wordToCheck);
        miscOprns.writeToMisspeltWordsFile(writer, wordToCheck + ":");
        List<String> strList=new LinkedList<String>();
        //Check the possible correct words from the dictionary
        dictHashMap.forEach((dictKey, dictValue) -> {
            currEditDistance[0] = getEditDistance(dictKey.toString(), wordToCheck);
            checkEditDist(writer,strList,dictKey.toString(),currEditDistance[0],maxDistValue);
        });
        miscOprns.seperateCorrWordsWithComma(writer,strList);

        writer.newLine();
    }

    //Check if edit distance is lesser than max edit distance
    private void checkEditDist(BufferedWriter writer,List strList,String correctWord,
                               int currentEditDistance,int maxDistValue){
            if (currentEditDistance <= maxDistValue) {
            strList.add(correctWord);
            strList.add(", ");
        }
    }


    //get the edit distance
    public int getEditDistance(String parentString, String childString) {
        int parentStringLen = parentString.length();
        int childStringLen = childString.length();
        //Use an edit dist matrix to calculate the editdistance
        int distMatrix[][] = new int[parentStringLen + 1][childStringLen + 1];
        for (int row = 0; row < parentStringLen + 1; row++) {
            for (int col = 0; col < childStringLen + 1; col++) {
                if (row == 0) {
                    //Populate the initial row and column
                    distMatrix[row][col] = col;
                } else if (col == 0) {
                    distMatrix[row][col] = row;
                } else if (parentString.charAt(row - 1) == childString.charAt(col - 1)) {
                    //Assign the diagonal element if two characters match
                    distMatrix[row][col] = distMatrix[row - 1][col - 1];
                } else {
                    //If it doesnt match, get the minimum possible value
                    //of top, left and diagonal value
                    distMatrix[row][col] = 1 + Math.min(Math.min(distMatrix[row][col - 1],
                            distMatrix[row - 1][col]), distMatrix[row - 1][col - 1]);
                }
            }
        }
        //System.out.println(distMatrix[parentStringLen][childStringLen]);
        //return the last element of the matrix to get the final edit distance
        return distMatrix[parentStringLen][childStringLen];
    }
}
