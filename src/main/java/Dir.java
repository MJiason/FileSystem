import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Setter
@Getter
public class Dir extends SystemFile {
    private String path;
    public Dir(UUID fileId, FileTypes fileType, String fileName, int size, List<String> links, List<Integer> fileBlocks, List<UUID> fd) {
        super(fileId, fileType, fileName, size, links, fileBlocks, fd);
    }

    public Dir(UUID fileId, FileTypes fileType, String fileName, int size, List<String> links, List<Integer> fileBlocks, List<UUID> fd, String path) {
        super(fileId, fileType, fileName, size, links, fileBlocks, fd);
        this.path = path;
    }
}
