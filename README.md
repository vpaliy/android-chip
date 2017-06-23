# ChipsLover

[![](https://jitpack.io/v/vpaliyX/ChipsLover.svg)](https://jitpack.io/#vpaliyX/ChipsLover)

This repository provides an implementation of a material chip. Chips represent complex entities in small blocks, such as a contact. Also it could be used to represent a category or a  simple tag.

I followed the guidelines from the [Material Design](https://material.io/guidelines/components/chips.html#) website during the implementation of this library.
However, I have also added additional functionality such as a `ChipsLayout`, default animations, selections, color customization, text appearance, etc.

Take a glance at the demo:

![](https://github.com/vpaliyX/ChipsLover/blob/master/art/ezgif.com-video-to-gif(8).gif)

Also I've created the customization sheet in the sample, so go ahead and download the sample, and you'll get a chance to poke around in the library a little bit:

![](https://github.com/vpaliyX/ChipsLover/blob/master/art/ezgif.com-video-to-gif(9).gif) 

## How to use? ##

Check out all properties you can set to the `ChipView` as well as `ChipsLayout` [here](https://github.com/vpaliyX/ChipsLover/blob/master/chips-lover/src/main/res/values/attrs.xml). They should be intuitive, well, at least I tried to make them so; if it's not the case then I failed miserably. 

### Note! ### 
If you are going to use the `CbipsLayout` view, you may find yourself in a situation where the chips have completely occupied the screen, so in this case you will not be able to scroll your layout. The solution is to wrap the `ChipsLayout` up into a `ScrollView` or `NestedScrollView` (if you're using a `CoordinatorLayout`). 

## How to download? ##

### Step 1 ###  

Add it in your root build.gradle at the end of repositories:

``` gradle
allprojects {
  repositories {
     maven { url 'https://jitpack.io' }
  }
}
  
```
### Step 2 ###

Add the dependency

``` gradle
dependencies {
	compile 'com.github.vpaliyX:ChipsLover:v1.0'
}

```
You're good to go!


``````
MIT License

Copyright (c) 2017 Vasyl Paliy

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
``````
