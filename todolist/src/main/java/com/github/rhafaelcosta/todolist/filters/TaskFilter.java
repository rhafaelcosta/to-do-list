package com.github.rhafaelcosta.todolist.filters;

public record TaskFilter (Long userId, Long severityTypeCode, Long taskStatusTypeCode) {

}
