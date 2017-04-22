package io.litun.complexview;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.WorkerThread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Litun on 21.04.2017.
 */

public class MarkdownParser {

    private final Resources resources;
    private final ResourceCache resourceCache;

    public MarkdownParser(Context context, ResourceCache resourceCache) {
        this.resources = context.getResources();
        this.resourceCache = resourceCache;
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

    private String readStringFile(String name) {
        try {
            return readFile(name);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
