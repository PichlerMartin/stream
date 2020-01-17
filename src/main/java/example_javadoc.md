```java
@SupressWarnings("all")
/**
* @return      provides a description of what a method will or can return
* <p></p>
* <hr><blockquote><pre>
*     class PrimeRun implements Runnable {
*         long minPrime;
*         PrimeRun(long minPrime) {
*             this.minPrime = minPrime;
*         }
*
*         public void run() {
*             // compute primes larger than minPrime
*             &nbsp;.&nbsp;.&nbsp;.
*         }
*     }
* </pre></blockquote></hr>
* @param       provides any useful description about a method's parameter or input it should expect
* @see         will generate a link similar to the {@link} tag, but more in the context of a reference and not inline
* @since       specifies which version the class, field, or method was added to the project
* @version     specifies the version of the software, commonly used with %I% and %G% macros
* @throws      is used to further explain the cases the software would expect an exception
* @deprecated  gives an explanation of why code was deprecated, when it may have been deprecated, and what the alternatives are
* {@link}      provides an inline link to a referenced part of our source code
* @author      the name of the author who added the class, method, or field that is commented
* {@inheritDoc}
* {@value #MY_MAGIC_NUMBER}
*/
 public class ThisIsJavaDocFromThreadClassAndOnlyServesAsAnExampleForJavaDocUsage{
     private static Integer MY_INT = 0;
 }
```