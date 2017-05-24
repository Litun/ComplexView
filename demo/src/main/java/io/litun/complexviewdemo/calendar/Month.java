package io.litun.complexviewdemo.calendar;

import io.litun.complexview.ComplexViewModel;

/**
 * Created by Litun on 24.05.2017.
 */
public class Month {
    private final String name;
    private final ComplexViewModel complexViewModel;

    public Month(String name, ComplexViewModel complexViewModel) {
        this.name = name;
        this.complexViewModel = complexViewModel;
    }

    public String getName() {
        return name;
    }

    public ComplexViewModel getComplexViewModel() {
        return complexViewModel;
    }
}
