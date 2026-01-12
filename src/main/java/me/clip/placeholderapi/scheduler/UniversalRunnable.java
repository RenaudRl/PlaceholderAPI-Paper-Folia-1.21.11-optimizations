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

package me.clip.placeholderapi.scheduler;

import me.clip.placeholderapi.scheduler.scheduling.schedulers.TaskScheduler;
import me.clip.placeholderapi.scheduler.scheduling.tasks.MyScheduledTask;
import org.bukkit.plugin.Plugin;

/** Just modified BukkitRunnable */
public abstract class UniversalRunnable implements Runnable {
    MyScheduledTask task;

    public synchronized void cancel() throws IllegalStateException {
        checkScheduled();
        task.cancel();
    }

    /**
     * Returns true if this task has been cancelled.
     *
     * @return true if the task has been cancelled
     * @throws IllegalStateException if task was not scheduled yet
     */
    public synchronized boolean isCancelled() throws IllegalStateException {
        checkScheduled();
        return task.isCancelled();
    }

    /**
     * Schedules this in the Bukkit scheduler to run on next tick.
     *
     * @param plugin the reference to the plugin scheduling task
     * @return {@link MyScheduledTask}
     * @throws IllegalArgumentException if plugin is null
     * @throws IllegalStateException    if this was already scheduled
     * @see TaskScheduler#runTask(Runnable)
     */

    public synchronized MyScheduledTask runTask(Plugin plugin) throws IllegalArgumentException, IllegalStateException {
        checkNotYetScheduled();
        return setupTask(UniversalScheduler.getScheduler(plugin).runTask(this));
    }

    /**
     * <b>Asynchronous tasks should never access any API in Bukkit. Great care
     * should be taken to assure the thread-safety of asynchronous tasks.</b>
     * <p>
     * Schedules this in the Bukkit scheduler to run asynchronously.
     *
     * @param plugin the reference to the plugin scheduling task
     * @return {@link MyScheduledTask}
     * @throws IllegalArgumentException if plugin is null
     * @throws IllegalStateException    if this was already scheduled
     * @see TaskScheduler#runTaskAsynchronously(Runnable)
     */

    public synchronized MyScheduledTask runTaskAsynchronously(Plugin plugin)
            throws IllegalArgumentException, IllegalStateException {
        checkNotYetScheduled();
        return setupTask(UniversalScheduler.getScheduler(plugin).runTaskAsynchronously(this));
    }

    /**
     * Schedules this to run after the specified number of server ticks.
     *
     * @param plugin the reference to the plugin scheduling task
     * @param delay  the ticks to wait before running the task
     * @return {@link MyScheduledTask}
     * @throws IllegalArgumentException if plugin is null
     * @throws IllegalStateException    if this was already scheduled
     * @see TaskScheduler#runTaskLater(Runnable, long)
     */

    public synchronized MyScheduledTask runTaskLater(Plugin plugin, long delay)
            throws IllegalArgumentException, IllegalStateException {
        checkNotYetScheduled();
        return setupTask(UniversalScheduler.getScheduler(plugin).runTaskLater(this, delay));
    }

    /**
     * <b>Asynchronous tasks should never access any API in Bukkit. Great care
     * should be taken to assure the thread-safety of asynchronous tasks.</b>
     * <p>
     * Schedules this to run asynchronously after the specified number of
     * server ticks.
     *
     * @param plugin the reference to the plugin scheduling task
     * @param delay  the ticks to wait before running the task
     * @return {@link MyScheduledTask}
     * @throws IllegalArgumentException if plugin is null
     * @throws IllegalStateException    if this was already scheduled
     * @see TaskScheduler#runTaskLaterAsynchronously(Runnable, long)
     */

    public synchronized MyScheduledTask runTaskLaterAsynchronously(Plugin plugin, long delay)
            throws IllegalArgumentException, IllegalStateException {
        checkNotYetScheduled();
        return setupTask(UniversalScheduler.getScheduler(plugin).runTaskLaterAsynchronously(this, delay));
    }

    /**
     * Schedules this to repeatedly run until cancelled, starting after the
     * specified number of server ticks.
     *
     * @param plugin the reference to the plugin scheduling task
     * @param delay  the ticks to wait before running the task
     * @param period the ticks to wait between runs
     * @return {@link MyScheduledTask}
     * @throws IllegalArgumentException if plugin is null
     * @throws IllegalStateException    if this was already scheduled
     * @see TaskScheduler#runTaskTimer(Runnable, long, long)
     */

    public synchronized MyScheduledTask runTaskTimer(Plugin plugin, long delay, long period)
            throws IllegalArgumentException, IllegalStateException {
        checkNotYetScheduled();
        return setupTask(UniversalScheduler.getScheduler(plugin).runTaskTimer(this, delay, period));
    }

    /**
     * <b>Asynchronous tasks should never access any API in Bukkit. Great care
     * should be taken to assure the thread-safety of asynchronous tasks.</b>
     * <p>
     * Schedules this to repeatedly run asynchronously until cancelled,
     * starting after the specified number of server ticks.
     *
     * @param plugin the reference to the plugin scheduling task
     * @param delay  the ticks to wait before running the task for the first
     *               time
     * @param period the ticks to wait between runs
     * @return {@link MyScheduledTask}
     * @throws IllegalArgumentException if plugin is null
     * @throws IllegalStateException    if this was already scheduled
     * @see TaskScheduler#runTaskTimerAsynchronously(Runnable, long, long)
     */

    public synchronized MyScheduledTask runTaskTimerAsynchronously(Plugin plugin, long delay, long period)
            throws IllegalArgumentException, IllegalStateException {
        checkNotYetScheduled();
        return setupTask(UniversalScheduler.getScheduler(plugin).runTaskTimerAsynchronously(this, delay, period));
    }

    private void checkScheduled() {
        if (task == null) {
            throw new IllegalStateException("Not scheduled yet");
        }
    }

    private void checkNotYetScheduled() {
        if (task != null) {
            throw new IllegalStateException("Already scheduled");
        }
    }

    private MyScheduledTask setupTask(final MyScheduledTask task) {
        this.task = task;
        return task;
    }

}
