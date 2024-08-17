package currying

object Currying {

  def myFunction(arg1: Int)(arg2: String): String = { arg1.toString + arg2 }

  val strToStrF = myFunction(1)(_)
  val intToStringF = myFunction(_)("1")

  def myFunction2(arg1: Int, arg2: String): String = { arg1.toString + arg2 }
  val strToStrF2 = myFunction2(1, _)
  val intToStringF2 = myFunction2(_, "1")

}
