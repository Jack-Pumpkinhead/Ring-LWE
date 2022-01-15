先实现immutable内容的计算  
if a sort of object have regular (usually combination of) properties/constructions, use encoded (short) name  
extend BoundedMultiIndex to support BigInteger index currently useless, because index of Array in Java/Kotlin is restricted to int, but restrict implementation for UInt needs a tedious overflow-checking (costly and error-prone).



##debug  
test 的命名空间与 main相同，避免重名