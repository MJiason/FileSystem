import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class File {
    private int fileId;
    private FileTypes fileType;
    private String fileName;
    private int size;
    private List<Integer> links;
    private List<Integer> fileBlocks;
}
