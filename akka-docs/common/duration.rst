.. _Duration:

########
Duration
########

Module stability: **SOLID**

Durations are used throughout the Akka library, wherefore this concept is
represented by a special data type, :class:`Duration`. Values of this type may
represent infinite (:obj:`Duration.Inf`, :obj:`Duration.MinusInf`) or finite
durations.

Scala
=====

In Scala durations are constructable using a mini-DSL and support all expected operations:

.. code-block:: scala

   import akka.util.duration._   // notice the small d

   val fivesec = 5.seconds
   val threemillis = 3.millis
   val diff = fivesec - threemillis
   assert (diff < fivesec)
   val fourmillis = threemillis * 4 / 3   // though you cannot write it the other way around
   val n = threemillis / (1 millisecond)

.. note::

   You may leave out the dot if the expression is clearly delimited (e.g.
   within parentheses or in an argument list), but it is recommended to use it
   if the time unit is the last token on a line, otherwise semi-colon inference
   might go wrong, depending on what starts the next line.

Java
====

Java provides less syntactic sugar, so you have to spell out the operations as
method calls instead:

.. code-block:: java

   final Duration fivesec = Duration.create(5, "seconds");
   final Duration threemillis = Duration.parse("3 millis");
   final Duration diff = fivesec.minus(threemillis);
   assert (diff.lt(fivesec));
   assert (Duration.Zero().lt(Duration.Inf()));


