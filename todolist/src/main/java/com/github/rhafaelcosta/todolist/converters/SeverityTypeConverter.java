package com.github.rhafaelcosta.todolist.converters;

import com.github.rhafaelcosta.todolist.enums.SeverityType;
import com.github.rhafaelcosta.todolist.exceptions.EnumNotFoundException;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class SeverityTypeConverter implements AttributeConverter<SeverityType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(SeverityType attribute) {
        if (attribute == null) {
            return null;
        }

        return attribute.getCode();
    }

    @Override
    public SeverityType convertToEntityAttribute(Integer value) {
        try {
            if (value == null) {
                return null;
            }

            return SeverityType.getSeverityTypeByCode(value);
        } catch (EnumNotFoundException e) {
            return null;
        }
    }

}
