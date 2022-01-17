package math.abstract_structure

/**
 * Created by CowardlyLion at 2022/1/9 13:09
 *
 * TODO add description of all mathematical object (definition, etc)
 */
interface Module<R, A> : AddGroup<A> {

//    val ring: CRing<R>    //should acted by a ring

    fun action(r: R, a: A): A

}