## misc
- 先实现immutable内容的计算  
- if a sort of object have regular (usually combination of) properties/constructions, use encoded (short) name  
- extend BoundedMultiIndex to support BigInteger index currently useless, because index of Array in Java/Kotlin is restricted to int, but restrict implementation for UInt needs a tedious overflow-checking (costly and error-prone).
- no need to implement radix-2 FFt since it is equivalent to prime power case when p=2
- after making abstract matrix to interface, implement equals method for every class is very tedious
- kotlin cannot override equals() and hashcode() in interface
  - a workaround is abandon equals()/hashcode(), write custum equals1() in interface
  - if sometimes want to use a class that use equals() without totally rewrite the class, can warp object to a class that implement equals() as equals1()
- never use 'open class', use interface instead
- introduce 'Two' related class introduce complexity, unless make some class open
- parallel method need to call ordinary method in small case.
- Cannot check for instance of erased type (so there is no Generic BatchMapper)

## todo
- R[x]/f(x) for f monic
- floating point sqrt() validity
    

## debug  
- test 的命名空间与 main相同，避免重名
- make sure variables captured by launch{} is val (e.g. in a for() loop)
- if 0 containers and 2 tests were Method or class mismatch, try gradle clean (reload gradle project / restart IDEA not work) 

## references
- (2013) [Optimal Discrete Uniform Generation from Coin Flips, and Applications --Jérémie Lumbroso](https://arxiv.org/abs/1304.1916)
- (2000, 2003) [Generating Random Factored Numbers, Easily --Adam Kalai](http://citeseerx.ist.psu.edu/viewdoc/download?doi=10.1.1.135.8031&rep=rep1&type=pdf)
- (2013, 2014, 2016) [Sampling exactly from the normal distribution --Charles F. F. Karney](https://arxiv.org/abs/1303.6257)

- (2012) [On Ideal Lattices and Learning with Errors Over Rings --Vadim Lyubashevsky and Chris Peikert and Oded Regev](https://eprint.iacr.org/2012/230)
- (2013) [A Toolkit for Ring-LWE Cryptography --Vadim Lyubashevsky and Chris Peikert and Oded Regev](https://eprint.iacr.org/2013/293)
- (2016) [Implementing a Toolkit for Ring-LWE Based Cryptography in Arbitrary Cyclotomic Number Fields --Christoph M. Mayer](https://eprint.iacr.org/2016/049)

- (2014) [Relationship between the Inverses of a Matrix and a Submatrix --E. Juárez-Ruiz, R. Cortés-Maldonado, F. Pérez-Rodríguez](http://www.scielo.org.mx/scielo.php?script=sci_arttext&pid=S1405-55462016000200251)

## symbols
- ℕ
- ℤ
- ℝ
- ℚ
- ℂ

## data structure
- ULong 64-bit
- Int.MAX_VALUE = 2147483647 = 2^31 -1
- UInt.MAX_VALUE = 4294967295 = 2^32 -1 < 64 * Int.MAX_VALUE < ULong.MAX_VALUE = 2^64 -1

so addition of two positive Int in UInt would not overflow  
multiplication of two UInt in ULong would not overflow  