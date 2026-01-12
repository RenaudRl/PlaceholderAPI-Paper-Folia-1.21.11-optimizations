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
import me.clip.placeholderapi.expansion.internal.formatter.utils.StringUtils;
import me.clip.placeholderapi.expansion.internal.formatter.utils.logging.CachedWarnHelper;

public class Replace implements IFormatter{
    
    private final FormatterExpansion expansion;
    
    public Replace(FormatterExpansion expansion){
        this.expansion = expansion;
    }
    
    @Override
    public String name(){
        return "replace";
    }
    
    @Override
    public String parse(String raw, String option, String... values){
        if(values.length < 3){
            CachedWarnHelper.warn(expansion, raw, "Placeholder does not have a [target], [replacement] and text value.");
            return null;
        }
        
        String target = values[0].replace("{{u}}", "_");
        String replacement = values[1].replace("{{u}}", "_");
        
        return StringUtils.merge(2, "_", values).replace(target, replacement);
    }
}
