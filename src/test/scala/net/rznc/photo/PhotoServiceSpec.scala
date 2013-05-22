package net.rznc.photo

import org.specs2.mutable.Specification
import spray.testkit.Specs2RouteTest
import spray.http._
import StatusCodes._


class PhotoServiceSpec extends Specification with Specs2RouteTest with PhotoService {
  def actorRefFactory = system

  "The PhotoService" should {

    "return the app index for GET requests to the root path" in {
      Get() ~> serviceRoute ~> check { entityAs[String] must contain("Photo.RZNC") }
    }

    "return the server status for GET requests to /server-status" in {
      Get("/server-status") ~> serviceRoute ~> check {
        entityAs[String] must contain("Total requests!")
      }
    }

    "return a list of images for GET requests to /api/browse" in {
      Get("/api/browse") ~> serviceRoute ~> check {
        entityAs[String] must contain("\"id\": \"image0\"")
      }
    }

    "return a NotFound error for other paths" in {
      Get("/unknown-path") ~> serviceRoute ~> check {
        status === NotFound
        entityAs[String] === "The requested resource could not be found but may be available again in the future."
      }
    }

    "return a MethodNotAllowed error for PUT requests to the root path" in {
      Put() ~> sealRoute(serviceRoute) ~> check {
        status === MethodNotAllowed
        entityAs[String] === "HTTP method not allowed, supported methods: GET"
      }
    }
  }
}
