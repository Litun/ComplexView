package io.litun.complexview;

import android.content.res.Resources;
import android.support.annotation.WorkerThread;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Litun on 21.04.2017.
 */

public class MarkdownParser {

    private final Resources resources;

    public MarkdownParser(Resources resources) {
        this.resources = resources;
    }

    @WorkerThread
    private String readFile(String name) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                resources.getAssets().open(name)));
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }
        bufferedReader.close();
        return stringBuilder.toString();
    }

    public JSONObject readJsonFile(String name) {
        try {
            String fileString = readFile(name);
            return new JSONObject(fileString);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return new JSONObject();
    }
}
