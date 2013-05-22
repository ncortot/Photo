package net.rznc.photo

import com.typesafe.config.{ Config, ConfigFactory }
import akka.actor.ActorSystem

case class PhotoSettings (
    interface: String,
    port: Int) {

  require(interface.nonEmpty, "interface must be non-empty")
  require(0 < port && port < 65536, "illegal port")
}

object PhotoSettings {
  def apply(system: ActorSystem): PhotoSettings =
    apply(system.settings.config getConfig "net.rznc.photo")

  def apply(config: Config): PhotoSettings = {
    val c = config withFallback ConfigFactory.defaultReference(getClass.getClassLoader)
    PhotoSettings(
      c getString "interface",
      c getInt "port")
  }
}
