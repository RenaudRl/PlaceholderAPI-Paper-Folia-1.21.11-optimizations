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

package me.clip.placeholderapi.scheduler.folia;

import me.clip.placeholderapi.scheduler.scheduling.schedulers.TaskScheduler;
import me.clip.placeholderapi.scheduler.scheduling.tasks.MyScheduledTask;
import io.papermc.paper.threadedregions.scheduler.AsyncScheduler;
import io.papermc.paper.threadedregions.scheduler.GlobalRegionScheduler;
import io.papermc.paper.threadedregions.scheduler.RegionScheduler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

import java.util.concurrent.TimeUnit;

public class FoliaScheduler implements TaskScheduler {

    final Plugin plugin;

    public FoliaScheduler(Plugin plugin) {
        this.plugin = plugin;
    }

    private final RegionScheduler regionScheduler = Bukkit.getServer().getRegionScheduler();
    private final GlobalRegionScheduler globalRegionScheduler = Bukkit.getServer().getGlobalRegionScheduler();
    private final AsyncScheduler asyncScheduler = Bukkit.getServer().getAsyncScheduler();

    @Override
    public boolean isGlobalThread() {
        return Bukkit.getServer().isGlobalTickThread();
    }

    @Override
    public boolean isTickThread() {
        return Bukkit.getServer().isPrimaryThread(); // The Paper implementation checks whether this is a tick thread,
                                                     // this method exists to avoid confusion.
    }

    @Override
    public boolean isEntityThread(Entity entity) {
        return Bukkit.getServer().isOwnedByCurrentRegion(entity);
    }

    @Override
    public boolean isRegionThread(Location location) {
        return Bukkit.getServer().isOwnedByCurrentRegion(location);
    }

    @Override
    public MyScheduledTask runTask(Runnable runnable) {
        return new FoliaScheduledTask(globalRegionScheduler.run(plugin, task -> runnable.run()));
    }

    @Override
    public MyScheduledTask runTaskLater(Runnable runnable, long delay) {
        // Folia exception: Delay ticks may not be <= 0
        if (delay <= 0) {
            return runTask(runnable);
        }
        return new FoliaScheduledTask(globalRegionScheduler.runDelayed(plugin, task -> runnable.run(), delay));
    }

    @Override
    public MyScheduledTask runTaskTimer(Runnable runnable, long delay, long period) {
        // Folia exception: Delay ticks may not be <= 0
        delay = getOneIfNotPositive(delay);
        return new FoliaScheduledTask(
                globalRegionScheduler.runAtFixedRate(plugin, task -> runnable.run(), delay, period));
    }

    @Override
    public MyScheduledTask runTask(Plugin plugin, Runnable runnable) {
        return new FoliaScheduledTask(globalRegionScheduler.run(plugin, task -> runnable.run()));
    }

    @Override
    public MyScheduledTask runTaskLater(Plugin plugin, Runnable runnable, long delay) {
        // Folia exception: Delay ticks may not be <= 0
        if (delay <= 0) {
            return runTask(plugin, runnable);
        }
        return new FoliaScheduledTask(globalRegionScheduler.runDelayed(plugin, task -> runnable.run(), delay));
    }

    @Override
    public MyScheduledTask runTaskTimer(Plugin plugin, Runnable runnable, long delay, long period) {
        // Folia exception: Delay ticks may not be <= 0
        delay = getOneIfNotPositive(delay);
        return new FoliaScheduledTask(
                globalRegionScheduler.runAtFixedRate(plugin, task -> runnable.run(), delay, period));
    }

    @Override
    public MyScheduledTask runTask(Location location, Runnable runnable) {
        return new FoliaScheduledTask(regionScheduler.run(plugin, location, task -> runnable.run()));
    }

    @Override
    public MyScheduledTask runTaskLater(Location location, Runnable runnable, long delay) {
        // Folia exception: Delay ticks may not be <= 0
        if (delay <= 0) {
            return runTask(runnable);
        }
        return new FoliaScheduledTask(regionScheduler.runDelayed(plugin, location, task -> runnable.run(), delay));
    }

