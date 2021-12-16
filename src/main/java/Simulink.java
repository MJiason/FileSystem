import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class Simulink extends SystemFile{

    private String link;

    public Simulink(UUID fileId, FileTypes fileType, String fileName, int size, List<String> links, List<Integer> fileBlocks, List<UUID> fd) {
        super(fileId, fileType, fileName, size, links, fileBlocks, fd);
    }

    public Simulink(UUID fileId, FileTypes fileType, String fileName, int size, List<String> links, List<Integer> fileBlocks, List<UUID> fd, String link) {
        super(fileId, fileType, fileName, size, links, fileBlocks, fd);
        this.link = link;
    }
}
