package com.pawlowskip.collection.immutable.aatree

import com.pawlowskip.collection.immutable.aatree.AbstractAATree.Level

import scala.annotation.meta.getter
import scala.annotation.tailrec

private[aatree] object Node {
  @inline def apply[A](value: A, level: Level, left: AbstractAATree[A], right: AbstractAATree[A])(implicit ord: Ordering[A]): AANode[A] =
    new AANode[A](value, level, left, right)

  @tailrec
  def leftMostChild[A](tree: AbstractAATree[A]): AbstractAATree[A] =
    if (tree.left.isEmpty) tree
    else leftMostChild(tree.left)

  @tailrec
  def rightMostChild[A](tree: AbstractAATree[A]): AbstractAATree[A] =
    if (tree.right.isEmpty) tree
    else rightMostChild(tree.right)

}

private[aatree] final class AANode[+A](
    @(inline @getter) val value: A,
    @(inline @getter) val level: Level,
    @(inline @getter) val left: AbstractAATree[A],
    @(inline @getter) val right: AbstractAATree[A]
  )(implicit ord: Ordering[A]) extends AbstractAATree[A] {

  @inline override def isEmpty: Boolean = false

  @inline override def isNotEmpty: Boolean = true

  override def insert[B >: A](elem: B)(implicit ordering: Ordering[B]): AbstractAATree[B] = {
    val cmp = ordering.compare(elem, value)
    if (cmp < 0) Node(value, level, left.insert(elem), right).balance
    else if (cmp > 0) Node(value, level, left, right.insert(elem)).balance
    else this
  }

  override def balance[B >: A]: AbstractAATree[B] = skew.split

  override def skew: AANode[A] = {
    if (left.isEmpty || level != left.level) this
    else {
      val withModifiedLeft = Node(value, level, left.right, right)
      Node(left.value, left.level, left.left, withModifiedLeft)
    }
  }

  override def split: AANode[A] = {
    if (right.isEmpty || right.right.isEmpty) this
    else if (level == right.right.level) {
      val withModifiedRight = Node(value, level, left, right.left)
      Node(right.value, right.level + 1, withModifiedRight, right.right)
    }
    else this
  }

  override def remove[B >: A](elem: B)(implicit ordering: Ordering[B]): AbstractAATree[B] = {
    val cmp = ordering.compare(elem, value)
    if (cmp < 0) Node(value, level, left.remove(elem), right)
    else if (cmp > 0) Node(value, level, left, right.remove(elem))
    else {
      if (this.isLeaf) AAEmpty
      else if (left.isEmpty) {
        val successor = nodeWithRightChildSuccessor()
        Node(successor, level, AAEmpty, right.remove(successor)).balanceAfterRemove()
      } else {
        val predecessor = nodeWithLeftChildPredecessor()
        Node(predecessor, level, left.remove(predecessor), right).balanceAfterRemove()
      }
    }
  }

  private def balanceAfterRemove(): AbstractAATree[A] = {
    val tree = decreaseLevels().skew
    val skewedTreeRight = tree.right.skew
    val newRight =
      if (skewedTreeRight.isEmpty) skewedTreeRight
      else Node(skewedTreeRight.value, skewedTreeRight.level, skewedTreeRight.left, skewedTreeRight.right.skew)
    val newNode = Node(tree.value, tree.level, tree.left, newRight).split
    Node(newNode.value, newNode.level, newNode.left, newNode.right.split)
  }

  private def decreaseLevels(): AANode[A] = {
    val newLevel =  Math.min(left.level, right.level) + 1
    if (newLevel < level)
      if (newLevel < right.level)
        Node(value, newLevel, left, Node(right.value, newLevel, right.left, right.right))
      else
        Node(value, newLevel, left, right)
    else
      this
  }

  /**
    * assumes that tree has right child
    */
  private def nodeWithRightChildSuccessor(): A = {
    if (right.left.isEmpty) right.value
    else Node.leftMostChild(right).value
  }

  /**
    * assumes that tree has right child
    */
  private def nodeWithLeftChildPredecessor(): A = {
    Node.leftMostChild(this).value
  }

  override def min: Option[A] = Some(Node.leftMostChild(this).value)

  override def max: Option[A] = Some(Node.rightMostChild(this).value)

  override def contains[B >: A](elem: B)(implicit ordering: Ordering[B]): Boolean = {
    val cmp = ordering.compare(elem, value)
    if (cmp < 0) left.contains(elem)
    else if (cmp > 0) right.contains(elem)
    else true
  }

  override def iterator: Iterator[A] = new NodeIterator[A](this)

  override def foreach[U](f: (A) => U): Unit = {
    if (left.isNotEmpty) left.foreach(f)
    f(value)
    if (right.isNotEmpty) right.foreach(f)
  }

  override def successor[B >: A](elem: B)(implicit ordering: Ordering[B]): Option[B] = {
    @tailrec
    def loop(tree: AbstractAATree[B], currentSuccessor: Option[B]): Option[B] = {
      if (tree.isEmpty) currentSuccessor
      else {
        val cmp = ordering.compare(elem, tree.value)
        if (cmp == 0) {
          if (tree.right.isNotEmpty) tree.right.min
          else currentSuccessor
        }
        else if (cmp < 0) loop(tree.left, Some(tree.value))
        else loop(tree.right, currentSuccessor)
      }
    }
    loop(this, None)
  }

  override def predecessor[B >: A](elem: B)(implicit ordering: Ordering[B]): Option[B] = {
    @tailrec
    def loop(tree: AbstractAATree[B], currentPredecessor: Option[B]): Option[B] = {
      if (tree.isEmpty) currentPredecessor
      else {
        val cmp = ordering.compare(elem, tree.value)
        if (cmp == 0) {
          if (tree.left.isNotEmpty) tree.left.max
          else currentPredecessor
        }
        else if (cmp < 0) loop(tree.left, currentPredecessor)
        else loop(tree.right, Some(tree.value))
      }
    }
    loop(this, None)
  }

}

private[aatree] class NodeIterator[A](private var tree: AbstractAATree[A]) extends Iterator[A] {
  private[this] val MAX_HEIGHT = (tree.level - 1) * 2
  private[this] val parentStack = new Array[AbstractAATree[A]](MAX_HEIGHT)

  private[this] var lastParentPushedIndex = -1
  private[this] var lastUsed: AbstractAATree[A] = _

  override def hasNext: Boolean = {
    tree.right.isNotEmpty || lastParentPushedIndex != -1 ||
      (tree.left.isNotEmpty && lastUsed != tree.left && lastUsed != tree && lastUsed != tree.right)
  }

  override def next(): A = {
    if (tree.left.isNotEmpty && lastUsed != tree.left && lastUsed != tree && lastUsed != tree.right) {
      while(tree.left.isNotEmpty) {
        lastParentPushedIndex += 1
        parentStack(lastParentPushedIndex) = tree
        tree = tree.left
      }
      next()
    } else if (lastUsed == tree.left || (tree.left.isEmpty && lastUsed != tree.right && lastUsed != tree)) {
      lastUsed = tree
      tree.value
    } else if (tree.right.isNotEmpty && lastUsed != tree.right) {
      if (lastParentPushedIndex != -1) {
        lastParentPushedIndex += 1
        parentStack(lastParentPushedIndex) = tree
      }
      lastUsed = tree.right
      tree = tree.right
      lastUsed.value
    } else {
      lastUsed = tree
      tree = parentStack(lastParentPushedIndex)
      lastParentPushedIndex -= 1
      next()
    }
  }

}
