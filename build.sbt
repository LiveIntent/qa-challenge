import com.typesafe.sbt.packager.docker.{Cmd, DockerPermissionStrategy, ExecCmd}

name := "qa-challenge"
organization := "com.liveintent"

version := "0.0.1"

lazy val root = (project in file("."))
  .enablePlugins(PlayScala)
  .settings(
    maintainer := "Devs <dev-berlin@liveintent.com>",
    dockerExposedPorts := Seq(9000),
    dockerBaseImage := "openjdk:8-jdk-alpine",
    dockerPermissionStrategy := DockerPermissionStrategy.CopyChown,
    dockerLabels := Map("version" -> (version in ThisBuild).value),
    dockerCommands := dockerCommands.value.flatMap {
      // Find the right place to insert additional commands...
      case cmd @ Cmd("COPY", args @ _*) =>
        // ...and append package installation commands
        Seq(Cmd("RUN", "apk add --no-cache bash"), Cmd("RUN", "apk add --no-cache tini"), cmd)

      case cmd @ ExecCmd("CMD", args @ _*) if args.isEmpty =>
        Seq(ExecCmd("CMD", "bin/qa-challenge", "-Dstdout_log_format=json"))

      case cmd =>
        Seq(cmd)
    },
    dockerEntrypoint := Seq("/sbin/tini", "--"),
    dockerUsername := Some("liveintentberlin")
  )

scalaVersion := "2.13.2"
val awsVersion = "1.11.754"

libraryDependencies ++= Seq(
  guice,
  "com.amazonaws" % "aws-java-sdk-kinesis" % awsVersion
)
