package com.pawlowskip.collection.immutable

import com.pawlowskip.collection.immutable.aatree.AATree
import com.pawlowskip.collection.immutable.utils.AverageTimeBench
import com.pawlowskip.collection.immutable.utils.PerformanceUtils._
import org.scalameter.Gen

class AATreePerformanceSpec extends AverageTimeBench {

  performance of "AATree" in {
    measure method "insert" in {
      using(listGenOfSizes(expSizes, randomInt())) config runTimes(2000) in { elems =>
        insertElemsIntoAATree(AATree.empty[Int], elems)
      }
    }

    measure method "remove" in {
      using(aaTreeWithElemsGenSizes(expSizes, randomInt())) config runTimes(2000) in { case (tree, elems) =>
        removeAllElemsFromAATree(tree, elems)
      }
    }

    measure method "max" in {
      using(aaTreeGenOfSizes(expSizes, randomInt())) config runTimes(2000) in { tree =>
        tree.max
      }
    }

    measure method "min" in {
      using(aaTreeGenOfSizes(expSizes, randomInt())) config runTimes(2000) in { tree =>
        tree.max
      }
    }

    measure method "contains" in {
      using(aaTreeWithElemsGenSizes(expSizes, randomInt())) config runTimes(2000) in { case (tree, elems) =>
        elems.foreach(elem => tree.contains(elem))
      }
    }

    measure method "foreach" in {
      using(aaTreeGenOfSizes(expSizes, randomInt())) config runTimes(2000) in { tree =>
        tree.foreach(i => 2 * 2)
      }
    }

    measure method "iterator" in {
      using(aaTreeGenOfSizes(expSizes, randomInt())) config runTimes(2000) in { tree =>
        val it = tree.iterator
        while (it.hasNext) it.next()
      }
    }

    measure method "++" in {
      using(
        Gen.crossProduct(
          aaTreeGenOfSizes(expSizes, randomInt()),
          aaTreeGenOfSizes(expSizes, randomInt())
        )
      ) config runTimes(2000) in { case (t1, t2) =>
        t1 ++ t2
      }
    }

    measure method "map" in {
      using(aaTreeGenOfSizes(expSizes, randomInt())) config runTimes(2000) in { tree =>
        tree.map(_ * 2)
      }
    }

    measure method "flatMap" in {
      using(aaTreeGenOfSizes(expSizes, randomInt())) config runTimes(2000) in { tree =>
        tree.flatMap(i => AATree(i, i, i))
      }
    }

  }

}
