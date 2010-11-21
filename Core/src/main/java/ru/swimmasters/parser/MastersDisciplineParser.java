package ru.swimmasters.parser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.swimmasters.domain.Discipline;
import ru.swimmasters.domain.Gender;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: dedmajor
 * Date: Nov 21, 2010
 */
public class MastersDisciplineParser implements DisciplineParser {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final Pattern distancePattern = Pattern.compile("(([0-9]+) м).*");
    private final Pattern strokePattern = Pattern.compile("([a-zA-Zа-яА-Я ]+).*");

    @Override
    public Discipline parseDiscipline(String textContent) {
        logger.debug(String.format("parsing discipline from string [%s]", textContent));
        Gender gender = getDisciplineGender(textContent);
        logger.debug("found gender: " + gender);
        int distance = getDisciplineDistance(textContent);
        logger.debug("found distance: " + distance);
        String stroke = getDisciplineStroke(textContent);
        logger.debug("found stroke: " + stroke);
        return new Discipline(gender, distance, stroke);
    }

    private String getDisciplineStroke(String textContent) {
        String textWithoutDistance = cutDistanceFromText(textContent);
        Matcher strokeMatcher = strokePattern.matcher(textWithoutDistance);
        if (!strokeMatcher.matches()) {
            throw new IllegalArgumentException("text " + textWithoutDistance
                    + " doesn't match pattern " + strokePattern);
        }
        return strokeMatcher.group(1);
    }

    private String cutDistanceFromText(String textContent) {
        Matcher matcher = distancePattern.matcher(textContent);
        matcher.matches();
        String distanceMatch = matcher.group(1);
        return textContent.replace(distanceMatch, "").trim();
    }

    private int getDisciplineDistance(String textContent) {
        Matcher matcher = distancePattern.matcher(textContent);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("text " + textContent
                    + " doesn't matches pattern " + distancePattern);
        }
        String stringDistance = matcher.group(2);
        return Integer.valueOf(stringDistance);
    }

    private static Gender getDisciplineGender(String textContent) {
        Gender gender;
        if (textContent.contains("мужчины")) {
            gender = Gender.MEN;
        } else if (textContent.contains("женщины")) {
            gender = Gender.WOMEN;
        } else {
            throw new IllegalArgumentException("content " + textContent + " is not a discipline");
        }
        return gender;
    }

    @Override
    public boolean isDiscipline(String textContent) {
        // TODO: FIXME: relays, too
        if (textContent.contains("Эстафета")) {
            return false;
        }
        return textContent.contains("мужчины") || textContent.contains("женщины");
    }
}
