package io.litun.complexview;

import io.litun.complexview.model.Markup2;

/**
 * Created by Litun on 22.04.2017.
 */

public interface MarkupProcessor {
    Markup2 process(String object, ResourceCache resourceCache);
}
