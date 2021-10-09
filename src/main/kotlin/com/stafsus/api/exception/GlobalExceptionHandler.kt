package com.stafsus.api.exception

import com.stafsus.api.constant.MessageKey
import com.stafsus.api.dto.ResponseDto
import com.stafsus.api.service.TranslateService
import org.hibernate.StaleObjectStateException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.validation.FieldError
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.lang.Exception
import javax.validation.ConstraintViolationException

@ControllerAdvice
class GlobalExceptionHandler(
	val translateService: TranslateService
) : ResponseEntityExceptionHandler() {
	private val log = LoggerFactory.getLogger(javaClass)

	@ExceptionHandler(value = [ValidationException::class])
	fun handleValidationException(ex: ValidationException): ResponseEntity<ResponseDto> {
		log.info("handleValidationException: {}", ex.message)
		return ResponseEntity(
			ResponseDto(false, translateService.toLocale(ex.message!!), ex.message),
			HttpStatus.INTERNAL_SERVER_ERROR
		)
	}

	@ExceptionHandler(value = [AccessDeniedException::class])
	fun handleAccessDeniedException(ex: AccessDeniedException): ResponseEntity<ResponseDto> {
		log.info("handleAccessDeniedException: {}", ex.message)
		return ResponseEntity(
			ResponseDto(false, translateService.toLocale(String.format(ex.message!!, ex.args)), ex.message),
			HttpStatus.INTERNAL_SERVER_ERROR
		)
	}

	@ExceptionHandler(value = [MidtransException::class])
	fun handleMidtransException(ex: MidtransException): ResponseEntity<ResponseDto> {
		log.info("handleMidtransException: {}", ex.message)
		return ResponseEntity(
			ResponseDto(false, errors = ex.messages),
			HttpStatus.BAD_REQUEST
		)
	}

	@ExceptionHandler(value = [QuotaLimitException::class])
	fun handleQuotaLimitException(ex: QuotaLimitException): ResponseEntity<ResponseDto> {
		log.info("handleQuotaLimitException: {}", ex.message)
		return ResponseEntity(
			ResponseDto(false, translateService.toLocale(ex.message!!), ex.message),
			HttpStatus.NOT_ACCEPTABLE,
		)
	}


	@ExceptionHandler(value = [UsernameNotFoundException::class])
	fun handleUsernameNotFoundException(ex: UsernameNotFoundException): ResponseEntity<ResponseDto> {
		log.info("handleUsernameNotFoundException: {}", ex)
		return ResponseEntity(
			ResponseDto(false, translateService.toLocale(ex.message!!), ex.message),
			HttpStatus.INTERNAL_SERVER_ERROR
		)
	}

	@ExceptionHandler(value = [StaleObjectStateException::class])
	fun handleStaleObjectStateException(ex: StaleObjectStateException): ResponseEntity<ResponseDto> {
		log.info("handleStaleObjectStateException: {}", ex)
		return ResponseEntity(
			ResponseDto(false, translateService.toLocale(MessageKey.INVALID_VERSION), MessageKey.INVALID_VERSION),
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
		log.info("handleMethodArgumentNotValid: {}", ex)
		val errors: MutableMap<String, Map<String, String>> = mutableMapOf()
		ex.bindingResult.allErrors.forEach { error ->
			val field = (error as FieldError)
			errors[field.field] = mapOf(
				"code" to error.code!!,
				"message" to error.defaultMessage!!,
			)
		}
		return ResponseEntity(ResponseDto(false, errors = errors), HttpStatus.BAD_REQUEST)
	}

	//	@ExceptionHandler(HttpMessageNotReadableException::class)
	override fun handleHttpMessageNotReadable(
		ex: HttpMessageNotReadableException,
		headers: HttpHeaders,
		status: HttpStatus,
		request: WebRequest
	): ResponseEntity<Any> {
		log.info("handleHttpMessageNotReadable: {}", ex)
		return ResponseEntity(ResponseDto(false, message = ex.message), HttpStatus.BAD_REQUEST)
	}

	@ExceptionHandler(ConstraintViolationException::class)
	fun handleConstraintViolationException(ex: ConstraintViolationException): ResponseEntity<ResponseDto> {
		val errors: MutableMap<String, String> = mutableMapOf()
		log.info("handleConstraintViolationException: {}", ex)
		ex.constraintViolations.forEach { err ->
			val errorMessage: String = err.message!!
			val args =
				err.constraintDescriptor.attributes.entries.filter { it.key == "value" || it.key == "min" || it.key == "max" }
					.map { it.value }.toTypedArray()
			errors[err.propertyPath.last().name] = translateService.toLocale(errorMessage.trim(), args)
		}
		return ResponseEntity(ResponseDto(false, errors = errors), HttpStatus.BAD_REQUEST)
	}

	@ExceptionHandler(Exception::class)
	override fun handleExceptionInternal(
		ex: Exception,
		body: Any?,
		headers: HttpHeaders,
		status: HttpStatus,
		request: WebRequest
	): ResponseEntity<Any> {
		return ResponseEntity(
			ResponseDto(false, message = ex.message, key = MessageKey.UNEXPECTED_EXCEPTION),
			HttpStatus.INTERNAL_SERVER_ERROR
		)
	}
}