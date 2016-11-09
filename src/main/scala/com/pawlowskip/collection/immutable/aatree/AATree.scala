package com.pawlowskip.collection.immutable.aatree

import scala.collection.generic.{CanBuildFrom, ImmutableSortedSetFactory}
import scala.collection.immutable.SortedSet
import scala.collection.{SortedSetLike, mutable}

trait AATree[A] extends SortedSetLike[A, AATree[A]] with SortedSet[A] {
  def insert(elem: A): AATree[A]
  def remove(elem: A): AATree[A]
  def min: Option[A]
  def max: Option[A]
  override def empty: AATree[A] = AATree.empty
  override def newBuilder: mutable.Builder[A, AATree[A]] = AATree.newBuilder
}

object AATree extends ImmutableSortedSetFactory[AATree] {

  override def newBuilder[A](implicit ordering: Ordering[A]): mutable.Builder[A, AATree[A]] =
    new AATreeBuilder[A](AbstractAATree.empty)

  implicit def canBuildFrom[A](implicit ordering: Ordering[A]): CanBuildFrom[Coll, A, AATree[A]] =
    newCanBuildFrom[A]

  override def newCanBuildFrom[A](implicit ord: Ordering[A]): CanBuildFrom[Coll, A, AATree[A]] =
    new CanBuildFrom[Coll, A, AATree[A]] {
      override def apply(from: Coll): mutable.Builder[A, AATree[A]] = new AATreeBuilder[A](AbstractAATree.empty)(ord)
      override def apply(): mutable.Builder[A, AATree[A]] = new AATreeBuilder[A](AbstractAATree.empty)(ord)
    }

  private[this] class AATreeBuilder[A](var tree: AbstractAATree[A])(implicit ordering: Ordering[A])
    extends mutable.Builder[A, AATree[A]] {

    override def +=(elem: A): AATreeBuilder.this.type = {tree = tree.insert(elem); this}
    override def clear(): Unit = tree = AbstractAATree.empty
    override def result(): AATree[A] = new AATrunk(tree)
  }

  def empty[A](implicit ord: Ordering[A]): AATree[A] = new AATrunk(AbstractAATree.empty)

  override def apply[A](elems: A*)(implicit ordering: Ordering[A]): AATree[A] = new AATrunk(AbstractAATree.apply(elems: _*))

  def apply[A](elems: List[A])(implicit ordering: Ordering[A]): AATree[A] = new AATrunk(AbstractAATree.apply(elems))


  class AATrunk[A](val tree: AbstractAATree[A])(implicit val ordering: Ordering[A]) extends AATree[A] {

    if (ordering eq null)
      throw new NullPointerException("ordering must not be null")

    override def contains(elem: A): Boolean = tree.contains(elem)

    override def +(elem: A): AATree[A] = new AATrunk(tree.insert(elem))

    override def -(elem: A): AATree[A] = new AATrunk(tree.remove(elem))

    override def iterator: Iterator[A] = tree.iterator

    override def seq: AATree[A] = this

    override def nonEmpty: Boolean = tree.isNotEmpty

    override def isEmpty: Boolean = tree.isEmpty

    override def insert(elem: A): AATree[A] = new AATrunk(tree.insert(elem))

    override def remove(elem: A): AATree[A] = new AATrunk(tree.remove(elem))

    override def min: Option[A] = tree.min

    override def max: Option[A] = tree.max

    override def rangeImpl(from: Option[A], until: Option[A]): AATree[A] = ???

    override def keysIteratorFrom(start: A): Iterator[A] = ???
  }

}
