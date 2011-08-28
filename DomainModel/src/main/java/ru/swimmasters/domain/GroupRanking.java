package ru.swimmasters.domain;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * User: dedmajor
 * Date: 8/27/11
 */
public interface GroupRanking {
    AgeRanking getAgeRanking();

    /**
     * @return positive value
     */
    @Nullable
    Integer getPlace();

    @NotNull
    Result getResult();
}
