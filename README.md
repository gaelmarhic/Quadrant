![Pipeline](https://github.com/gaelmarhic/Quadrant/workflows/Pipeline/badge.svg?branch=master)

Quadrant
========

Quadrant is a Gradle plugin for Android that makes navigation easy in multi-module projects.

There is no magic here. Quadrant simply parses your entire project's `AndroidManifest.xml` files, whichever module they are in, and generates for you a series of constants that contain your `Activities`' classnames.

```kotlin
object QuadrantConstants {
  
  const val MAIN_ACTIVITY: String = "com.gaelmarhic.quadrant.MainActivity"

  const val SECONDARY_ACTIVITY: String = "com.gaelmarhic.quadrant.SecondaryActivity"

  const val TERTIARY_ACTIVITY: String = "com.gaelmarhic.quadrant.TertiaryActivity"
}
```

This way you do not need any direct dependency to any `Activity` class inside of your project to be able to navigate to it. You can just use one of the constants generated by Quadrant in the following way:

```kotlin
val intent = Intent()
intent.setClassName(context, QuadrantConstants.MAIN_ACTIVITY)
startActivity(intent)
``` 

Download
--------

To use Quadrant in your project, add the plugin to your buildscript:

```groovy
buildscript {
    repositories {
        maven {
            url "https://plugins.gradle.org/m2/"
        }
    }
    dependencies {
        classpath "gradle.plugin.com.gaelmarhic:quadrant:$version"
    }
}
``` 

and then apply it in the `build.gradle` file of one of your `Android` modules (`app` or `library`):

```groovy
apply plugin: "com.gaelmarhic.quadrant"
``` 

If you prefer to use the `Plugins DSL`, you have to edit your `settings.gradle`, first, and set the repositories where Gradle should resolve plugins from (needs to be at top of file):

```groovy
pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
    }
}
```

This tells Gradle to resolve plugins from the Gradle plugin repo and from the Google maven repo. This is necessary, because Quadrant depends on the Android Gradle plugin, which can only be resolved from the latter one.

Aftwards you can use the Quadrant plugin with:

```groovy
plugins {
    id "com.gaelmarhic.quadrant" version "$version"
}
```



**ATTENTION**: It is strongly recommended to apply Quadrant to only one module of your project in order to not harm your build time. 
As an example, you could create a `navigation` module which will contain all your navigation-related code and apply Quadrant only to that one.

Prerequisites
-------------

In order for Quadrant to work properly, your project's `AndroidManifest.xml` files must be declared in a Quadrant-suitable way.

This implies that:
- You cannot have duplicated `Activity` names.

Addressability
--------------

By default, and just by applying the plugin, Quadrant will generate a classname constant for each and every `Activity` inside your project.

However, in order to not get polluted by classname constants you may not need, Quadrant provides you with some configuration options.

First of all, you can decide of an `Activity`'s addressability, and therefore whether its classname constant is generated or not, by applying, to its `<activity>` tag or to its parent `<application>` tag, one of the following snippets:

```xml
<meta-data
    android:name="addressable"
    android:value="true" />
```

or

```xml
<meta-data
    android:name="addressable"
    android:value="false" />
```

You can also decide to not generate any constant by default, just by pasting the following snippet in the `build.gradle` file where Quadrant is applied:

```groovy
quadrantConfig {
    generateByDefault false
}
```

Applying an addressable `<meta-data>` directly on an `<activity>` tag will force this `Activity`'s addressability, no matter what the parent `<application>` tag and the `generateByDefault` configuration say.

On the other hand, applying an addressable `<meta-data>` directly on an `<application>` tag will force this `<application>` tag's children `Activities`'s addressability (unless those children `Activities` have an addressable `<meta-data>` themselves), no matter what the `generateByDefault` configuration say. 

Here are a couple of examples with different combinations:

#### Example 1
**build.gradle**
```groovy
quadrantConfig {
    generateByDefault false
}
```

**AndroidManifest.xml**
````xml
<application>

    <activity android:name="com.gaelmarhic.quadrant.MainActivity">

        <meta-data
            android:name="addressable"
            android:value="true" />

    </activity>

    <activity android:name="com.gaelmarhic.quadrant.SecondaryActivity" />

</application>
````

In this example, only the classname constant of the `MainActivity` will be generated.

#### Example 2
**build.gradle**
```groovy
quadrantConfig {
    generateByDefault true
}
```

**AndroidManifest.xml**
````xml
<application>

    <meta-data
        android:name="addressable"
        android:value="false" />

    <activity android:name="com.gaelmarhic.quadrant.MainActivity" />

    <activity android:name="com.gaelmarhic.quadrant.SecondaryActivity" />

    <activity android:name="com.gaelmarhic.quadrant.TertiaryActivity">

        <meta-data
            android:name="addressable"
            android:value="true" />

    </activity>

</application>
````

In this example, only the classname constant of the `TertiaryActivity` will be generated.


**ATTENTION**: You cannot have conflicts of addressable `<meta-data>` for a same `<activity>` tag or a same `<application>` tag, whether it is in the same `AndroidManifest.xml` file or in `AndroidManifest.xml` files from different source sets.

Ignoring `<activity>` tags
------------------------

There may be some cases when you will want or need Quadrant to not take into account some `<activity>` tags at all. For example, this may happen with some third-party libraries that make you declare their `Activity` in your own `AndroidManifest.xml` files.

If this occurs, you just have to paste the following snippet inside the `<activity>` tag that you want or need to ignore:

```xml
<meta-data
    android:name="quadrant"
    android:value="ignore" />
```

#### Example

```xml
<activity android:name="com.gaelmarhic.quadrant.MainActivity">

    <meta-data
        android:name="quadrant"
        android:value="ignore" />

</activity>
```

In this example, `MainActivity` will literally be ignored by Quadrant.

Generated file(s)
-----------------

Quadrant will, by default, generate all the classname constants in a unique file called `QuadrantConstants`.

<img src="https://user-images.githubusercontent.com/16627604/75117741-778ac680-5674-11ea-9946-6038d872dbd0.png" width=300/>

<br>

However, Quadrant provides you with a configuration option that allows you to have the constants generated in different files named after the different modules. You just need to paste the following snippet in the `build.gradle` file where Quadrant is applied:

```groovy
quadrantConfig {
    perModule true
}
```

<img src="https://user-images.githubusercontent.com/16627604/75117768-b15bcd00-5674-11ea-8a46-16ec3db97162.png" width=300/>

License
-------

    Copyright 2020 Gaël Marhic

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
