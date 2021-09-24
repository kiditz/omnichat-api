package com.stafsus.api.service

import com.stafsus.api.constant.MessageKey
import com.stafsus.api.dto.WaSyncContactDto
import com.stafsus.api.exception.ValidationException
import com.stafsus.api.repository.ChannelRepository
import com.stafsus.api.repository.ContactRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ContactServiceImpl(
	private val contactRepository: ContactRepository,
	private val channelRepository: ChannelRepository,
) : ContactService {
	private val log = LoggerFactory.getLogger(javaClass)

	@Transactional
	override fun syncFromWhatsApp(waContactDto: WaSyncContactDto) {
		try {
			val channel = channelRepository.findByDeviceId(waContactDto.deviceId!!)
				.orElseThrow { ValidationException(MessageKey.SYNC_CONTACT_FAILED) }
			log.info("Incoming : {}", waContactDto.contacts.size)
			val idsOnly = waContactDto.contacts.map { it.id }.toList()
			val existingContactsIds = contactRepository.findByIdIn(idsOnly)
			log.info("Existing Contacts : {}", existingContactsIds.size)
			val newContacts = waContactDto.contacts
				.filterNot { x -> existingContactsIds.any { m -> m.id == x.id } }
			newContacts.forEach {
				it.company = channel.company
//				it.createdBy = channel.company?.email
//				it.updatedBy = channel.company?.user?.email
			}
			log.info("New Contacts: {}", newContacts.size)
			contactRepository.saveAll(newContacts)
		} catch (e: ValidationException) {
			log.error("Exception: {}", e.message)
		}
	}
}