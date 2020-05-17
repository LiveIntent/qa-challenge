package utils

import scala.concurrent.duration._
import scala.util.control.NonFatal

object Retries {

  @annotation.tailrec
  def retry[T](n: Int)(fn: => T): T = {
    try {
      return fn
    } catch {
      case NonFatal(_) if n > 1 => Thread.sleep(1.second.toMillis)
    }
    retry(n - 1)(fn)
  }
}
