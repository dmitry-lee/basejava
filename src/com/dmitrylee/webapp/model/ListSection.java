package com.dmitrylee.webapp.model;

import java.util.ArrayList;
import java.util.List;

public class ListSection extends AbstractSection{
    private final List<String> list;

    public ListSection() {
        list = new ArrayList<>();
    }

    public ListSection(List<String> list) {
        this.list = list;
    }

    public List<String> getList() {
        return list;
    }

    @Override
    public void printSection() {
        for (String s: list) {
            System.out.println("• " + s);
        }
    }
}
