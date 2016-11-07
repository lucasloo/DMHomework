import fileReader.ReadFileService;
import wordSegmentation.WordSegmentationService;

/**
 * Created by luke on 2016/11/6.
 */
public class TFIDF {
    public static void main(String args[]){
        ReadFileService readFileService = new ReadFileService();
        readFileService.init("data");
        WordSegmentationService wordSegmentationService = new WordSegmentationService();
        wordSegmentationService.TFIDFAlgorithm(readFileService.getFileNamePathStrList());
        ReadFileService.writeFile(wordSegmentationService.getTf_idfResultMap());
    }
}
