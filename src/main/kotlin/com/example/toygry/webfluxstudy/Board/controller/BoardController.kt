package com.example.toygry.webfluxstudy.Board.controller

import com.example.toygry.webfluxstudy.Board.service.BoardService
import jakarta.validation.constraints.NotBlank
import org.jetbrains.annotations.NotNull
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/board")
class BoardController(
    private val boardService: BoardService
) {

    @PostMapping
    fun createBoard(@RequestBody request: CreateBoardRequest): Mono<BoardResponse> {
        return boardService.createBoard(request)
    }

    @GetMapping
    fun getAllBoards(): Flux<BoardResponse> {
        return boardService.getAllBoards()
    }
}

data class CreateBoardRequest(
    @field:NotBlank(message = "title required")
    val title: String,

    @field:NotBlank(message = "content required")
    val content: String
)

data class BoardResponse(
    val id: Long,
    val title: String,
    val content: String
)
