import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


import java.util.List;
import java.util.UUID;


@AllArgsConstructor
@Getter
@Setter
public class SystemFile {
    private UUID fileId;
    private FileTypes fileType;
    private String fileName;
    private int size;
    private List<String> links;
    private List<Integer> fileBlocks;
    private List<UUID> fd;

    @Override
    public String toString() {
        return "SystemFile{" +
                "fileType=" + fileType +
                ", fileName='" + fileName + '\'' +
                ", size=" + size +
                '}';
    }


    public String toStringExtended() {
        return "SystemFile{" +
                "fileId=" + fileId +
                ", fileType=" + fileType +
                ", fileName='" + fileName + '\'' +
                ", size=" + size +
                ", links=" + links +
                ", fileBlocks=" + fileBlocks +
                ", fd=" + fd +
                '}';
    }

}
