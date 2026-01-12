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

import com.google.common.hash.Hashing;
import com.google.common.io.Files;
import com.google.common.io.Resources;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.stream.Collectors;

public final class ExpansionSafetyCheck {
    private static final String MESSAGE = "\n###############################################\n" +
            "###############################################\n" +
            "PlaceholderAPI performs checks at startup and /papi reload for known malicious expansions. If you're seeing this message, there are the following malicious expansions in plugins/PlaceholderAPI/expansions.\n"
            +
            "%s" +
            "To prevent further infection PlaceholderAPI has stopped the server.\n" +
            "If you're seeing this message after updating PAPI, your server may have been infected for some time, so best practice is a complete system wipe and reinstall of your server software and plugins to be safe.\n"
            +
            "If you're seeing this after downloading an expansion however, PAPI hasn't loaded any of the malicious expansions above so you should be safe to simply delete the expansion in question.\n"
            +
            "###############################################\n" +
            "###############################################";

    private final PlaceholderAPIPlugin main;

    public ExpansionSafetyCheck(@NotNull final PlaceholderAPIPlugin main) {
        this.main = main;
    }

    public boolean runChecks() {
        if (!main.getPlaceholderAPIConfig().detectMaliciousExpansions()) {
            return false;
        }

        final File expansionsFolder = new File(main.getDataFolder(), "expansions");

        if (!expansionsFolder.exists()) {
            return false;
        }

        final Set<String> knownMaliciousExpansions;

        try {
            final String hashes = Resources.toString(new URL("https://check.placeholderapi.com"),
                    StandardCharsets.UTF_8);
            knownMaliciousExpansions = Arrays.stream(hashes.split("\n")).collect(Collectors.toSet());
        } catch (Exception e) {
            main.getLogger().log(Level.SEVERE,
                    "Failed to download anti malware hash check list from https://check.placeholderapi.com", e);
            return false;
        }

        final Set<String> maliciousPaths = new HashSet<>();

        for (File file : expansionsFolder.listFiles()) {
            try {
                final String hash = Hashing.sha256().hashBytes(Files.asByteSource(file).read()).toString();

                if (knownMaliciousExpansions.contains(hash)) {
                    maliciousPaths.add(file.getAbsolutePath());
                }
            } catch (Exception e) {
                main.getLogger().log(Level.SEVERE, "Error occurred while trying to read " + file.getAbsolutePath(), e);
            }
        }

        if (maliciousPaths.isEmpty()) {
            return false;
        }

        main.getLogger()
                .severe(String.format(MESSAGE,
                        maliciousPaths.stream()
                                .map(p -> "HASH OF " + p + " MATCHES KNOWN MALICIOUS EXPANSION DELETE IMMEDIATELY\n")
                                .collect(Collectors.joining())));

        main.getServer().shutdown();
        return true;
    }
}
