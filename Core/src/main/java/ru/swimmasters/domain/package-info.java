@XmlJavaTypeAdapters
({
    @XmlJavaTypeAdapter(value = LocalDateXmlAdapter.class, type = LocalDate.class)
})
package ru.swimmasters.domain;

import org.joda.time.LocalDate;
import ru.swimmasters.jaxb.LocalDateXmlAdapter;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapters;


