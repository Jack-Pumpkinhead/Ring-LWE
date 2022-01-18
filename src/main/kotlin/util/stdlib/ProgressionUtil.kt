package util.stdlib

/**
 * Created by CowardlyLion at 2022/1/17 23:11
 */

infix fun ULongProgression.stepForward(step: ULong): ULongForwardProgression = ULongForwardProgression(first,last,step)