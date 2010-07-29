package ru.swimmasters.format;

import ru.swimmasters.domain.Meet;

import java.io.Writer;

/**
 * @author dedmajor
 * @since 28.07.2010
 */
public interface MeetFormatter {
    void format(Meet meet, Writer writer);
}
