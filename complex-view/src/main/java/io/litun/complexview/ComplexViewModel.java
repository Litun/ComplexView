package io.litun.complexview;

import android.content.Context;

import io.litun.complexview.model.DynamicLayout;
import io.litun.complexview.model.Markdown;

/**
 * Created by Litun on 22.03.17.
 */
public class ComplexViewModel {
    private Markdown markdown;
    private final DynamicLayout layout = new DynamicLayout();

    public ComplexViewModel(Markdown markdown) {
        this.markdown = markdown;
    }

    public Markdown getMarkdown() {
        return markdown;
    }

    public DynamicLayout getLayout() {
        return layout;
    }

    public static class Builder {
        private ResourceCache cache;
        private MarkdownParser parser;
        private String fileName;
        private MarkdownProcessor processor;

        public Builder(Context context) {
            cache = new ResourceCache(context);
            parser = new MarkdownParser(context, cache);
        }

        public Builder setSourceFileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        public Builder setMarkdownProcessor(MarkdownProcessor processor) {
            this.processor = processor;
            return this;
        }

        public ComplexViewModel build() {
            String stringFile = parser.readStringFile(fileName);
            Markdown markdown = processor.process(stringFile, cache);
            return new ComplexViewModel(markdown);
        }
    }
}
