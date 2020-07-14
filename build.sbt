

name := "fluent"
version := "0.1.0"

scalaVersion := "2.12.7"

// for scala
lazy val scalaDependencies = Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.5.26",
  "com.typesafe.akka" %% "akka-testkit" % "2.5.26" % Test,
  "com.typesafe.akka" %% "akka-http" % "10.1.11",
  "com.typesafe.akka" %% "akka-stream" % "2.5.26",
  "com.typesafe.akka" %% "akka-http-testkit" % "10.1.11" % Test,
  "org.scalatest" %% "scalatest" % "3.0.5" % Test,
  "com.typesafe.akka" %% "akka-stream-testkit" % "2.5.26" % Test,

  // SQL generator
//  "com.typesafe.slick" %% "slick" % "3.2.3",
//  "io.underscore" %% "slickless" % "0.3.2",

  // quill for sql
  "io.getquill" %% "quill-jdbc" % "3.4.10",


  // Config file parser
  "com.github.pureconfig" %% "pureconfig" % "0.12.3",

  // JSON serialization library
  "io.circe" %% "circe-core" % "0.10.0",
  "io.circe" %% "circe-generic" % "0.10.0",
  "io.circe" %% "circe-parser" % "0.10.0",
  "io.circe" %% "circe-optics" % "0.10.0",

  // Sugar for serialization and deserialization in akka-http with circe
  "de.heikoseeberger" %% "akka-http-circe" % "1.20.1",

  // Validation library
  "com.wix" %% "accord-core" % "0.7.2",

  // Use for logging
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.0",

  // For validation
  //"org.squbs" %% "squbs-pattern" % "0.11.0",
  "com.wix" %% "accord-core" % "0.7.2",

  // For http client
  "com.softwaremill.sttp" %% "core" % "1.4.2",
  "com.softwaremill.sttp" %% "akka-http-backend" % "1.4.2",
  "com.softwaremill.sttp" %% "circe" % "1.4.2",

  // For scala DI
  "com.softwaremill.macwire" %% "macros" % "2.3.3",


  // scala-async
  "org.scala-lang.modules" %% "scala-async" % "0.9.7",

  "org.scalamock" %% "scalamock" % "4.1.0" % Test,
  "net.codingwell" %% "scala-guice" % "4.2.3",

  // kamon for monitor
  "io.kamon" %% "kamon-bundle" % "2.1.0",
  "io.kamon" %% "kamon-logback" % "2.1.0",
  "io.kamon" %% "kamon-prometheus" % "2.1.0"

)

// for java
lazy val javayDependencies = Seq(

  // Migration for SQL databases
  "org.flywaydb" % "flyway-core" % "5.2.1",

  // Connection pool for database
  "com.zaxxer" % "HikariCP" % "3.4.1",

  // msql driver
  "mysql" % "mysql-connector-java" % "5.1.22",

  // Mock for test
  "org.mockito" % "mockito-all" % "1.9.5" % Test,

  // "ch.qos.logback" % "logback-classic" % "1.2.3"
  "ch.qos.logback" % "logback-classic" % "1.2.3",

  // 文件上传
  "commons-io" % "commons-io" % "2.6",

  // for trace id
  "com.ofpay" % "logback-mdc-ttl" % "1.0.2",
  "com.alibaba" % "transmittable-thread-local" % "2.11.4",

  "net.bytebuddy" % "byte-buddy-parent" % "1.9.12" pomOnly()
)


lazy val assemblySettings = Seq(
  assemblyJarName in assembly := s"${name.value}.jar",
  assemblyMergeStrategy in assembly := {
    case PathList("META-INF", xs @ _*) => MergeStrategy.discard
    case "reference.conf" => MergeStrategy.concat
    case x => MergeStrategy.first
  },
  // shutdown unit test
  test in assembly := {},
  excludeFilter in unmanagedResources := {
    val resource = ((resourceDirectory in Compile).value).getCanonicalPath
    new SimpleFileFilter(_.getCanonicalPath startsWith resource)
  }
)

lazy val universalSettings = Seq(
  packageName in Universal := name.value,

  // add config
  mappings in Universal := {
    val universalMappings = (mappings in Universal).value

    val confFile = buildEnv.value match {
      case BuildEnv.Developement => "application.conf"
      case BuildEnv.Test => "application.test.conf"
      case BuildEnv.Production => "application.prod.conf"
    }
    val resourcesPath = (resourceDirectory in Compile).value

    val otherMappings = universalMappings :+ (resourcesPath / confFile) ->
      // main config file of application
      "config/application.conf" :+
      // add other config
      (resourcesPath / "logback.xml") -> "config/logback.xml" :+
      (resourcesPath / "supervisor.conf") -> "config/supervisor.conf" :+
      // add dockfile
//      (resourcesPath / "Dockerfile") -> "Dockerfile" :+
      // add bin file
      (resourcesPath / "bin" / "app.sh") -> "bin/app.sh" :+
      (resourcesPath / "entry-point.sh") -> "bin/entry-point.sh"
    // here, add other file based on different env using envPath
    import Path.relativeTo

    // add migration config files
    val migrationPath = (resourcesPath / "db" / "migration")
    //  import Path.relativeTo
    val migrationFinder = (migrationPath ** "*") filter { !_.isDirectory }
    val migrationMappings = (migrationFinder.get pair relativeTo(migrationPath.getParentFile)) map {
      case (file, path) => (file, s"migrate/db/${path}")
    }
    otherMappings ++ migrationMappings
  },

  // add jar
  mappings in Universal := {
    // universalMappings: Seq[(File,String)]
    val universalMappings = (mappings in Universal).value
    val fatJar = (assembly in Compile).value
    // removing means filtering
    val filtered = universalMappings filter {
      case (file, name) =>  ! name.endsWith(".jar")
    }
    // add the fat jar
    filtered :+ (fatJar -> ("lib/" + fatJar.getName))
  },
  // the bash scripts classpath only needs the fat jar
  scriptClasspath := Seq( (assemblyJarName in assembly).value )
)

lazy val dockerSettings = Seq(
  dockerfile in docker := {
    val appDir = stage.value
    val targetDir = "/root/app/fluent"

    new Dockerfile {
      from("openjdk:8")
      expose(9002)
      copy(appDir, targetDir)
      workDir(targetDir)
      entryPoint(s"./bin/entry-point.sh")
      cmd("java", "-cp", "./lib/fluent.jar:./config", "-Duser.timezone=Asia/Shanghai", "app.Demo", "2&1")
    }
  }
)

lazy val rootProject = (project in file("."))
  .settings(
    assemblySettings,
    libraryDependencies ++= scalaDependencies ++ javayDependencies
  )
  .enablePlugins(UniversalPlugin)
  .settings(universalSettings)
  .enablePlugins(sbtdocker.DockerPlugin)
  .settings(dockerSettings)

