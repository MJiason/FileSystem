import java.util.List;

public interface Persistence {
    List<SystemFile> loadFileSystem();
    void saveFileSystem(List<SystemFile> fileSystem);
}
