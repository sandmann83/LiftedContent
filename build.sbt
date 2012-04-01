name := "LiftedContent"

version := "1.0.0"
 
scalaVersion := "2.9.1"

scanDirectories in Compile := Nil

scalacOptions in Compile ++= Seq(
  "-deprecation",
   "-g:vars"
)
