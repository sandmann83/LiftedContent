import sbt._
import Keys._

import com.github.siasia._
import WebappPlugin.webappSettings

object LiftedContentBuild extends Build {
    val liftVersion = "2.5-SNAPSHOT"
  
    lazy val container = Container("container")

    lazy val globalSettings = Seq(
      libraryDependencies ++= Seq(
	  "net.liftweb"       	%% "lift-webkit"        	% liftVersion        			% "compile",
	  "net.liftweb"		%% "lift-mapper"	 	% liftVersion	     			% "compile",
	  "net.liftweb"		%% "lift-wizard"		% liftVersion				% "compile",
	  "net.liftmodules"	%% "textile"			% (liftVersion + "-1.3-SNAPSHOT")	% "compile",
	  "net.liftmodules"	%% "widgets"			% (liftVersion + "-1.2-SNAPSHOT")	% "compile",
	  "org.specs2"        	%% "specs2"             	% "1.13"            			% "test",
	  "junit" 		% "junit" 			% "4.11"				% "test",
	  "ch.qos.logback"    	% "logback-classic"     	% "1.0.9",
	  "org.slf4j" 		% "slf4j-api" 			% "1.7.2"
      ),
      resolvers ++= Seq("snapshots"	at "http://oss.sonatype.org/content/repositories/snapshots",
                  "releases"	at "http://oss.sonatype.org/content/repositories/releases",
		  "liftmodules" at "http://repository-liftmodules.forge.cloudbees.com/release")
    )

    lazy val rootSettings = Seq(
      libraryDependencies ++= {
	val jettyVersion = "8.1.8.v20121106"
	Seq(
	  "org.eclipse.jetty" % "jetty-webapp" % jettyVersion % "container,compile",
	  "org.eclipse.jetty.orbit" % "javax.servlet" % "3.0.0.v201112011016" % "container,compile" artifacts Artifact("javax.servlet", "jar", "jar")
	)
      }
    ) ++ container.deploy(
      //"/cms" -> cmsExampleModule
      //"/blog" -> blogExampleModule,
      "/" -> homepage
    )
    
    lazy val coreSettings = Seq(
      libraryDependencies ++= Seq("nl.bitwalker" % "UserAgentUtils" % "1.2.4")
    ) ++ globalSettings

    lazy val cmsExampleSettings = webappSettings ++ globalSettings
    lazy val blogExampleSettings = webappSettings ++ globalSettings
    lazy val homepageSettings = webappSettings ++ globalSettings
    
    lazy val root = Project(id = "root", base = file(".")) settings(rootSettings:_*) aggregate(examples)

    lazy val examples = Project(id = "examples", base = file("liftedcontent-examples")) aggregate(cmsExample, blogExample)
    
    lazy val cmsExample = Project(id = "cmsExample", base = file("liftedcontent-examples/CMSExample")) settings(cmsExampleSettings:_*) dependsOn(core)
    lazy val blogExample = Project(id = "blogExample", base = file("liftedcontent-examples/BlogExample")) settings(blogExampleSettings:_*) dependsOn(blog)
    lazy val homepage = Project(id = "homepage", base = file("Homepage")) settings(homepageSettings:_*) dependsOn(blog)
    
    lazy val core = Project(id = "core", base = file("liftedcontent-core")) settings(coreSettings:_*) dependsOn(util, richTextEditor, repository)
    lazy val blog = Project(id = "blog", base = file("liftedcontent-blog")) settings(globalSettings:_*) dependsOn(util, core, autocomplete, microformats)
    
    lazy val util = Project(id = "util", base = file("liftedcontent-util")) settings(globalSettings:_*)
    
    lazy val autocomplete = Project(id = "autocomplete", base = file("liftedcontent-autocomplete")) settings(globalSettings:_*)

    lazy val microformats = Project(id = "microformats", base = file("liftedcontent-microformats")) settings(globalSettings:_*) dependsOn(util)
    lazy val repository = Project(id = "repository",base =  file("liftedcontent-repository")) settings(globalSettings:_*) dependsOn(util)
    lazy val richTextEditor = Project(id = "rte", base = file("liftedcontent-rte")) settings(globalSettings:_*) dependsOn(util, repository)
} 
