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
package me.clip.placeholderapi.expansion.internal.formatter.utils;

import java.util.Arrays;
import java.util.Locale;
import java.util.StringJoiner;
import java.util.concurrent.TimeUnit;

public class StringUtils{
    
    public static String[] getSplit(String text, String split, int length){
        return Arrays.copyOf(text.split(split,length), length);
    }
    
    public static String merge(int startIndex, String delimiter, String... inputs){
        StringJoiner joiner = new StringJoiner(delimiter);
        for(int i = startIndex; i < inputs.length; i++)
            joiner.add(inputs[i]);
        
        return joiner.toString();
    }
    
    public static boolean isNullOrEmpty(String... strings){
        for(String s : strings){
            if(s == null || s.isEmpty())
                return true;
        }
        
        return false;
    }
    
    public static TimeUnit getTimeUnit(String value){
        return switch(value.toLowerCase(Locale.ROOT)){
            case "days", "day" -> TimeUnit.DAYS;
            case "fromhours", "fromhrs", "hours", "hour", "hrs" -> TimeUnit.HOURS;
            case "fromminutes", "frommins", "minutes", "minute", "mins", "min" -> TimeUnit.MINUTES;
            case "fromseconds", "fromsecs", "seconds", "second", "secs", "sec" -> TimeUnit.SECONDS;
            case "frommilliseconds", "fromms", "milliseconds", "millisecond", "millis", "ms" -> TimeUnit.MILLISECONDS;
            default -> null;
        };
    }
}
