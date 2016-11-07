import fileReader.ReadFileService;

import java.io.*;
import java.util.*;

/**
 * Created by luke on 2016/11/5.
 */
public class NounSplit {

    public Map<String, Integer> nounCount = new HashMap<>();

    public static void main(String args[]) {
        ReadFileService readFileService = new ReadFileService();
        readFileService.init("data");
        NounSplit nounSplitManager = new NounSplit();
        for(String filePath : readFileService.getFileNamePathStrList()){
            nounSplitManager.analysisFileForNoun(filePath);
        }
        ReadFileService.writeFile(nounSplitManager.sortNounCount());
    }

    public void analysisFileForNoun(String filePath){
        try {
            String encoding="UTF-8";
            File file=new File(filePath);
            if(file.isFile() && file.exists()){
                InputStreamReader read = new InputStreamReader(new FileInputStream(file),encoding);
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineText;
                while((lineText = bufferedReader.readLine()) != null){
                    analysisLineForNoun(lineText);
                }
                read.close();
            }else{
                System.out.println("找不到指定的文件");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void analysisLineForNoun(String lineText){
        String[] strArr = lineText.split("  ");
        for(String tmp : strArr){
            if(tmp.contains("/n")){
                int count = 1;
                if(nounCount.containsKey(tmp)){
                    nounCount.put(tmp, nounCount.get(tmp) + count);
                }else{
                    nounCount.put(tmp, count);
                }
            }
        }
    }

    public List<Map.Entry<String, Integer>> sortNounCount(){
        List<Map.Entry<String, Integer>> nounCountMapList = new ArrayList<>(nounCount.entrySet());
        Collections.sort(nounCountMapList, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return (o2.getValue() - o1.getValue());
            }
        });
        return nounCountMapList;
    }

}
