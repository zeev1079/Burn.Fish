import java.nio.file.StandardCopyOption

enablePlugins(ScalaJSPlugin)

name := "CoinBurn"

version := "0.1"

scalaVersion := "2.13.4"

scalaJSUseMainModuleInitializer := true

libraryDependencies += "org.scala-js" %%% "scalajs-dom" % "1.1.0"


lazy val copyTask = taskKey[Unit]("copyJS")

copyTask := {
	val bundle = (Compile / fullOptJS).value
	val destinationPath =
		file(s"docs/javascripts/${bundle.data.name}").toPath
	java.nio.file.Files.copy(
		bundle.data.toPath,
		destinationPath,
		StandardCopyOption.REPLACE_EXISTING
	)
	val destinationPathMap =
		file(s"docs/javascripts/${bundle.data.name}.map").toPath
	val sourcePathMap = file(s"${bundle.data.toPath}.map").toPath
	java.nio.file.Files.copy(
		sourcePathMap,
		destinationPathMap,
		StandardCopyOption.REPLACE_EXISTING
	)
}



