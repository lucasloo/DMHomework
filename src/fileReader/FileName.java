package fileReader;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

/**
 * Created by luke on 2016/11/5.
 */
public class FileName {
    /**file name list*/
    public List<String> fileNamePathStrList = new ArrayList<>();

    public void init(String folderPath){
//        Path filePath = Paths.get("/Users/luke/develop/leture/data");
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

    public List<String> getFileNamePathStrList() {
        return fileNamePathStrList;
    }

}
