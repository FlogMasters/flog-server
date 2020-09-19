package com.flogmasters.flog.common.model

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*

@Entity
@Table(name = "dmghdfmdg")
class User(

        @Id
        @Column(name = "id")
        val id: String,
        @JsonIgnore
        @Column(name="password")
        val password: String,
        @Column(name="name")
        val name: String
){

}