name := "PPA"

version := "1.0"

scalaVersion := "2.11.0"

resolvers += "Scalaz Bintray Repo" at "http://dl.bintray.com/scalaz/releases"

resolvers += "Typesafe Repo" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies ++= Seq(
  "com.datastax.cassandra" % "cassandra-driver-core" % "2.1.7",
  "ch.qos.logback" % "logback-classic" % "1.1.2",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.1.0",
  "org.webjars" %% "webjars-play" % "2.3.0-3",
  "com.typesafe.play" %% "play-json" % "2.3.0",
  "com.typesafe.akka" %% "akka-actor" % "2.3.6",
  "com.typesafe.akka" %% "akka-slf4j" % "2.3.6",
  "io.spray" %% "spray-can" % "1.3.1",
  "io.spray" %% "spray-client" % "1.3.1",
  "io.spray" %% "spray-routing" % "1.3.1",
  "io.spray" %% "spray-json" % "1.3.0",
  "org.xerial.snappy" % "snappy-java" % "1.1.1.3",
  "org.scala-lang" % "scala-reflect" % "2.10.4",
  "org.specs2" %% "specs2" % "2.4.6" % "test"
  // "io.spray"                % "spray-testkit"         % "1.3.1"   % "test",
  //  "com.typesafe.akka"      %% "akka-testkit"          % "2.3.6"    % "test",
)
    