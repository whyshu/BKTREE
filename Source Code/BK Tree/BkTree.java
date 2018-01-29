import java.io.BufferedWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class BkTree {

    private Node _rootNode;
    MiscOprns miscOprns=new MiscOprns();

    public BkTree(String rootData) {
        _rootNode = new Node(rootData, 0);
        _rootNode.childNodes = new ArrayList<Node>();
    }

    public void addNode(String inputWord) {
        this.addNode(inputWord, _rootNode);
    }

    //Function to add the new word to the BkTreeMain
    public void addNode(String inputWord, Node currNode) {
        double cost = getEditDistance(currNode.word, inputWord);
        if (currNode.childNodes.size() == 0) {
            currNode.childNodes.add(new Node(inputWord, cost));
        } else {
            //Check if collision occurs
            List<Node> nodes = currNode.childNodes.stream().filter(a -> Objects.equals(a.cost, cost)).collect(Collectors.toList());
            //if yes, recursively call add node
            if (nodes.size() > 0 ) {
                addNode(inputWord, nodes.get(0));
            } else {
                //else add the new node to the root
                currNode.childNodes.add(new Node(inputWord, cost));
            }
        }
    }

    public void printNodes(){
        this.printNodes(_rootNode);
    }

    public void printNodes(Node childNode) {
        //System.out.println("Node :: " + childNode.word + "::" + childNode.cost);
        for (int NodeCnt = 0; NodeCnt < childNode.childNodes.size(); NodeCnt++) {
            printNodes(childNode.childNodes.get(NodeCnt));
        }
    }


    public void traverseNodes(HashMap wordListMap,int maxEditDist) throws IOException {
            populateMisSpeltWordFile(wordListMap,maxEditDist);
    }
    //Populate the misspelt words file
    public void populateMisSpeltWordFile(HashMap wordListMap,int maxDistValue) throws IOException {
        //Read the input files and populate the wordlist and hashmap
        BufferedWriter writer=miscOprns.createFile("MisspelledWords.txt");
        iterateWordListMap(writer,wordListMap,maxDistValue);
    }

    //Iterate the words list map
    public void iterateWordListMap(BufferedWriter writer,HashMap wordListMap,int maxDistValue) throws IOException {
        wordListMap.forEach((wordKey,wordValue)-> {
            if (wordValue.toString().equals("1")) {
                miscOprns.writeToMisspeltWordsFile(writer, wordKey.toString() + ":");
                //System.out.println("Misspelt word :: "+wordKey.toString());
                List<String> possibleCorrWords=new ArrayList<String>();
                this.traverseNodes(possibleCorrWords,_rootNode, wordKey.toString(), maxDistValue);
                miscOprns.seperateCorrWordsWithComma(writer,possibleCorrWords);
                try {
                    writer.newLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        //for(Iterator<String> corrWord=possibleCorrWords.iterator();corrWord.hasNext();){
        writer.close();
    }
    //Traverse nodes
    public void traverseNodes(List possibleCorrectWords,Node childNode,String wordToCheck,int maxEditDist){
            //Calculate the edit distance with the root and the current misspelt word
            double editDistance = getEditDistance(childNode.word, wordToCheck.toString());
            //Get the tolerance value, and calculate d-tol,d+tol
            double lowerLimit=Math.abs(editDistance-maxEditDist);
            double higherLimit=editDistance+maxEditDist;
            //Check if edit distance is less than TOL
            //Add add to possible correct words list
            isTolRange(possibleCorrectWords,childNode,editDistance,maxEditDist);
            //For all the childnodes in the range
            for (Iterator<Node> currNode = childNode.childNodes.iterator(); currNode.hasNext();) {
                Node iterNode = currNode.next();
                //Check whether the given node is within the tol range
                //if true
                if (isWithinRange(iterNode.cost,higherLimit,lowerLimit)) {
                    //Traverse for its child nodes
                    traverseNodes(possibleCorrectWords,iterNode, wordToCheck, maxEditDist);
                }
            }
    }

    private boolean isWithinRange(double editDistance,double higherLimit,double lowerLimit){
        if (editDistance>=lowerLimit&&editDistance<=higherLimit){
            return true;
        }
        return false;
    }
    private boolean isTolRange(List possibleCorrWords,Node iterNode,double editDistance,int maxEditDist){
        //Add to possible correct words list, only if editdistance
        //is less than or equal to maximum edit distance
        if (editDistance<=maxEditDist) {
            //Add to possible correct words list
            possibleCorrWords.add(iterNode.word);
            possibleCorrWords.add(", ");
            return true;
        }
       return false;
    }
    //get the edit distance
    public double getEditDistance(String parentString, String childString) {
        //if any of the srting is null, return 0
        if (parentString.equals(null)||childString.equals(null)){
            return 0;
        }
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
                    //If it doesnt match, get the minimum possible value of top, left and diagonal value
                    distMatrix[row][col] = 1 + Math.min(Math.min(distMatrix[row][col - 1], distMatrix[row - 1][col]), distMatrix[row - 1][col - 1]);
                }
            }
        }
        //System.out.println(distMatrix[parentStringLen][childStringLen]);
        //return the last element of the matrix to get the final edit distance
        return distMatrix[parentStringLen][childStringLen];
    }
}

