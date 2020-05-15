package services

import java.nio.ByteBuffer

import akka.actor.ActorSystem
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration
import com.amazonaws.regions.Regions
import com.amazonaws.services.kinesis.{AmazonKinesis, AmazonKinesisClientBuilder}
import javax.inject.{Inject, Singleton}

import scala.concurrent.{ExecutionContext, Future, blocking}
import scala.util.control.NonFatal

@Singleton
class KinesisService @Inject()(implicit as: ActorSystem, ec: ExecutionContext) {

  System.setProperty("com.amazonaws.sdk.disableCbor", "true")

  private val localEndpointConfig: EndpointConfiguration =
    new EndpointConfiguration("http://localhost:4568", Regions.US_EAST_1.getName)

  private val client: AmazonKinesis =
    AmazonKinesisClientBuilder.standard().withEndpointConfiguration(localEndpointConfig).build()

  KinesisService.createStreamsUsing(client)

  def put(streamName: String, data: ByteBuffer, partitionKey: String): Future[Unit] = Future {
    blocking {
      client.putRecord(streamName, data, partitionKey)
      ()
    }
  }
}

object KinesisService {

  val shardCount = 1

  def createStreamsUsing(client: AmazonKinesis): Unit =
    try {
      client.createStream(RouterService.StreamEvenName, shardCount)
      client.createStream(RouterService.StreamOddName, shardCount)
    } catch { case NonFatal(_) => }
}
