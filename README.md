# PebbleGame
Repository to hold source code for implementing Pebble game for 2nd year project for software development module

Using JUnit version 4.12

## Compile

* `cd test/com/company`
* `javac -cp .:"./*" PebbleGameTest.java`
* `javac -cp .:"./*" BagTest.java`
* `cd ..`
* `cd ..`

## Run tests

* `java -cp .:test/example_file_2.txt:./com/company/*:./com/* org.junit.runner.JUnitCore com.company.PebbleGameTest`
* `java -cp .:test/example_file_2.txt:./com/company/*:./com/* org.junit.runner.JUnitCore com.company.BagTest`

## Test Results
For the PebbleGameTest should get OK (11 tests)
For the BagTest should get OK (4 tests)
