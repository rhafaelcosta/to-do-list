package com.github.rhafaelcosta.todolist.converters;

import com.github.rhafaelcosta.todolist.enums.TaskStatusType;
import com.github.rhafaelcosta.todolist.exceptions.EnumNotFoundException;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class TaskStatusTypeConverter implements AttributeConverter<TaskStatusType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(TaskStatusType attribute) {
        if (attribute == null) {
            return null;
        }

        return attribute.getCode();
    }

    @Override
    public TaskStatusType convertToEntityAttribute(Integer value) {
        try {
            if (value == null) {
                return null;
            }

            return TaskStatusType.getTaskStatusTypeByCode(value);
        } catch (EnumNotFoundException e) {
            return null;
        }
    }

}
