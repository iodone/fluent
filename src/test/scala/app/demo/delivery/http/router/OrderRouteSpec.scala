package app.demo.delivery.http.router

/**
  * Created by iodone on {18-11-5}.
  */

import org.mockito.Mockito._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.model.{HttpEntity, MediaTypes}
import io.circe.syntax._
import io.circe.generic.auto._
import io.circe.parser._

import scala.concurrent.{Future, Promise}
import app.BaseServiceTest
import app.demo.application.OrderAppService
import app.demo.domain.Entity._
import core.Entity._
import core.ResponseEntity._

class OrderRouteSpec extends BaseServiceTest {

  "OrderRoute" when {
    "POST /order/save" should {
      "return 200 if save successful" in new Context {
        val order0 = Order(1L, "orderName", List(Item(1, "shoe"), Item(2, "wear")))
        when(orderService.saveOrder(order0.id, order0.items)).thenReturn(Promise.successful(1L).future)

        val reqEntity = HttpEntity(MediaTypes.`application/json`, order0.asJson.noSpaces)
        Post("/order/save", reqEntity) ~> orderRoute ~> check {
          val res = decode[Response[Id]](responseAs[String]) match {
            case Right(res) => res
            case Left(error) => throw new RuntimeException("get response error")
          }
          res.data shouldBe (1L)
          status.intValue() shouldBe (200)
        }
      }

      "return 500 if sava faild" in new Context {

        val order0 = Order(1L, "orderName", List(Item(1, "shoe"), Item(2, "wear")))
        when(orderService.saveOrder(order0.id, order0.items)).thenReturn(Future.failed(new Exception("Save Failed")))

        val reqEntity = HttpEntity(MediaTypes.`application/json`, order0.asJson.toString)
        Post("/order/save", reqEntity) ~> orderRoute ~> check {
          status.intValue() shouldBe (500)
        }
      }
    }

    "POST /order/get-item" should {
      "return 200 if id exist" in new Context {

        val item = Item(1, "shoe")
        when(orderService.fetchItem(1L)).thenReturn(Future.successful(Some(item)))

        val reqEntity = HttpEntity(MediaTypes.`application/json`, item.asJson.noSpaces)
        Post("/order/get-item", reqEntity) ~> orderRoute ~> check {
          val res = decode[Response[Item]](responseAs[String]) match {
            case Right(res) => res
            case Left(error) => throw new RuntimeException("get response error")
          }
          res.data.id shouldBe (1L)
          res.data.name shouldBe ("shoe")
          status.intValue() shouldBe (200)
        }
      }
      "return 404 if id exist" in new Context {
        val item = Item(1, "shoe")
        when(orderService.fetchItem(1L)).thenReturn(Future.successful(None))

        val reqEntity = HttpEntity(MediaTypes.`application/json`, item.asJson.noSpaces)
        Post("/order/get-item", reqEntity) ~> orderRoute ~> check {
          status.intValue() shouldBe (404)
        }
      }
    }
  }

  trait Context {
    val orderService = mock[OrderAppService]
    val orderRoute = new OrderRouter(orderService).routes
  }
}


