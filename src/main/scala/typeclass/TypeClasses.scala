package typeclass

object TypeClasses extends App {

  implicit class StringTypeClass(string: String) {
    def ===(otherString: String): Boolean = string == otherString
    def splitOnComma = string.split(',')
    def concat(other: String) = string + "!@#"
  }

  val eval = false
  def lazyVal(x: () => Int) =
    if (eval) println(x)


  lazyVal(() => {1 + 2})



  println("20".concat("other String"))
//  println(StringTypeClass("20").toString)


}
