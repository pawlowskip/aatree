package com.pawlowskip.collection.immutable

import com.pawlowskip.collection.immutable.aatree.AATree
import org.scalatest.FlatSpec
import org.scalatest.prop.PropertyChecks
import org.scalatest.Matchers._
import org.scalacheck.Gen

/**
  * Created by przemek on 27/10/16.
  */
class AATreePropertyBasedSpec extends FlatSpec with PropertyChecks {

  val numbersGen = Gen.chooseNum(-100, 100)

  val aaTreeGen: Gen[AATree[Int]] =
    Gen.listOf(numbersGen).map(list => AATree.apply(list))

  implicit val config =
    PropertyCheckConfig(minSize = 10000, maxSize = 20000)

  "An AATree iterator" should "returns all elems from tree" in {
    forAll(aaTreeGen) { tree: AATree[Int] =>
      val t = tree
      val it = tree.iterator
      it.forall(elem => tree.contains(elem))
    }
  }

  "An AATree iterator" should "returns all elems in ascending order" in {
    forAll(Gen.listOf(numbersGen)) { l: List[Int] =>
      val list = AATree(l).iterator.toList
      list should equal(list.sorted)
    }
  }

  "An AATree" should "should be empty after removing all elems" in {
    forAll(aaTreeGen) { tree: AATree[Int] =>
      tree.iterator.foldLeft(tree)((t, elem) => t.remove(elem)).isEmpty
    }
  }

  "An AATree" should "should properly return minimum" in {
    forAll(aaTreeGen) { tree: AATree[Int] =>
      whenever(tree.nonEmpty) {
        tree.iterator.toList.min === tree.min.get
      }
    }
  }

  "An AATree" should "should properly return maximum" in {
    forAll(aaTreeGen) { tree: AATree[Int] =>
      whenever(tree.nonEmpty) {
        tree.iterator.toList.max === tree.max.get
      }
    }
  }

  "An AATree" should "insert properly all elements" in {
    forAll(Gen.listOf(numbersGen)) { elems: List[Int] =>
      val tree = AATree.apply(elems)
      elems.forall(elem => tree.contains(elem))
    }
  }

  "An AATree" should "insert and removes properly all elements" in {
    forAll(Gen.listOf(numbersGen)) { elems: List[Int] =>
      val tree = AATree.apply(elems)
      elems.foldLeft(tree)((t, elem) => t.remove(elem)).isEmpty
    }
  }

}
