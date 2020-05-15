package controllers

import javax.inject._
import play.api.mvc.{Action, AnyContent, InjectedController}
import services.RouterService

import scala.concurrent.ExecutionContext

@Singleton
class RouterController @Inject()(routerService: RouterService)
                                (implicit ec: ExecutionContext) extends InjectedController {

  def route(seed: Long): Action[AnyContent] = Action.async {
    routerService.route(seed).map(uuid =>
      Ok("").withHeaders("X-Transaction-Id" -> uuid)
    )
  }
}
