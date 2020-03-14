# API change detection

Only signature level changes are detected. Changed annotations, attributes, javadocs, etc. are
ignored as they don't affect the contract. 

## Definitions

Throughout this document words _previous_ and _current_ will be used to refer to two versions
of the public API compared, where _current_ version replaces _previous_ one.

It is not required that _current_ version is higher than _previous_, however this is probably 
the most popular use case.

Only public classes, fields and methods are inspected.

## Notes

This is an MVP, including only detection of basic, on-the-surface, changes. No deeper
inspection is performed and this may results in API changes rated higher in the severity
than actual change is or false positives.
<table>
<tr><th>Previous</th><th>Current</th></tr>
<tr>
<td>

```java
interface A {}
class B implements A {}

class C {
  void method(B b);
}
```
</td>
<td>

```java
interface A {}
class B implements A {}

class C {
  void method(A b);
}
```
</td>
</tr>
</table>

All the calls to `C.method(...)` in the new version are still compatible with the existing source,
with `A` being a superclass for `B`. This however might be not easy to detect if new levels of inheritance
where introduced between A and B, especially if A and its descendants are declared in 3rd party library, that
is not available at the time of analysis.
As the change of parameter type from `B` to `A` changes method signature it will be detected as breaking change, 
as the old method no longer exist.


## Severity levels

### Breaking

- Classes
  - [x] Changing superclass of a class
  - [x] Removing implemented interfaces
  - [x] Changing class visibility to a stricter one
  - [x] Changing class modifiers static, final, abstract 
- Fields. Fields are identified solely by their name. Two fields with the same name are considerd the same. 
  - [x] Removing a field (including changing visibility to more restricted)
  - [x] Changing field type
  - [x] Changing field visibility to a stricter one
  - [x] Changing field modifier
- Methods. Methods are identified by complete signature.
  - [ ] Removing a method - name with the same name and number and type of parameters does not exist. 
  - [ ] Changing method return type 
  - [ ] Changing method visibility to a stricter one
  - [ ] Changing method modifier  

### Warning

- Class
    - [x] Changing class bytecode version
    - [ ] Annotations changed
- Fields
    - [ ] Annotations changed
- Methods 
    - [ ] Annotations changed

### Safe

- Class
    - [x] Implementing new interfaces
    - [ ] Adding field
    - [ ] Adding method