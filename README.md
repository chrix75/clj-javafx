# clj-javafx

A wrapper to develop application with Clojure and JavaFX. This project lets you manipulate JavaFX in the REPL.

## Prerequisites

To use this project, you need to have JavaFX 2.x on your computer. For that, you need to install Java JDK v7 (now, it contains JavaFX) or install only JavaFX.

For that, go to http://www.oracle.com/technetwork/java/javafx/downloads/index.html

## Maven configuration

There is no Maven repository with JavaFX 2. So, you need to install it by yourself in your local Maven repository. It's easy, once you installed JavaFX 2 on your computer, you search the path of the file jfxrt.jar

In my case, this file is in the directory /usr/lib/jvm/jdk1.7.0/jre/lib and I use the JavaFX 2.2.3
Then, I call this command 
```
mvn install:install-file -DgroupId=local.oracle -DartifactId=javafxrt -Dversion=2.2.3 -Dpackaging=jar -Dfile=/usr/lib/jvm/jdk1.7.0/jre/lib/jfxrt.jar
```

Thus, in my project config file project.clj, I use this dependency local.oracle/javafxrt "2.2.3"

## Installation

Fetch the code, either by downloading the archive or by forking the repository.

## See one example

Go to the project folder and run

```
lein test
```

## Usage in REPl

Code example (in your REPL, you can remove comments):

```clojure
;; An example of use of the Clojure Javafx wrapper.
;;
;; Note: This example code was written before any development on the wrapper.
;; It's to validate the ideas and how it should be used.


; The first need is to launch the JavaFX thread.
; This launching is interesting if we have a scene for displaying.

(require 'clj-javafx.wrapper) ; you could use the use function to avoid repeat the namespace for each call
(import 'javafx.scene.control.Button)
(import 'javafx.scene.paint.Color)

(clj-javafx.wrapper/launch :width 300 :height 300)

; NOTE: The launch method doesn't show the scene. Thus, the caller can make all the changes he wants before displaying.

; show the main window
(clj-javafx.wrapper/show)

; get the primary stage (aka the one is passed in the hood to the start function of the extended Application class
@clj-javafx.wrapper/primary-stage

; get the root group (the one is created in the launch method to contain the scene
@clj-javafx.wrapper/primary-root

; get the primary scene
@clj-javafx.wrapper/primary-scene

; interact with the JavaFX environment
(clj-javafx.wrapper/with-javafx
  (let [btn (Button.)]
    (.setLayoutX btn 100)
    (.setLayoutY btn 150)
    (.setText btn "Hello World!")
    (clj-javafx.wrapper/add-child @clj-javafx.wrapper/primary-root btn)))

; in the following call, the let variable is defined as a function argument
(clj-javafx.wrapper/with-javafx-let
  [btn (Button.)]
  (.setLayoutX btn 100)
  (.setLayoutY btn 80)
  (.setText btn "Hello World!")
  (clj-javafx.wrapper/add-child @clj-javafx.wrapper/primary-root btn))

```

## Other calls of clj-javafx.wrapper/launch

```clojure
(clj-javafx.wrapper/launch) ; the scene is created but we can't see it because its size is set to 0
(clj-javafx.wrapper/launch :width 300 :height 300)
(clj-javafx.wrapper/launch :width 300 :height 300 :depth-buffer true)
(clj-javafx.wrapper/launch :width 300 :height 300 :paint Color/BLACK)
(clj-javafx.wrapper/launch :paint Color/BLACK)
```


### Issues

You cannot launch javafx.application.Application more one time. The problem in REPL is when launchapp was call once, if you want to call it again you need to restart your REPL session.

### Might be Useful

Use JavaFX in REPL is useful but don't forget you can use clj-javafx to develop your real application. 

## License

Eclipse Public License
http://www.eclipse.org/legal/epl-v10.html

Copyright © 2012 Christian Sperandio

Distributed under the Eclipse Public License, the same as Clojure.
