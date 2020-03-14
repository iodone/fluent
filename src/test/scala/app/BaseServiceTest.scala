package app

/**
  * Created by iodone on {18-11-5}.
  */
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest._
import org.scalatest.mockito.MockitoSugar

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

// TODO 增加注解处理mock对象,以及mock对象注入
// TODO 增加选项,测试完毕后回滚数据库至测试前
trait BaseServiceTest extends WordSpec with Matchers with ScalatestRouteTest with MockitoSugar {

  def awaitForResult[T](futureResult: Future[T]): T = {
    Await.result(futureResult, 5.second)
  }

}
