/**
 * This file is part of PlaceholderAPI
 *
 * PlaceholderAPI
 * Copyright (c) 2015 - 2024 PlaceholderAPI Team
 *
 * PlaceholderAPI free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PlaceholderAPI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.clip.placeholderapi.expansion.internal.formatter.formatters.number;

import me.clip.placeholderapi.expansion.internal.formatter.FormatterExpansion;
import me.clip.placeholderapi.expansion.internal.formatter.formatters.IFormatter;
import me.clip.placeholderapi.expansion.internal.formatter.utils.NumberUtils;
import me.clip.placeholderapi.expansion.internal.formatter.utils.StringUtils;
import me.clip.placeholderapi.expansion.internal.formatter.utils.logging.CachedWarnHelper;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class FromTo implements IFormatter {

    private final FormatterExpansion expansion;

    public FromTo(FormatterExpansion expansion) {
        this.expansion = expansion;
    }

    @Override
    public String name() {
        return null; // Never used for this special case.
    }

    @Override
    public String parse(String raw, String option, String... values) {
        if (values.length < 2) {
            CachedWarnHelper.warn(expansion, "too-short", raw,
                    "Placeholder to short. Requires from:<value>, to:<value> and <number>");
            return null;
        }

        if (StringUtils.isNullOrEmpty(values[0], values[1])) {
            CachedWarnHelper.warn(expansion, "null-values", raw, "Either to:<value> or <number> was empty/null.");
            return null;
        }

        if (!values[0].toLowerCase(Locale.ROOT).startsWith("to:")) {
            CachedWarnHelper.warn(
                    expansion,
                    "no-to",
                    raw,
                    "Placeholder does not have to:<value> set.");
            return null;
        }

        String from = StringUtils.getSplit(option, ":", 2)[1];
        String to = StringUtils.getSplit(values[0], ":", 2)[1];

        if (StringUtils.isNullOrEmpty(from, to)) {
            CachedWarnHelper.warn(
                    expansion,
                    "from-to-null",
                    raw,
                    "from:<value> and/or to:<value> had an empty/null <value>.");
            return null;
        }

        return convert(raw, StringUtils.merge(1, "", values), from, to);
    }

    private String convert(String raw, String number, String from, String to) {
        Long finalNumber = NumberUtils.optLong(number);
        if (finalNumber == null) {
            CachedWarnHelper.warn(
                    expansion,
                    "from-to-long",
                    raw,
                    "Cannot convert " + number + " into a long.");
            return null;
        }

        TimeUnit fromUnit = StringUtils.getTimeUnit(from);
        TimeUnit toUnit = StringUtils.getTimeUnit(to);

        if (fromUnit == null || toUnit == null) {
            CachedWarnHelper.warn(
                    expansion,
                    "from-to-invalid-timeunit",
                    raw,
                    "Unsupported time unit for either from:<value> or to:<value>.");
            return null;
        }

        finalNumber = toUnit.convert(finalNumber, fromUnit);

        switch (toUnit) {
            case DAYS:
                return finalNumber + expansion.getString("time.days", "d");
            case HOURS:
                return finalNumber + expansion.getString("time.hours", "h");
            case MINUTES:
                return finalNumber + expansion.getString("time.minutes", "m");
            case SECONDS:
                return finalNumber + expansion.getString("time.seconds", "s");
            case MILLISECONDS:
                return finalNumber + expansion.getString("time.milliseconds", "ms");
            default:
                CachedWarnHelper.warn(expansion, "unknown-to-time", raw, "Unknown target time unit '" + to + "'.");
                return null;
        }
    }
}
