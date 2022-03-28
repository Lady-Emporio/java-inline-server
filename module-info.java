module T_2022_01_10 {
	requires ormlite.jdbc;
	requires java.sql;
	requires thymeleaf;
	requires slf4j.api;
	requires java.logging;
	
	 exports app.models;
	 exports app.server;
	 opens app.models;
}