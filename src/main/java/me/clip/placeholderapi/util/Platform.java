/*
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

package me.clip.placeholderapi.util;

/**
 * Utility class to detect the current server platform.
 */
public final class Platform {

  private static final boolean IS_FOLIA = check("io.papermc.paper.threadedregions.RegionScheduler");
  private static final boolean IS_AS_PAPER = check("com.infernalsuite.asp.api.AdvancedSlimePaperAPI");
  private static final boolean IS_BTC_CORE = check("com.infernalsuite.asp.config.BTCCoreConfig");

  private Platform() {
    throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
  }

  /**
   * Check if the current platform is Folia.
   *
   * @return true if Folia
   */
  public static boolean isFolia() {
    return IS_FOLIA;
  }

  /**
   * Check if the current platform is Advanced Slime Paper (ASPaper).
   *
   * @return true if ASPaper
   */
  public static boolean isASPaper() {
    return IS_AS_PAPER;
  }

  /**
   * Check if the current platform is BTC Core.
   *
   * @return true if BTC Core
   */
  public static boolean isBTCCore() {
    return IS_BTC_CORE;
  }

  /**
   * Get the name of the current platform for logging.
   *
   * @return platform name
   */
  public static String getPlatformName() {
    if (IS_BTC_CORE) {
      return "BTC-CORE";
    }
    if (IS_AS_PAPER) {
      return "Advanced Slime Paper";
    }
    if (IS_FOLIA) {
      return "Folia";
    }
    return "Paper/Spigot";
  }

  private static boolean check(String className) {
    try {
      Class.forName(className);
      return true;
    } catch (ClassNotFoundException e) {
      return false;
    }
  }
}
