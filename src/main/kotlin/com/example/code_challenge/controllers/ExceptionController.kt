package com.example.code_challenge.controllers

import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.sql.BatchUpdateException


@ControllerAdvice
class ExceptionController: ResponseEntityExceptionHandler() {
    @ExceptionHandler(BatchUpdateException::class)
    fun handleConflictException(
        ex: Exception?, request: WebRequest?
    ): ResponseEntity<Any> {
        return ResponseEntity<Any>(
            "This user already exists", HttpHeaders(), HttpStatus.CONFLICT
        )
    }

    @ExceptionHandler(NoSuchElementException::class)
    fun handleNotFoundException(
        ex: Exception?, request: WebRequest?
    ): ResponseEntity<Any> {
        return ResponseEntity<Any>(
            "not found user", HttpHeaders(), HttpStatus.NOT_FOUND
        )
    }
}