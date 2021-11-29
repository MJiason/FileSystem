import java.util.UUID;

public interface FileSystem {
    void w();
    void mount();
    void unmount();
    void filestat(UUID fileId);
    void ls();
    void create(String fileName);
    void open(String fileName);
    void close(UUID dsId);
    void read(UUID fdId, int offset);
    void write(UUID fdId, int offset);
    void link(String name1, String name2);
    void unlink(String name);
    void truncate(UUID fdId, int offset);
}
