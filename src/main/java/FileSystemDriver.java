import java.util.*;

public class FileSystemDriver implements FileSystem {

    private final static FileSystemDriver systemDriver = new FileSystemDriver();

    private final int BYTES_FOR_BLOCK = 30;

    private List<Integer> fileBlocs = new ArrayList<>();

    private Persistence persistence = new PersistanceImpl();

    private List<SystemFile> files = new ArrayList<>();

    private FileSystemDriver() {
    }

    public static FileSystemDriver getInstance() {
        return systemDriver;
    }

    @Override
    public void w() {
        if (files.isEmpty()) {
            System.out.println("file system not mounted");
            return;
        }
        for (SystemFile file : files) {
            System.out.println(file.toStringExtended());
        }
    }

    @Override
    public void mount() {
        this.files = persistence.loadFileSystem();
        loadFileBlocks();
    }

    @Override
    public void unmount() {
        System.out.println("FS unmount");
        this.files = new ArrayList<>();
    }

    @Override
    public void filestat(UUID fileId) {
        if (files.isEmpty()) {
            System.out.println("file system not mounted");
            return;
        }
        for (SystemFile file : files) {
            if (file.getFileId().equals(fileId)) {
                System.out.println(file.toStringExtended());
            }
        }
    }

    @Override
    public void ls() {
        if (files.isEmpty()) {
            System.out.println("file system not mounted");
            return;
        }
        for (SystemFile file : files) {
            if (file.getFileType() == FileTypes.LINK) {
                System.out.println(file.toString());
            }
        }
    }

    @Override
    public void create(String fileName) {
        if (files.isEmpty()) {
            System.out.println("file system not mounted");
            return;
        }

        if (!isNameUnique(fileName)) {
            System.out.println("file with that name already exist");
            return;
        }

        List<String> testLinks = new ArrayList<>();
        List<Integer> testLinks1 = new ArrayList<>();
        SystemFile testFile = new SystemFile(Utils.generateId(), FileTypes.FILE, fileName, 0, testLinks, testLinks1, new ArrayList<>());
        files.add(testFile);
        createDirLink(fileName);
        link(fileName, fileName);
        System.out.println("file created");
        persistence.saveFileSystem(this.files);
    }

    @Override
    public void open(String fileName) {
        for (SystemFile file : files) {
            if (file.getFileName().equals(fileName) && file.getFileType() == FileTypes.FILE) {
                List<UUID> fd = new ArrayList<>();
                if (file.getFd() != null) {
                    fd = file.getFd();
                }
                fd.add(UUID.randomUUID());
                file.setFd(fd);
            }
        }
        persistence.saveFileSystem(this.files);
    }

    @Override
    public void close(UUID fdId) {
        for (SystemFile file : files) {
            for (UUID fd : file.getFd()) {
                if (fd.equals(fdId)) {
                    file.getFd().remove(fd);
                    System.out.println("file closed " + file.getFileName());
                    persistence.saveFileSystem(this.files);
                    return;
                }
            }
        }
        System.out.println("file not found");
    }

    @Override
    public void read(UUID fdId, int offset) {
        for (SystemFile file : files) {
            if (file.getFd() == null ){
                file.setFd(new ArrayList<>());
            }
            for (UUID fd : file.getFd()) {
                if (fd.equals(fdId)) {
                    if (file.getSize() < offset) {
                        System.out.println("Out of file error " + file.getFileName());
                    }
                    System.out.println("file read " + file.getFileName() + " " + offset + " bytes");
                    persistence.saveFileSystem(this.files);
                    return;
                }
            }
        }
        System.out.println("file not found");
    }

    @Override
    public void write(UUID fdId, int offset) {
        for (SystemFile file : files) {
            if (file.getFileType() != FileTypes.FILE) {
                continue;
            }
            for (UUID fd : file.getFd()) {
                if (fdId.equals(fd)) {
                    file.setSize(file.getSize() + offset);
                    file.setFileBlocks(setFileBlocs(file.getSize()));
                    System.out.println("file write " + file.getFileName() + " " + offset + " bytes");
                    return;
                }
            }
        }
        System.out.println("file not found");
    }

    @Override
    public void link(String name1, String name2) {
        boolean isFilePresent = false;
        for (SystemFile file : files) {
            if (Objects.equals(file.getFileName(), name2) && file.getFileType() == FileTypes.FILE) {
                List<String> links = file.getLinks();
                links.add(name1);
                file.setLinks(links);
                isFilePresent = true;
            }
        }
        if (isFilePresent) {
            SystemFile testFile = new SystemFile(Utils.generateId(), FileTypes.LINK, name1, 0,
                    Collections.singletonList(name2), null, null);
            files.add(testFile);
            persistence.saveFileSystem(files);
        }
    }

    @Override
    public void unlink(String name) {
        files.removeIf(file -> file.getFileName().equals(name) && file.getFileType() == FileTypes.LINK);
    }

    @Override
    public void truncate(UUID fdId, int offset) {
        for (SystemFile file : files) {
            for (UUID fd : file.getFd()) {
                if (fd.equals(fdId)) {
                    file.setSize(offset);
                    file.setFileBlocks(setFileBlocs(file.getSize()));
                    System.out.println("file write " + file.getFileName() + " " + offset + " bytes");
                    return;
                }
            }
        }
        System.out.println("file not found");
    }

    private boolean isNameUnique(String fileName) {
        boolean isUnique = true;
        for (SystemFile file : files) {
            if (file.getFileName().equals(fileName)) {
                isUnique = false;
                break;
            }
        }
        return isUnique;
    }

    private void createDirLink(String fileLink) {
        SystemFile file = this.files.get(0);
        List<String> links = file.getLinks();
        links.add("home/" + fileLink);
    }

    private List<Integer> setFileBlocs(int size) {
        List<Integer> freeBlocks = new ArrayList<>();
        Collections.sort(this.fileBlocs);
        int lastBlock;
        if (fileBlocs.isEmpty()) {
            lastBlock = 0;
        } else {
            lastBlock = fileBlocs.get(fileBlocs.size() - 1);
        }

        int countOfBlocs = size / BYTES_FOR_BLOCK;
        for (int i = 0; i < countOfBlocs; i++) {
            if (!fileBlocs.isEmpty() && fileBlocs.size() < fileBlocs.get(fileBlocs.size() - 1)) {
                freeBlocks.add(nextFreeFileBlock());
            } else {
                freeBlocks.add(++lastBlock);
            }
        }
        return freeBlocks;
    }

    private int nextFreeFileBlock() {
        for (int i = 0; i < fileBlocs.get(fileBlocs.size() - 1); i++) {
            if (fileBlocs.get(i) != i) {
                return i;
            }
        }
        return 0;
    }

    private void loadFileBlocks() {
        for (SystemFile file : files) {
            if (file.getFileBlocks() != null) {
                fileBlocs.addAll(file.getFileBlocks());
            }
        }
    }
}
