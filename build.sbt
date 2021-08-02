name := "doobie-examples"

scalaVersion := "2.13.5"

lazy val doobieVersion 	    = "0.12.1"
lazy val fs2Version 	      = "2.5.9"
lazy val catsVersion 	      = "2.6.1"
lazy val catsEffectVersion  = "2.5.1"

libraryDependencies ++= Seq(
  "org.tpolecat"   %% "doobie-core"     % doobieVersion,
  "org.tpolecat"   %% "doobie-postgres" % doobieVersion,
  "org.tpolecat"   %% "doobie-specs2"   % doobieVersion,
  "org.tpolecat"   %% "doobie-quill"    % doobieVersion,
  "co.fs2"         %% "fs2-core"        % fs2Version,
  "org.typelevel"  %% "cats-core"       % catsVersion,
  "org.typelevel"  %% "cats-free"       % catsVersion,
  "org.typelevel"  %% "cats-effect"     % catsEffectVersion,
)
