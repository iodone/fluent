package core

/**
  * Created by iodone on {19-11-13}.
  */

import akka.http.scaladsl.model.StatusCode

abstract class BaseException(val errorCode: Int, val message: String, val statusCode: StatusCode) extends Exception(message)
class ExitTrappedException(message: String = "System.exit call caught") extends Exception(message)



