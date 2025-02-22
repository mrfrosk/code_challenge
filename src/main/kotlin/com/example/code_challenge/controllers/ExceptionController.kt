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
import java.util.NoSuchElementException


@ControllerAdvice
class ExceptionController: ResponseEntityExceptionHandler() {
    @ExceptionHandler(NoSuchElementException::class)
    fun handleNotFoundException() = ResponseEntity.status(HttpStatus.NOT_FOUND).build<Nothing>()

    @ExceptionHandler(BatchUpdateException::class)
    fun handleConflictException() = ResponseEntity.status(HttpStatus.CONFLICT).build<Nothing>()
}