name := "aatree"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.0"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.0" % "test"
libraryDependencies += "org.scalacheck" %% "scalacheck" % "1.13.3" % "test"
libraryDependencies += "com.storm-enroute" %% "scalameter" % "0.8" % "test"

testFrameworks += new TestFramework(
  "org.scalameter.ScalaMeterFramework"
)

logBuffered := false

parallelExecution in Test := false