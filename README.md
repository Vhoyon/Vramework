[![Build Status](https://travis-ci.com/Vhoyon/Vramework.svg?branch=dev)](https://travis-ci.com/Vhoyon/Vramework) [![Version](https://img.shields.io/maven-central/v/io.github.vhoyon/vramework.svg?label=version)](https://search.maven.org/artifact/io.github.vhoyon/vramework) 
[![version (SNAPSHOT)](https://img.shields.io/nexus/s/https/oss.sonatype.org/io.github.vhoyon/vramework.svg?label=version%20(SNAPSHOT)&colorB=e60000)](https://oss.sonatype.org/#nexus-search;quick~vramework)


# Vramework

Vramework is a framework for creating Discord bots using [DV8FromTheWorld's JDA](https://github.com/DV8FromTheWorld/JDA) as its backend.

This framework can :
- manage commands simply by creating a new class in a package;
- deal with different languages;
- handle settings;
- create an automatic help list;

and much more!

## Getting Started

These instructions will allow you to start writing up your own bot using our framework.

### Prerequisites

You will require Maven and Java >1.8 to use our framework.

Maven could be installed through most Java IDE : your mileage may vary.

### Installing

#### Maven

```xml
<dependency>
  <groupId>io.github.vhoyon</groupId>
  <artifactId>vramework</artifactId>
  <version>LATEST</version>
</dependency>
```

(you can switch `LATEST` to the version number up top)

#### TODO : Describe how to start up with this framework.
We know, it's quite important. We'll do it ASAP! :)

## Built With

- [JDA](https://github.com/DV8FromTheWorld/JDA) - The Java Discord APIs to allow a bot to run in Java
- [Maven](https://maven.apache.org/) - Dependency Management
- [ClassGraph](https://github.com/classgraph/classgraph) - Used to find Commands without declaring them

## GitFlow

We use a slightly modified [GitFlow](https://www.atlassian.com/git/tutorials/comparing-workflows/gitflow-workflow) methodology, where our modifications types are used when creating branches from the `dev` branch. For example, creating a `README.md` file means creating a branch from `dev` named `tasks/README`.

## ZenHub

We use [ZenHub](https://www.zenhub.com/) to easily manage our [issues](https://github.com/Vhoyon/Vramework/issues). If you haven't tried it yet, we highly recommend it, inline GitHub integrations for issues management is really useful!

## Authors

- Guillaume Marcoux ([V-ed](https://github.com/V-ed)) - Owner and main maintainer
- Stephano Mehawej ([StephanoMehawej](https://github.com/StephanoMehawej)) - Maintainer

See also the list of [contributors](https://github.com/Vhoyon/Vramework/contributors) who participated in this project.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details
