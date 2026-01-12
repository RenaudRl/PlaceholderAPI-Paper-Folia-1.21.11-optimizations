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
package me.clip.placeholderapi.expansion.internal.formatter.formatters.text;

import me.clip.placeholderapi.expansion.internal.formatter.FormatterExpansion;
import me.clip.placeholderapi.expansion.internal.formatter.formatters.IFormatter;
import me.clip.placeholderapi.expansion.internal.formatter.utils.NumberUtils;
import me.clip.placeholderapi.expansion.internal.formatter.utils.StringUtils;
import me.clip.placeholderapi.expansion.internal.formatter.utils.logging.CachedWarnHelper;

import java.util.Locale;

public class Substring implements IFormatter{
    
    private final FormatterExpansion expansion;
    
    public Substring(FormatterExpansion expansion){
        this.expansion = expansion;
    }
    
    @Override
    public String name(){
        return "substring";
    }
    
    @Override
    public String parse(String raw, String option, String... values){
        if(values.length < 2){
            CachedWarnHelper.warn(expansion, "length", raw, "Placeholder requires a [start]:[end] and <text>.");
            return null;
        }
        
        String[] ranges = StringUtils.getSplit(values[0], ":", 2);
        String text = StringUtils.merge(1, "_", values);
        
        int start = NumberUtils.parseNumber(ranges[0]);
        int end = NumberUtils.parseNumber(ranges[1]);
        
        // start isn't a number and start text isn't empty: Try getting index of provided text.
        if(start == -1 && !ranges[0].isEmpty())
            start = text.toLowerCase(Locale.ROOT).indexOf(ranges[0].toLowerCase(Locale.ROOT));
    
        // end isn't a number and end text isn't empty: Try getting index of provided text.
        if(end == -1 && !ranges[1].isEmpty())
            end = text.toLowerCase(Locale.ROOT).indexOf(ranges[1].toLowerCase(Locale.ROOT));
        
        return subString(raw, text, start, end);
    }
    
    private String subString(String raw, String text, int start, int end){
        if(start < 0)
            start = 0; // Make sure start isn't negative.
        
        if(end < 0 || end > text.length())
            end = text.length(); // Make sure end isn't negative nor larger than the string itself.
        
        if(start > (text.length() - 1) || end <= start){
            CachedWarnHelper.warn(expansion, raw, "Start index was either bigger than text length or bigger than end index.");
            return null; // Invalid range (Start is larger than text length or end is less than start)
        }
        
        return text.substring(start, end);
    }
}
