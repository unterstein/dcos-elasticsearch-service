name := "eaas-web"

organization := "io.github.unterstein"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

resolvers += Resolver.sonatypeRepo("snapshots")

scalaVersion := "2.12.2"

libraryDependencies += guice
libraryDependencies += "org.elasticsearch.client" % "transport" % "5.6.3"

libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test
libraryDependencies += "org.apache.commons" % "commons-lang3" % "3.6"
libraryDependencies += "commons-io" % "commons-io" % "2.6"
libraryDependencies += "org.apache.httpcomponents" % "httpclient" % "4.5.3"
