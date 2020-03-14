package core

/**
  * Created by iodone on {19-11-14}.
  */

import com.typesafe.scalalogging.LazyLogging
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import akka.http.scaladsl.server.Route

trait Router extends FailFastCirceSupport with LazyLogging {
  def routes: Route
}
