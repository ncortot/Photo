package net.rznc.photo

import akka.actor.Props
import spray.can.server.SprayCanHttpServerApp
import org.slf4j.LoggerFactory

import util.Properties


object Main extends App with SprayCanHttpServerApp {

  // Initialize SLF4J early
  LoggerFactory.getLogger(getClass)

  // Load settings from application.conf and environment
  val settings = PhotoSettings(system)
  val host = settings.interface
  val port = Option(System.getenv("PORT")) map { _.toInt } getOrElse settings.port

  // Start the service
  val service = system.actorOf(Props[PhotoServiceActor], "photo-service")
  newHttpServer(service) ! Bind(interface = host, port = port)
}
