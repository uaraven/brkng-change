# Brkng Change

Java api change detection. Detects changes in API between different versions of the jar files and
generates reports with indication of potential breaking changes in the API.

This is a **POC** only, I found myself not working on it anymore, so I decided to open it up.

## Idea

This tool is for library builders and for library users. 

Run it comparing previous version jar with new version jar and find out if you need to increase minor version number or just patch number.

Get a new version of library you use? Compare it to the current one to see if it will break API, before you update
dependency version.

Detected changes

| Level       | Description                                           | Severity |
|:-----------:|:------------------------------------------------------|:---------|
| Class       |                                                       |          |
|             | Changing superclass of a class                        | Breaking |
|             | Removing implemented interfaces                       | Breaking |
|             | Changing class visibility to a stricter one           | Breaking |
|             | Changing class modifiers static, final, abstract      | Breaking |
|             | Changing class bytecode version                       | Warning  |
|             | Annotations changed                                   | Warning  |
|             | Super class changed to subclass of previous one       | Warning  |
|             | Implementing new interfaces                           | Safe     |
|             | Adding field                                          | Safe     |
|             | Adding method                                         | Safe     |
| Fields*     |                                                       |          |
|             | Removing a field                                      | Breaking |
|             | Changing field type                                   | Breaking |
|             | Changing field visibility to a stricter one           | Breaking |
|             | Changing field modifier                               | Breaking |
|             | Changing annotations                                  | Warning  |
|             | Type changed to a subclass of previous                | Warning  |
| Methods**   |                                                       |          |
|             | Removing a method                                     | Breaking |  
|             | Changing method return type                           | Breaking | 
|             | Changing method visibility to a stricter one          | Breaking |
|             | Changing method modifier                              | Breaking |  
|             | Exception list changed                                | Breaking |
|             | Annotations changed                                   | Warning  |
|             | Return type changed to a subclass of previous one     | Warning  |

Notes:
 - *Fields are identified by names only. `int field` and `float field` are treated as the same field.
 - **Methods are identified by a signature
 - Only public APIs are inspected 
 
 
## Usage

Included CLI tool can compare local jars or two jars by their GAVs (groupId, artifactId, version). It can automatically
pull jars from maven-compatible repo and run comparison. Default repo is maven central, but it can be changed.  