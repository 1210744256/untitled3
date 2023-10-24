package com.example.a23;

import lombok.extern.slf4j.Slf4j;
import org.springframework.format.Formatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
@Slf4j
public class MyDataFormatter implements Formatter<Date> {
    private String desc;

    public MyDataFormatter(String desc) {
        this.desc = desc;
        log.info(desc);
    }

    @Override
    public Date parse(String text, Locale locale) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy|MM|dd");
        return simpleDateFormat.parse(text);
    }

    @Override
    public String print(Date object, Locale locale) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy|MM|dd");
        return simpleDateFormat.format(object);
    }
}
