package io.litun.complexview;

import org.json.JSONObject;

import java.util.List;

import io.litun.complexview.model.MarkdownElement;

/**
 * Created by Litun on 22.04.2017.
 */

public interface MarkdownProcessor {
    List<MarkdownElement> process(JSONObject object, ResourceCache resourceCache);
}
