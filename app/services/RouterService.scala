package services

import java.nio.ByteBuffer
import java.util.UUID

import javax.inject.{Inject, Singleton}

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class RouterService @Inject()(kinesisService: KinesisService)(implicit ec: ExecutionContext) {

  def route(seed: Long): Future[String] = {
    val randomUuid = UUID.randomUUID().toString
    kinesisService
      .put(streamName(seed), ByteBuffer.wrap(payload(randomUuid, seed).getBytes()), randomUuid)
      .map(_ => randomUuid)
  }

  private def streamName(seed: Long): String =
    if (seed % 2 == 0) RouterService.StreamEvenName else RouterService.StreamOddName

  private def payload(uuid: String, seed: Long): String =
    s"""{"uuid": "$uuid", "seed": $seed}"""
}

object RouterService {
  val StreamEvenName = "li-stream-even"
  val StreamOddName = "li-stream-odd"
}
