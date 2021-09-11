package com.stafsus.api.service

import com.stafsus.api.constant.MessageKey
import com.stafsus.api.dto.DepartmentFilterDto
import com.stafsus.api.entity.Department
import com.stafsus.api.entity.UserPrincipal
import com.stafsus.api.execption.ValidationException
import com.stafsus.api.repository.DepartmentRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class DepartmentServiceImpl(
	private val departmentRepository: DepartmentRepository
) : DepartmentService {

	@Transactional
	override fun save(department: Department, userPrincipal: UserPrincipal): Department {
		department.user = userPrincipal
		return departmentRepository.save(department)
	}

	@Transactional
	override fun delete(id: Long) {
		if (!departmentRepository.existsById(id)) {
			throw ValidationException(MessageKey.DEPARTMENT_NOT_FOUND)
		}
		departmentRepository.deleteById(id)
	}

	override fun findByName(departmentFilterDto: DepartmentFilterDto): Page<Department> {
		val userId =
			if (departmentFilterDto.userPrincipal.parentId != null) departmentFilterDto.userPrincipal.parentId else departmentFilterDto.userPrincipal.id
		val newName = departmentFilterDto.name ?: ""
		return departmentRepository.findByNameContainingAndUserId(
			newName, userId!!, PageRequest.of(departmentFilterDto.page, departmentFilterDto.size).withSort(
				Sort.by("name").ascending()
			)
		)
	}
}