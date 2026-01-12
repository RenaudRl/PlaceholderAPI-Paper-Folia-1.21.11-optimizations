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

import me.clip.placeholderapi.scheduler.scheduling.tasks.MyScheduledTask;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.plugin.Plugin;

public class FoliaScheduledTask implements MyScheduledTask {
    private final ScheduledTask task;

    public FoliaScheduledTask(final ScheduledTask task) {
        this.task = task;
    }

    public void cancel() {
        this.task.cancel();
    }

    public boolean isCancelled() {
        return this.task.isCancelled();
    }

    public Plugin getOwningPlugin() {
        return this.task.getOwningPlugin();
    }

    public boolean isCurrentlyRunning() {
        final ScheduledTask.ExecutionState state = this.task.getExecutionState();
        return state == ScheduledTask.ExecutionState.RUNNING || state == ScheduledTask.ExecutionState.CANCELLED_RUNNING;
    }

    public boolean isRepeatingTask() {
        return this.task.isRepeatingTask();
    }
}