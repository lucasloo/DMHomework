package wordSegmentation;

import fileReader.ReadFileService;
import org.wltea.analyzer.lucene.IKAnalyzer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by luke on 2016/11/6.
 */
public class WordSegmentationService {

    /** term frequency map of all files */
    private HashMap<String, HashMap<String, Float>> allTFMap = new HashMap<>();
    /** the number of files each term occurs */
    private HashMap<String, Integer> occurTimesMap = new HashMap<>();
    /** inverse document frequency */
    private HashMap<String, Float> idfMap = new HashMap<>();
    /** tf-idf result */
    private HashMap<String, HashMap<String, Float>> tf_idfResultMap = new HashMap<>();


    public void TFIDFAlgorithm(List<String> pathList){
        int fileNum = pathList.size();
        //initialize allTFMap
        for(String path : pathList){
            List<String> fileContents;
            try{
                fileContents = cutWords(path);
                HashMap<String, Float> tfMap = calculateTF(fileContents);
                allTFMap.put(path, tfMap);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        //initialize occurTimesMap
        for(String path : pathList){
            HashMap<String, Float> tmpMap = allTFMap.get(path);
            for(Map.Entry<String, Float> tmp : tmpMap.entrySet()){
                if(occurTimesMap.get(tmp.getKey()) == null){
                    occurTimesMap.put(tmp.getKey(), 1);
                }else {
                    occurTimesMap.put(tmp.getKey(), occurTimesMap.get(tmp.getKey()) + 1);
                }
            }
        }
        //initialize idfMap
        calculateIDF(fileNum);
        //initialize tf_idfResultMap
        for(String path : pathList){
            tf_idf(path);
        }
    }

    /**
     * word Segmentation
     * @param path
     * @return
     * @throws IOException
     */
    public List<String> cutWords(String path) throws IOException{

        List<String> words = new ArrayList<>();
        String text = ReadFileService.getFileContentToString(path);
        IKAnalyzer analyzer = new IKAnalyzer();
        if(text != null){
            words = analyzer.split(text);
        }
        return words;
    }

    /**
     * record the term Occurrences
     * @param wordSegStr
     * @return
     */
    public HashMap<String, Integer> recordOccurrence(List<String> wordSegStr){
        HashMap<String, Integer> occurrenceMap = new HashMap<>();
        for(String word : wordSegStr){
            if(occurrenceMap.get(word) == null){
                occurrenceMap.put(word, 1);
            }
            else{
                occurrenceMap.put(word, occurrenceMap.get(word) + 1);
            }
        }
        return occurrenceMap;
    }


    /**
     * calculate the term frequency
     * @param wordSegStr
     * @return
     */
    public HashMap<String, Float> calculateTF(List<String> wordSegStr){
        HashMap<String, Float> tfMap = new HashMap<>();
        Map<String, Integer> occMap = recordOccurrence(wordSegStr);
        int wordLenth = wordSegStr.size();
        for(Map.Entry<String, Integer> tmp : occMap.entrySet()){
            tfMap.put(tmp.getKey(), Float.parseFloat(tmp.getValue().toString()) / wordLenth);
        }
        return tfMap;
    }

    /**
     * calculate inverse document frequency
     * @param fileNum
     */
    public void calculateIDF(int fileNum){
        for(Map.Entry<String, Integer> tmp : occurTimesMap.entrySet()){
            float idfValue = (float)Math.log(fileNum / Float.parseFloat(tmp.getValue().toString()));
            idfMap.put(tmp.getKey(), idfValue);
        }
    }

    /**
     * calculate tf-idf
     * @param filePath
     */
    public void tf_idf(String filePath){
        HashMap<String, Float> tf_idfValueMap = new HashMap<>();
        HashMap<String, Float> tfMap = allTFMap.get(filePath);
        for(Map.Entry<String, Float> tmp: tfMap.entrySet()){
            Float tf_idfValue = tmp.getValue() * idfMap.get(tmp.getKey());
            tf_idfValueMap.put(tmp.getKey(), tf_idfValue);
        }
        tf_idfResultMap.put(filePath, tf_idfValueMap);
    }

    public HashMap<String, HashMap<String, Float>> getAllTFMap() {
        return allTFMap;
    }

    public void setAllTFMap(HashMap<String, HashMap<String, Float>> allTFMap) {
        this.allTFMap = allTFMap;
    }

    public HashMap<String, HashMap<String, Float>> getTf_idfResultMap() {
        return tf_idfResultMap;
    }

}
