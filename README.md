# Weelorum Kotlin Style Guide

You should also check out our other style guides too:

-   [Swift](https://github.com/Weelorum/Weelorum-Swift-Style-Guide)
-   [Flutter](https://github.com/Weelorum/Weelorum-Flutter-Style-Guide)
-   [About Weelorum](#about-weelorum)
-   [Weelorum Blog](https://weelorum.com/blog/)

## Inspiration

The language guidance is drawn from:

-   The [Android Kotlin style guide](https://android.github.io/kotlin-guides/style.html)
-   The [Kotlin Coding Conventions](https://kotlinlang.org/docs/reference/coding-conventions.html)
-   The [Android contributors style guide](https://source.android.com/source/code-style.html)
-   The [Google Java Style Guide](https://google-styleguide.googlecode.com/svn/trunk/javaguide.html).

## Android Studio Coding Style

It is possible to get Android Studio to adhere to these style guidelines, via a rather complex sequence of menus. To make it easier, we've provided a coding style that can be imported into Android Studio.

The file can be found in current GIT.

To install the file, open Android Studio Settings and go to **Editor > Code Style > Kotlin**, then click the gear menu and choose **Import Scheme...**.

From now on, projects you create _should_ follow the correct style guidelines.

## Table of Contents

-   [Nomenclature](#nomenclature)
    -   [Packages](#packages)
    -   [Classes & Interfaces](#classes--interfaces)
    -   [Methods](#methods)
    -   [Fields](#fields)
    -   [Variables & Parameters](#variables--parameters)
    -   [Misc](#misc)
-   [Declarations](#declarations)
    -   [Visibility Modifiers](#visibility-modifiers)
    -   [Fields & Variables](#fields--variables)
    -   [Classes](#classes)
    -   [Data Type Objects](#data-type-objects)
    -   [Enum Classes](#enum-classes)
    -   [Expressions](#expressions)
-   [Spacing](#spacing)
    -   [Indentation](#indentation)
    -   [Line Length](#line-length)
    -   [Vertical Spacing](#vertical-spacing)
-   [Semicolons](#semicolons)
-   [Getters & Setters](#getters--setters)
-   [Brace Style](#brace-style)
-   [When Statements](#when-statements)
-   [Functions](#functions)
    -   [Named arguments](#named-arguments)
    -   [Functional type variables](#functional-type-variables)
    -   [Lambda Expressions Formatting](#lambda-expressions-formatting)
    -   [Composable functions](#composable-functions)
-   [Annotations](#annotations)
-   [Types](#types)
    -   [Type Inference](#type-inference)
    -   [Constants vs. Variables](#constants-vs-variables)
    -   [Companion Objects](#companion-objects)
    -   [Nullable type](#nullable-types)
    -   [Nullable boolean check](#nullable-boolean-check)
-   [Android Specific](#android-specific)

*   [Localization](#localization)
*   [About Weelorum](#about-weelorum)

-   [Code Examples](#code-examples)

## Nomenclature

On the whole, naming should follow Java standards, as Kotlin is a JVM-compatible language.

### Packages

Package names are similar to Java: all **lower-case**, multiple words concatenated together, without hypens or underscores:

**BAD**:

```kotlin
com.Weelorum.name_project
```

**GOOD**:

```kotlin
com.weelorum.nameproject
```

### Classes & Interfaces

Written in **UpperCamelCase**. For example `RadialSlider`.

### Methods

Written in **lowerCamelCase**. For example `setValue`.

### Fields

Generally, written in **lowerCamelCase**. Fields should **not** be named with Hungarian notation, as Hungarian notation is [erroneously thought](http://jakewharton.com/just-say-no-to-hungarian-notation/) to be recommended by Google.

Example field names:

```kotlin
class MyClass {
  var publicField: Int = 0
  val person = Person()
  private var privateField: Int?
}
```

Constant values in the companion object should be written in **uppercase**, with an underscore separating words:

```kotlin
companion object {
  const val THE_ANSWER = 42
}
```

### Variables & Parameters

Written in **lowerCamelCase**.

Single character values must be avoided, except for temporary looping variables.

When declaring constants, fields or function arguments, it is recommended to additionally specify the dimension if the context or name of the function does not provide an unambiguous understanding of their purpose:

**BAD:**

```kotlin
const val TIMEOUT = 1000L
const val PADDING = 24

fun someFunction(timeout: Long)

val defaultTimeout get() = 1000L
```

**GOOD:**

```kotlin
const val TIMEOUT_MILLIS = 1000L
const val PADDING_DP = 24

val TIMEOUT = 1000.milliseconds
val PADDING = 24.dp

fun preferGoodNames(timeoutMillis: Long)

val defaultTimeoutMillis
    get() = 1000L
```

## Functions

### Named arguments

If there is more than one argument in the function, it should be named. If the purpose of an argument is not clear from the context, it should be named also.

```kotlin
runOperation(
    method = operation::run,
    consumer,
    errorHandler,
    tag,
    cacheSize = 3,
    cacheMode
)

// BAD:
calculateSquare(6, 19)

// GOOD:
calculateSquare(
  x = 6,
  y = 19
)

getCurrentUser(skipCache = false)
setProgressBarVisible(true)
```

If there are more than 2 named arguments on one line, each argument should be moved to a new line (as in the example above).

It is necessary to name all lambdas accepted by a function as arguments (except when the lambda is outside parentheses) so that the purpose and responsibility of each lambda is clear when reading the code.

```kotlin
editText.addTextChangedListener(
    onTextChanged = { text, _, _, _ ->
        viewModel.onTextChanged(text?.toString())
    },
    afterTextChanged = { text ->
        viewModel.onAfterTextChanged(text?.toString())
    }
)
```

Arguments of the same types must be named to avoid accidentally mixing them up.

```kotlin
val startDate: Date = ..
val endDate: Date = ..
compareDates(startDate = startDate, endDate = endDate)
```

Arguments should be named when passing `null`:

```kotlin
navigateTo(
  screen = screenName,
  arguments = null
)
```

### Functional type variables

It is allowed to call a lambda with either `invoke` or the shortened version `()` if there is no agreement within the project. However, explicit `invoke` has several advantages:

> [!TIP]
> One of the main reasons for using explicit `invoke` is the conceptual separation of a function as a class member and a lambda as an input parameter to a function..
> The use of `invoke` clearly indicates that a lambda is being used, not a function.
>
> An additional argument for using `invoke` is its visibility. When calling a lambda without `invoke`, it can lose parentheses at the call site, which will lead to incorrect behavior.

```kotlin
@Composable
fun CardContent(
  header: @Composable LazyItemScope.() -> Unit,
  body: @Composable LazyListScope.() -> Unit,
) {
  LazyColumn {
    item(content = header)

    // Bad
    body
    // Good
    body()
    body.invoke(this@LazyColumn)
  }
}
```

### Lambda Expressions Formatting

It's better to pass the method by reference:

```kotlin
ItemState(item, onItemClickListener = ::onQuestClicked)
```

When writing a lambda expression on more than one line, always use a named argument instead of `it`:

```kotlin
StateHandler(
    state,
    onClickListener = { item ->
        Log.d(..)
        viewModel.onItemClicked(item)
    }
)
```

Always replace unused lambda expression parameters with the `_` character.

## Composable functions

Writing `Composable`-functions you should follow the [official docs](https://developer.android.com/develop/ui/compose/mental-model).

Composable function should be written in `UpperCamelCase`. For example RadialSlider. All other cases described for methods and functions are applicable for `Composable`-functions too.

Unless there are additional project agreements, previews for the `Composable` function are not required and may be added at the developer's discretion.

### Misc

In code, acronyms should be treated as words. For example:

**BAD:**

```kotlin
XMLHTTPRequest
URL: String?
findPostByID
```

**GOOD:**

```kotlin
XmlHttpRequest
url: String
findPostById
```

## Declarations

### Visibility Modifiers

Only include visibility modifiers if you need something other than the default of public.

**BAD:**

```kotlin
public val wideOpenProperty = 1
private val myOwnPrivateProperty = "private"
```

**GOOD:\***

```kotlin
val wideOpenProperty = 1
private val myOwnPrivateProperty = "private"
```

### Access Level Modifiers

Access level modifiers should be explicitly defined for classes, methods and member variables.

### Fields & Variables

Prefer single declaration per line.

**GOOD:**

```kotlin
username: String
twitterHandle: String
```

### Classes

Exactly one class per source file, although inner classes are encouraged where scoping appropriate.

### Data Type Objects

Prefer data classes for simple data holding objects.

**BAD:**

```kotlin
class Person(val name: String) {
  override fun toString() : String {
    return "Person(name=$name)"
  }
}
```

**GOOD:**

```kotlin
data class Person(val name: String)
```

### Enum Classes

Enum classes should be avoided where possible, due to a large memory overhead. Static constants are preferred. See [docs](http://developer.android.com/training/articles/memory.html#Overhead) for further details.

Enum classes without methods may be formatted without line-breaks, as follows:

```kotlin
enum class CompassDirection { EAST, NORTH, WEST, SOUTH }
```

Each enum should be _all uppercased_.

### Expressions

When wrapping a method call chain to a new line, the . symbol or the ?. operator are wrapped to the next line, while property is allowed to remain on one line:

```kotlin
val collectionItems = source.collectionItems
    ?.dropLast(10)
    ?.sortedBy { it.progress }
```

Elvis operator ?: in a multi-line expression also wraps to a new line:

```kotlin
val throwableMessage: String = throwable?.message
    ?: DEFAULT_ERROR_MESSAGE

throwable.message?.let { showError(it) }
    ?: showError(DEFAULT_ERROR_MESSAGE)
```

If there is a multi-line lambda before the elvis operator ?:, it is advisable to move the lambda as well:

**BAD:**

```kotlin
result.value?.let { message ->
   ...
   proceed(message)
}
   ?: proceed(DEFAULT_MESSAGE)
```

**GOOD:**

```kotlin
result.value
    ?.let { message ->
       ...
       proceed(message)
    }
    ?: proceed(DEFAULT_MESSAGE)
```

When declaring a variable with a delegate that does not fit on one line, leave the declaration with the opening curly brace on one line, moving the rest of the expression to the next line:

```kotlin
private val item: Item by lazy {
    loadItem() as Item
}
```

## Spacing

Spacing is especially important, as code needs to be easily readable as part of the tutorial.

### Indentation

Indentation is using spaces - never tabs.

#### Blocks

Indentation for blocks uses 2 spaces (not the default 4):

**BAD:**

```kotlin
for (i in 0..9) {
    Log.i(TAG, "index=" + i);
}
```

**GOOD:**

```kotlin
for (i in 0..9) {
  Log.i(TAG, "index=" + i)
}
```

#### Line Wraps

Indentation for line wraps should use 4 spaces (not the default 8):

**BAD:**

```kotlin
val widget: CoolUiWidget =
        someIncrediblyLongExpression(that, reallyWouldNotFit, on, aSingle, line)
```

**GOOD:**

```kotlin
val widget: CoolUiWidget =
    someIncrediblyLongExpression(that, reallyWouldNotFit, on, aSingle, line)
```

### Line Length

Lines should be no longer than _100 characters long_.

### Vertical Spacing

There should be exactly one blank line between methods to aid in visual clarity and organization. Whitespace within methods should separate functionality, but having too many sections in a method often means you should refactor into several methods.

## Semicolons

Semicolons ~~are dead to us~~ should be avoided wherever possible in Kotlin.

**BAD:**:

```kotlin
val horseGiftedByTrojans = true;
if (horseGiftedByTrojans) {
  bringHorseIntoWalledCity();
}
```

**GOOD:**:

```kotlin
val horseGiftedByTrojans = true
if (horseGiftedByTrojans) {
  bringHorseIntoWalledCity()
}
```

## Getters & Setters

Unlike Java, direct access to fields in Kotlin is preferred.

If custom getters and setters are required, they should be declared [following Kotlin conventions](https://kotlinlang.org/docs/reference/properties.html) rather than as separate methods.

## Brace Style

Only trailing closing-braces are awarded their own line. All others appear the same line as preceding code:

**BAD:**

```kotlin
class MyClass
{
  fun doSomething()
  {
    if (someTest)
    {
      // ...
    }
    else
    {
      // ...
    }
  }
}
```

**GOOD:**

```kotlin
class MyClass {
  fun doSomething() {
    if (someTest) {
      // ...
    } else {
      // ...
    }
  }
}
```

Conditional statements are always required to be enclosed with braces, irrespective of the number of lines required.

**BAD:**

```kotlin
if (someTest)
  doSomething()
if (someTest) doSomethingElse()
```

**GOOD:**

```kotlin
if (someTest) {
  doSomething()
}
if (someTest) { doSomethingElse() }
```

## When Statements

Unlike `switch` statements in Java, `when` statements do not fall through. Separate cases using commas if they should be handled the same way. Always include the else case.

**BAD:**

```kotlin
when (anInput) {
  1 -> doSomethingForCaseOne()
  2 -> doSomethingForCaseOneOrTwo()
  3 -> doSomethingForCaseThree()
}
```

**GOOD:**

```kotlin
when (anInput) {
  1, 2 -> doSomethingForCaseOneOrTwo()
  3 -> doSomethingForCaseThree()
  else -> println("No case satisfied")
}
```

It is good practice to use the `when` operator to handle multiple branches without parameters, and to handle boolean values instead of `if/else`:

```kotlin
val text: String = ...

when {
  text.startsWith(PREFIX_1) -> doSomethingForCaseOne()
  text.startsWith(PREFIX_2) -> doSomethingForCaseTwo()
  else -> println("No case satisfied")
}


val isEnabled: Boolean = ...
when (isEnabled) {
  true -> {
    Log.d()
    // some multiline code
  }
  false -> {
      // ...
  }
}

```

## Annotations

Annotations should be located above the description of the class/field/method to which they apply.

If multiple annotations are applied to a class/field/method, place each annotation on a new line:

```kotlin
@Singleton
@Provides
fun providesAppPrefManager(): AppPrefManager {
    ...
}
```

Annotations on arguments in a class constructor or function declaration can be written on the same line as the corresponding argument.  
In this case, if there are several annotations for one argument, then all annotations are written on a new line, and the corresponding argument is separated from the others at the top and bottom by empty lines.

```kotlin
data class UserDto (
    @SerializedName("firstName") val firstName: String? = null,
    @SerializedName("secondName") val secondName: String? = null
)

@Entity(tableName = "users")
data class UserEntity (
    @PrimaryKey val id: Int,

    @SerializedName("firstName")
    @ColumnInfo(name = "firstName")
    val firstName: String? = null,

    @SerializedName("secondName")
    @ColumnInfo(name = "secondName")
    val secondName: String? = null
)
```

## Types

Always use Kotlin's native types when available. It's preferably to use Kotlin collections rather than Java-native collections. More about Kotlin Collections you can find in [official docs](https://kotlinlang.org/docs/collections-overview.html).

### Type Inference

Type inference should be preferred where possible to explicitly declared types.

**BAD:**

```kotlin
val something: MyType = MyType()
val meaningOfLife: Int = 42
```

**GOOD:**

```kotlin
val something = MyType()
val meaningOfLife = 42
```

### Constants vs. Variables

Constants are defined using the `val` keyword, and variables with the `var` keyword. Always use `val` instead of `var` if the value of the variable will not change.

_Tip_: A good technique is to define everything using `val` and only change it to `var` if the compiler complains!

### Companion Objects

Object declarations and object expressions are best used for scenarios when:

-   _Using singletons for shared resources:_ You need to ensure that only one instance of a class exists throughout the application. For example, managing a database connection pool.

-   _Creating factory methods:_ You need a convenient way to create instances efficiently. Companion objects allow you to define class-level functions and properties tied to a class, simplifying the creation and management of these instances.

-   _Modifying existing class behavior temporarily:_ You want to modify the behavior of an existing class without the need to create a new subclass. For example, adding temporary functionality to an object for a specific operation.

-   _Storing constants:_ all the project constants should be stored in the respective objects to avoid using hardcoded values:

**BAD:**

```kotlin
data class BaseResponse (
  @SerializedName("data")
  val data: T?,

  @SerializedName("error")
  val error: String? = null
)
```

**GOOD:**

```kotlin
object ApiFields {
    const val data = "data"
    const val error = "error"
}

data class BaseResponse (
  @SerializedName(ApiFields.data)
  val data: T?,

  @SerializedName(ApiFields.error)
  val error: String? = null
)
```

### Nullable Types

Declare variables and function return types as nullable with `?` where a `null` value is acceptable.

Use implicitly unwrapped types declared with `!!` only for instance variables that you know will be initialized before use, such as subviews that will be set up in `onCreate` for an Activity or `onCreateView` for a Fragment.

When naming nullable variables and parameters, avoid naming them like `nullableString` or `maybeView` since their nullability is already in the type declaration.

When accessing a nullable value, use the safe call operator if the value is only accessed once or if there are many nullables in the chain:

```kotlin
editText?.setText("foo")
```

Use `orEmpty()` for collections and strings:

**BAD:**

```kotlin
// Bad
nullableString ?: ""
someList ?: emptyList()
```

**GOOD:**

```kotlin
nullableString.orEmpty()
nullableObject?.toString().orEmpty()
someList.orEmpty {
    // something
}
```

### Nullable boolean check

When checking for a nullable boolean, instead of adding `?: false` in the condition, explicitly check `boolean == true`.  
This is one of the common [Kotlin idioms](https://kotlinlang.org/docs/idioms.html#nullable-boolean).

**BAD:**

```kotlin
val b: Boolean? = ...
if (boolean ?: false) {
    ...
} else {
    // `b` is false or null
}
```

**GOOD:**

```kotlin
val b: Boolean? = ...
if (b == true) {
    ...
} else {
    // `b` is false or null
}
```

## Android Specific

It is required to annotate the parameter with the `@StringRes` or `@DrawableRes` annotation and to add the `Res` suffix to the parameter name, while using the project resource reference as the function / method argument or as the class property:

**BAD:**

```kotlin
@Composable
fun CartItem(
    title: Int,
    icon: Int,
    value: Double
)
```

**GOOD:**

```kotlin
@Composable
fun CartItem(
  @StringRes titleRes: Int,
  @DrawableRes iconRes: Int,
  value: Double
)
```

The same is applied to function names that return the resource reference:

**BAD:**

```kotlin
enum class IconType { HOME, GARDEN, LIVING_ROOM }

fun IconType.toIcon(): Int {
    return when (this) {
      HOME -> R.drawable.ic_home
      GARDEN -> R.drawable.ic_garden
      LIVING_ROOM -> R.drawable.ic_living_room
    }
}

```

**GOOD:**

```kotlin
enum class IconType { HOME, GARDEN, LIVING_ROOM }

fun IconType.toIconRes(): Int {
    return when (this) {
      HOME -> R.drawable.ic_home
      GARDEN -> R.drawable.ic_garden
      LIVING_ROOM -> R.drawable.ic_living_room
    }
}

```

## Localization

For application localization standard Android approach is used.

## Code Examples

You can find practical code examples in the [code-examples](./code-examples) folder. These examples illustrate recommended patterns and best practices for Kotlin development in our company.

## About Weelorum

[<img src="https://weelorum.com/wp-content/uploads/2022/05/logo.png" alt="www.weelorum.com">][weelorum]

NEWPROJECT are maintained by Weelorum. We specialize in providing all-in-one solution in mobile and web development. Our team follows Lean principles and works according to agile methodologies to deliver the best results reducing the budget for development and its timeline.

Find out more [here][weelorum], read our [blog][weelorum_blog], and don't hesitate to [contact us][contact]!

[weelorum]: https://www.weelorum.com/
[weelorum_blog]: https://weelorum.com/blog/
[contact]: https://weelorum.com/contact/
