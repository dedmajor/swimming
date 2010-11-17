package ru.swimmasters.format;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.context.Context;
import ru.swimmasters.domain.MeetRegister;

import java.io.IOException;
import java.io.Writer;

/**
 * @author dedmajor
 * @since 28.07.2010
 */
public class VelocityMeetFormatter implements MeetFormatter {
    private final Template template;

    public VelocityMeetFormatter(Template template) {
        this.template = template;
    }

    @Override
    public void format(MeetRegister meetRegister, Writer writer) {
        Context context = new VelocityContext();
        context.put("meetRegister", meetRegister);
        try {
            template.merge(context, writer);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
