package com.stafsus.api.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.stafsus.api.client.MidtransClient
import com.stafsus.api.constant.MessageKey
import com.stafsus.api.dto.midtrans.*
import com.stafsus.api.dto.midtrans.MidtransFraudStatus.Companion.ACCEPT
import com.stafsus.api.dto.midtrans.MidtransFraudStatus.Companion.CHALLENGE
import com.stafsus.api.dto.midtrans.MidtransFraudStatus.Companion.DENY
import com.stafsus.api.dto.midtrans.MidtransTransactionStatus.Companion.CANCEL
import com.stafsus.api.dto.midtrans.MidtransTransactionStatus.Companion.CAPTURE
import com.stafsus.api.dto.midtrans.MidtransTransactionStatus.Companion.EXPIRE
import com.stafsus.api.dto.midtrans.MidtransTransactionStatus.Companion.SETTLEMENT
import com.stafsus.api.entity.ItemDetails
import com.stafsus.api.entity.Transaction
import com.stafsus.api.entity.TransactionStatus
import com.stafsus.api.entity.UserPrincipal
import com.stafsus.api.execption.MidtransException
import com.stafsus.api.execption.ValidationException
import com.stafsus.api.repository.PriceRepository
import com.stafsus.api.repository.TransactionRepository
import feign.FeignException
import org.apache.commons.codec.digest.DigestUtils
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*


@Service
class TransactionServiceImpl(
	@Value("\${midtrans.merchantName}") val merchantName: String,
	@Value("\${midtrans.serverKey}") val serverKey: String,
	private val priceRepository: PriceRepository,
	private val transactionRepository: TransactionRepository,
	private val midtransClient: MidtransClient,
	private val mapper: ObjectMapper
) : TransactionService {
	private val log = LoggerFactory.getLogger(javaClass)

	@Transactional
	override fun buyPackage(userPrincipal: UserPrincipal, priceId: Long): MidtransDto {
		val transaction = buildTransactionData(priceId)
		val midtrans = buildMidtransRequest(transaction)
		return try {
			val result = midtransClient.buyPackage(midtrans).toDto()
			transactionRepository.save(transaction)
			result
		} catch (ex: FeignException) {
			val responseBody = ex.contentUTF8()
			val result = mapper.readValue(responseBody, MidtransResponse::class.java).toDto()
			throw MidtransException(result.errorMessages, "")
		}
	}

	@Transactional
	override fun paymentNotification(notification: MidtransNotification): Transaction {
		val currentSignature =
			DigestUtils.sha512Hex("${notification.orderId}${notification.statusCode}${notification.grossAmount}${serverKey}")
		log.info("Current : {}", currentSignature)
		log.info("Caller : {}", notification.signatureKey)
		if (!currentSignature.equals(notification.signatureKey)) {
			throw ValidationException(MessageKey.INVALID_SIGNATURE_KEY)
		}
		val transaction = transactionRepository.findById(notification.orderId!!)
			.orElseThrow { ValidationException(MessageKey.TRANSACTION_NOT_FOUND) }
		updateStatus(notification, transaction)
		transactionRepository.save(transaction)
		return transaction
	}

	private fun updateStatus(
		notification: MidtransNotification,
		transaction: Transaction
	) {
		val transactionStatus = notification.transactionStatus
		val fraudStatus = notification.fraudStatus
		if (transactionStatus == CAPTURE) {
			if (fraudStatus == CHALLENGE) {
				transaction.status = TransactionStatus.CHALLENGE
			} else if (fraudStatus == ACCEPT) {
				transaction.status = TransactionStatus.COMPLETED
			}
		} else if (transactionStatus == SETTLEMENT) {
			transaction.status = TransactionStatus.COMPLETED
		} else if (transactionStatus == CANCEL || transactionStatus == DENY || transactionStatus == EXPIRE) {
			transaction.status = TransactionStatus.REJECTED
		} else {
			transaction.status = TransactionStatus.PENDING
		}
	}

	private fun buildMidtransRequest(transaction: Transaction): MidtransRequest {
		val transactionDetails = MidtransTransactionDetails(
			grossAmount = transaction.grossAmount.toInt(),
			orderId = transaction.orderId,
		)
		val items = transaction.items!![0]
		val itemDetails = mutableListOf(
			MidtransItems(
				quantity = items.quantity.toInt(),
				price = items.price!!.price.toLong(),
				name = items.name,
				merchantName = items.merchantName,
			)
		)
		return MidtransRequest(transactionDetails = transactionDetails, itemDetails = itemDetails)
	}

	private fun buildTransactionData(priceId: Long): Transaction {
		val price = priceRepository.findById(priceId).orElseThrow { ValidationException(MessageKey.PRICE_NOT_FOUND) }
		if (price.price.toDouble() <= 0) {
			throw ValidationException(MessageKey.PRICE_MUST_GREATER_THAN_ZERO)
		}
		val uuid = UUID.randomUUID().toString()
		val transaction = Transaction(uuid, price.price)
		val details = ItemDetails(
			name = "${price.product!!.name} - ${price.name}",
			price = price,
			quantity = 1L,
			transaction = transaction,
			merchantName = merchantName
		)
		transaction.items = listOf(details)
		return transaction
	}

}