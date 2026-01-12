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
import me.clip.placeholderapi.expansion.internal.formatter.utils.logging.CachedWarnHelper;

import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public class Shorten implements IFormatter{
    
    private final FormatterExpansion expansion;
    private final NavigableMap<Long, String> suffixes = new TreeMap<>();
    
    public Shorten(FormatterExpansion expansion){
        this.expansion = expansion;
        suffixes.put(1_000L, expansion.getString("shorten.thousands", "K"));
        suffixes.put(1_000_000L, expansion.getString("shorten.millions", "M"));
        suffixes.put(1_000_000_000L, expansion.getString("shorten.billions", "B"));
        suffixes.put(1_000_000_000_000L, expansion.getString("shorten.trillions", "T"));
        suffixes.put(1_000_000_000_000_000L, expansion.getString("shorten.quadrillions", "Q"));
    }
    
    @Override
    public String name(){
        return "shorten";
    }
    
    @Override
    public String parse(String raw, String option, String... values){
        Long value = NumberUtils.optLong(String.join("", values));
        if(value == null){
            CachedWarnHelper.warn(expansion, raw, "Cannot convert " + String.join("", values) + " to a long.");
            return null;
        }
        
        return truncateNumber(value);
    }
    
    /*
     * Source from StackOverflow: https://stackoverflow.com/a/30661479/11496439
     */
    private String truncateNumber(long value){
        if(value == Long.MIN_VALUE)
            return truncateNumber(Long.MIN_VALUE + 1);
        
        if(value < 0)
            return "-" + truncateNumber(-value);
        
        if(value < 1000)
            return Long.toString(value);
        
        Map.Entry<Long, String> entry = suffixes.floorEntry(value);
        long divideBy = entry.getKey();
        String suffix = entry.getValue();
        
        long truncated = value / (divideBy / 10);
        boolean hasDecimal = truncated < 100 && (truncated / 10d) != (truncated / 10);
        
        return hasDecimal ? (truncated / 10d) + suffix : (truncated / 10) + suffix;
    }
}
