-verbose

# Make sure to check everything always, can't hurt.

-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers
-forceprocessing

# We're a bit oldschool

-target 1.6

# TODO Figure out how to do this but for annotations

-adaptclassstrings
#-adaptresourcefilenames
#-adaptresourcefilecontents

# Keep all methods that overrides a super method, or methods that are an event handler.

-keepclassmembers,allowobfuscation class * {
    @java.lang.Override,cpw.mods.fml.common.eventhandler.SubscribeEvent,cpw.mods.fml.common.Mod$EventHandler <methods>;
}

# Keep all classes with public methods

-keepclasseswithmembers,allowobfuscation public class * {
    public <methods>;
}

# This class is referred to in String constants inside of annotations. TODO see above (around -adaptclassstrings).

-keepnames,allowoptimization public class com.github.NeRdTheNed.ReciPic.ReciPic

# Keep all initialisers, because ProGuard keeps trying to make them private.

-keepclassmembers class * { public <init>(...); }

# TODO Check if anything else should be kept. Annotations are mandatory, as Forge uses them for reflection.

-keepattributes *Annotation*

# Repackage all classes into com.github.NeRdTheNed.ReciPic

-repackageclasses com.github.NeRdTheNed.ReciPic

# Bonus optimisations

-optimizationpasses 64
-mergeinterfacesaggressively
-overloadaggressively

# TODO Decide if this is a good idea. allowaccessmodification is probably safe. dontpreverify seems to work on modern JVMs (target needs to be Java 6, lower doesn't matter as preverification doesn't exist yet), need to do more exhaustive testing.

-dontpreverify
-allowaccessmodification

# Debug info

-printusage
-whyareyoukeeping class iDiamondhunter.**
#-printconfiguration proguardDebug.txt
