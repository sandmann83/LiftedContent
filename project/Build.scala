import sbt._

import com.github.siasia._
import WebappPlugin.webappSettings
import Keys._
    
object WebBuild extends Build {
  val liftVersion = "2.4"

  lazy val container = Container("container")

  lazy val globalSettings = Seq(
    libraryDependencies ++= Seq(
      "junit" % "junit" % "4.10" % "test",
      "org.specs2" %% "specs2" % "1.7.1" % "test",
      "org.slf4j" % "slf4j-log4j12" % "1.6.4",
      "net.liftweb" %% "lift-webkit" % liftVersion % "compile->default" withSources,
      "net.liftweb" %% "lift-util" % liftVersion % "compile->default" withSources,
      "net.liftweb" %% "lift-common" % liftVersion % "compile->default" withSources,
      "net.liftweb" %% "lift-mapper" % liftVersion % "compile->default" withSources,
      "net.liftweb" %% "lift-wizard" % liftVersion % "compile->default" withSources,
      "net.liftweb" %% "lift-widgets" % liftVersion % "compile->default" withSources,
      "net.liftweb" %% "lift-textile" % liftVersion % "compile->default" withSources
    )
  )

  lazy val rootSettings = Seq(
    libraryDependencies += "org.eclipse.jetty" % "jetty-webapp" % "8.1.0.v20120127" % "container"
  ) ++ container.deploy(
    "/cms" -> cmsExampleModule,
    "/blog" -> blogExampleModule
  )

  lazy val utilSettings = globalSettings

  lazy val coreSettings = Seq(
    libraryDependencies ++= Seq("nl.bitwalker" % "UserAgentUtils" % "1.2.4" % "compile->default" withSources)
  ) ++ globalSettings

  lazy val rteSettings = webappSettings ++ globalSettings
  lazy val blogSettings = globalSettings

  lazy val cmsExampleSettings = webappSettings ++ globalSettings
  lazy val blogExampleSettings = webappSettings ++ globalSettings

  lazy val root = Project("root", file(".")) settings(rootSettings:_*) aggregate(examples)
  lazy val examples = Project("Examples", file("liftedcontent-examples")) aggregate(cmsExampleModule, blogExampleModule)

  lazy val util = Project("Util", file("liftedcontent-util")) settings(utilSettings:_*)
  lazy val autocomplete = Project("AutoComplete", file("liftedcontent-autocomplete")) settings(globalSettings:_*)

  lazy val microformats = Project("Microformats", file("liftedcontent-microformats")) settings(globalSettings:_*) dependsOn(util)
  lazy val richTextEditor = Project("RichTextEditor", file("liftedcontent-rte")) settings(rteSettings:_*) dependsOn(util)

  lazy val core = Project("Core", file("liftedcontent-core")) settings(coreSettings:_*) dependsOn(util, richTextEditor)

  lazy val blog = Project("Blog", file("liftedcontent-blog")) settings(blogSettings:_*) dependsOn(core, autocomplete)

  
  lazy val cmsExampleModule = Project("CMSExample", file("liftedcontent-examples/CMSExample")) settings(cmsExampleSettings:_*) dependsOn(core)
  lazy val blogExampleModule = Project("BlogExample", file("liftedcontent-examples/BlogExample")) settings(blogExampleSettings:_*) dependsOn(core, blog)
} 
