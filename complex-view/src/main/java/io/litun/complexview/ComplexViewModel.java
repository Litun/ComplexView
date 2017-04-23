package io.litun.complexview;

import android.content.Context;

import java.util.List;

import io.litun.complexview.model.DynamicDrawableElement;
import io.litun.complexview.model.DynamicLayout;
import io.litun.complexview.model.DynamicTextElement;
import io.litun.complexview.model.Markup;
import io.litun.complexview.model.MarkupDrawableElement;
import io.litun.complexview.model.MarkupElement;
import io.litun.complexview.model.MarkupTextElement;

/**
 * Created by Litun on 22.03.17.
 */
public class ComplexViewModel {
    private Markup markup;
    private final DynamicLayout layout;

    public ComplexViewModel(Markup markup) {
        this.markup = markup;
        layout = new DynamicLayout();
    }

    public Markup getMarkup() {
        return markup;
    }

    public DynamicLayout getLayout() {
        return layout;
    }

    public void onLayout(int width, int height) {
        float widthScale = width / markup.getWidth();
        float heightScale = height / markup.getHeight();
        int drawableInd = 0;
        int textInd = 0;
        for (int i = 0; i < markup.getLayers().size(); i++) {
            List<MarkupElement> layer = markup.getLayers().get(i);
            for (MarkupElement markupElement : layer) {
                if (markupElement instanceof MarkupDrawableElement) {
                    List<DynamicDrawableElement> drawableElements = layout.getDrawableElements();
                    DynamicDrawableElement dynamicDrawableElement;
                    if (drawableElements.size() <= drawableInd) {
                        dynamicDrawableElement = new DynamicDrawableElement();
                        drawableElements.add(dynamicDrawableElement);
                    } else {
                        dynamicDrawableElement = drawableElements.get(drawableInd);
                    }
                    layoutDrawable(dynamicDrawableElement, (MarkupDrawableElement) markupElement,
                            width, height);
                    drawableInd++;
                } else if (markupElement instanceof MarkupTextElement) {
                    List<DynamicTextElement> textElements = layout.getTextElements();
                    DynamicTextElement dynamicTextElement;
                    if (textElements.size() <= textInd) {
                        dynamicTextElement = new DynamicTextElement();
                        textElements.add(dynamicTextElement);
                    } else {
                        dynamicTextElement = textElements.get(textInd);
                    }
                    layoutText(dynamicTextElement, (MarkupTextElement) markupElement,
                            width, height);
                    textInd++;
                }
            }
        }
    }

    private void layoutText(DynamicTextElement dynamicTextElement,
                            MarkupTextElement markupElement, int width, int height) {
    }

    private void layoutDrawable(DynamicDrawableElement dynamicDrawableElement,
                                MarkupDrawableElement markupElement, int width, int height) {
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
