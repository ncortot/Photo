package net.rznc.photo

import scala.concurrent.duration._
import scala.util.{Success, Failure}
import akka.actor.Actor
import akka.event.Logging._
import akka.pattern.ask
import spray.can.server.HttpServer
import spray.http._
import spray.routing.{HttpService, RequestContext}
import spray.routing.directives.LogEntry
import spray.util._
import MediaTypes._
import StatusCodes._
import spray.routing.directives.MiscDirectives._
import spray.http.HttpResponse
import scala.util.Success
import spray.routing.RequestContext
import scala.util.Failure
import scala.Some
import spray.routing.directives.FileAndResourceDirectives._
import spray.http.HttpResponse
import scala.util.Success
import spray.routing.RequestContext
import scala.util.Failure
import scala.Some


/**
 * The HTTP service endpoint.
 */
class PhotoServiceActor extends Actor with PhotoService {

  def actorRefFactory = context
  def receive = runRoute(serviceRoute)
}

/**
 * This trait defines the HTTP service behaviour.
 *
 * The behaviour definition is kept independent of the actual Actor for easier testing.
 */
trait PhotoService extends HttpService {

  val serviceRoute = {
    get {
      logRequestResponse(showResponses _) {
        path("") {
          getFromFile("static/index.html")
        } ~
        path("favicon.ico") {
          getFromResource("favicon.ico")
        } ~
        path("server-status") {
          showServerStats
        } ~
        path("api" / "browse" / Rest) {
          path => getFromResource("example.json")
        } ~
        path("static" / Rest) {
          path => getFromDirectory("static/" + path)
        } ~
        complete(NotFound)
      }
    }
  }

  def showResponses(request: HttpRequest): Any => Option[LogEntry] = {
    case HttpResponse(status, _, _, _) => {
      val message = "%s %s %d" format (request.method, request.uri, status.value)
      status match {
        case OK => Some(LogEntry(message, InfoLevel))
        case _ => Some(LogEntry(message, WarningLevel))
      }
    }
  }

  def showServerStats(ctx: RequestContext) {
    actorRefFactory.actorFor("../http-server")
      .ask(HttpServer.GetStats)(1 second span)
      .mapTo[HttpServer.Stats]
      .onComplete {
      case Success(stats) => ctx.complete {
        "Uptime                : " + stats.uptime.formatHMS + '\n' +
        "Total requests        : " + stats.totalRequests + '\n' +
        "Open requests         : " + stats.openRequests + '\n' +
        "Max open requests     : " + stats.maxOpenRequests + '\n' +
        "Total connections     : " + stats.totalConnections + '\n' +
        "Open connections      : " + stats.openConnections + '\n' +
        "Max open connections  : " + stats.maxOpenConnections + '\n' +
        "Requests timed out    : " + stats.requestTimeouts + '\n' +
        "Connections timed out : " + stats.idleTimeouts + '\n'
      }
      case Failure(ex) => ctx.complete(500, "Couldn't get server stats due to " + ex.getMessage)
    }
  }
}
