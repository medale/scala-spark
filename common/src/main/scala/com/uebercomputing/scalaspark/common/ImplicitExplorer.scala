package com.uebercomputing.scalaspark.common

import scala.language.implicitConversions

class StringFoo(s: String) {

  def foo: String = {
    s"${s}foo"
  }

  def bar(implicit b: String): String = {
    s"${s}${b}"
  }
}

//to show error
class StringFooToo(s: String) {
  def foo: String = {
    s"${s}fooToo"
  }
}

/**
  *
  */
object ImplicitExplorer {

  //implicit value
  implicit val z = "zbar"

  //Error: two implicit values of same type
  //when compile:
  //Error:(46, 19) ambiguous implicit values:
  //both value z in object ImplicitExplorer of type => String
  //and value zz in object ImplicitExplorer of type => String
  //match expected type String
  //println(hello.bar)
  //implicit val zz = "zzbar"

  //implicit conversion
  implicit def stringToStringFoo(s: String): StringFoo = {
    new StringFoo(s)
  }

  //Error: two possible conversions to .foo
  //when compile:
  //Error:(28, 13) type mismatch;
  //found   : ImplicitExplorer.this.s.type (with underlying type String)
  //required: ?{def foo: ?}
  //Note that implicit conversions are not applicable because they are ambiguous:
  //  both method stringToStringFoo in object ImplicitExplorer of type (s: String)com.uebercomputing.scalaspark.common.StringFoo
  //and method stringToStringFooToo in object ImplicitExplorer of type (s: String)com.uebercomputing.scalaspark.common.StringFooToo
  //are possible conversion functions from ImplicitExplorer.this.s.type to ?{def foo: ?}
  //println(s.foo)
  //implicit conversion
  //implicit def stringToStringFooToo(s: String): StringFooToo = {
  //  new StringFooToo(s)
  //}

  def main(args: Array[String]): Unit = {
    val hello = "hello"
    println(hello.foo)
    println(hello.bar)
    println(hello.bar("explicitBar"))
  }
}
