package com.stafsus.api.service

import com.stafsus.api.dto.DepartmentFilterDto
import com.stafsus.api.entity.Department
import org.springframework.data.domain.Page

interface DepartmentService {
	fun addDepartment(department: Department): Department
	fun delete(id: Long)
	fun findByName(departmentFilterDto: DepartmentFilterDto): Page<Department>
}