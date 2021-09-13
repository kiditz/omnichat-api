package com.stafsus.api.service

import com.stafsus.api.constant.MessageKey
import com.stafsus.api.dto.WhatsAppContacts
import com.stafsus.api.repository.ChannelRepository
import com.stafsus.api.repository.ContactRepository
import org.apache.commons.lang3.StringUtils
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.xml.bind.ValidationException

@Service
class ContactServiceImpl(
	private val contactRepository: ContactRepository,
	private val channelRepository: ChannelRepository,
) : ContactService {
	private val log = LoggerFactory.getLogger(javaClass)

	@Transactional
	override fun syncFromWhatsApp(waContacts: WhatsAppContacts) {
		val channel = channelRepository.findByDeviceId(waContacts.deviceId!!)
			.orElseThrow { ValidationException(MessageKey.SYNC_CONTACT_FAILED) }
		log.info("Incoming : {}", waContacts.contacts.size)
		val numbersOnly = waContacts.contacts.map { it.number }.toList()
		val existingContacts = contactRepository.findByNumberIn(numbers = numbersOnly)
		log.info("Existing Contacts : {}", existingContacts.size)
		val newContacts = waContacts.contacts
			.filter { x -> StringUtils.isNotEmpty(x.number) }
			.filterNot { x -> existingContacts.any { m -> m.number == x.number } }
		newContacts.forEach {
			it.user = channel.user
		}
		log.info("New Contacts: {}", newContacts.size)
		contactRepository.saveAll(newContacts)

	}
}