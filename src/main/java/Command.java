import lombok.AllArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
public class Command {
    private CommandType type;
    private String singleOperand;
    private String secondOperand;
    private final FileSystemDriver fileSystemDriver = FileSystemDriver.getInstance();



    public void execute() {
        switch (this.type){

            case MOUNT:
                fileSystemDriver.mount();
                break;
            case UNMOUNT:
                fileSystemDriver.unmount();
                break;
            case FILE_STAT:
                fileSystemDriver.filestat(UUID.fromString(singleOperand));
                break;
            case LS:
                fileSystemDriver.ls();
                break;
            case CREATE:
                fileSystemDriver.create(this.singleOperand);
                break;
            case OPEN:
                fileSystemDriver.open(this.singleOperand);
                break;
            case CLOSE:
                fileSystemDriver.close(UUID.fromString(this.singleOperand));
                break;
            case WRITE:
                fileSystemDriver.write(UUID.fromString(this.singleOperand), Integer.parseInt(this.secondOperand));
                break;
            case LINK:
                fileSystemDriver.link(this.singleOperand, this.secondOperand);
                break;
            case UNLINK:
                fileSystemDriver.unlink(this.singleOperand);
                break;
            case TRUNCATE:
                fileSystemDriver.truncate(UUID.fromString(this.singleOperand), Integer.parseInt(this.secondOperand));
                break;
            case READ:
                fileSystemDriver.read(UUID.fromString(this.singleOperand), Integer.parseInt(this.secondOperand));
                break;
            case W:
                fileSystemDriver.w();
                break;
        }
    }

}
