package io.litun.complexview;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Litun on 22.04.2017.
 */

public class ResourceCache {
    private final Map<Integer, Drawable> drawableCache = new HashMap<>();
    private final Map<Integer, String> stringCache = new HashMap<>();
    private final Resources resources;

    public ResourceCache(Context context) {
        resources = context.getResources();
    }

    public Drawable getDrawable(@DrawableRes int drawableRes) {
        Drawable result;
        if (!drawableCache.containsKey(drawableRes)) {
            result = resources.getDrawable(drawableRes);
            drawableCache.put(drawableRes, result);
        } else {
            result = drawableCache.get(drawableRes);
        }
        return result;
    }

    public String getString(@StringRes int stringRes) {
        String result;
        if (!stringCache.containsKey(stringRes)) {
            result = resources.getString(stringRes);
            stringCache.put(stringRes, result);
        } else {
            result = stringCache.get(stringRes);
        }
        return result;
    }
}
