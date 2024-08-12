package com.github.rhafaelcosta.todolist.filters;

public record TaskFilter (Long userId, String title, Integer priority, Long severityTypeCode, Long taskStatusTypeCode) {

}
