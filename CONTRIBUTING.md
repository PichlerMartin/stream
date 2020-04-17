# How to Contribute

Due to the fact that our diploma thesis was officially finished in april 2020, foreign coders are now free to contribute to our project in any way. If you'd like to contribute your own features and patches to our application, please use the standard-git features to do so, such as pushing, opening issues and branching.

## Contributor License Agreement

Contributions to this project must be accompanied by a Contributor License
Agreement. You (or your employer) retain the copyright to your contribution;
this simply gives us permission to use and redistribute your contributions as
part of the project.

You generally only need to submit a CLA once, so if you've already submitted one
(even if it was for a different project), you probably don't need to do it
again.

## Code reviews

All submissions, including submissions by project members, require review. We
use GitHub pull requests for this purpose. Consult
[GitHub Help](https://help.github.com/articles/about-pull-requests/) for more
information on using pull requests.

## Building the project

To build, package, and run all unit tests run the command

```
mvn clean verify
```

## Code Samples

Code Samples must be bundled in separate Maven modules, and guarded by a
Maven profile with the name `enable-samples`.

The samples must be separate from the primary project for a few reasons:
1. Primary projects have a minimum Java version of Java 7 whereas samples have
   a minimum Java version of Java 8. Due to this we need the ability to
   selectively exclude samples from a build run.
2. Many code samples depend on external GCP services and need
   credentials to access the service.
3. Code samples are not released as Maven artifacts and must be excluded from 
   release builds.
   
### Building

We recommend you to build the project within your IntelliJ IDEA IDE. In order to
make it work properly, you need run UI_Main from the package testui, which can be
found in the sources directory.

#### Maven `settings.xml`

To activate the Maven profile in your `~/.m2/settings.xml` add an entry of
`enable-samples` following the instructions in [Active Profiles][2].

This method has the benefit of applying to all projects you build (and is
respected by IntelliJ IDEA) and is recommended if you are going to be
contributing samples to several projects.

#### IntelliJ IDEA

To activate the Maven Profile inside IntelliJ IDEA, follow the instructions in
[Activate Maven profiles][3] to activate `enable-samples`.

[1]: https://maven.apache.org/settings.html#Active_Profiles
[2]: https://www.jetbrains.com/help/idea/work-with-maven-profiles.html#activate_maven_profiles
