package com.stafsus.waapi.api.exception

import com.stafsus.waapi.constant.MessageKey
import com.stafsus.waapi.exception.ValidationException
import com.stafsus.waapi.service.TranslateService
import com.stafsus.waapi.service.dto.ApiResponse
import org.hibernate.StaleObjectStateException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.validation.FieldError
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import javax.validation.ConstraintViolationException

@ControllerAdvice
class GlobalExceptionHandler(
    val translateService: TranslateService
) : ResponseEntityExceptionHandler() {
    @ExceptionHandler(value = [ValidationException::class])
    fun handleValidationException(ex: ValidationException): ResponseEntity<ApiResponse> {
        return ResponseEntity(
            ApiResponse(false, translateService.toLocale(ex.message!!), null),
            HttpStatus.INTERNAL_SERVER_ERROR
        )
    }

    @ExceptionHandler(value = [UsernameNotFoundException::class])
    fun handleUsernameNotFoundException(ex: UsernameNotFoundException): ResponseEntity<ApiResponse> {
        return ResponseEntity(
            ApiResponse(false, translateService.toLocale(ex.message!!), null),
            HttpStatus.INTERNAL_SERVER_ERROR
        )
    }

    @ExceptionHandler(value = [StaleObjectStateException::class])
    fun handleStaleObjectStateException(ex: StaleObjectStateException): ResponseEntity<ApiResponse> {
        return ResponseEntity(
            ApiResponse(false, translateService.toLocale(MessageKey.INVALID_VERSION), null),
            HttpStatus.BAD_REQUEST
        )
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    override fun handleMethodArgumentNotValid(
        ex: org.springframework.web.bind.MethodArgumentNotValidException,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> {
        val errors: MutableMap<String, String> = HashMap()
        ex.bindingResult.allErrors.forEach { error ->
            val field = (error as FieldError)
//            val errorMessage: String = error.defaultMessage!!
//            val args = error.arguments!!.drop(1).toTypedArray()
            errors[field.field] = translateService.getMessage(field)
        }
        return ResponseEntity(ApiResponse(false, payload = errors), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolationException(ex: ConstraintViolationException): ResponseEntity<ApiResponse> {
        val errors: MutableMap<String, String> = mutableMapOf()

        ex.constraintViolations.forEach { err ->
            val errorMessage: String = err.message!!
            val args =
                err.constraintDescriptor.attributes.entries.filter { it.key == "value" || it.key == "min" || it.key == "max" }
                    .map { it.value }.toTypedArray()
            errors[err.propertyPath.last().name] = translateService.toLocale(errorMessage.trim(), args)
        }
        return ResponseEntity(ApiResponse(false, payload = errors), HttpStatus.BAD_REQUEST)
    }
}