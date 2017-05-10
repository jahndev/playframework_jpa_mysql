name := "prosoft-testPart2-jahn"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.11"

libraryDependencies += javaJpa
libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.34"
libraryDependencies += "org.hibernate" % "hibernate-core" % "5.2.10.Final"
libraryDependencies += "org.hibernate" % "hibernate-entitymanager" % "5.2.10.Final"
libraryDependencies += javaWs % "test"
libraryDependencies += "org.awaitility" % "awaitility" % "2.0.0" % "test"
libraryDependencies += "org.assertj" % "assertj-core" % "3.6.2" % "test"
libraryDependencies += "org.mockito" % "mockito-core" % "2.1.0" % "test"
testOptions in Test += Tests.Argument(TestFrameworks.JUnit, "-a", "-v")
