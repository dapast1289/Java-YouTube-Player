package base;

import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by Jaap Heijligers on 3/24/15.
 */
public class CacheDatabase {
    JSONObject dataBase;
    Path cacheFile = Paths.get("./cache.json");

    public CacheDatabase() {
        try {
            readDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readDataBase() throws IOException {
        if (dataBase != null) return;
        if (!Files.exists(cacheFile)) {
            Files.createFile(cacheFile);
            dataBase = new JSONObject();
            return;
        }
        dataBase = new JSONObject(new String(Files.readAllBytes(cacheFile)));
    }

    private void writeDatabase() {
        try {
            Files.write(cacheFile, dataBase.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean hasMethod(String key) {
        return dataBase.has(key);
    }

    public String getMethod(String key) {
        return dataBase.getString(key);
    }

    public void addMethod(String key, String method) {
        dataBase.put(key, method);
        writeDatabase();
    }
}
