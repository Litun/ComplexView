package io.litun.complexview;

import android.content.Context;

import io.litun.complexview.model.DynamicLayout;
import io.litun.complexview.model.Markup;

/**
 * Created by Litun on 22.03.17.
 */
public class ComplexViewModel {
    private Markup markup;
    private final DynamicLayout layout = new DynamicLayout();

    public ComplexViewModel(Markup markup) {
        this.markup = markup;
    }

    public Markup getMarkup() {
        return markup;
    }

    public DynamicLayout getLayout() {
        return layout;
    }

    public static class Builder {
        private ResourceCache cache;
        private MarkupParser parser;
        private String fileName;
        private MarkupProcessor processor;

        public Builder(Context context) {
            cache = new ResourceCache(context);
            parser = new MarkupParser(context, cache);
        }

        public Builder setSourceFileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        public Builder setMarkupProcessor(MarkupProcessor processor) {
            this.processor = processor;
            return this;
        }

        public ComplexViewModel build() {
            String stringFile = parser.readStringFile(fileName);
            Markup markup = processor.process(stringFile, cache);
            return new ComplexViewModel(markup);
        }
    }
}
