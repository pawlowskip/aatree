package com.pawlowskip.collection.immutable.utils

import com.pawlowskip.collection.immutable.aatree.AATree
import org.scalameter.api._
import org.scalameter.picklers.Implicits._

import scala.annotation.tailrec
import scala.collection.immutable.TreeSet
import scala.util.Random

/**
  * Created by przemek on 29/10/16.
  */
object PerformanceUtils {

  def runTimes(n: Int): Context = Context.apply(
    exec.benchRuns -> n,
    exec.independentSamples -> 1
  )

  def randomDouble(from: Double, to: Double): Double = from + Random.nextDouble() * (to - from)
  def randomInt(from: Int, to: Int): Int = from + Random.nextInt() * (to - from)
  def randomInt(): Int = randomInt(Integer.MIN_VALUE, Integer.MAX_VALUE)

  val smallSizes = Gen.range("smallSize")(10, 500, 10)
  val mediumSizes = Gen.range("mediumSize")(1000, 5000, 1000)
  val bigSizes = Gen.range("bigSize")(10000, 50000, 10000)
  val hugeSizes = Gen.range("hugeSize")(100000, 500000, 100000)
  val expSizes = Gen.exponential("exponential")(4, Math.pow(2, 13).toInt, 2)
  val expSizesBig = Gen.exponential("exponential")(4, Math.pow(2, 19).toInt, 2)

  def aaTreeGenOfSizes[T](sizes: Gen[Int], f: => T)(implicit ordering: Ordering[T]): Gen[AATree[T]] = for {
    size <- sizes
    tree <- singleAATreeGen(size, f)
  } yield tree

  def treeSetGenOfSizes[T](sizes: Gen[Int], f: => T)(implicit ordering: Ordering[T]): Gen[TreeSet[T]] = for {
    size <- sizes
    tree <- singleTreeSet(size, f)
  } yield tree

  def listGenOfSizes[T](sizes: Gen[Int], f: => T): Gen[List[T]] = for {
    size <- sizes
    list <- singleListGen(size, f)
  } yield list

  def singleListGen[T](size: Int, f: => T): Gen[List[T]] = for {
    size <- Gen.single("singleSize")(size)
  } yield List.fill(size)(f)

  def aaTreeWithElemsGenSizes[T](sizes: Gen[Int], f: => T)(implicit ordering: Ordering[T]): Gen[(AATree[T], List[T])] =
    for {
      size <- sizes
      tree <- singleAATreeWithElems(size, f)
    } yield tree

  def treeSetWithElemsGenSizes[T](sizes: Gen[Int], f: => T)(implicit ordering: Ordering[T]): Gen[(TreeSet[T], List[T])] =
    for {
      size <- sizes
      tree <- singleTreeSetWithElems(size, f)
    } yield tree

  def single[T, A](size: Int, f: => T, builder: List[T] => A)(implicit ordering: Ordering[T]): Gen[A] = for {
    size <- Gen.single("singleSize")(size)
    elems <- singleListGen(size, f)
  } yield builder(elems)

  def singleAATreeWithElems[T](size: Int, f: => T)(implicit ordering: Ordering[T]): Gen[(AATree[T], List[T])] =
    single(size, f, (elems: List[T]) => (AATree(elems), elems))

  def singleTreeSetWithElems[T](size: Int, f: => T)(implicit ordering: Ordering[T]): Gen[(TreeSet[T], List[T])] =
    single(size, f, (elems: List[T]) => (TreeSet(elems: _*), elems))

  def singleAATreeGen[T](size: Int, f: => T)(implicit ordering: Ordering[T]): Gen[AATree[T]] =
    single(size, f, (elems: List[T]) => AATree(elems))

  def singleTreeSet[T](size: Int, f: => T)(implicit ordering: Ordering[T]): Gen[TreeSet[T]] =
    single(size, f, (elems: List[T]) => TreeSet(elems: _*))


  @tailrec
  def insertElemsIntoAATree[T](tree: AATree[T], elems: List[T])(implicit ordering: Ordering[T]): AATree[T] =
    elems match {
      case x :: xs => insertElemsIntoAATree(tree + x, xs)
      case Nil => tree
    }

  @tailrec
  def insertElemsIntoTreeSet[T](tree: TreeSet[T], elems: List[T])(implicit ordering: Ordering[T]): TreeSet[T] =
    elems match {
      case x :: xs => insertElemsIntoTreeSet(tree.insert(x), xs)
      case Nil => tree
    }

  @tailrec
  def removeAllElemsFromAATree[T](tree: AATree[T], elems: List[T])(implicit ordering: Ordering[T]): AATree[T] = {
    elems match {
      case x :: xs => removeAllElemsFromAATree(tree - x, xs)
      case Nil => tree
    }
  }

  @tailrec
  def removeAllElemsFromTreeSet[T](tree: TreeSet[T], elems: List[T])(implicit ordering: Ordering[T]): TreeSet[T] = {
    elems match {
      case x :: xs => removeAllElemsFromTreeSet(tree - x, xs)
      case Nil => tree
    }
  }

}
