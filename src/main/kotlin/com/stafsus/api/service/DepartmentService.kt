package com.stafsus.api.service

import com.stafsus.api.dto.DepartmentFilterDto
import com.stafsus.api.entity.Department
import com.stafsus.api.entity.UserPrincipal
import org.springframework.data.domain.Page

interface DepartmentService {
	fun save(department: Department, userPrincipal: UserPrincipal): Department
	fun delete(id: Long)
	fun findByName(departmentFilterDto: DepartmentFilterDto): Page<Department>
}