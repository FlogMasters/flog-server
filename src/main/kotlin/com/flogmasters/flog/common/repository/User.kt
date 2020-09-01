package com.flogmasters.flog.common.repository

import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
class User(
        @Id
        val id: String,
        val password: String
) {}