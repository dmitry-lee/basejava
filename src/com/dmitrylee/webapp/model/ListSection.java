package com.dmitrylee.webapp.model;

import java.util.ArrayList;
import java.util.List;

public class ListSection extends AbstractSection {
    private static final long serialVersionUID = 1L;

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
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String s : list) {
            sb.append(s).append("\n");
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ListSection that = (ListSection) o;

        return list.equals(that.list);
    }

    @Override
    public int hashCode() {
        return list.hashCode();
    }
}
