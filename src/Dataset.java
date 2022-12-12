import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Dataset {
    File dataFile;
    Scanner scan;
    ArrayList<String> fileArray = new ArrayList<String>();
    ArrayList<String> wordKey = new ArrayList<String>();
    double[][] prob;

    public Dataset() {
    }

    public void addData(String path) {
        dataFile = new File(getClass().getResource(path).getPath());

        try {
            scan = new Scanner(dataFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        while (scan.hasNextLine()) {
            fileArray.add(scan.nextLine().replaceAll("[^a-zA-Z0-9\\s]", "").toLowerCase());
        }
    }

    public void printDataset() {
        System.out.println(wordKey);
    }

    public void processData() {
        for (String line: fileArray) {
            String[] words = line.split("\\s");
            for (String w: words) {
                if (!(wordKey.contains(w))) wordKey.add(w);
            }
        }
        wordKey.add("\n");
        
        prob = new double[wordKey.size()][wordKey.size()];

        for (String line: fileArray) {
            String[] words = line.split("\\s");
            for (int i=0; i<words.length-1; i++) {
                prob[wordKey.indexOf(words[i+1])][wordKey.indexOf(words[i])]++;
            }
            prob[wordKey.indexOf(words[0])][wordKey.indexOf("\n")]++;
            prob[wordKey.indexOf("\n")][wordKey.indexOf(words[words.length-1])]++;
        }

        for (int i=0; i<prob.length; i++) {
            double sum = 0;
            for (int j=0; j<prob.length; j++) {
                sum+=prob[j][i];
            }
            for (int j=0; j<prob.length; j++) {
                prob[j][i]/=sum;
            }
        }

        // System.out.println(wordKey);
        // for (double[] v: prob) {
        //     for (double v1: v) {
        //         System.out.print(v1 + " ");
        //     }
        //     System.out.println();
        // }
    }

    public String generate(String startingWord, int wordCount) {
        int indexCurrent = wordKey.indexOf(startingWord.replaceAll("[^a-zA-Z0-9\\s]", "").toLowerCase());
        String output = wordKey.get(indexCurrent) + " ";
        for (int i=0; i<wordCount-1; i++) {
            double[] probVec = new double[prob.length];
            for (int j=0; j<probVec.length; j++) {
                probVec[j] = prob[j][indexCurrent];
            }
            double randNum = Math.random();
            double sum = 0;
            int realValue = wordKey.size()-1;

            // double sum2 = 0;
            // for (double j: probVec) {
            //     sum2 += j;
            // }
            // System.out.println(sum2);

            for (int j=0; j<probVec.length; j++) {
                if (probVec[j] > 0) {
                    
                    if (randNum > sum && randNum < sum + probVec[j]) {
                        realValue = j;
                        break;
                    }
                    sum += probVec[j];
                }
            }
            if (realValue == wordKey.size()-1) output += wordKey.get(realValue);
            else output += wordKey.get(realValue) + " ";
            indexCurrent = realValue;
        }
        return output;
    }
}
