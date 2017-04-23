package io.litun.complexview;

import io.litun.complexview.model.Markup;

/**
 * Created by Litun on 22.04.2017.
 */

public interface MarkupProcessor {
    Markup process(String object, ResourceCache resourceCache);
}
