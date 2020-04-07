# Library

**Instructions to build and run tests**

Before trying to build, make sure Java is installed on the machine and configured in PATH. </br>
You can verify the installation by running command `java -version`. </br>

The easiest way to build/test application is by using the official sbt ditribution:

1. Install from [https://www.scala-sbt.org/download.html]() with version 1.3.9
   
   Note: For Windows system it's recommended to use msi-installer

2. Navigate to the project directory in the terminal window depending on the operation system in use

3. Build project

    `sbt compile`

4. Run tests

    `sbt test`


Alternative way (using standalone script)

1. If you're using Windows, first install Git for Windows from [https://git-scm.com/download/win](), which comes with Git Bash - terminal enabling to run bash scripts.

2. Open terminal window depending on the operation system in use (e.g. for Windows - Git Bash)
   
3. Install cURL (if it's not installed). You can verify the installation by running `curl --version`

4. Navigate to the project directory

5. Install sbt launcher script

    `curl -Lo csbt https://raw.githubusercontent.com/coursier/sbt-extras/master/sbt`
    
    `chmod +x csbt`

6. Build project

    `./csbt compile`

7. Run tests

    `./csbt test`
