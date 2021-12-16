import java.util.Scanner;

public class CommandEngine {


    private Scanner scanner = new Scanner(System.in);


    public void run() {
        while (true) {
            try {
                parseCommand().execute();
            }catch (Exception exception) {
                exception.printStackTrace();
                //System.out.println("Invalid command");
            }
        }

    }

    private Command parseCommand() {
        String[] command = scanner.nextLine().split(" ");

        if (command.length == 1) {
            switch (command[0]) {
                case "mount": return new Command(CommandType.MOUNT, null, null);
                case "unmount": return new Command(CommandType.UNMOUNT, null, null);
                case "ls": return new Command(CommandType.LS, null, null);
                case "w": return new Command(CommandType.W, null, null);
            }
        }
        if (command.length == 2) {
            switch (command[0]) {
                case "create": return new Command(CommandType.CREATE, command[1], null);
                case "filestat": return new Command(CommandType.FILE_STAT, command[1], null);
                case "open": return new Command(CommandType.OPEN, command[1], null);
                case "close": return new Command(CommandType.CLOSE, command[1], null);
                case "unlink": return new Command(CommandType.UNLINK, command[1], null);
                case "mkdir": return new Command(CommandType.MKDIR, command[1], null);
                case "rmdir": return new Command(CommandType.RMDIR, command[1], null);
                case "cd": return new Command(CommandType.CD, command[1], null);
            }
        }
        if (command.length == 3) {
            switch (command[0]) {
                case "read": return new Command(CommandType.READ, command[1], command[2]);
                case "write": return new Command(CommandType.WRITE, command[1], command[2]);
                case "truncate": return new Command(CommandType.TRUNCATE, command[1], command[2]);
                case "link": return new Command(CommandType.LINK, command[1], command[2]);
                case "simulink": return new Command(CommandType.SIMULINK, command[1], command[2]);
            }
        }
        return null;
    }
}
