package ru.swimmasters.format;

import ru.swimmasters.domain.MeetRegister;

import java.io.Writer;

/**
 * @author dedmajor
 * @since 28.07.2010
 */
public interface MeetFormatter {
    void format(MeetRegister meetRegister, Writer writer);
}
