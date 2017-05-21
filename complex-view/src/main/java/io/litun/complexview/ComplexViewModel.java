package io.litun.complexview;

import android.content.Context;

import io.litun.complexview.model.Markup;
import io.litun.complexview.model.Markup2;

/**
 * Created by Litun on 22.03.17.
 */
public class ComplexViewModel {
    private Markup2 markup;

    public ComplexViewModel(Markup2 markup) {
        this.markup = markup;
    }

    public Markup2 getMarkup() {
        return markup;
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
            Markup2 markup = processor.process(stringFile, cache);
            return new ComplexViewModel(markup);
        }
    }
}
