package com.github.rhafaelcosta.todolist.responses;

import java.time.LocalDateTime;

/**
 * Error response class used to send error details in the response.
 */
public record ErrorResponse(LocalDateTime timestamp, String message, String details) {

}