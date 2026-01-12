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

import java.util.StringJoiner;
import java.util.concurrent.TimeUnit;

public class Time implements IFormatter{
    
    private final FormatterExpansion expansion;
    
    public Time(FormatterExpansion expansion){
        this.expansion = expansion;
    }
    
    @Override
    public String name(){
        return "time";
    }
    
    @Override
    public String parse(String raw, String option, String... values){
        if(values.length == 1 || !StringUtils.isNullOrEmpty(values[0]))
            return formatTime(raw, values[0], "fromSeconds");
        
        return formatTime(raw, StringUtils.merge(1, "", values), values[0]);
    }
    
    private String formatTime(String raw, String number, String unit){
        Long finalNumber = NumberUtils.optLong(number);
        if(finalNumber == null || finalNumber < 0L){
            CachedWarnHelper.warn(expansion, "long", raw, "Cannot convert " + number + " to a long.");
            return null;
        }
        
        TimeUnit timeUnit = StringUtils.getTimeUnit(unit);
        if(timeUnit == null){
            CachedWarnHelper.warn(expansion, raw, "Unsupported time unit '" + unit + "'.");
            return null;
        }
        
        long days = 0, hours = 0, minutes = 0, seconds = 0, milliseconds = 0;
        
        final StringJoiner joiner = new StringJoiner(expansion.isCondensed() ? "" : " ");
    
        switch(timeUnit){
            case HOURS -> {
                days = timeUnit.toDays(finalNumber);
                hours = timeUnit.toHours(finalNumber) - (days * 24);
            }
            case MINUTES -> {
                days = timeUnit.toDays(finalNumber);
                hours = timeUnit.toHours(finalNumber) - (days * 24);
                minutes = timeUnit.toMinutes(finalNumber) - (hours * 60) - (days * 1440);
            }
            case SECONDS -> {
                days = timeUnit.toDays(finalNumber);
                hours = timeUnit.toHours(finalNumber) - (days * 24);
                minutes = timeUnit.toMinutes(finalNumber) - (hours * 60) - (days * 1440);
                seconds = timeUnit.toSeconds(finalNumber) - (minutes * 60) - (hours * 3600) - (days * 86400);
            }
            case MILLISECONDS -> {
                days = timeUnit.toDays(finalNumber);
                hours = timeUnit.toHours(finalNumber) - (days * 24);
                minutes = timeUnit.toMinutes(finalNumber) - (hours * 60) - (days * 1440);
                seconds = timeUnit.toSeconds(finalNumber) - (minutes * 60) - (hours * 3600) - (days * 86400);
                milliseconds = finalNumber - (seconds * 1000) - (minutes * 60000) - (hours * 3600000) - (days * 86400000);
            }
        }
        
        if(days > 0)
            joiner.add(days + expansion.getString("time.days", "d"));
        
        if(hours > 0)
            joiner.add(hours + expansion.getString("time.hours", "h"));
        
        if(minutes > 0)
            joiner.add(minutes + expansion.getString("time.minutes", "m"));
        
        if(seconds > 0)
            joiner.add(seconds + expansion.getString("time.seconds", "s"));
        
        if(milliseconds > 0)
            joiner.add(milliseconds + expansion.getString("time.milliseconds", "ms"));
        
        return joiner.toString();
    }
}