    @Override
    public MyScheduledTask runTaskTimer(Location location, Runnable runnable, long delay, long period) {
        // Folia exception: Delay ticks may not be <= 0
        delay = getOneIfNotPositive(delay);
        return new FoliaScheduledTask(
                regionScheduler.runAtFixedRate(plugin, location, task -> runnable.run(), delay, period));
    }

    @Override
    public MyScheduledTask runTask(Entity entity, Runnable runnable) {
        return new FoliaScheduledTask(entity.getScheduler().run(plugin, task -> runnable.run(), null));
    }

    @Override
    public MyScheduledTask runTaskLater(Entity entity, Runnable runnable, long delay) {
        // Folia exception: Delay ticks may not be <= 0
        if (delay <= 0) {
            return runTask(entity, runnable);
        }
        return new FoliaScheduledTask(entity.getScheduler().runDelayed(plugin, task -> runnable.run(), null, delay));
    }

    @Override
    public MyScheduledTask runTaskTimer(Entity entity, Runnable runnable, long delay, long period) {
        // Folia exception: Delay ticks may not be <= 0
        delay = getOneIfNotPositive(delay);
        return new FoliaScheduledTask(
                entity.getScheduler().runAtFixedRate(plugin, task -> runnable.run(), null, delay, period));
    }

    @Override
    public MyScheduledTask runTaskAsynchronously(Runnable runnable) {
        return new FoliaScheduledTask(asyncScheduler.runNow(plugin, task -> runnable.run()));
    }

    @Override
    public MyScheduledTask runTaskLaterAsynchronously(Runnable runnable, long delay) {
        // Folia exception: Delay ticks may not be <= 0
        delay = getOneIfNotPositive(delay);
        return new FoliaScheduledTask(
                asyncScheduler.runDelayed(plugin, task -> runnable.run(), delay * 50L, TimeUnit.MILLISECONDS));
    }

    @Override
    public MyScheduledTask runTaskTimerAsynchronously(Runnable runnable, long delay, long period) {
        return new FoliaScheduledTask(asyncScheduler.runAtFixedRate(plugin, task -> runnable.run(), delay * 50,
                period * 50, TimeUnit.MILLISECONDS));
    }

    @Override
    public MyScheduledTask runTaskAsynchronously(Plugin plugin, Runnable runnable) {
        return new FoliaScheduledTask(asyncScheduler.runNow(plugin, task -> runnable.run()));
    }

    @Override
    public MyScheduledTask runTaskLaterAsynchronously(Plugin plugin, Runnable runnable, long delay) {
        // Folia exception: Delay ticks may not be <= 0
        delay = getOneIfNotPositive(delay);
        return new FoliaScheduledTask(
                asyncScheduler.runDelayed(plugin, task -> runnable.run(), delay * 50L, TimeUnit.MILLISECONDS));
    }

    @Override
    public MyScheduledTask runTaskTimerAsynchronously(Plugin plugin, Runnable runnable, long delay, long period) {
        // Folia exception: Delay ticks may not be <= 0
        delay = getOneIfNotPositive(delay);
        return new FoliaScheduledTask(asyncScheduler.runAtFixedRate(plugin, task -> runnable.run(), delay * 50,
                period * 50, TimeUnit.MILLISECONDS));
    }

    @Override
    public void execute(Runnable runnable) {
        globalRegionScheduler.execute(plugin, runnable);
    }

    @Override
    public void execute(Location location, Runnable runnable) {
        regionScheduler.execute(plugin, location, runnable);
    }

    @Override
    public void execute(Entity entity, Runnable runnable) {
        entity.getScheduler().execute(plugin, runnable, null, 1L);
    }

    @Override
    public void cancelTasks() {
        globalRegionScheduler.cancelTasks(plugin);
        asyncScheduler.cancelTasks(plugin);
    }

    @Override
    public void cancelTasks(Plugin plugin) {
        globalRegionScheduler.cancelTasks(plugin);
        asyncScheduler.cancelTasks(plugin);
    }

    private long getOneIfNotPositive(long x) {
        return x <= 0 ? 1L : x;
    }
}
