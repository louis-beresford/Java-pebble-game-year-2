# PebbleGame
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

## Test Resul
For the PebbleGameTest should get OK (11 tests)
For the PebbleGameTest should get OK (4 tests)
