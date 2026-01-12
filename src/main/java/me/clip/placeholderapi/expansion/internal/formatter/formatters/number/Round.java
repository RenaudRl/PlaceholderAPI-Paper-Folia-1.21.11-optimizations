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
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Locale;

public class Round implements IFormatter{
    
    private final FormatterExpansion expansion;
    
    public Round(FormatterExpansion expansion){
        this.expansion = expansion;
    }
    
    @Override
    public String name(){
        return "round";
    }
    
    @Override
    public String parse(String raw, String option, String... values){
        int precision = expansion.getInt("rounding.precision", 0);
        String rounding = expansion.getString("rounding.mode", "half-up");
        
        if(values.length == 1)
            return roundNumber(raw, values[0], precision, rounding);
        
        String[] roundingOptions = StringUtils.getSplit(values[0], ":", 2);
        
        if(!StringUtils.isNullOrEmpty(roundingOptions[0]))
            precision = NumberUtils.parseNumber(roundingOptions[0]);
        
        if(!StringUtils.isNullOrEmpty(roundingOptions[1]))
            rounding = roundingOptions[1];
        
        return roundNumber(raw, StringUtils.merge(1, "", values), precision, rounding);
    }
    
    private String roundNumber(String raw, String number, int precision, String roundingMode){
        // Allow arbitrary numbers
        BigDecimal decimal = NumberUtils.getBigDecimal(number);
        if(decimal == null){
            CachedWarnHelper.warn(expansion, raw, "Cannot convert " + number + " to a BigDecimal.");
            return null;
        }
        
        if(precision < 0)
            precision = 0; // Making sure precision isn't negative
        
        RoundingMode mode = getRoundingMode(roundingMode);
        MathContext context = new MathContext(precision, mode);
        
        return decimal.round(context).toPlainString();
    }
    
    private RoundingMode getRoundingMode(String roundingMode){
        return switch(roundingMode.toLowerCase(Locale.ROOT)){
            case "up" -> RoundingMode.UP;
            case "down" -> RoundingMode.DOWN;
            case "ceiling" -> RoundingMode.CEILING;
            case "floor" -> RoundingMode.FLOOR;
            case "half-down" -> RoundingMode.HALF_DOWN;
            case "half-even" -> RoundingMode.HALF_EVEN;
            default -> RoundingMode.HALF_UP;
        };
    }
}
