import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Scanner;
import java.io.File;


public class PersistanceImpl implements Persistence {

    private final Type listType = new TypeToken<List<SystemFile>>() {
    }.getType();
    private final Gson gson = new Gson();

    @Override
    public List<SystemFile> loadFileSystem() {
        return gson.fromJson(readFile(), this.listType);
    }

    @Override
    public void saveFileSystem(List<SystemFile> fileSystem) {
        writeFile(gson.toJson(fileSystem, listType));
    }

    public String readFile() {
        try {
            File myObj = new File("src/main/resources/FS.json");
            Scanner myReader = new Scanner(myObj);
            StringBuilder builder = new StringBuilder();
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                builder.append(data);
            }
            myReader.close();
            return builder.toString();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return "";
    }

    private void writeFile(String content) {
        try {
            FileWriter myWriter = new FileWriter("src/main/resources/FS.json");
            myWriter.write(content);
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
