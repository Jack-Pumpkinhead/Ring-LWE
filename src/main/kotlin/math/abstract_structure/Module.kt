package math.abstract_structure

/**
 * Created by CowardlyLion at 2022/1/9 13:09
 *
 * TODO add description of all mathematical object (definition, etc)
 */
abstract class Module<R, A>(descriptions: MutableSet<String>, ring: CRing<R>, zero: A) : AddGroup<A>(descriptions.mapTo(mutableSetOf()) { "additive group of ($it)" }, zero) {

    abstract fun action(r: R, a: A): A

}