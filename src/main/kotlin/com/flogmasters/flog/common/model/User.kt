package com.flogmasters.flog.common.model

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*

@Entity
class User(
        @Id
        val id: String,
        @JsonIgnore
        val password: String,
        val name: String
){

}