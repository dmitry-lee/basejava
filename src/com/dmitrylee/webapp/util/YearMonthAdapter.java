package com.dmitrylee.webapp.util;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.YearMonth;

public class YearMonthAdapter extends XmlAdapter<String, YearMonth> {

    @Override
    public YearMonth unmarshal(String v) {
        return YearMonth.parse(v);
    }

    @Override
    public String marshal(YearMonth v) {
        return v.toString();
    }
}
