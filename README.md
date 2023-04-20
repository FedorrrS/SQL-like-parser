# SQL-like-language-parser

## Introduction

[Source document with terms of reference](https://docs.google.com/document/d/1QNtu5L3ppvNF-o06ho7eU4jm1R2MQK-For_DWnNZRRE/edit)

Implement some semblance of a data management language for the collection. The basic commands that must be supported are inserting items into the collection, removing items from the collection, searching for items in the collection, and changing items in the collection.
The structure of the collection is **predefined**.

**Task description:**
A data collection is a structure which is a data table with column names and each row in the table is an element of the collection.
A method is implemented that receives a command in the form of a string. The command must perform the four basic operations: _insert_, _update_, _select_, _delete_.
Also, commands support sample conditions from the collection.

## Request examples

- _Insert_ <br/>
  execute("Insert Values 'lastName' = 'Stankevich', 'age' = 42, 'id' = 1, 'active' = false", 'cost' = 0.74)

- _Update_ <br/>
  execute("update values 'cost' = 1.5 where 'age' > 20")

- _Select_ <br/>
  execute("select where 'id' < 5")

- _Delete_ <br/>
  execute("delete where 'active' =false and 'cost' > 2.3")

## Code style features

- _toString()_ <br/>
  toString() method is replaced by more efficient _string + ""_ concatenation <br/>
  sources ([stackoverflow](https://stackoverflow.com/questions/15669068/fastest-way-of-converting-integer-to-string-in-java), [happycoders](https://www.happycoders.eu/java/how-to-convert-int-to-string-fastest/))

## Features need to be implemented

- _Add up to 5 predicate support to **where**_

## Author

Fedor Selishchev

- https://github.com/FedorrrS
- https://t.me/FedorSelishchev
