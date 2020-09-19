package com.flogmasters.flog.common.repository

import com.flogmasters.flog.common.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository:JpaRepository<User, String>{

}