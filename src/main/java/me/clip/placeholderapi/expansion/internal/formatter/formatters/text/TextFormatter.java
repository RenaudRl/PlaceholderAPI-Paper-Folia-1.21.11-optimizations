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

import java.util.Arrays;
import java.util.List;

public class TextFormatter implements IFormatter{
    
    private final List<IFormatter> subFormatters;
    
    public TextFormatter(FormatterExpansion expansion){
        this.subFormatters = Arrays.asList(
            new Length(),
            new Lowercase(),
            new Replace(expansion),
            new Substring(expansion),
            new Uppercase()
        );
    }
    
    @Override
    public String name(){
        return "text";
    }
    
    @Override
    public String parse(String raw, String option, String... values){
        for(IFormatter subFormatter : subFormatters){
            if(subFormatter.name().equalsIgnoreCase(option))
                return subFormatter.parse(raw, option, values);
        }
        
        return null;
    }
}
