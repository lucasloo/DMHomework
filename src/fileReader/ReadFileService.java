package fileReader;

import com.sun.org.apache.bcel.internal.generic.FLOAD;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

/**
 * Created by luke on 2016/11/6.
 */
public class ReadFileService {

    /**file name list*/
    public List<String> fileNamePathStrList = new ArrayList<>();

    public void init(String folderPath){
        Path filePath = Paths.get(folderPath);
        try {
            Files.walkFileTree(filePath, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    String fileString = file.toAbsolutePath().toString();
                    if(fileString.toUpperCase().endsWith(".TXT")){
                        fileNamePathStrList.add(fileString);
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    public static String getFileContentToString(String path){
        try{
            StringBuffer strSb = new StringBuffer(); //String is constant£¬ StringBuffer can be changed.
            InputStreamReader inStrR = new InputStreamReader(new FileInputStream(path), "UTF-8"); //byte streams to character streams
            BufferedReader br = new BufferedReader(inStrR);
            String line = br.readLine();
            while(line != null){
                strSb.append(line).append("\r\n");
                line = br.readLine();
            }
            br.close();
            return strSb.toString();
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static void writeFile(HashMap<String, HashMap<String, Float>> tf_idfMap){
        FileWriter writer;
        try {
            writer = new FileWriter("output/tf_idf_result.txt");
            for(Map.Entry<String, HashMap<String, Float>> tmpEntry : tf_idfMap.entrySet()){
                writer.write(tmpEntry.getKey() + "\n");
                List<Map.Entry<String, Float>> sortedList = sortNounCount(tmpEntry.getValue());
                writer.write(sortedList.toString() + "\n");
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeFile(List<Map.Entry<String, Integer>> nounCountMapList){
        FileWriter writer;
        try {
            writer = new FileWriter("output/output.txt");
            for(Map.Entry<String, Integer> tmp : nounCountMapList){
                writer.write(tmp.toString() + "\n");
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Map.Entry<String, Float>> sortNounCount(HashMap<String, Float> curMap){
        List<Map.Entry<String, Float>> nounCountMapList = new ArrayList<>(curMap.entrySet());
        Collections.sort(nounCountMapList, new Comparator<Map.Entry<String, Float>>() {
            public int compare(Map.Entry<String, Float> o1, Map.Entry<String, Float> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });
        return nounCountMapList;
    }

    public List<String> getFileNamePathStrList() {
        return fileNamePathStrList;
    }
}
