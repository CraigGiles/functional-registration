lazy val databaseUrl = sys.env.getOrElse("DB_DEFAULT_URL", "jdbc:mysql://127.0.0.1:32771/test")
lazy val databaseUser = sys.env.getOrElse("DB_DEFAULT_USER", "root")
lazy val databasePassword = sys.env.getOrElse("DB_DEFAULT_PASSWORD", "root")

lazy val jdbcDriver = "com.mysql.jdbc.Driver"
lazy val slickDriver = slick.driver.MySQLDriver
lazy val outputPackage = "com.gilesc.dataaccess"

slickCodegenSettings
slickCodegenDatabaseUrl := databaseUrl
slickCodegenDatabaseUser := databaseUser
slickCodegenDatabasePassword := databasePassword
slickCodegenDriver := slickDriver
slickCodegenJdbcDriver := jdbcDriver
slickCodegenOutputPackage := outputPackage
slickCodegenExcludedTables := Seq("schema_version")

sourceGenerators in Compile <+= slickCodegen
// sourceGenerators in Compile += { slickCodegen }
