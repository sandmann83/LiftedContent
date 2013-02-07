name := "LiftedContent"

version := "1.0.1"

organization := "sbradl"

scalaVersion in ThisBuild := "2.10.0"

seq(com.github.siasia.WebPlugin.webSettings :_*)

seq(org.scalastyle.sbt.ScalastylePlugin.Settings :_*)

scalacOptions ++= Seq("-deprecation", "-unchecked")

EclipseKeys.createSrc := EclipseCreateSrc.Default + EclipseCreateSrc.Resource

parallelExecution in Test := false

testOptions in Test += Tests.Argument("junitxml")
