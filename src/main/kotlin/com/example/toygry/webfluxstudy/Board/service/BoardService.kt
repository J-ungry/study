package com.example.toygry.webfluxstudy.Board.service

import com.example.toygry.webfluxstudy.Board.controller.BoardResponse
import com.example.toygry.webfluxstudy.Board.controller.CreateBoardRequest
import com.example.toygry.webfluxstudy.Board.entity.Board
import com.example.toygry.webfluxstudy.Board.repository.BoardRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import org.slf4j.LoggerFactory
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.transaction.reactive.TransactionalOperator
import java.time.Duration

@Service
class BoardService(
    private val boardRepository: BoardRepository,
    private val txOperator: TransactionalOperator,
    private val redisTemplate: ReactiveRedisTemplate<String, BoardResponse>
) {
   private val log = LoggerFactory.getLogger(javaClass)
   fun createBoard(request: CreateBoardRequest): Mono<BoardResponse> {
       val board  = Board(title = request.title, content = request.content)

//       return boardRepository.save(board)
//           .flatMap { saved ->
//               if (saved.title == "ROLLBACK") {
//                   return@flatMap Mono.error(RuntimeException("강제 롤백"))
//               }
//
//               Mono.just(BoardResponse(
//                   id = saved.id!!,
//                   title = saved.title,
//                   content = saved.content
//               ))
//           }
       return boardRepository.save(board)
           .map { saved ->
               BoardResponse(
                   id = saved.id!!,
                   title = saved.title,
                   content = saved.content
               )
           }
           .`as`(txOperator::transactional)
   }

//    fun getAllBoards(): Flux<BoardResponse> {
//        log.info("getAllBoards 호출됨, thread=${Thread.currentThread().name}")
//        return boardRepository.findAll()
//            .doOnNext{
//                log.info("게시글 조회됨 (id=${it.id}), thread=${Thread.currentThread().name}")
//            }
//            .map {
//                board -> BoardResponse(
//                    id = board.id!!,
//                    title = board.title,
//                    content = board.content
//                )
//            }
//    }

    fun getAllBoards(): Flux<BoardResponse> {
        log.info("getAllBoards 호출됨, thread=${Thread.currentThread().name}")

        return boardRepository.findAll()
            .map { board ->
                BoardResponse(
                    id = board.id!!,
                    title = board.title,
                    content = board.content
                )
            }
            .flatMap { response ->
                // Redis 캐싱 (key = board:{id})
                redisTemplate.opsForValue()
                    .set("board:${response.id}", response, Duration.ofMinutes(10))
                    .thenReturn(response) // 캐시 저장 후 원래 데이터 반환
            }
    }
}
