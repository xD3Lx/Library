# Library

**Instructions to build and run tests**

### MacOS/Unix/Linux

The easiest way to build/test application is by using the official sbt installer

1. Install from [https://www.scala-sbt.org/download.html]() with version 1.3.9
2. Make sure Java is installed on the machine
3. Build project

    `sbt compile`

4. Run tests

    `sbt test`


Alternative way (using standalone script)

1. Install cURL (if it's not installed)

    e.g. for Linux: `sudo apt-get install curl`

2. Install sbt launcher script

    `curl -Lo csbt https://raw.githubusercontent.com/coursier/sbt-extras/master/sbt`
    `chmod +x csbt`

3. Make sure Java is installed on the machine

4. Build project

    `./csbt compile`

5. Run tests

    `./csbt test`


### Windows