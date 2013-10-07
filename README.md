Gaelyk Console Plugin
=====================

Gaelyk Console Plugin serves as key-hole surgery tool for [Gaelyk](http://gaelyk.appspot.com) applications.
You can use it to run scripts whithin your App Engine environement such as occasional admin tasks or just some
experiments. [Gaelyk shortcuts](http://gaelyk.appspot.com/tutorial/app-engine-shortcuts) are available whitin
the scripts. You can also protoype your templates.

Gaelyk Console Plugin uses [Gaelyk Bootstrap Resources Plugin](https://github.com/gaelyk/gaelyk-bootstrap-resources) 
so you can easily update the look and feel of the console configuring this plugin.

You need to have admin rights to execute scripts and templates using this plugin. You can make scripts visible to other
visitors setting `GaelykConsolePlugin.allowReadOnly=true` when your application initializes (e.g. in `routes.groovy`).

## Installation

Gaelyk Console Plugin is distributed via Maven Central as binary plugin. Use following dependency in your
`build.gradle` file:

```
dependencies {
    ...
    compile 'org.gaelyk:gaelyk-console:2.0'
    ...
}
```

When installed the plugin is available at `http://yourapp/_ah/gaelyk-console/` URL

## Common Features

You can execute, save, load and delete your scripts and templates. Script or template body and name is required
if you want to save the content.


## Scripts
Sripts are enhanced by [Gaelyk shortcuts](http://gaelyk.appspot.com/tutorial/app-engine-shortcuts) such as 
`datastore` or `search` but you obviously can't use servlet specific bindings such as `request` or `response`.

There are three ways how to show script's output.

1. using `println` - there resulting text is shown once the script execution is finished
2. returning the value - the result is shown once the script execution is finished, empty values are not shown at all
3. using `report` - the results are displayed immediately, this is usefull for long running tasks

![Gealyk Console Script View](/docs/Gaelyk_Console_Scripts.png)

## Templates

Templates view has two separate text area - one for writing body of the template one for initializtion. Initialization 
contains parameters for the template. Maps called `request`, `response`, `headers`, `params`, `session`
is available in both inputs so you can e.g.  `request.attr`
and than simply copy paste your template to separate file when tuned up.

Templates are rendered as an iframes in the results area.

![Gealyk Console Script View](/docs/Gaelyk_Console.png)
