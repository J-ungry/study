package com.example.toygry.webfluxstudy.Board.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("board")
data class Board(
    @Id // R2DBC 는 알아서 시퀀스 처리함 똑똑 boy
    val id: Long? = null,
    val title: String,
    val content: String,
)