# PorterStemmer
Implementation of the Porter Stemmer algorithm in Java, which is outlined [here](http://snowball.tartarus.org/algorithms/english/stemmer.html).

The algorithm retrieves the stem of the word and provides a cleaner system as similar words (i.e. generous and generosity) are very similar so queries results will be handled accordingly.

#### To use the implementation of the Porter Stemmer algorithm
1. Add the dependency to your `pom.xml`:
```
<dependency>
  <groupId>com.github.xjavathehutt</groupId>
  <artifactId>porterstemmer</artifactId>
  <version>1.1.0</version>
</dependency>
```
2. Stem your word: `String stemmed = PorterStemmer.stem(word);`
