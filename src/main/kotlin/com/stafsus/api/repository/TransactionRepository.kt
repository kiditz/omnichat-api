package com.stafsus.api.repository

import com.stafsus.api.entity.Transaction
import org.springframework.data.jpa.repository.JpaRepository

interface TransactionRepository : JpaRepository<Transaction, String> {

}