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

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class Format implements IFormatter{
    
    private final FormatterExpansion expansion;
    
    public Format(FormatterExpansion expansion){
        this.expansion = expansion;
    }
    
    @Override
    public String name(){
        return "format";
    }
    
    @Override
    public String parse(String raw, String option, String... values){
        String locale = expansion.getString("formatting.locale", "en-US");
        String pattern = expansion.getString("formatting.pattern", "#,###,###.##");
        
        // %formatter_number_format_<number>%
        if(values.length == 1 || !values[0].contains(":"))
            return formatNumber(raw, String.join("", values), locale, pattern);
        
        String[] options = StringUtils.getSplit(values[0], ":", 2);
        
        if(!StringUtils.isNullOrEmpty(options[0]))
            locale = options[0];
        
        if(!StringUtils.isNullOrEmpty(options[1]))
            pattern = options[1];
        
        return formatNumber(raw, StringUtils.merge(1, "", values), locale, pattern);
    }
    
    private String formatNumber(String raw, String number, String locale, String format){
        // Allow arbitrary numbers
        BigDecimal decimal = NumberUtils.getBigDecimal(number);
        if(decimal == null){
            CachedWarnHelper.warn(expansion, raw, "Cannot convert " + number + " into a BigDecimal.");
            return null;
        }
        
        Locale loc = getLocale(locale);
        NumberFormat numberFormat = NumberFormat.getNumberInstance(loc);
        DecimalFormat decimalFormat = (DecimalFormat)numberFormat;
        
        decimalFormat.applyPattern(format);
        
        return decimalFormat.format(decimal);
    }
    
    private Locale getLocale(String input){
        if(input.contains("-")){
            String[] args = StringUtils.getSplit(input, "-", 2);
            if(!StringUtils.isNullOrEmpty(args[0], args[1])){
                return new Locale(args[0], args[1]);
            }else
            if(!StringUtils.isNullOrEmpty(args[0])){
                return new Locale(args[0]);
            }else{
                return Locale.US;
            }
        }else{
            return new Locale(input);
        }
    }
}
