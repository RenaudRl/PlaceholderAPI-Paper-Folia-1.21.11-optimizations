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
package me.clip.placeholderapi.expansion.internal.formatter;

import me.clip.placeholderapi.expansion.internal.formatter.formatters.IFormatter;
import me.clip.placeholderapi.expansion.internal.formatter.formatters.number.NumberFormatter;
import me.clip.placeholderapi.expansion.internal.formatter.formatters.text.TextFormatter;
import me.clip.placeholderapi.expansion.internal.formatter.utils.StringUtils;
import me.clip.placeholderapi.expansion.internal.formatter.utils.logging.CachedWarnHelper;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.Configurable;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FormatterExpansion extends PlaceholderExpansion implements Configurable {

    private final Map<String, Object> defaults = new HashMap<>();
    private final List<IFormatter> formatters;

    public FormatterExpansion() {
        loadDefaults();
        this.formatters = Arrays.asList(
                new NumberFormatter(this),
                new TextFormatter(this));
    }

    @Override
    @Nonnull
    public String getIdentifier() {
        return "formatter";
    }

    @Override
    @Nonnull
    public String getAuthor() {
        return "BTCSTUDIO";
    }

    @Override
    @Nonnull
    public String getVersion() {
        return "1.0.0-INTERNAL";
    }

    @Override
    public Map<String, Object> getDefaults() {
        return defaults;
    }

    @Override
    public String onRequest(OfflinePlayer player, @Nonnull String identifier) {
        String raw = "%formatter_" + identifier + "%";
        identifier = PlaceholderAPI.setBracketPlaceholders(player, identifier);
        String[] temp = StringUtils.getSplit(identifier, "_", 3);

        if (StringUtils.isNullOrEmpty(temp[0], temp[1], temp[2])) {
            CachedWarnHelper.warn(this, raw,
                    "Placeholder needs to be of format '%formatter_<type>_<option>_<values>%'");
            return null;
        }

        for (IFormatter formatter : formatters) {
            if (formatter.name().equalsIgnoreCase(temp[0]))
                return formatter.parse(raw, temp[1], temp[2].split("_"));
        }

        CachedWarnHelper.warn(this, raw, "Unknown placeholder type '" + temp[0] + "'.");
        return null;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isCondensed() {
        Object condensed = this.get("time.condensed", null);

        if (condensed == null)
            return false;

        if (condensed instanceof String condensedString)
            return condensedString.equalsIgnoreCase("yes");

        if (condensed instanceof Boolean condensedBool)
            return condensedBool;

        return false;
    }

    private void loadDefaults() {
        defaults.put("formatting.pattern", "#,###,###.##");
        defaults.put("formatting.locale", "en-US");

        defaults.put("shorten.thousands", "K");
        defaults.put("shorten.millions", "M");
        defaults.put("shorten.billions", "B");
        defaults.put("shorten.trillions", "T");
        defaults.put("shorten.quadrillions", "Q");

        defaults.put("time.milliseconds", "ms");
        defaults.put("time.seconds", "s");
        defaults.put("time.minutes", "m");
        defaults.put("time.hours", "h");
        defaults.put("time.days", "d");
        defaults.put("time.condensed", false);

        defaults.put("rounding.precision", 0);
        defaults.put("rounding.mode", "half-up");
    }
}
