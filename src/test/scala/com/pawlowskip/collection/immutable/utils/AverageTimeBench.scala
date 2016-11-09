package com.pawlowskip.collection.immutable.utils

import org.scalameter.{Aggregator, Bench}

trait AverageTimeBench extends Bench.LocalTime {
  override val aggregator = Aggregator.average
}
