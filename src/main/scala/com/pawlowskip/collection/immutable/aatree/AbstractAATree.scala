package com.pawlowskip.collection.immutable.aatree

import com.pawlowskip.collection.immutable.aatree.AbstractAATree.Level

import scala.annotation.meta.getter
import scala.annotation.tailrec

private[aatree] abstract class AbstractAATree[+A] extends Iterable[A] with Serializable {

  @(inline @getter) def value: A

  @(inline @getter) val level: Level

  @(inline @getter) val right: AbstractAATree[A]

  @(inline @getter) val left: AbstractAATree[A]

  def isEmpty: Boolean

  def isNotEmpty: Boolean

  def insert[B >: A](elem: B)(implicit ordering: Ordering[B]): AbstractAATree[B]

  def remove[B >: A](elem: B)(implicit ordering: Ordering[B]): AbstractAATree[B]

  def +[B >: A](elem: B)(implicit ordering: Ordering[B]): AbstractAATree[B] = insert(elem)

  def -[B >: A](elem: B)(implicit ordering: Ordering[B]): AbstractAATree[B] = remove(elem)

  def min: Option[A]

  def max: Option[A]

  def contains[B >: A](elem: B)(implicit ordering: Ordering[B]): Boolean

  def successor[B >: A](elem: B)(implicit ordering: Ordering[B]): Option[B]

  def predecessor[B >: A](elem: B)(implicit ordering: Ordering[B]): Option[B]

  private[aatree]  def balance[B >: A]: AbstractAATree[B]

  private[aatree] def skew: AbstractAATree[A]

  private[aatree]  def split: AbstractAATree[A]

  private[aatree]  def isLeaf: Boolean = left.isEmpty && right.isEmpty

  override def seq: AbstractAATree[A] = this

}

private[aatree] object AbstractAATree {

  type Level = Int

  @inline def empty[A]: AbstractAATree[A] = AAEmpty

  def apply[A](elems: A*)(implicit ordering: Ordering[A]): AbstractAATree[A] = {
    var i = 0
    var tree: AbstractAATree[A] = AAEmpty
    while(i < elems.length) {
      tree = tree + elems(i)
      i += 1
    }
    tree
  }

  def apply[A](elems: List[A])(implicit ordering: Ordering[A]): AbstractAATree[A] = {
    @tailrec
    def putAll(acc: AbstractAATree[A], elems: List[A]): AbstractAATree[A] = elems match {
      case x :: xs => putAll(acc.insert(x), xs)
      case Nil => acc
    }
    putAll(AAEmpty, elems)
  }

}

