package io.litun.complexview;

import io.litun.complexview.model.Markdown;

/**
 * Created by Litun on 22.04.2017.
 */

public interface MarkdownProcessor {
    Markdown process(String object, ResourceCache resourceCache);
}
