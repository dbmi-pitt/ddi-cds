compile:
	mvn clean
	mvn install


run: compile
	java -jar target/droolstest-1.0.jar
