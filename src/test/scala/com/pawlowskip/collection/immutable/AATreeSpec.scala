package com.pawlowskip.collection.immutable

import com.pawlowskip.collection.immutable.aatree.{AATree}
import org.scalatest.FlatSpec
import org.scalatest.Matchers._

class AATreeSpec extends FlatSpec {

  "An AATree" should "properly insert one number and contains it" in {
    val tree = AATree.empty[Int] + 1
    assert(tree.contains(1))
  }

  "An AATree" should "properly insert numbers and contains it" in {
    val tree = AATree(-1, 4)
    assert(tree.contains(-1))
    assert(tree.contains(4))
  }

  "An AATree" should "properly insert numbers and removes them" in {
    val tree = AATree(1, 4, 6)
    assert(tree.contains(1))
    assert(tree.contains(4))
    assert(tree.contains(6))
    assert(tree.remove(4).contains(4) === false)
    assert(tree.remove(6).contains(6) === false)
    assert(tree.remove(1).contains(6))
    assert(tree.remove(4).remove(1).contains(6))
  }

  "An AATree" should "properly returns empty status" in {
    val tree = AATree.empty[Int]
    assert(tree.isEmpty)
    assert(!tree.nonEmpty)
    assert(!tree.insert(1).isEmpty)
    assert(tree.insert(1).nonEmpty)
    assert(tree.insert(1).remove(1).isEmpty)
    assert(!tree.insert(1).remove(1).nonEmpty)
  }

  "An AATree" should "properly returns minimum" in {
    val tree = AATree.empty[Int]
    assert(tree.min === None)
    assert(tree.insert(0).insert(-1).insert(1).min === Option(-1))
  }

  "An AATree" should "properly returns maximum" in {
    val tree = AATree.empty[Int]
    assert(tree.max === None)
    assert(tree.insert(2).insert(-100).insert(100).insert(34).max === Option(100))
  }

  "An AATree" should "return empty iterator for empty tree" in {
    val tree = AATree.empty[Int]
    assert(tree.iterator.hasNext === false)
  }

  "An AATree" should "iterator should return all elements" in {
    val tree = AATree.empty[Int].insert(-100).insert(1).insert(10).insert(-19).insert(100)
    val elems = tree.iterator.toList
    assert(List(-100, 1, 10, -19, 100).forall(elem => elems.contains(elem)))
  }

  "An AATree" should "return ascending iterator with one elem" in {
    val tree = AATree.empty[Int].insert(0)
    tree.iterator.toList should equal (List(0))
  }

  "An AATree" should "return ascending iterator" in {
    val tree = AATree.empty[Int].insert(0).insert(-1).insert(1)
    tree.iterator.toList should equal (List(-1, 0, 1))
  }

  "An AATree" should "ignore duplicates" in {
    val tree = AATree.empty[Int].insert(13).insert(13).insert(3).insert(6)
    tree.iterator.toList should equal (List(3, 6, 13))
  }

}
