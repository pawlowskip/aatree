package com.pawlowskip.collection.immutable

import com.pawlowskip.collection.immutable.utils.AverageTimeBench
import com.pawlowskip.collection.immutable.utils.PerformanceUtils._
import org.scalameter.Gen

import scala.collection.immutable.TreeSet

class TreeSetPerformanceSpec extends AverageTimeBench {

  performance of "TreeSet" in {
    measure method "insert" in {
      using(listGenOfSizes(expSizes, randomInt())) config runTimes(2000) in { elems =>
        insertElemsIntoTreeSet(TreeSet.empty[Int], elems)
      }
    }

    measure method "remove" in {
      using(treeSetWithElemsGenSizes(expSizes, randomInt())) config runTimes(2000) in { case (tree, elems) =>
        removeAllElemsFromTreeSet(tree, elems)
      }
    }

    measure method "max" in {
      using(treeSetGenOfSizes(expSizes, randomInt())) config runTimes(2000) in { tree =>
        tree.max
      }
    }

    measure method "min" in {
      using(treeSetGenOfSizes(expSizes, randomInt())) config runTimes(2000) in { tree =>
        tree.max
      }
    }

    measure method "contains" in {
      using(treeSetWithElemsGenSizes(expSizes, randomInt())) config runTimes(2000) in { case (tree, elems) =>
        elems.foreach(elem => tree.contains(elem))
      }
    }

    measure method "foreach" in {
      using(treeSetGenOfSizes(expSizes, randomInt())) config runTimes(2000) in { tree =>
        tree.foreach(i => 2 * 2)
      }
    }

    measure method "iterator" in {
      using(treeSetGenOfSizes(expSizes, randomInt())) config runTimes(2000) in { tree =>
        val it = tree.iterator
        while (it.hasNext) it.next()
      }
    }

    measure method "++" in {
      using(
        Gen.crossProduct(
          treeSetGenOfSizes(expSizes, randomInt()),
          treeSetGenOfSizes(expSizes, randomInt())
        )
      ) config runTimes(2000) in { case (t1, t2) =>
        t1 ++ t2
      }
    }

    measure method "map" in {
      using(treeSetGenOfSizes(expSizes, randomInt())) config runTimes(2000) in { tree =>
        tree.map(_ * 2)
      }
    }

    measure method "flatMap" in {
      using(treeSetGenOfSizes(expSizes, randomInt())) config runTimes(2000) in { tree =>
        tree.flatMap(i => TreeSet(i, i, i))
      }
    }

  }

}
