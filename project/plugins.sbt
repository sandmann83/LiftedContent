libraryDependencies <+= sbtVersion(v => v match {
case x if (x.startsWith("0.12")) => "com.github.siasia" %% "xsbt-web-plugin" % "0.12.0-0.2.11.1"
})

addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "2.1.1")

addSbtPlugin("org.scalastyle" %% "scalastyle-sbt-plugin" % "0.2.0")