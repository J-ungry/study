package com.example.toygry.webfluxstudy.Board.repository

import com.example.toygry.webfluxstudy.Board.entity.Board
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface BoardRepository: ReactiveCrudRepository<Board, Long>