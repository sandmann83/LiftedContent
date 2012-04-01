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
      "org.specs2" %% "specs2" % "1.8.2" % "test",
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
    //"/cms" -> cmsExampleModule,
    //"/blog" -> blogExampleModule,
   "/" -> homepage
  )

  lazy val coreSettings = Seq(
    libraryDependencies ++= Seq("nl.bitwalker" % "UserAgentUtils" % "1.2.4" % "compile->default" withSources)
  ) ++ globalSettings

  lazy val cmsExampleSettings = webappSettings ++ globalSettings
  lazy val blogExampleSettings = webappSettings ++ globalSettings
  lazy val homepageSettings = webappSettings ++ globalSettings

  lazy val root = Project("root", file(".")) settings(rootSettings:_*) aggregate(examples)
  lazy val examples = Project("Examples", file("liftedcontent-examples")) aggregate(cmsExampleModule, blogExampleModule)

  lazy val util = Project("liftedcontent-util", file("liftedcontent-util")) settings(globalSettings:_*)
  lazy val autocomplete = Project("liftedcontent-autocomplete", file("liftedcontent-autocomplete")) settings(globalSettings:_*)

  lazy val microformats = Project("liftedcontent-microformats", file("liftedcontent-microformats")) settings(globalSettings:_*) dependsOn(util)
  lazy val repository = Project("liftedcontent-repository", file("liftedcontent-repository")) settings(globalSettings:_*) dependsOn(util)
  lazy val richTextEditor = Project("liftedcontent-rte", file("liftedcontent-rte")) settings(globalSettings:_*) dependsOn(util, repository)

  lazy val core = Project("liftedcontent-core", file("liftedcontent-core")) settings(coreSettings:_*) dependsOn(util, richTextEditor, repository)

  lazy val blog = Project("liftedcontent-blog", file("liftedcontent-blog")) settings(globalSettings:_*) dependsOn(util, core, autocomplete, microformats)

  
  lazy val cmsExampleModule = Project("CMSExample", file("liftedcontent-examples/CMSExample")) settings(cmsExampleSettings:_*) dependsOn(core)
  lazy val blogExampleModule = Project("BlogExample", file("liftedcontent-examples/BlogExample")) settings(blogExampleSettings:_*) dependsOn(core, blog)
  lazy val homepage = Project("Homepage", file("Homepage")) settings(homepageSettings:_*) dependsOn(core, blog)
} 
