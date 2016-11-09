package com.pawlowskip.collection.immutable.aatree

import com.pawlowskip.collection.immutable.aatree.AbstractAATree.Level

import scala.annotation.meta.getter

private[aatree] object AAEmpty extends AbstractAATree[Nothing] {
  @(inline @getter) override val isEmpty = true
  @(inline @getter) override val isNotEmpty = false
  override def value: Nothing = throw new NoSuchElementException
  @(inline @getter) override val level: Level = 0
  @(inline @getter) override val left: AbstractAATree[Nothing] = AAEmpty
  @(inline @getter) override val right: AbstractAATree[Nothing] = AAEmpty

  @inline override def insert[B >: Nothing](elem: B)(implicit ordering: Ordering[B]): AbstractAATree[B] =
    new AANode(elem, 1, AAEmpty, AAEmpty)

  @inline override def balance[B >: Nothing]: AbstractAATree[B] = this

  @inline override def remove[B >: Nothing](elem: B)(implicit ordering: Ordering[B]): AbstractAATree[B] = this

  @inline override def skew: AbstractAATree[Nothing] = this

  @inline override def split: AbstractAATree[Nothing] = this

  @inline override def min: Option[Nothing] = None

  @inline override def max: Option[Nothing] = None

  @inline override def contains[B >: Nothing](elem: B)(implicit ordering: Ordering[B]): Boolean = false

  @inline override def iterator: Iterator[Nothing] = Iterator.empty

}
