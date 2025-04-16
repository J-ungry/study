package com.example.toygry.webfluxstudy.Board.service

import com.example.toygry.webfluxstudy.Board.controller.BoardResponse
import com.example.toygry.webfluxstudy.Board.controller.CreateBoardRequest
import com.example.toygry.webfluxstudy.Board.entity.Board
import com.example.toygry.webfluxstudy.Board.repository.BoardRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import org.slf4j.LoggerFactory

@Service
class BoardService(
    private val boardRepository: BoardRepository
) {
   private val log = LoggerFactory.getLogger(javaClass)
   fun createBoard(request: CreateBoardRequest): Mono<BoardResponse> {
       val board  = Board(title = request.title, content = request.content)

       return boardRepository.save(board)
           .map {
               saved -> BoardResponse(
                   id = saved.id!!,
                   title = saved.title,
                   content = saved.content
               )
           }
   }

    fun getAllBoards(): Flux<BoardResponse> {
        log.info("getAllBoards 호출됨, thread=${Thread.currentThread().name}")
        return boardRepository.findAll()
            .doOnNext{
                log.info("게시글 조회됨 (id=${it.id}), thread=${Thread.currentThread().name}")
            }
            .map {
                board -> BoardResponse(
                    id = board.id!!,
                    title = board.title,
                    content = board.content
                )
            }
    }
}
