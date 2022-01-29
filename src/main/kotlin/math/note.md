##misc
- 先实现immutable内容的计算  
- if a sort of object have regular (usually combination of) properties/constructions, use encoded (short) name  
- extend BoundedMultiIndex to support BigInteger index currently useless, because index of Array in Java/Kotlin is restricted to int, but restrict implementation for UInt needs a tedious overflow-checking (costly and error-prone).
- no need to implement radix-2 FFt since it is equivalent to prime power case when p=2
- after making abstract matrix to interface, implement equals method for every class is very tedious

##todo
- R[x]/f(x) for f monic
    

##debug  
- test 的命名空间与 main相同，避免重名

##references
- [Sampling exactly from the normal distribution --Charles F. F. Karney](https://arxiv.org/abs/1303.6257)
- [On Ideal Lattices and Learning with Errors Over Rings --Vadim Lyubashevsky and Chris Peikert and Oded Regev](https://eprint.iacr.org/2012/230)
- [A Toolkit for Ring-LWE Cryptography --Vadim Lyubashevsky and Chris Peikert and Oded Regev](https://eprint.iacr.org/2013/293)
- [Implementing a Toolkit for Ring-LWE Based Cryptography in Arbitrary Cyclotomic Number Fields --Christoph M. Mayer](https://eprint.iacr.org/2016/049)

##symbols
- ℕ
- ℤ
- ℝ
- ℚ
- ℂ