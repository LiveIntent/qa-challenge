name := "qa-challenge"
organization := "com.liveintent"

version := "0.0.1"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.2"
val awsVersion = "1.11.754"

libraryDependencies ++= Seq(
  guice,
  "com.amazonaws" % "aws-java-sdk-kinesis" % awsVersion
)
