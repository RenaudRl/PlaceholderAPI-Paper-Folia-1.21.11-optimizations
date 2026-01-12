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

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class NumberFormatter implements IFormatter{
    
    private final List<IFormatter> subFormatters;
    private final FromTo fromToFormatter;
    
    public NumberFormatter(FormatterExpansion expansion){
        this.subFormatters = Arrays.asList(
            new Format(expansion),
            new Round(expansion),
            new Shorten(expansion),
            new Time(expansion)
        );
        this.fromToFormatter = new FromTo(expansion);
    }
    
    @Override
    public String name(){
        return "number";
    }
    
    @Override
    public String parse(String raw, String option, String... values){
        if(option.toLowerCase(Locale.ROOT).startsWith("from:")){
            return fromToFormatter.parse(raw, option, values);
        }
            
        for(IFormatter subFormatter : subFormatters){
            if(subFormatter.name().equalsIgnoreCase(option))
                return subFormatter.parse(raw, option, values);
        }
        
        return null;
    }
}
