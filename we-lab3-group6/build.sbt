name := """we-lab3-group6"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
   jdbc,
   anorm,
   cache,
   ws,
   javaJdbc,   javaCore,   javaJpa,   "org.hibernate" % "hibernate-entitymanager" % "4.3.1.Final",
   "com.google.code.gson" % "gson" % "2.2"
)

templatesImport += "scala.collection._"

templatesImport += "at.ac.tuwien.big.we15.lab2.api._"