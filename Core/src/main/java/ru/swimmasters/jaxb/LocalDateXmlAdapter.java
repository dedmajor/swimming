package ru.swimmasters.jaxb;

import org.joda.time.LocalDate;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.Date;

/**
 * TODO: xs:date instead of xs:dateTime
 * 
 * @author dedmajor
 * @since 24.06.2010
 */
public class LocalDateXmlAdapter extends XmlAdapter<Date, LocalDate> {
    @Override
    public LocalDate unmarshal(Date date) throws Exception {
        return new LocalDate(date);
    }

    @Override
    public Date marshal(LocalDate localDate) throws Exception {
        return localDate.toDateMidnight().toDate();
    }
}
